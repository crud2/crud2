package org.crud2.autoengine.config.json;

import com.alibaba.fastjson.JSON;
import org.crud2.autoengine.config.Module;
import org.crud2.autoengine.config.ModuleConfigReader;
import org.crud2.util.StringUtil;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class JsonModuleConfigReader implements ModuleConfigReader {
    @Override
    public Module read(InputStream inputStream) {
        String configStr = StringUtil.fromInputStream(inputStream);
        if (configStr.length() == 0) return null;
        return JSON.parseObject(configStr, Module.class);
    }
}
