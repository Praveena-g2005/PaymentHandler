package com.paymenthandler.web;

import org.h2.jdbcx.JdbcDataSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

@WebListener
public class DatabaseInitListener implements ServletContextListener {

    private static final String DB_URL = "jdbc:h2:mem:paymenthandler;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("=== Payment Handler Application Starting ===");
        System.out.println("Initializing database...");

        initializeDatabase();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("=== Payment Handler Application Stopping ===");
    }

    private void initializeDatabase() {
        try {
           
            JdbcDataSource dataSource = new JdbcDataSource();
            dataSource.setURL(DB_URL);
            dataSource.setUser(DB_USER);
            dataSource.setPassword(DB_PASSWORD);

            
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {

                
                InputStream is = getClass().getClassLoader().getResourceAsStream("schema.sql");
                if (is == null) {
                    System.err.println("ERROR: schema.sql not found in classpath!");
                    return;
                }

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                    String sql = reader.lines().collect(Collectors.joining("\n"));

                    String[] statements = sql.split(";");
                    for (String sqlStatement : statements) {
                        
                        String cleaned = sqlStatement.lines()
                            .filter(line -> !line.trim().startsWith("--"))
                            .collect(Collectors.joining("\n"))
                            .trim();

                        if (!cleaned.isEmpty()) {
                            System.out.println("Executing SQL: " + cleaned.substring(0, Math.min(50, cleaned.length())) + "...");
                            stmt.execute(cleaned);
                        }
                    }

                    System.out.println("✓ Database schema initialized successfully");
                    System.out.println("✓ Sample data loaded");
                }
            }

        } catch (Exception e) {
            System.err.println("ERROR initializing database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
