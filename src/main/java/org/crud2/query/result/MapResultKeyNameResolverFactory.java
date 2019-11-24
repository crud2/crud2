package org.crud2.query.result;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapResultKeyNameResolverFactory {
    @Bean
    public MapResultKeyNameResolver mapResultKeyNameResolver() {
        return new LowerCaseMapResultKeyNameResolver();
    }
}
