package com.bikeshare.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfig {

    @Value("${DB_URL:}")
    private String dbUrl;

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Value("${DB_USERNAME:}")
    private String dbUsername;

    @Value("${DB_PASSWORD:}")
    private String dbPassword;

    @Bean
    public DataSource dataSource() throws URISyntaxException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");

        String targetUrl = dbUrl;
        if (targetUrl == null || targetUrl.isEmpty()) {
            targetUrl = databaseUrl;
        }

        if (targetUrl != null && !targetUrl.isEmpty()) {
            if (targetUrl.startsWith("postgres://")) {
                URI dbUri = new URI(targetUrl);
                String username = null;
                String password = null;
                
                if (dbUri.getUserInfo() != null) {
                    String[] userInfo = dbUri.getUserInfo().split(":");
                    username = userInfo[0];
                    if (userInfo.length > 1) {
                        password = userInfo[1];
                    }
                }
                
                String dbUrlConverted = "jdbc:postgresql://" + dbUri.getHost() + 
                                       (dbUri.getPort() != -1 ? ":" + dbUri.getPort() : "") + 
                                       dbUri.getPath();
                
                dataSource.setJdbcUrl(dbUrlConverted);
                if (username != null) dataSource.setUsername(username);
                if (password != null) dataSource.setPassword(password);
            } else {
                dataSource.setJdbcUrl(targetUrl);
            }
            
            // Override with custom user/pass if supplied explicitly
            if (dbUsername != null && !dbUsername.isEmpty()) {
                dataSource.setUsername(dbUsername);
            }
            if (dbPassword != null && !dbPassword.isEmpty()) {
                dataSource.setPassword(dbPassword);
            }
            
        } else {
            // Local fallback
            dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/bikeshare");
            dataSource.setUsername("root");
            dataSource.setPassword("password");
        }

        return dataSource;
    }
}
