# 后端手册

## 基础功能

### 数据权限 DataScope

```java
@DataScope(deptAlias = "d")
@DataScope(deptAlias = "d", userAlias = "u")
```

**基于 AOP 实现**

> 核心思想：拿到当前用户角色的数据权限，拼接 sql 子串，放入注解所在方法的第一个参数对应的 BaseEntity 的 params 上，最后在 MyBatis 的 mapper.xml 中去使用

```xml
<select id="select" parameterType="..." resultMap="...Result">
    <include refid="select...Vo"/>
    <!-- 数据范围过滤 -->
    ${params.dataScope}
</select>
```

```java
@Before("@annotation(controllerDataScope)")
public void doBefore(JoinPoint point, DataScope controllerDataScope) {
   LoginUser loginUser = SecurityUtils.getLoginUser();
   SysUser user = loginUser.getUser();
   String deptAlias = controllerDataScope.deptAlias();
   String userAlias = controllerDataScope.userAlias();
   StringBuilder sqlString = new StringBuilder();

   for (SysRole role : user.getRoles()) {
       String dataScope = role.getDataScope();
       if (DATA_SCOPE_ALL.equals(dataScope)) {
           sqlString = new StringBuilder();
           break;
       } else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {
           sqlString.append(StringUtils.format(
               " OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", deptAlias,
               role.getRoleId()));
       } else if (DATA_SCOPE_DEPT.equals(dataScope)) {
           sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", deptAlias, user.getDeptId()));
       } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
           sqlString.append(StringUtils.format(
               " OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or ancestors like {} OR ancestors like {}} OR ancestors like {} )",
               deptAlias, user.getDeptId(), "," + user.getDeptId() + ",", "," + user.getDeptId(), user.getDeptId() + ","));
       } else if (DATA_SCOPE_SELF.equals(dataScope)) {
           if (StringUtils.isNotBlank(userAlias)) {
               sqlString.append(StringUtils.format(" OR {}.user_id = {} ", userAlias, user.getUserId()));
           } else {
               // 数据权限为仅本人且没有userAlias别名不查询任何数据
               sqlString.append(" OR 1=0 ");
           }
       }
   }

   if (StringUtils.isNotBlank(sqlString.toString())) {
       Object params = joinPoint.getArgs()[0];
       if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
           BaseEntity baseEntity = (BaseEntity) params;
           baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
       }
   }
}
```

### 多数据源 Master Slave

```java
@Master
@Slave
```

重定义注解，简化对@DS("master")和@DS("slave")的使用

