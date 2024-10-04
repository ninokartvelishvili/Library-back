package me.nino.library_back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DataBaseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String checkDatabaseConnection() {
        int a = 5;
        try {
            jdbcTemplate.execute("SELECT 1");
            return "Connection to the database was successful!";
        } catch (Exception e) {
            return "Failed to connect to the database: " + e.getMessage();
        }
    }
}