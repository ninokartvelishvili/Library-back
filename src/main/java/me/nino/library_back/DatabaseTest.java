package me.nino.library_back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DatabaseTest {

    @Autowired
    private DataBaseController dataBaseController;

    @GetMapping("/check-connection")
    public String connectionCheck() {
        try {
            dataBaseController.checkDatabaseConnection();
            return "its working";
        } catch (Exception e) {
            return "try again";
        }
    }

}