> 基于[dynamic-datasource-spring-boot-starter](https://baomidou.com/pages/a61e1b/#%E6%96%87%E6%A1%A3-documentation)来支持多数据源

#### 单体版

支持 mysql、sqlite 两个数据库，采用手动注入多数据源并动态添加数据源的方式来实现。

```yml
spring:
  datasource:
    dynamic:
      primary: master
      strict: false
```

```java
@Slf4j
@Configuration
public class DataSourceConfig {
    @Primary
    @Bean
    public DataSource dataSource(DynamicDataSourceProperties properties) {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());

        // 动态添加数据源
        switch (dbType) {
            case "mysql":
                addMySQLDataSource(dataSource);
                break;
            default:
                addSQLiteDataSource(dataSource);
        }

        try {
            org.flywaydb.core.api.configuration.Configuration configuration = Flyway.configure().dataSource(dataSource).baselineDescription("initByServer").baselineOnMigrate(true).validateOnMigrate(false).locations(String.format("classpath:data/migration/%s", dbType));
            Flyway flyway = new Flyway(configuration);
            flyway.migrate();
        } catch (Exception e) {
            log.error("数据库迁移出现异常", e);
        }

        return dataSource;
    }
}
```

#### 微服务版

支持主从数据库的配置，默认只配置了 master 数据源为 mysql。

```yml
spring:
  datasource:
    dynamic:
      datasource:
        # 主库数据源
        master:
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://mapgis-mysql:3306/mapgis-cloud-xxx?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
          username: root
          password: cloud123.mapgis
        # 从库数据源
        # slave:
        # username:
        # password:
        # url:
        # driver-class-name:
```

### 限流 RateLimiter

```java
@RateLimiter(count = 100, time = 60)
```

> 核心思想：通过 Redis 实现，在 RedisConfig 中自定义限流脚本，统计以方法调用为 key 的缓存有效期内的执行次数，是否大于限制，大于则抛出异常

因在单体版本中未使用 Redis（采用 Ehcache），所以该注解要使用必须引入对 Redis 的依赖，在微服务版中可直接使用。

```java
@Before("@annotation(rateLimiter)")
public void doBefore(JoinPoint point, RateLimiter rateLimiter) throws Throwable {
  String key = rateLimiter.key();
  int time = rateLimiter.time();
  int count = rateLimiter.count();

  String combineKey = getCombineKey(rateLimiter, point);
  List<Object> keys = Collections.singletonList(combineKey);
  try {
      Long number = redisTemplate.execute(limitScript, keys, count, time);
      if (StringUtils.isNull(number) || number.intValue() > count) {
          throw new ServiceException("访问过于频繁，请稍候再试");
      }
      log.info("限制请求'{}',当前请求'{}',缓存key'{}'", count, number.intValue(), key);
  } catch (ServiceException e) {
      throw e;
  } catch (Exception e) {
      throw new RuntimeException("服务器限流异常，请稍候再试");
  }
}
```

### 防重复提交 RepeatSubmit

```java
@RepeatSubmit
```

> 核心思路：基于过滤器和拦截器实现，在过滤器的 doFilter 中对用户的请求进行预处理，把请求体为"application/json"开头的转换成可重复读取 inputStream 的 request：RepeatedlyRequestWrapper，在拦截器的 preHandle 中去验证是否有重复提交，存在则返回 false 结束请求

```java
@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
  throws IOException, ServletException {
  ServletRequest requestWrapper = null;
  if (request instanceof HttpServletRequest
      && StringUtils.startsWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
      requestWrapper = new RepeatedlyRequestWrapper((HttpServletRequest) request, response);
  }
  if (null == requestWrapper) {
      chain.doFilter(request, response);
  } else {
      chain.doFilter(requestWrapper, response);
  }
}

@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
  if (handler instanceof HandlerMethod) {
      HandlerMethod handlerMethod = (HandlerMethod) handler;
      Method method = handlerMethod.getMethod();
      RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
      if (annotation != null) {
          if (this.isRepeatSubmit(request, annotation)) {
              AjaxResult ajaxResult = AjaxResult.error(annotation.message());
              ServletUtils.renderString(response, JSONObject.toJSONString(ajaxResult));
              return false;
          }
      }
      return true;
  } else {
      return true;
  }
}
```

### 日志 Log

```java
@Log(title = "通知公告", businessType = BusinessType.INSERT)
```

**基于 AOP 实现**

> 核心思想：在后置返回通知和异常通知中，获取当前用户信息（用户名、操作模块、业务类型、操作类别）和客户端信息（请求方法、请求方式、请求地址、操作地点、返回参数、状态、异常信息）等，以异步的方式在给定一定延迟后（10ms）写入数据库中（单体版）或调用系统的日志服务写入数据库中

```java
/**
 * 处理完请求后执行
 *
 * @param joinPoint 切点
 */
@AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
  handleLog(joinPoint, controllerLog, null, jsonResult);
}

/**
 * 拦截异常操作
 *
 * @param joinPoint 切点
 * @param e         异常
 */
@AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
  handleLog(joinPoint, controllerLog, e, null);
}

protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
  try {

      // 获取当前的用户
      LoginUser loginUser = SecurityUtils.getLoginUser();

      // *========数据库日志=========*//
      SysOperLog operLog = new SysOperLog();
      operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
      // 请求的地址
      String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
      operLog.setOperIp(ip);
      operLog.setOperUrl(ServletUtils.getRequest().getRequestURI());
      if (loginUser != null) {
          operLog.setOperName(loginUser.getUsername());
      }

      if (e != null) {
          operLog.setStatus(BusinessStatus.FAIL.ordinal());
          operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
      }
      // 设置方法名称
      String className = joinPoint.getTarget().getClass().getName();
      String methodName = joinPoint.getSignature().getName();
      operLog.setMethod(className + "." + methodName + "()");
      // 设置请求方式
      operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
      // 处理设置注解上的参数
      getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);
      // 保存数据库（单体版）
      AsyncManager.me().execute(AsyncFactory.recordOper(operLog));

      // 保存数据库（微服务版）
      // asyncLogService.saveSysLog(operLog);
  } catch (Exception exp) {
      // 记录本地异常日志
      log.error("==前置通知异常==");
      log.error("异常信息:{}", exp.getMessage());
      exp.printStackTrace();
  }
}
```

### Excel 导出 Excel、Excels

```java
@Excel(name = "序号", cellType = ColumnType.NUMERIC)
@Excel(name = "角色状态", readConverterExp = "0=正常,1=停用")
@Excel(name = "部门编号", type = Type.IMPORT)
@Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")

@Excels({
 @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
 @Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT)
})
```

> 核心思想：基于 poi-ooxml 库，查看类的每个字段上是否有对应的注解，再解析注解，把字段的内容写到文档中

### 权限控制 RequiresPermissions、RequiresRoles、RequiresLogin

```
@RequiresPermissions("system:logininfor:list")
```

RequiresPermissions、RequiresRoles、RequiresLogin 这 3 个注解专用于微服务版。

因微服务版的权限控制是自定义的，单体版是基于 Spring Security 的，所以要保证一套代码支持单体和微服务切换，现阶段是通过在需要添加权限控制的方法上同时添加有@PreAuthorize 和@RequiresPermissions。

```java
@ApiOperation("查询系统访问记录列表")
@PreAuthorize("@ss.hasPermi('system:logininfor:list')")
@RequiresPermissions("system:logininfor:list")
@GetMapping("/list")
public TableDataInfo list(SysLogininfor logininfor) {
    startPage();
    List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
    return getDataTable(list);
}
```

### 全局控制器前缀 ManagerRestController、ServicesRestController

```java
@ServicesRestController("/auth")
@ManagerRestController("/system/user/profile")
```

用于快速给所有的控制器添加相应的前缀。

```yml
# API前缀
api:
  path:
    services-prefix: xxx/rest/services
    manager-prefix: xxx/rest/manager
```

```java
@Configuration
public class ControllerPrefixWebConfiguration implements WebMvcConfigurer {
    @Resource
    private ApiPathProperties apiPathProperties;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer
                .addPathPrefix(apiPathProperties.getServicesPrefix(), c -> c.isAnnotationPresent(ServicesRestController.class))
                .addPathPrefix(apiPathProperties.getManagerPrefix(), c -> c.isAnnotationPresent(ManagerRestController.class));
    }
}
```

### 防止 XSS 攻击

#### 定义 XSS 注解来对需要的基本信息进行校验

```java
@Xss(message = "用户账号不能包含脚本字符")
```

#### 更多信息通过过滤器来处理

##### 单体版

```java
public class XssFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (handleExcludeURL(req, resp)) {
            chain.doFilter(request, response);
            return;
        }
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }
}
```

##### 微服务版

```java
@Component
@ConditionalOnProperty(value = "security.xss.enabled", havingValue = "true")
public class XssFilter implements GlobalFilter, Ordered {
@Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // GET DELETE 不过滤
        HttpMethod method = request.getMethod();
        if (method == null || method.matches("GET") || method.matches("DELETE")) {
            return chain.filter(exchange);
        }
        // 非json类型，不过滤
        if (!isJsonRequest(exchange)) {
            return chain.filter(exchange);
        }
        // excludeUrls 不过滤
        String url = request.getURI().getPath();
        if (StringUtils.matches(url, xss.getExcludeUrls())) {
            return chain.filter(exchange);
        }
        ServerHttpRequestDecorator httpRequestDecorator = requestDecorator(exchange);
        return chain.filter(exchange.mutate().request(httpRequestDecorator).build());

    }
}
```

### AOP 支持自调用

> 核心思路：采用 AOP 底层原理 JDK 动态代理方式，被代理对象可以通过 AopContext.currentProxy()获取到代理对象，通过调用代理对象的方法进而访问被代理对象的方法（不能直接通过 this 调用，这将调用到被代理对象的该方法，可能失去一些代理对象增强的逻辑）

#### **单体版**

```java
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
public class ApplicationConfig {
}
```

#### **微服务版**

```java
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
public @interface EnableMapConfig {
}
```

### 配置时区

> 核心思路：解决 Jackson 日期反序列化时间问题，配置一个 bean 实现整体修改

```java
public class ApplicationConfig {
    /**
     * 时区配置
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
    }
}
```

### 只读模式

```yml
# 安全配置
security:
  access:
    # 只读开关
    readonlyEnabled: ${SECURITY_ACCESS_READONLYENABLED:false}
```

> 基于@ModelAttribute 在控制器每个方法执行前被执行，检查当前的请求方法只要是增删改，就抛出 ReadonlyModeException，在 GlobalExceptionHandler 中处理

```java
public class BaseController {
    @ModelAttribute
    public void init(HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException     {
        String url = ServletUtils.getRequest().getRequestURI();

        if (!readonlyEnabled) {
            return;
        }

        // 增删改 请求
        if ("DELETE".equals(httpServletRequest.getMethod()) || "POST".equals(httpServletRequest.getMethod())
                || "PUT".equals(httpServletRequest.getMethod())) {
            throw new ReadonlyModeException();
        }
    }
}

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 只读模式异常
     */
    @ExceptionHandler(ReadonlyModeException.class)
    public AjaxResult handleDemoModeException(ReadonlyModeException e) {
        return AjaxResult.error("只读模式，不允许操作");
    }
}
```

### 单体和微服务自由切换

支持通过切换 profile 为 local 或 cloud 来实现单体和微服务的自由切换，可满足一个项目在不同环境下部署和应用的需求。

为了实现这套机制，项目中很多模块都包含 base、local、cloud 三个子模块，通过 maven 的 profile 来定义不同配置下具体的模块。

```xml
<profiles>
    <!-- 单体模式 -->
    <profile>
        <id>local</id>
        <activation>
            <!--默认激活配置-->
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <mapgis.common.security.artifact>mapgis-common-local-security</mapgis.common.security.artifact>
        </properties>
    </profile>
    <!-- 微服务模式 -->
    <profile>
        <id>cloud</id>
        <activation>
            <!--默认激活配置-->
            <activeByDefault>false</activeByDefault>
        </activation>
        <properties>
            <mapgis.common.security.artifact>mapgis-common-cloud-security</mapgis.common.security.artifact>
        </properties>
    </profile>
</profiles>
```

对于一个微服务中的接口需要被其他微服务调用时，会将这些接口抽出来定义到`-api`的模块中，其中`-cloud-api`用于微服务间调用，`-local-api`用于模块间调用。这些接口都会在`-biz`中进行实现，而且`-cloud-api`的实现会调用`-local-api`的实现。

下面以 system 模块进行举例说明：

`mapgis-module-system-local-api`定义了接口`ISysServiceApi`

```java
public interface ISysServiceApi {
}
```

`mapgis-module-system-cloud-api`也定义了接口`ISysServiceApi`

```java
@FeignClient(contextId = "remoteSysServiceApi", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteSysServiceApiFallbackFactory.class)
public interface ISysServiceApi {
}
```

`mapgis-module-system-biz`有以上 2 个接口的实现，分别是：

```java
@Service("SysServiceApiImpl")
public class SysServiceApiImpl implements ISysServiceApi  {
}
```

```java
@ManagerRestController("/system/api")
public class SysServiceApiController extends BaseController {

    @Autowired
    @Qualifier("SysServiceApiImpl")
    private ISysServiceApi sysServiceApi;
}
```

这样在任何其他模块中不管是否跨微服务，只要调用`ISysServiceApi`的接口，在微服务和单体模式下都能调用成功。

### 多数据库支持

MyBatis 可以根据不同的数据库厂商执行不同的语句，可参考[Mybatis 多数据库支持](https://blog.csdn.net/qq_28898917/article/details/103634570)。

#### 配置

```java
@Configuration
public class MyBatisConfig {
	@Bean
    public DatabaseIdProvider getDatabaseIdProvider() {
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("MySQL", "mysql");
        properties.setProperty("SQLite", "sqlite");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }
}
```

获取数据库品牌标识的示例代码：

```java
@SpringBootTest
public class DataBaseProduct {
    @Test
    void getProductName() throws SQLException, ClassNotFoundException {
        String driver = "org.sqlite.JDBC";
        String url = "jdbc:sqlite::resource:static/dq.db?date_string_format=yyyy-MM-dd HH:mm:ss";
        String username = "";
        String password = "";
        Class.forName(driver);
        Connection con = (Connection) DriverManager.getConnection(url, username, password);
        DatabaseMetaData metaData = (DatabaseMetaData) con.getMetaData();
        System.out.println("数据库的产品名称:" + metaData.getDatabaseProductName());
    }
}
```

常见的品牌标识如下：

```
<property name="SQL Server" value="sqlserver"/>
<property name="DB2" value="db2"/>
<property name="Oracle" value="oracle" />
<property name="MySQL" value="mysql" />
<property name="SQLite" value="sqlite" />
<property name="PostgreSQL" value="postgre" />
```

#### 属性 databaseId

Mybatis 支持给每个 statement 添加属性`databaseId`，可根据 databaseId 的取值来写不同的语句，未指明属性的为通用语句。

```xml
<update id="cleanJobLog">
    truncate table sys_job_log
</update>

<update id="cleanJobLog" databaseId="sqlite">
    delete
    from sys_job_log;
    update sqlite_sequence
    SET seq = 0
    where name = 'sys_job_log';
</update>
```

#### 内置参数\_databaseId

表示当前数据库的别名，可参考[Mybatis 动态 sql 之内置参数\_parameter 和\_databaseId](https://cloud.tencent.com/developer/article/1687061)。

```xml
<update id="updateJob" parameterType="SysJob">
    update sys_job
    <set>
        <if test="jobName != null and jobName != ''">job_name = #{jobName},</if>
        <if test="jobGroup != null and jobGroup != ''">job_group = #{jobGroup},</if>
        <if test="invokeTarget != null and invokeTarget != ''">invoke_target = #{invokeTarget},</if>
        <if test="cronExpression != null and cronExpression != ''">cron_expression = #{cronExpression},</if>
        <if test="misfirePolicy != null and misfirePolicy != ''">misfire_policy = #{misfirePolicy},</if>
        <if test="concurrent != null and concurrent != ''">concurrent = #{concurrent},</if>
        <if test="status !=null">status = #{status},</if>
        <if test="remark != null and remark != ''">remark = #{remark},</if>
        <if test="updateBy != null and updateBy != ''">update_by = #{updateBy},</if>
        <choose>
            <when test="_databaseId == 'sqlite'">
                update_time = (datetime(CURRENT_TIMESTAMP,'localtime'))
            </when>
            <otherwise>
                update_time = sysdate()
            </otherwise>
        </choose>
    </set>
    where job_id = #{jobId}
</update>
```

### flyway 数据库版本管理

单体版采用 flyway 来管理数据库版本，支持在部署的时候执行相应的数据库脚本，这里采用手动编写代码的方式进行，可参考[SpringBoot 中使用 Flyway](https://blog.csdn.net/qq_35995691/article/details/122770451)。

#### 配置（关闭自动配置)

```yml
spring:
  flyway:
    # flyway自动配置 true 开启
    enabled: true
```

#### 通过 java 代码进行迁移

因需要支持 mysql 和 sqlite，所以在`data/migration`目录下分别有 2 个数据库的迁移脚本。

```java
@Slf4j
@Configuration
public class DataSourceConfig {
    @Value("${DB_TYPE:sqlite}")
    private String dbType;

	@Primary
    @Bean
    public DataSource dataSource(DynamicDataSourceProperties properties) {
        // ...
    try {
            org.flywaydb.core.api.configuration.Configuration configuration = Flyway.configure().dataSource(dataSource).baselineDescription("initByServer").baselineOnMigrate(true).validateOnMigrate(false).locations(String.format("classpath:data/migration/%s", dbType));
            Flyway flyway = new Flyway(configuration);
            flyway.migrate();
        } catch (Exception e) {
            log.error("数据库迁移出现异常", e);
        }
    }
}
```

### 缓存管理

项目中存在用户信息、字典数据等需要缓存的内容，为使单体版更加轻量化，采用了 ehcache 来实现，微服务版采用的是 redis（需要单独启动 redis 服务）。

#### ehcache 与 redis 在实现上存在差异

获取对象列表时，redis 原生支持模糊匹配，ehcache 不支持，现有实现只支持完全匹配和带有\*后缀的模糊匹配

```java
@Component
public class EhcacheCacheServiceImpl implements CacheService {
	/**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    @Override
    public Collection<String> keys(final String pattern) {
        Collection<String> keys = cache.getKeys();
        final String query = pattern.endsWith("*") ? pattern.substring(0, pattern.length() - 2) : pattern;

        return keys.stream().
                filter(k -> k.indexOf(query) == 0).
                collect(Collectors.toList());
    }
}
```

```java
@Component
public class RedisCacheServiceImpl implements CacheService {
    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    @Override
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }
}
```

### 验证码开启和关闭

目前后端支持开启和关闭，单体版在`mapgis-server`模块的配置文件`application.yml`中：

```yml
# 安全配置
security:
  # 验证码
  captcha:
    enabled: true
    # 验证码类型 math 数组计算 char 字符验证
    type: math
```

微服务版在网关模块的配置文件`mapgis-xxx-gateway-server-xxx.yml`中（位于 nacos）:

```yml
# 安全配置
security:
  # 验证码
  captcha:
    enabled: true
    # 验证码类型 math 数组计算 char 字符验证
    type: math
```

前端目前验证码是否开启是固定的，因为后端没有提供获取是否验证码开启和关闭的接口。

### 基于OAuth2的第三方用户登录
第三方平台在互联网行业多用户社交
> 核心思路：借助JustAuth实现了对第三方平台的OAuth2登录，目前默认集成了对GitHub、Gitee和Gitlab的支持

通过sys_auth_user的user_id字段建立起与sys_user的联系，当第三方平台初次登录后，会检测本平台内部是否有同名账号，存在同名时，则提示用户直接可以绑定或创建新账号，不存在同名时，直接提示创建新创号，同时，平台已有用户也可以后台设置页面手动进行绑定和解绑。

核心流程：
- 已绑定第三方平台用户：请求第三方平台登录->第三方平台登录回调结果->响应token到前端->获取第三方登录用户信息
- 未绑定第三方平台用户：请求第三方平台登录->第三方平台登录回调结果->响应绑定前的第三方登录用户信息到前端->提示绑定或创建新账户->响应token到前端->获取绑定后的第三方登录用户信息

详见[thirdLoginMixin.js](http://192.168.200.88/webgis/server/mapgis-boot/blob/master/mapgis-manager/src/views/user/third/thirdLoginMixin.js)和[ThirdLoginController.java](http://192.168.200.88/webgis/server/mapgis-boot/blob/master/mapgis-boot/mapgis-module-auth/mapgis-module-auth-biz/src/main/java/com/zondy/mapgis/auth/controller/ThirdLoginController.java)

目前后端支持开启和关闭，单体版在`mapgis-server`模块的配置文件`application.yml`中：

```yml
# 第三方登录
justauth:
  enabled: true
  type:
    GITHUB:
      client-id: ${JUSTAUTH_GITHUB_CLIENT_ID}
      client-secret: ${JUSTAUTH_GITHUB_CLIENT_SECRET}
      redirect-uri: ${JUSTAUTH_GITHUB_REDIRECT_URI:http://127.0.0.1:8080/xxx/rest/services/auth/thirdLogin/callback/github}
    GITEE:
      client-id: ${JUSTAUTH_GITEE_CLIENT_ID}
      client-secret: ${JUSTAUTH_GITEE_CLIENT_SECRET}
      redirect-uri: ${JUSTAUTH_GITEE_REDIRECT_URI:http://127.0.0.1:8080/xxx/rest/services/auth/thirdLogin/callback/gitee}
  cache:
    type: default
  extend:
    enum-class: ${JUSTAUTH_CUSTOM_ENUM_CLASS:com.zondy.mapgis.auth.justauth.source.AuthCustomSource}
    authorize: ${JUSTAUTH_CUSTOM_AUTHORIZE_URL}
    access-token: ${JUSTAUTH_CUSTOM_ACCESS_TOKEN_URL}
    user-info: ${JUSTAUTH_CUSTOM_USER_INFO_URL}
    config:
      CUSTOM:
        request-class: ${JUSTAUTH_CUSTOM_REQUEST_CLASS:com.zondy.mapgis.auth.justauth.request.AuthCustomRequest}
        client-id: ${JUSTAUTH_CUSTOM_CLIENT_ID}
        client-secret: ${JUSTAUTH_CUSTOM_CLIENT_SECRET}
        redirect-uri: ${JUSTAUTH_CUSTOM_REDIRECT_URI:http://127.0.0.1:8080/xxx/rest/services/auth/thirdLogin/callback/custom}
        scopes: ${JUSTAUTH_CUSTOM_SCOPES}
```

微服务版在网关模块的配置文件`mapgis-xxx-auth-server-xxx.yml`中（位于 nacos）:

```yml
# 第三方登录
justauth:
  enabled: true
  type:
    GITHUB:
      client-id: f770f675866957c53ce6
      client-secret: 79fdc00cb1fc6b4b4bf9bfc5738b56b14529ca7b
      redirect-uri: http://127.0.0.1:8080/xxx/rest/services/auth/thirdLogin/callback/github
    GITEE:
      client-id: a3641bbe80e305d7810d1e848e4e1a9b338066ec16de19792fba1d1a304c8a2f
      client-secret: f4123a258799dc60284e1e30241ba7276d141aaddea777716f7e675f167d2e4b
      redirect-uri: http://127.0.0.1:8080/xxx/rest/services/auth/thirdLogin/callback/gitee
  cache:
    type: default
  extend:
    enum-class: com.zondy.mapgis.auth.justauth.source.AuthCustomSource
    authorize: http://192.168.200.88/oauth/authorize
    access-token: http://192.168.200.88/oauth/token
    user-info: http://192.168.200.88/api/v4/user
    config:
      CUSTOM:
        request-class: com.zondy.mapgis.auth.justauth.request.AuthCustomRequest
        client-id: dcaf95ad5e5cc3d7f5ff8ef06960c3d57a7b18582b5d12b2367388cbf7cd7db5
        client-secret: 83dc146f7bbca52e14b5eaa6f1963739035c0f625e9cc9c99b43ee846dd90c5b
        redirect-uri: http://127.0.0.1:8080/xxx/rest/services/auth/thirdLogin/callback/custom
        scopes: read_user openid
```

### CAS单点登录
单点登录SSO(Single Sign-On) 是一种统一认证和授权机制，指访问同一服务器不同应用中的受保护资源的同一用户，只需要登录一次，即通过一个应用中的安全验证后，再访问其他应用中的受保护资源时，不再需要重新登录验证。

#### CAS单点登录服务端准备
需要搭建单独的服务端应用，可参考官网地址[https://github.com/apereo/cas-overlay-template](https://github.com/apereo/cas-overlay-template)
> 一般情况下需要基于JDBC提供对动态用户的支持

配置application.properties参考：
```properties
# http端口
server.port=8888
# https
server.ssl.enabled=false
# 证书路径
server.ssl.key-store=config/ldkeystore.p12
# 别名密码
server.ssl.key-store-password=123456
server.ssl.key-password=123456
# 证书类型
server.ssl.key-store-type=PKCS12
# 别名
server.ssl.key-alias=undertow
# 设置安全为false
cas.tgc.secure=false
# 开启识别json文件，默认false
cas.serviceRegistry.initFromJson=true
cas.logout.follow-service-redirects=true

##
# CAS Authentication Credentials
#
cas.authn.jdbc.query[0].url=jdbc:mysql://localhost:3306/mapgis-xxx?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
cas.authn.jdbc.query[0].user=root
cas.authn.jdbc.query[0].password=cloud123.mapgis
cas.authn.jdbc.query[0].sql=select * from sys_user where user_name=?
cas.authn.jdbc.query[0].fieldPassword=password
cas.authn.jdbc.query[0].driverClass=com.mysql.cj.jdbc.Driver
#开启自定义密码验证
cas.authn.jdbc.query[0].passwordEncoder.type=com.cas.password.MyPasswordEncoder
cas.authn.jdbc.query[0].fieldDisabled=status
```

启动脚本startup.bat参考：
```shell
@echo off
cd /d %~dp0
cd ..
@echo on
java.exe -jar lib/cas.war --cas.standalone.configuration-directory=config
pause
```

#### 单点登录流程
在本平台前后端分离模式下，单点登录中service采用的是后端接口，而非前端，当后端验证通过后，会携带token重定向到前端，前端基于token校验用户信息再跳转到去除token参数的前端页面
> 核心思想：基于spring-security-cas，在单体版和微服务版中均提供了对cas的对接支持

后端关键代码[CasSecurityConfig.java](http://192.168.200.88/webgis/server/mapgis-boot/blob/master/mapgis-boot/mapgis-module-auth/mapgis-module-auth-biz/src/main/java/com/zondy/mapgis/auth/cas/config/CasSecurityConfig.java)
```java
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@ConditionalOnProperty(prefix = "cas", name = "enabled", havingValue = "true")
public class CasSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // ...
        // 添加CAS 认证filter
        httpSecurity.addFilter(casAuthenticationFilter());
        // 添加JWT filter
        httpSecurity.addFilterBefore(authenticationTokenFilter, CasAuthenticationFilter.class);
        httpSecurity.addFilterBefore(casLogoutFilter(), LogoutFilter.class);
        httpSecurity.addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class);

        // 添加CORS filter
        httpSecurity.addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class);
        httpSecurity.addFilterBefore(corsFilter, LogoutFilter.class);
        // ...
    }

    /**
     * 身份认证接口
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.authenticationProvider(casAuthenticationProvider());
    }

    /**
     * 指定service相关信息
     */
    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        // 设置cas客户端登录完整的url
        serviceProperties.setService(casProperties.getCasServiceUrl() + casProperties.getCasServiceLoginUrl());
        serviceProperties.setAuthenticateAllArtifacts(true);
        return serviceProperties;
    }

    /**
     * CAS认证过滤器
     */
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        casAuthenticationFilter.setFilterProcessesUrl(casProperties.getCasServiceLoginUrl());
        casAuthenticationFilter.setAuthenticationSuccessHandler(casAuthenticationSuccessHandler);
        return casAuthenticationFilter;
    }

    /**
     * CAS认证Provider
     */
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        casAuthenticationProvider.setAuthenticationUserDetailsService(casUserDetailsService);
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
        casAuthenticationProvider.setKey("casAuthenticationProviderKey");
        return casAuthenticationProvider;
    }

    /**
     * CAS票证验证器
     */
    @Bean
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
        return new Cas20ServiceTicketValidator(casProperties.getCasServerUrl());
    }

    /**
     * 单点注销过滤器
     * 用于接收cas服务端的注销请求
     */
    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

    /**
     * 单点退出过滤器
     * 用于跳转到cas服务端
     */
    @Bean
    public LogoutFilter casLogoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(casProperties.getCasServerLogoutUrl(), new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl(casProperties.getCasServiceLogoutUrl());
        return logoutFilter;
    }

    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> servletListenerRegistrationBean = new ServletListenerRegistrationBean<>();
        servletListenerRegistrationBean.setListener(new SingleSignOutHttpSessionListener());
        return servletListenerRegistrationBean;
    }
}
```
前端关键代码[permission.js](http://192.168.200.88/webgis/server/mapgis-boot/blob/master/mapgis-manager/src/permission.js)
```js
router.beforeEach((to, from, next) => {
  NProgress.start() // start progress bar
  to.meta && typeof to.meta.title !== 'undefined' && setDocumentTitle(`${i18nRender(to.meta.title)} - ${domTitle}`)
  /* has token */
  if (storage.get(ACCESS_TOKEN)) {
    // ...
  } else {
    if (allowList.includes(to.name)) {
      // 在免登录名单，直接进入
      next()
    } else {
      if (window._CONFIG['enableSSO']) {
        const queryParams = qs.parse(document.location.toString().split('?')[1])
        const token = queryParams.token

        // 判断来源是不是cas的地址
        if (
          token &&
          (window._CONFIG['casLoginUrl'].includes(document.referrer) ||
            document.referrer.includes(document.location.host))
        ) {
          validateToken(token, to, from, next)
        } else {
          window.location.href = window._CONFIG['casLoginUrl']
        }
      } else {
        next({ path: loginRoutePath, query: { redirect: to.fullPath } })
      }
      NProgress.done() // if current page is login will not trigger afterEach hook, so manually handle it
    }
  }
})

function validateToken(token, to, from, next) {
  store
    .dispatch('ValidateLogin', token)
    .then(res => {
      const url = document.location.toString().split('?')[0]
      window.location.href = url
    })
    .catch(() => {
      window.location.href = window._CONFIG['casLoginUrl']
    })
}
```
#### 前后端配置
**前端配置**

`public/static/config.js`
> 注意：service后面url，单体版为单体服务的主机端口，微服务版为授权服务的主机端口
```js
/**
 * 存放配置常量
 */
window._CONFIG = {
  // 单点登录地址
  VUE_APP_CAS_LOGIN_URL: 'http://localhost:8888/cas/login?service=http://localhost:8080/login/cas',
  // 单点登出地址
  VUE_APP_CAS_LOGOUT_URL: 'http://localhost:8888/cas/logout?service=http://localhost:8080/login/cas',
  // 开启单点登录
  VUE_APP_SSO: false
}
```

**后端配置**

单体版在`mapgis-server`模块的配置文件`application.yml`中：
```yml
# cas配置
cas:
  enabled: ${CAS_ENABLED:false}
  server:
    host:
      # cas服务端地址
      url: ${CAS_SERVER_HOST_URL:http://localhost:8888/cas}
      #cas服务端登录地址
      login_url: ${cas.server.host.url}/login
      #cas服务端登出地址 service参数后面跟就是需要跳转的页面/接口 这里指定的是cas客户端登录接口
      logout_url: ${cas.server.host.url}/logout?service=${cas.service.host.url}${cas.service.host.login_url}
  service:
    host:
      # cas客户端地址
      url: ${CAS_SERVICE_HOST_URL:http://localhost:${server.port}}
      # cas客户端地址登录地址
      login_url: ${CAS_SERVICE_LOGIN_URL:/login/cas}
      # cas客户端地址登出地址
      logout_url: ${CAS_SERVICE_LOGOUT_URL:/logout/cas}
    web:
      url: ${CAS_SERVICE_WEB_RUL:http://localhost:8000}
```

微服务版在网关模块的配置文件`mapgis-xxx-auth-server-xxx.yml`中（位于 nacos）:

```yml
# cas配置
cas:
  enabled: true
  server:
    host:
      # cas服务端地址
      url: http://localhost:8888/cas
      #cas服务端登录地址
      login_url: /login
      #cas服务端登出地址 service参数后面跟就是需要跳转的页面/接口 这里指定的是cas客户端登录接口
      logout_url: /logout?service=${cas.service.host.url}${cas.service.host.login_url}
  service:
    host:
      # cas客户端地址
      url: http://localhost:${server.port}
      # cas客户端地址登录地址
      login_url: /login/cas
      # cas客户端地址登出地址
      logout_url: /logout/cas
    web:
      url: http://localhost:8000
```
## 单体版核心功能

### 安全配置

允许登录`/xxx/rest/services/auth/login`、注册`/xxx/rest/services/auth/register`、验证码`/xxx/rest/services/auth/captchaImage`请求路径

对于退出`/xxx/rest/services/auth/logout`请求设置处理类`LogoutSuccessHandlerImpl`

```java
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
    	httpSecurity
        // CSRF禁用，因为不使用session
        .csrf().disable()
        // 认证失败处理类
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        // 基于token，所以不需要session
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        // 过滤请求
        .authorizeRequests()
        // 对于登录login 注册register 验证码captchaImage 允许匿名访问
        .antMatchers("/xxx/rest/services/auth/login", "/xxx/rest/services/auth/register", "/xxx/rest/services/auth/captchaImage").anonymous();
        httpSecurity.logout().logoutUrl("/xxx/rest/services/auth/logout")
        .logoutSuccessHandler(logoutSuccessHandler);
    }
}
```

### 登录流程

#### 1、获取验证码/xxx/rest/services/auth/captchaImage

直接由 mapgis-xxx-auth-server 中 CaptchaController 控制器/auth/captchaImage 接口对应的方法处理。

```java
@ApiOperation("生成验证码")
@GetMapping("/captchaImage")
public AjaxResult createCaptcha() throws IOException {
    return validateCodeService.createCaptcha();
}
```

#### 2、AuthController 控制器方法 login 进行处理

```java
@ApiOperation("登录方法")
@PostMapping("/login")
public AjaxResult login(@RequestBody LoginBody loginBody) {
    AjaxResult ajax = AjaxResult.success();
    // 生成令牌
    String token = loginService.login(loginBody);
    ajax.put(TokenConstants.TOKEN, token);
    return ajax;
}
```

#### 3、验证登录信息的有效性

校验验证码、验证用户

```java
@Component
public class SysLoginService {
    /**
     * 登录验证
     */
    public String login(LoginBody loginBody) {
        String username = loginBody.getUsername(), password = loginBody.getPassword();

        // 校验验证码
        validateCodeService.checkCaptcha(loginBody.getCode(), loginBody.getUuid());

        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        // ...
    }
}
```

UserDetailsServiceImpl.loadUserByUsername

```java
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException("登录用户：" + username + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", username);
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user.getUserId()));
    }
}
```

返回用户信息 LoginUser，除包含 SysUser 外，还包含了角色、权限信息。

#### 4、记录登录日志，更新用户登录信息

```java
@Component
public class SysLoginService {
    /**
     * 登录验证
     */
    public String login(LoginBody loginBody) {
    	// ...
    	AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }
}
```

#### 5、返回登录令牌

设置用户登录的更多信息，缓存用户登录信息（`login_tokens:`开头），生成 JWT Token

```java
@Component
public class TokenService {
	/**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        setUserAgent(loginUser);
        refreshToken(loginUser);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, token);

        // 生成token
        return JwtUtils.createToken(claimsMap);
    }
}
```

### 登出流程

#### 1、携带 Token 访问服务/xxx/rest/services/auth/logout

#### 2、`LogoutSuccessHandlerImpl`的`onLogoutSuccess`

#### 3、删除用户缓存记录

> TokenService->delLoginUser->CacheService.deleteObject

```java
tokenService.delLoginUser(SecurityUtils.getToken());
```

#### 4、记录登出日志

```java
// 记录用户退出日志
AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, "退出成功"));
```

### 注册流程

#### 1、带上请求体 RegisterBody 访问/xxx/rest/services/auth/register

```java
public class RegisterBody extends LoginBody {

}
```

#### 2、AuthController 控制器方法 register 进行处理

```java
@ApiOperation("用户注册")
@PostMapping("/register")
public AjaxResult register(@RequestBody RegisterBody registerBody) {
	// 用户注册
	loginService.register(registerBody);
	return AjaxResult.success();
}
```

#### 3、验证注册信息的有效性，进行注册

```java
@Component
public class SysLoginService {
public void register(RegisterBody registerBody) {
    String username = registerBody.getUsername(), password = registerBody.getPassword();

    // 校验验证码
    validateCodeService.checkCaptcha(registerBody.getCode(), registerBody.getUuid());
    // 注册用户信息
    SysUser sysUser = new SysUser();
    sysUser.setUserName(username);
    sysUser.setNickName(username);
    sysUser.setPassword(SecurityUtils.encryptPassword(password));
    R<?> registerResult = sysServiceApi.registerUserInfo(sysUser, SecurityConstants.INNER);

    if (R.FAIL == registerResult.getCode()) {
    throw new ServiceException(registerResult.getMsg());
    }
}
```

#### 4、记录注册登录日志

```java
@Component
public class SysLoginService {
public void register(RegisterBody registerBody) {
	// ...
	AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER,
                MessageUtils.message("user.register.success")));
}
```

### 服务访问流程

#### 1、携带令牌 Token 访问服务

#### 2、进入到 Token 过滤器

```java
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	/**
     * token认证过滤器
     */
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
    	// ...
    	// 添加JWT filter
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // ...
    }
}
```

#### 3、Token 过滤器中通过 Token 从缓存中获取用户

```java
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        LoginUser loginUser = tokenService.getLoginUser(request);

        // ...
    }
}

@Component
public class TokenService {
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token) {
        LoginUser user = null;
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userkey = JwtUtils.getUserKey(token);
                user = cacheService.getCacheObject(getTokenKey(userkey));
                return user;
            }
        } catch (Exception e) {
        }
        return user;
    }
}

public class SecurityUtils {
	/**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request) {
        // 从header获取token标识
        String token = request.getHeader(TokenConstants.AUTHENTICATION);
        return replaceTokenPrefix(token);
    }
}
```

#### 4、将登录用户记录到安全上下文中，便于本线程后面获取

```java
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getAuthentication())) {
            tokenService.verifyToken(loginUser);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        chain.doFilter(request, response);
    }
}
```

```java
public class SecurityUtils {
    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
```

#### 5、进入到各控制器相应的方法中处理

有需要可以通过 SecurityUtils 获取用户 ID、用户名称、用户 Key、登录用户信息等。

## 微服务版核心功能

### Nacos 持久化

nacos 自身提供了 mysql 的脚本，位于 conf 目录下，创建数据库，导入 sql 脚本后，需要修改 conf/application.properties 配置文件，添加 mysql 的数据源信息。

```
# db mysql
spring.datasource.platform=mysql
db.num=1
db.url.0=jdbc:mysql://localhost:3306/mapgis-server-config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user=root
db.password=password
```

### 认证

> 核心思路：
>
> - 登录：**网关**模块的 RouterFunctionConfiguration 提供/captchaImage 获取验证码和 UUID，前端将认证信息包装成 LoginBody 调用/auth/login 请求，在网关的过滤器 ValidateCodeFilter 中进行验证码的检查，通过后，进到**认证授权**模块的 AuthController 的 login 方法，通过远程调用`sysServiceApi.getUserInfo`获取用户信息，当用户存在的时候比较密码，匹配就继续调用`sysServiceApi.saveLogininfor`记录成功登录，同时创建令牌`tokenService.createToken(userInfo)`返回 LoginUser，失败就记录访问失败。
>
>   > 用户 token（随机 UUID）、userId、userName 生成 jwt 的 token，返回的是这个 token，会在缓存中记录这个用户 token 跟 LoginUser 的关系并设定过期时间，每次外部访问携带 jwt 的 token 进来后，要获取用户时`tokenSerivice.getLoginUser(token)`，都会先拿到用户 token，进而去缓存中拿到 LoginUser。
>
> - 登出：通过 jwt 的 token 删除用户缓存记录`cacheService.deleteObject(getTokenKey(JwtUtils.getUserKey(token)))`，并拿到用户名`JwtUtils.getUserName(token)`后记录退出登录`sysServiceApi.saveLogininfor`
>
> - 在线用户：获取特定缓存“login_tokens:”前缀的缓存列表，转换成用户信息，当强退时，直接删除用户 token 对应的缓存即可，继续访问时，虽然访问令牌存在，但是因为对应的用户缓存被清除，所以会直接抛出异常

### 接口调用

> 核心思路：
>
> - 网关全局过滤器 AuthFilter 会验证令牌的有效期，通过后会把用户信息设置到请求头上(user_key、user_id、username)，同时移除内部请求参数来源（防止外部伪造）
> - Feign 的请求拦截器会在调用时，传递请求头上的用户信息设置到 requestTemplate 请求头上(authorization、user_id、username)，目前 Fengin 的拦截器只存在与使用了 EnableCustomConfig 的应用
> - 拦截器 HeaderInterceptor 会把用户信息从请求头上拿到后设置到线程变量中方便获取，在请求完成后从线程变量中清除，这样实际请求过程中，不依赖 token 就可以便捷地从 SecurityUtils 中获取用户名或用户

### 异步执行

@EnableASync

@ASync

### 网关

- 网关限流，使用的是路由 id
- 限流规则持久化

### 登录流程

#### 1、获取验证码/xxx/rest/services/auth/captchaImage

基于网关的 RouterFunctionConfiguration 提供了`/xxx/rest/services/auth/captchaImage`的路由功能，可用于获取验证码 validateCodeService.createCapcha()。

```java
@Configuration
public class RouterFunctionConfiguration {
    @Autowired
    private ValidateCodeHandler validateCodeHandler;

    @SuppressWarnings("rawtypes")
    @Bean
    public RouterFunction routerFunction() {
        return RouterFunctions.route(
                RequestPredicates.GET("/xxx/rest/services/auth/captchaImage").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                validateCodeHandler);
    }
}

@Component
public class ValidateCodeHandler implements HandlerFunction<ServerResponse> {
    @Autowired
    private ValidateCodeService validateCodeService;

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        AjaxResult ajax;
        try {
            ajax = validateCodeService.createCaptcha();
        } catch (CaptchaException | IOException e) {
            return Mono.error(e);
        }
        return ServerResponse.status(HttpStatus.OK).body(BodyInserters.fromValue(ajax));
    }
}

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {
	/**
     * 生成验证码
     */
    @Override
    public AjaxResult createCaptcha() throws IOException, CaptchaException {

    }
}
```

#### 2、网关验证码过滤器 ValidateCodeFilter 拿到 body 中的 code 和 uuid 后进行验证

认证中心路由 id 配置了 2 个过滤器，其中 CacheRequestFilter 实现 body 请求数据流可重复读，ValidateCodeFilter 专门用于验证验证码。

```yaml
spring:
  cloud:
    gateway:
      routes:
        # 认证中心
        - id: mapgis-xxx-auth-server
          uri: lb://mapgis-xxx-auth-server
          predicates:
            - Path=/xxx/rest/services/auth/**
          filters:
            # 验证码处理
            - CacheRequestFilter
            - ValidateCodeFilter
```

```java
@Component
public class ValidateCodeFilter extends AbstractGatewayFilterFactory<Object> {
	@Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 非登录/注册请求或验证码关闭，不处理
            if (!StringUtils.containsAnyIgnoreCase(request.getURI().getPath(), VALIDATE_URL) || !captchaProperties.getEnabled()) {
                return chain.filter(exchange);
            }

            try {
                String rspStr = resolveBodyFromRequest(request);
                JSONObject obj = JSONObject.parseObject(rspStr);
                validateCodeService.checkCaptcha(obj.getString(CODE), obj.getString(UUID));
            } catch (Exception e) {
                return ServletUtils.webFluxResponseWriter(exchange.getResponse(), e.getMessage());
            }
            return chain.filter(exchange);
        };
    }
}
```

#### 3、网关鉴权全局过滤器 AuthFilter 验证

网关全局过滤器 AuthFilter 首先校验请求路径，位于白名单中的全部跳过，正好`/xxx/rest/services/auth/login`在白名单中。

```yaml
# 安全配置
security:
  # 不校验白名单
  ignore:
    whites:
      - /xxx/rest/services/auth/logout
      - /xxx/rest/services/auth/login
      - /xxx/rest/services/auth/register
      - /*/v2/api-docs
      - /csrf
```

```java
@Component
public class AuthFilter implements GlobalFilter, Ordered {
	@Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        String url = request.getURI().getPath();
        // 跳过不需要验证的路径
        if (StringUtils.matches(url, ignoreWhite.getWhites())) {
            return chain.filter(exchange);
        }
        String token = getToken(request);
        if (StringUtils.isEmpty(token)) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null) {
            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
        }
        String userkey = JwtUtils.getUserKey(claims);
        boolean islogin = cacheService.hasKey(getTokenKey(userkey));
        if (!islogin) {
            return unauthorizedResponse(exchange, "登录状态已过期");
        }
        String userid = JwtUtils.getUserId(claims);
        String username = JwtUtils.getUserName(claims);
        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username)) {
            return unauthorizedResponse(exchange, "令牌验证失败");
        }

        // 设置用户信息到请求
        addHeader(mutate, SecurityConstants.USER_KEY, userkey);
        addHeader(mutate, SecurityConstants.DETAILS_USER_ID, userid);
        addHeader(mutate, SecurityConstants.DETAILS_USERNAME, username);
        // 内部请求来源参数清除
        removeHeader(mutate, SecurityConstants.FROM_SOURCE);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }
}
```

#### AuthController 控制器方法 login 进行处理

经由网关进入到了微服务 mapgis-xxx-auth-server 中 AuthController 控制器/auth/login 接口对应的方法后。

```yaml
spring:
  cloud:
    gateway:
      routes:
        # 认证中心
        - id: mapgis-xxx-auth-server
          uri: lb://mapgis-xxx-auth-server
          predicates:
            - Path=/xxx/rest/services/auth/**
          filters:
            # 验证码处理
            - CacheRequestFilter
            - ValidateCodeFilter
```

```java
@ServicesRestController("/auth")
public class AuthController {
	@ApiOperation("登录方法")
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody);
        ajax.put(TokenConstants.TOKEN, token);
        return ajax;
    }
}
```

#### 5、通过系统模块的用户服务获取登录用户的信息 LoginUser

在认证中心微服务中调用系统模块的用户服务，采用 OpenFeign 来进行调用，系统模块提供了一个对应的系统接口模块，把其他微服务要用到的接口通过 OpenFeign 进行了包装，这样可以像调用本地服务一样来调用远程服务。

在微服务模块的入口需要添加@EnableFeignClients，这里有自定义注解@EnableMapFeignClients，在服务消费者的访问接口上需要添加@FeignClient。

```JAVA
/**
 * 系统服务API，提供其他独立模块调用
 *
 */
@FeignClient(contextId = "remoteSysServiceApi", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteSysServiceApiFallbackFactory.class)
public interface ISysServiceApi {
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @param source   请求来源
     * @return 结果
     */
    @GetMapping("/xxx/rest/manager/system/api/user/info/{username}")
    public R<LoginUser> getUserInfo(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 注册用户信息
     *
     * @param sysUser 用户信息
     * @param source  请求来源
     * @return 结果
     */
    @PostMapping("/xxx/rest/manager/system/api/user/register")
    public R<Boolean> registerUserInfo(@RequestBody SysUser sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
```

#### 6、验证登录信息的有效性

从系统模块拿到的登录用户信息 LoginUser，除包含 SysUser 外，还包含了角色、权限信息。

```java
@ManagerRestController("/system/api")
public class SysServiceApiController extends BaseController {
    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/user/info/{username}")
    public R<LoginUser> getUserInfo(@PathVariable("username") String username) {
        return sysServiceApi.getUserInfo(username, "");
    }
}

@Service("SysServiceApiImpl")
public class SysServiceApiImpl implements ISysServiceApi {
    @Override
    public R<LoginUser> getUserInfo(String username, String source) {
        SysUser sysUser = userService.selectUserByUserName(username);
        if (StringUtils.isNull(sysUser)) {
            return R.fail("用户名或密码错误");
        }
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(sysUser.getUserId());
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(sysUser.getUserId());
        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setUser(sysUser);
        sysUserVo.setRoles(roles);
        sysUserVo.setPermissions(permissions);
        return R.ok(sysUserVo);
    }
}
```

调用安全模块进行密码匹配验证。

```java
if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
            recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match"));
            throw new ServiceException("用户不存在/密码错误");
}
```

#### 7、调用系统模块的访问日志服务记录成功和失败的登录操作

访问日志服务是由系统模块微服务提供的，在认证中心模块内调用，需要和获取系统模块的用户信息一样，定义服务接口，采用 OpenFeign 的方式进行调用。

```java
/**
 * 系统服务API，提供其他独立模块调用
 *
 */
@FeignClient(contextId = "remoteSysServiceApi", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteSysServiceApiFallbackFactory.class)
public interface ISysServiceApi {
    /**
     * 保存系统日志
     *
     * @param sysOperLog 日志实体
     * @param source     请求来源
     * @return 结果
     */
    @PostMapping("/xxx/rest/manager/system/api/operlog")
    public R<Boolean> saveLog(@RequestBody SysOperLog sysOperLog, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 保存访问记录
     *
     * @param sysLogininfor 访问实体
     * @param source        请求来源
     * @return 结果
     */
    @PostMapping("/xxx/rest/manager/system/api/logininfor")
    public R<Boolean> saveLogininfor(@RequestBody SysLogininfor sysLogininfor, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}

@Component
public class SysLoginService {
    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     * @return
     */
    public void recordLogininfor(String username, String status, String message) {
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setUserName(username);
        logininfor.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        logininfor.setMsg(message);
        // 日志状态
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            logininfor.setStatus(Constants.LOGIN_SUCCESS_STATUS);
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            logininfor.setStatus(Constants.LOGIN_FAIL_STATUS);
        }
        sysServiceApi.saveLogininfor(logininfor, SecurityConstants.INNER);
    }
}
```

#### 8、返回登录信息，包括令牌

基于获取到的 LoginUser，设置用户 key、用户 id、用户名、登录 ip、登录时间、令牌过期时间，并通过用户 key、用户 id 和用户名调用 jjwt 生成令牌，返回给外部。

过期原理：依靠的是 redis 的**TTL**（Time To Live，剩余生存时间）失效机制实现。

```json
{
  "code": 200,
  "msg": null,
  "data": {
    "access_token": "...",
    "expires_in": 720
  }
}
```

```java
@Component
public class TokenService {
    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        Long userId = loginUser.getUser().getUserId();
        String userName = loginUser.getUser().getUserName();
        loginUser.setToken(token);
        loginUser.setUserId(userId);
        loginUser.setUsername(userName);
        loginUser.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        refreshToken(loginUser);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, userName);

        // 生成token
        return JwtUtils.createToken(claimsMap);
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        cacheService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }
}
```

### 登出流程

#### 1、携带 Token 访问服务/xxx/rest/services/auth/logout

#### 2、网关过滤器跳过（验证码过滤器、全局过滤器）

位于验证码的忽略名单和全局过滤器的白名单中，直接跳过。

```java
@Component
public class ValidateCodeFilter extends AbstractGatewayFilterFactory<Object> {
	@Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 非登录/注册请求或验证码关闭，不处理
            if (!StringUtils.containsAnyIgnoreCase(request.getURI().getPath(), VALIDATE_URL) || !captchaProperties.getEnabled()) {
                return chain.filter(exchange);
            }
            //...
		}
	}
}
```

#### 3、网关路由到 LoginController 的/logout 映射的方法

#### 4、删除用户缓存记录

> AuthUtil.logoutByToken->AuthLogic.logoutByToken->TokenService->delLoginUser->CacheService.deleteObject

```java
@ApiOperation("用户退出")
@DeleteMapping("logout")
public AjaxResult logout(HttpServletRequest request) {
    String token = SecurityUtils.getToken(request);
    if (StringUtils.isNotEmpty(token)) {
        String username = JwtUtils.getUserName(token);
        // 删除用户缓存记录
        AuthUtil.logoutByToken(token);
        // 记录用户退出日志
        loginService.logout(username);
    }
    return AjaxResult.success();
}
```

#### 5、记录登出日志

```java
@Component
public class SysLoginService  {
	public void logout(String loginName) {
        recordLogininfor(loginName, Constants.LOGOUT, "退出成功");
    }
}
```

### 注册流程

#### 1、带上请求体 RegisterBody 访问/xxx/rest/services/auth/register

```java
public class RegisterBody extends LoginBody {

}
```

#### 2、网关过滤器跳过（验证码过滤器、全局过滤器）

#### 3、网关路由到 AuthController 的/register 映射的方法

#### 4、调用系统模块的用户注册服务

```java
@Component
public class SysLoginService {
    /**
     * 注册
     */
    public void register(RegisterBody registerBody) {
        String username = registerBody.getUsername(), password = registerBody.getPassword();
        // 注册用户信息
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        R<?> registerResult = sysServiceApi.registerUserInfo(sysUser, SecurityConstants.INNER);

        if (R.FAIL == registerResult.getCode()) {
            throw new ServiceException(registerResult.getMsg());
        }
        recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success"));
    }
}
```

#### 5、记录注册日志

```java
recordLogininfor(username, Constants.REGISTER, "注册成功");
```

### 服务调用流程

#### 1、服务提供者添加自定义注解@EnableMapServerFeignClients

服务提供者需要@EnableFeignClients 注解，这里有自定义注解@EnableMapServerFeignClients，指定了@FeignClient 所在的包路径。

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
public @interface EnableMapFeignClients {
    String[] value() default {};

    String[] basePackages() default {"com.zondy.mapgis"};

    Class<?>[] basePackageClasses() default {};

    Class<?>[] defaultConfiguration() default {};

    Class<?>[] clients() default {};
}
```

#### 2、服务提供者添加自定义注解@EnableMapConfig

配置 Feign 拦截器，实现服务间调用时，请求头信息的修改。

> 实现 RequestInterceptor 接口的 apply 方法，feign 在发送请求之前都会调用该接口的 apply 方法。

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
// 指定要扫描的Mapper类的包的路径
@MapperScan("com.zondy.mapgis.**.mapper")
// 开启线程异步执行
@EnableAsync
// 自动加载类
@Import({ApplicationConfig.class, FeignAutoConfiguration.class})
public @interface EnableMapConfig {

}

/**
 * Feign 配置注册
 **/
@Configuration
public class FeignAutoConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignRequestInterceptor();
    }
}

/**
 * feign 请求拦截器
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest httpServletRequest = ServletUtils.getRequest();
        if (StringUtils.isNotNull(httpServletRequest)) {
            Map<String, String> headers = ServletUtils.getHeaders(httpServletRequest);
            // 传递用户信息请求头，防止丢失
            String userId = headers.get(SecurityConstants.DETAILS_USER_ID);
            if (StringUtils.isNotEmpty(userId)) {
                requestTemplate.header(SecurityConstants.DETAILS_USER_ID, userId);
            }
            String userName = headers.get(SecurityConstants.DETAILS_USERNAME);
            if (StringUtils.isNotEmpty(userName)) {
                requestTemplate.header(SecurityConstants.DETAILS_USERNAME, userName);
            }
            String authentication = headers.get(SecurityConstants.AUTHORIZATION_HEADER);
            if (StringUtils.isNotEmpty(authentication)) {
                requestTemplate.header(SecurityConstants.AUTHORIZATION_HEADER, authentication);
            }

            // 配置客户端IP
            requestTemplate.header("X-Forwarded-For", IpUtils.getIpAddr(ServletUtils.getRequest()));
        }
    }
}
```

#### 3、对于内部服务（不能通过网关调用）在服务提供者的控制器方法上添加@InnerAuth 注解

用于验证内部服务调用的有效性，内部调用时，会在请求头上添加“from-source”为"inner"，假如用户从网关层调用该服务，会在网关的全局过滤器中移除请求头上的“from-source”标识，让其不能访问。

```java
/**
 * 内部服务调用验证处理
 */
@Aspect
@Component
public class InnerAuthAspect implements Ordered {
    @Around("@annotation(innerAuth)")
    public Object innerAround(ProceedingJoinPoint point, InnerAuth innerAuth) throws Throwable {
        String source = ServletUtils.getRequest().getHeader(SecurityConstants.FROM_SOURCE);
        // 内部请求验证
        if (!StringUtils.equals(SecurityConstants.INNER, source)) {
            throw new InnerAuthException("没有内部访问权限，不允许访问");
        }

        String userid = ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_USER_ID);
        String username = ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_USERNAME);
        // 用户信息验证
        if (innerAuth.isUser() && (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username))) {
            throw new InnerAuthException("没有设置用户信息，不允许访问 ");
        }
        return point.proceed();
    }

    /**
     * 确保在权限认证aop执行前执行
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}

/**
 * 网关鉴权
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
	@Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    	//...
    	// 内部请求来源参数清除
        removeHeader(mutate, SecurityConstants.FROM_SOURCE);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }
}
```

#### 4、服务消费者定义服务访问接口添加@FeignClient

对于只能内部访问的服务，会在定义服务访问接口时添加参数`@RequestHeader(SecurityConstants.FROM_SOURCE) String source`，对于 Feign 的调用添加容错处理，定义 fallbackFactory。当微服务出错时，进行提示。

```java
/**
 * 系统服务API，提供其他独立模块调用
 */
@FeignClient(contextId = "remoteSysServiceApi", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteSysServiceApiFallbackFactory.class)
public interface ISysServiceApi {
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @param source   请求来源
     * @return 结果
     */
    @GetMapping("/xxx/rest/manager/system/api/user/info/{username}")
    public R<LoginUser> getUserInfo(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
```

### 网关访问流程

#### 1、携带令牌 Token 访问服务

#### 2、网关全局过滤器 AuthFilter 验证

验证 Token 的有效性，令牌不能为空、令牌需要符合规范（能通过 jjwt 获取数据声明）、能从 cacheService 中得到用户 key（通过 token 获取）对应的信息（表示用户登录），通过后将用户信息添加到请求头上，这样网关路由后的服务可以从头上获取解析后的用户 key、用户 id 和用户名。

```java
/**
 * 网关鉴权
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        String url = request.getURI().getPath();
        // 跳过不需要验证的路径
        if (StringUtils.matches(url, ignoreWhite.getWhites())) {
            return chain.filter(exchange);
        }
        String token = getToken(request);
        if (StringUtils.isEmpty(token)) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null) {
            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
        }
        String userkey = JwtUtils.getUserKey(claims);
        boolean islogin = cacheService.hasKey(getTokenKey(userkey));
        if (!islogin) {
            return unauthorizedResponse(exchange, "登录状态已过期");
        }
        String userid = JwtUtils.getUserId(claims);
        String username = JwtUtils.getUserName(claims);
        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username)) {
            return unauthorizedResponse(exchange, "令牌验证失败");
        }

        // 设置用户信息到请求
        addHeader(mutate, SecurityConstants.USER_KEY, userkey);
        addHeader(mutate, SecurityConstants.DETAILS_USER_ID, userid);
        addHeader(mutate, SecurityConstants.DETAILS_USERNAME, username);
        // 内部请求来源参数清除
        removeHeader(mutate, SecurityConstants.FROM_SOURCE);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }
}
```

#### 3、网关路由到相应的微服务中，被 HeaderInterceptor 拦截

只要依赖了 mapgis-common-cloud-security 模块，会自动进行 WebMvcConfigurer 配置，添加 HeaderInterceptor 拦截器，HeaderInterceptor 负责从请求头上拿到在网关上添加的用户信息进行保存，并通过 SecurityUtils.getToken 获取到令牌，进而通过 AuthUtil.getLoginUser 获取到 LoginUser，验证 AuthUtil.verifyLoginUserExpire 有效后，会将 LoginUser 也保存到当前的线程变量中，这样微服务本身就可以直接通过 SecurityUtils 获取到用户和用户信息了。

> 方便获取，在请求头拦截器上做了此操作，其他地方可以非常简单地获取这些信息。

```java
/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取
 * 注意：此拦截器会同时验证当前用户有效期自动刷新有效期
 */
public class HeaderInterceptor implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        SecurityContextHolder.setUserId(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USER_ID));
        SecurityContextHolder.setUserName(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USERNAME));
        SecurityContextHolder.setUserKey(ServletUtils.getHeader(request, SecurityConstants.USER_KEY));

        String token = SecurityUtils.getToken();
        if (StringUtils.isNotEmpty(token)) {
            LoginUser loginUser = AuthUtil.getLoginUser(token);
            if (StringUtils.isNotNull(loginUser)) {
                AuthUtil.verifyLoginUserExpire(loginUser);
                SecurityContextHolder.set(SecurityConstants.LOGIN_USER, loginUser);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        SecurityContextHolder.remove();
    }
}
```

```java
/**
 * 安全服务工具类
 */
public class SecurityUtils {
    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return SecurityContextHolder.getUserId();
    }

    /**
     * 获取用户名称
     */
    public static String getUsername() {
        return SecurityContextHolder.getUserName();
    }

    /**
     * 获取用户key
     */
    public static String getUserKey() {
        return SecurityContextHolder.getUserKey();
    }

    /**
     * 获取登录用户信息
     */
    public static LoginUser getLoginUser() {
        return SecurityContextHolder.get(SecurityConstants.LOGIN_USER, LoginUser.class);
    }

    /**
     * 获取请求token
     */
    public static String getToken() {
        return getToken(ServletUtils.getRequest());
    }

    /**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request) {
        // 从header获取token标识
        String token = request.getHeader(TokenConstants.AUTHENTICATION);
        return replaceTokenPrefix(token);
    }
}
```

#### 4、路由到相应的微服务控制器中

有需要可以通过 SecurityUtils 获取用户 ID、用户名称、用户 Key、登录用户信息等。

### 网关限流流程

#### 1、配置各微服务采用 sentinel 进行限流

```yaml
# feign 配置
feign:
  sentinel:
    enabled: true
```

```xml
<!-- SpringCloud Alibaba Sentinel -->
<dependency>
	<groupId>com.alibaba.cloud</groupId>
	<artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>
```

#### 2、网关模块限流（支持根据 Route ID 进行规则设置）

```xml
<!-- SpringCloud Alibaba Sentinel Gateway -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-sentinel-gateway</artifactId>
</dependency>
```

```xml
sentinel:
  # 取消控制台懒加载
  eager: true
  transport:
    # 控制台地址
    dashboard: 127.0.0.1:8718
```

#### 3、配置限流规则持久化

```xml
spring:
  cloud:
    sentinel:
      # nacos配置持久化
      datasource:
        ds1:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: sentinel-mapgis-xxx-gateway-server
            data-type: json
            rule-type: flow
```

sentinel-mapgis-xxx-gateway-server 是 json 格式。

```
[
  {
    "resource": "mapgis-xxx-auth-server",
    "count": 500,
    "grade": 1,
    "limitApp": "default",
    "strategy": 0,
    "controlBehavior": 0
  },
	{
    "resource": "mapgis-xxx-system-server",
    "count": 1000,
    "grade": 1,
    "limitApp": "default",
    "strategy": 0,
    "controlBehavior": 0
  },
	{
    "resource": "mapgis-xxx-job-server",
    "count": 300,
    "grade": 1,
    "limitApp": "default",
    "strategy": 0,
    "controlBehavior": 0
  }
]
```

#### 4、自定义限流异常

```java
@Configuration
public class GatewayConfig {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelFallbackHandler sentinelGatewayExceptionHandler() {
        return new SentinelFallbackHandler();
    }
}

public class SentinelFallbackHandler implements WebExceptionHandler {
    private Mono<Void> writeResponse(ServerResponse response, ServerWebExchange exchange) {
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), "请求超过最大数，请稍候再试");
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        if (!BlockException.isBlockException(ex)) {
            return Mono.error(ex);
        }
        return handleBlockedRequest(exchange, ex).flatMap(response -> writeResponse(response, exchange));
    }

    private Mono<ServerResponse> handleBlockedRequest(ServerWebExchange exchange, Throwable throwable) {
        return GatewayCallbackManager.getBlockHandler().handleRequest(exchange, throwable);
    }
}
```
