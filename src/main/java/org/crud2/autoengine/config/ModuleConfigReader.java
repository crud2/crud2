package org.crud2.autoengine.config;

import java.io.InputStream;

public interface ModuleConfigReader {
    Module read(InputStream inputStream);
}
