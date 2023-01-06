package com.zondy.mapgis.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DruidDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.druid.DruidConfig;
import com.zondy.mapgis.common.core.utils.EnvironmentUtil;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
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

    @Value("${DB_NAME:${mapgis.product.full-name}}")
    private String dbName;

    @Value("${DB_USER:root}")
    private String dbUser;

    @Value("${DB_PWD:cloud123.mapgis}")
    private String dbPwd;

    @Primary
    @Bean
    public DataSource dataSource(DynamicDataSourceProperties properties) {
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();

        dynamicRoutingDataSource.setPrimary(properties.getPrimary());
        dynamicRoutingDataSource.setStrict(properties.getStrict());
        dynamicRoutingDataSource.setStrategy(properties.getStrategy());
        dynamicRoutingDataSource.setP6spy(properties.getP6spy());
        dynamicRoutingDataSource.setSeata(properties.getSeata());

        String rootPath = EnvironmentUtil.getCurrentProjectPath();
        DataSource masterDataSource = null;
        DataSource accessLogDataSource = null;
        DataSource hardwareMonitorDataSource = null;
        String accessLogDbName = dbName + "-access-log";
        String accessLogSqlPath = dbType + "-access-log";
        String hardwareMonitorDbName = dbName + "-hardware-monitor";
        String hardwareMonitorSqlPath = dbType + "-hardware-monitor";

        // 动态添加数据源
        switch (dbType) {
            case "mysql":
                masterDataSource = createMySQLDataSource(dbName);
                accessLogDataSource = createMySQLDataSource(accessLogDbName);
                hardwareMonitorDataSource = createMySQLDataSource(hardwareMonitorDbName);
                break;
            default:
                masterDataSource = createSQLiteDataSource(rootPath, dbName);
                accessLogDataSource = createSQLiteDataSource(rootPath, accessLogDbName);
                hardwareMonitorDataSource = createSQLiteDataSource(rootPath, hardwareMonitorDbName);
        }

        // master
        migrateDataBase(masterDataSource, dbName, dbType);
        dynamicRoutingDataSource.addDataSource("master", masterDataSource);

        // access_log
        migrateDataBase(accessLogDataSource, accessLogDbName, accessLogSqlPath);
        dynamicRoutingDataSource.addDataSource("access_log", accessLogDataSource);

        // hardware_monitor
        migrateDataBase(hardwareMonitorDataSource, hardwareMonitorDbName, hardwareMonitorSqlPath);
        dynamicRoutingDataSource.addDataSource("hardware_monitor", hardwareMonitorDataSource);

        return dynamicRoutingDataSource;
    }

    DataSource createMySQLDataSource(String db) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        String dbUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + db + "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";

        dataSourceProperty.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceProperty.setUrl(dbUrl);
        dataSourceProperty.setUsername(dbUser);
        dataSourceProperty.setPassword(dbPwd);

        return druidDataSourceCreator.createDataSource(dataSourceProperty);
    }

    DataSource createSQLiteDataSource(String rootPath, String db) {
        Path dbPath = Paths.get(rootPath, "data", db + ".db").toAbsolutePath();
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
        config.setBusyTimeout(60000);
        config.enforceForeignKeys(true);

        DruidConfig druidConfig = dataSourceProperty.getDruid();
        //解决sqlite "database is locked"的问题
        druidConfig.setInitialSize(1);
        druidConfig.setMaxActive(1);
        druidConfig.setMinIdle(0);
        dataSourceProperty.getDruid().setConnectionProperties(config.toProperties());
        return druidDataSourceCreator.createDataSource(dataSourceProperty);
    }

    void migrateDataBase(DataSource dataSource, String dbName, String sqlPath) {
        try {
            org.flywaydb.core.api.configuration.Configuration configuration = Flyway.configure().dataSource(dataSource).baselineDescription("initByServer").baselineOnMigrate(true).validateOnMigrate(false).locations(String.format("classpath:data/migration/%s", sqlPath));
            Flyway flyway = new Flyway(configuration);
            flyway.migrate();
        } catch (Exception e) {
            log.error("数据库" + dbName + "迁移出现异常", e);
        }
    }
}
