package org.crud2.autoengine.plugin;

import org.crud2.autoengine.config.Module;
import java.util.List;
import java.util.Map;

public interface Plugin<T> {
    List<T> query(Module module, Map<String, Object> params);
}
