package org.crud2.autoengine.config.json;

import com.alibaba.fastjson.JSON;
import org.crud2.autoengine.config.Module;
import org.crud2.autoengine.config.ModuleConfigReader;
import org.springframework.stereotype.Component;

@Component
public class JsonModuleConfigReader implements ModuleConfigReader {
    @Override
    public Module read(String configString) {
        return JSON.parseObject(configString, Module.class);
    }
}
