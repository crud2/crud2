package org.crud2.autoengine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AutoEngineSessionFactory {
    @Autowired
    private DataSource dataSource;

    @Bean
    public AutoEngineSessionBean autoEngineSessionBean() {
        return moduleId -> dataSource;
    }
}
