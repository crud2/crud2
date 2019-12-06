package org.crud2.autoengine.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class DefaultRequestSqlParameterGetter implements RequestSqlParameterGetter {
    private static Logger logger = LoggerFactory.getLogger(DefaultRequestSqlParameterGetter.class);

    /**
     * this config must implement by web client
     */
    @Autowired
    private ModuleSqlTextParameterConfig moduleSqlTextParameterConfig;

    @Override
    public Map<String, Object> get(String moduleId, HttpServletRequest request) {
        String[] names = moduleSqlTextParameterConfig.get(moduleId);
        return get(names,request);
    }

    @Override
    public Map<String, Object> get(String moduleId, String column, HttpServletRequest request) {
        String[] names = moduleSqlTextParameterConfig.get(moduleId,column);
        return get(names,request);
    }

    private Map<String,Object> get(String[] names,HttpServletRequest request){
        if (names == null) return null;
        Map<String, Object> parameters = new HashMap<>();
        Object value;
        /*
         * step1:simple parameters from form|querystring|payload
         */
        {
            if (request.getMethod().equals("POST")) {
                String contentType = request.getContentType();
                if (contentType.contains("application/json")) {
                    String json = getRequestBody(request);
                    JSONObject jsonObject = JSON.parseObject(json);
                    parameters.putAll(jsonObject);
                } else if (contentType.contains("application/xml")) {
                    //TODO:xml parse
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

    private String getRequestBody(HttpServletRequest request) {
        try {
            if (request instanceof ReusableHttpServletRequestWrapper) {//post json
                return ((ReusableHttpServletRequestWrapper) request).getRequestContent();
            } else {
                Map<String, Object> params = new HashMap<>(request.getParameterMap());
                if (params.size() > 0) {
                    return new JSONObject(params).toJSONString();
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("get request body error", e);
        } catch (Exception e) {
            logger.error("get request body error", e);
        }
        return null;
    }
}
