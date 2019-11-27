package org.crud2.autoengine.listsource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListSourceParseFactory {
    @Bean
    public ListSourceParse keyValueListSourceParse() {
        return new KeyValueListSourceParse();
    }

    @Bean
    public SqlListSourceParse sqlListSourceParse() {
        return new SqlListSourceParse();
    }
}
