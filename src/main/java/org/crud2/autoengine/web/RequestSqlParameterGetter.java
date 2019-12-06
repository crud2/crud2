package org.crud2.autoengine.web;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface RequestSqlParameterGetter {
    Map<String, Object> get(String moduleId, HttpServletRequest request);

    Map<String, Object> get(String moduleId, String column, HttpServletRequest request);
}
