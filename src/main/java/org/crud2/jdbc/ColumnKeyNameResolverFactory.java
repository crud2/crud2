package org.crud2.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ColumnKeyNameResolverFactory {
    @Bean
    public ColumnKeyNameResolver columnKeyNameResolver() {
        return new LowerCaseColumnKeyNameResolver();
    }
}
