package org.crud2.autoengine.web;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class DefaultRequestSqlParameterGetter implements RequestSqlParameterGetter {
    @Autowired
    private ModuleSqlTextParameterConfig moduleSqlTextParameterConfig;

    @Override
    public Map<String, Object> get(String moduleId, HttpServletRequest request) {
        String[] names = moduleSqlTextParameterConfig.get(moduleId);
        if (names == null) return null;
        Map<String, Object> parameters = new HashMap<>();
        Object value;
        /*
         * step1:simple parameters from form|querystring|payload
         */
        {
            if (request.getMethod().equals("FORM")) {
                String contentType = request.getContentType();
                if (contentType.contains("application/json")) {
                    //request.getInputStream();//?stream can just read once
                } else if (contentType.contains("application/xml")) {
                    //TODO:fix xml getter
                }
                for (String name : names) {
                    if (parameters.containsKey(name)) continue;
                    value = request.getParameter(name);
                    if (value != null) {
                        parameters.put(name, value);
                    }
                }
            }
        }
        /*
         * step2:simple parameters from session
         */
        {
            HttpSession session = request.getSession();
            for (String name : names) {
                if (parameters.containsKey(name)) continue;
                value = session.getAttribute(name);
                if (value != null) {
                    parameters.put(name, value);
                }
            }
        }
        /*
         * step3:fix null values
         */
        {
            for (String name : names) {
                if (parameters.containsKey(name)) continue;
                parameters.put(name, null);
            }
        }
        return parameters;
    }
}
