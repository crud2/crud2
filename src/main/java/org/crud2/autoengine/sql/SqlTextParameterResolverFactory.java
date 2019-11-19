package org.crud2.autoengine.sql;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SqlTextParameterResolverFactory {
    @Bean
    public SqlTextParameterResolver sqlTextParameterResolver() {
        return new SqlTextMacroParameterResolver();
    }
}
