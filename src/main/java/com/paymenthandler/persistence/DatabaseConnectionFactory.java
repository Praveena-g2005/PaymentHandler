package com.paymenthandler.persistence;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;


@ApplicationScoped
public class DatabaseConnectionFactory {

    private static final String DB_URL = "jdbc:h2:mem:paymenthandler;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private DataSource dataSource;

    @PostConstruct
    public void init() {
        
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL(DB_URL);
        ds.setUser(DB_USER);
        ds.setPassword(DB_PASSWORD);
        this.dataSource = ds;

        initializeSchema();
    }

    // CDI Producer method for DataSource
     
    @Produces
    @Named("dataSource")
    public DataSource getDataSource() {
        return dataSource;
    }

    
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private void initializeSchema() {
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            
            InputStream is = getClass().getClassLoader().getResourceAsStream("schema.sql");
            if (is == null) {
                System.err.println("Warning: schema.sql not found");
                return;
            }

            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String sql = reader.lines().collect(Collectors.joining("\n"));

                
                String[] statements = sql.split(";");
                for (String sqlStatement : statements) {
                    String trimmed = sqlStatement.trim();
                    if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
                        stmt.execute(trimmed);
                    }
                }

                System.out.println("✓ Database schema initialized successfully");
            }

        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
