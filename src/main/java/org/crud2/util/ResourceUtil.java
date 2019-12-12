package org.crud2.util;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

public class ResourceUtil {
    public static String readText(Resource resource) throws IOException {
        InputStream stream = null;
        stream = resource.getInputStream();
        String text = StringUtil.fromInputStream(stream);
        stream.close();
        return text;
    }
}
