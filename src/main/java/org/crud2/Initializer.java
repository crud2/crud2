package org.crud2;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.crud2.query.condition.operator.NormalOperatorRegistrar;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration("curd2initializer")
@MapperScan("org.crud2.mybatis.dao")
public class Initializer {
    private static Logger logger = LoggerFactory.getLogger(Initializer.class);

    @Autowired
    private SqlSessionFactory factory;

    @Autowired
    private NormalOperatorRegistrar normalOperatorRegistrar;

    public void initialize() {
        try {
            Resource[] mapperLocations = new PathMatchingResourcePatternResolver().getResources("classpath:mappers/*.xml");
            for (Resource mapper : mapperLocations) {
                XMLMapperBuilder builder = new XMLMapperBuilder(mapper.getInputStream(),
                        factory.getConfiguration(),
                        mapper.getFilename(),
                        factory.getConfiguration().getSqlFragments()
                );
                builder.parse();
                logger.debug("load crud2 mybatis xml mapper :" + mapper.getFilename());
            }
            logger.info("load crud2 mybatis xml mapper complete");

        } catch (Exception ex) {
            logger.error("init mapper fail", ex);
        }

        factory.getConfiguration().addInterceptor(
                new com.github.pagehelper.PageInterceptor()
        );
    }
}
