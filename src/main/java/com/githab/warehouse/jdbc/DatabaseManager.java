package com.githab.warehouse.jdbc;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseManager {

    private final static Logger L = LoggerFactory.getLogger(DatabaseManager.class);

    private static HikariDataSource ds;

    public static void setup (){
        Properties props = new Properties();
        InputStream inputStream = DatabaseManager.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            props.load(inputStream);
        } catch (IOException e) {
            L.error("There is an exception during loading DB props");
        }

        HikariConfig hikariConfig = new HikariConfig(props);
        ds = new HikariDataSource(hikariConfig);
    }

    public static HikariDataSource getDataSource() {
        return ds;
    }
    public static void destroy() {
        ds.close();
        L.info("Main database was closed.");

    }

}
