package com.githab.warehouse.dao;

import com.githab.warehouse.exception.DataBaseException;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PooledDataSource extends PGSimpleDataSource {

    private final Queue<Connection> connectionPool;
    private final int POOL_SIZE = 10;


    public PooledDataSource() throws SQLException {
        super();
        PGSimpleDataSource dataSource = initializeDataSource();

        connectionPool = new ConcurrentLinkedDeque<>();

        for (int i = 0; i < POOL_SIZE; i++) {
            Connection physicalConnection = dataSource.getConnection();
            ConnectionProxy connection = new ConnectionProxy(physicalConnection, connectionPool);
            connectionPool.add(connection);
        }
    }

    public void initPooledDataSource() throws SQLException {
        PooledDataSource dataSource = new PooledDataSource();
        Connection connection = dataSource.getConnection();
        initSqlScrypt(connection);

    }

    @Override
    public Connection getConnection() {
        return connectionPool.poll();
    }

    private static PGSimpleDataSource initializeDataSource() {

        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        Properties props = new Properties();
        InputStream inputStream = null;

        try {
            inputStream = PooledDataSource.class.getClassLoader().getResourceAsStream("application.properties");
            props.load(inputStream);

            String url = props.getProperty("datasource.url");
            String user = props.getProperty("datasource.username");
            String password = props.getProperty("datasource.password");

            dataSource.setURL(url);
            dataSource.setUser(user);
            dataSource.setPassword(password);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return dataSource;
    }

    private static void initSqlScrypt(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            String scriptPath = "src/main/resources/init.sql";
            String sqlScript = new String(Files.readAllBytes(Paths.get(scriptPath)), StandardCharsets.UTF_8);

            statement.execute(sqlScript);
        } catch (SQLException | IOException e) {
                throw new DataBaseException("There is an exception during init SQL scrypt", e.getCause());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DataBaseException("There is an exception during Connection close", e.getCause());
            }
        }
    }
}
