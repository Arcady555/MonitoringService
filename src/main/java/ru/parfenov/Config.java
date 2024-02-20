package ru.parfenov;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RestController;
import ru.parfenov.server.controller.login.PointValueController;
import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.service.JdbcPointValueService;
import ru.parfenov.server.service.JdbcUserService;
import ru.parfenov.server.service.PointValueService;
import ru.parfenov.server.service.UserService;
import ru.parfenov.server.store.PointValueStore;
import ru.parfenov.server.store.SqlPointValueStore;
import ru.parfenov.server.store.SqlUserStore;
import ru.parfenov.server.store.UserStore;

import java.sql.SQLException;

//@Configuration
//@ComponentScan(basePackages = "ru.parfenov")
//@PropertySource("classpath:application.yml")
public class Config {

 /*   @Bean
    public PointValueStore getPointValueStore() throws SQLException, ClassNotFoundException {
        return new SqlPointValueStore();
    }

    @Bean
    public UserStore getUserStore() throws SQLException, ClassNotFoundException {
        return new SqlUserStore();
    }

    @Bean
    public PointValueService getJdbcPointValueService() throws SQLException, ClassNotFoundException {
        return new JdbcPointValueService(getUserStore(), getPointValueStore());
    }

    @Bean
    public UserService getJdbcUserService() throws SQLException, ClassNotFoundException {
        return new JdbcUserService(getUserStore());
    } */
}
