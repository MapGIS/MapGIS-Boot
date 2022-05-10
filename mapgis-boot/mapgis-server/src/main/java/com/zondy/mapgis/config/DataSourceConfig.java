package com.zondy.mapgis.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DruidDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.zondy.mapgis.common.core.utils.EnvironmentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.sqlite.SQLiteConfig;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 数据源配置
 *
 * @author xiongbo
 * @since 2022/5/9 20:31
 */
@Slf4j
@Configuration
public class DataSourceConfig {
    @Autowired
    private DruidDataSourceCreator druidDataSourceCreator;

    @Value("${DB_TYPE:sqlite}")
    private String dbType;

    @Value("${DB_HOST:localhost}")
    private String dbHost;

    @Value("${DB_PORT:3306}")
    private String dbPort;

    @Value("${DB_NAME:mapgis-xxx}")
    private String dbName;

    @Value("${DB_USER:root}")
    private String dbUser;

    @Value("${DB_PWD:123456}")
    private String dbPwd;

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
        return dataSource;
    }

    void addMySQLDataSource(DynamicRoutingDataSource dynamicRoutingDataSource) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        String dbUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";

        dataSourceProperty.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceProperty.setUrl(dbUrl);
        dataSourceProperty.setUsername(dbUser);
        dataSourceProperty.setPassword(dbPwd);

        DataSource dataSource = druidDataSourceCreator.createDataSource(dataSourceProperty);
        dynamicRoutingDataSource.addDataSource("master", dataSource);
    }

    void addSQLiteDataSource(DynamicRoutingDataSource dynamicRoutingDataSource) {
        Path dbPath = Paths.get(EnvironmentUtil.getCurrentProjectPath(), "data", "mapgis-xxx" + ".db").toAbsolutePath();
        File dir = dbPath.toFile().getParentFile();
        if (!dir.exists() && !dir.mkdirs()) {
            log.error("生成data目录失败，无法创建数据库");
        }

        DataSourceProperty dataSourceProperty = new DataSourceProperty();

        String dbUrl = "jdbc:sqlite:" + dbPath + "?date_string_format=yyyy-MM-dd HH:mm:ss";
        dataSourceProperty.setDriverClassName("org.sqlite.JDBC");
        dataSourceProperty.setUrl(dbUrl);

        SQLiteConfig config = new SQLiteConfig();

        config.setJournalMode(SQLiteConfig.JournalMode.WAL);
        config.setSynchronous(SQLiteConfig.SynchronousMode.FULL);
        // 超过阻塞时间会报"database is locked"错误，暂时设置阻塞时间为60s
        config.setBusyTimeout(60000);
        config.enforceForeignKeys(true);

        dataSourceProperty.getDruid().setConnectionProperties(config.toProperties());
        DataSource dataSource = druidDataSourceCreator.createDataSource(dataSourceProperty);
        dynamicRoutingDataSource.addDataSource("master", dataSource);
    }
}
