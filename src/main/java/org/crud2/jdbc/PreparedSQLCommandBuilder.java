package org.crud2.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PreparedSQLCommandBuilder {
    private StringBuilder builder;
    private List<String> paramNames;
    private List<Object> params;

    private static final String PLACE_HOLDER = "?";

    private PreparedSQLCommandBuilder() {
        builder = new StringBuilder();
        paramNames = new ArrayList<>();
        params = new ArrayList<>();
    }

    public static PreparedSQLCommandBuilder newInstance() {
        return new PreparedSQLCommandBuilder();
    }

    public boolean contains(String text) {
        return builder.indexOf(text) >= 0;
    }

    public void append(String s) {
        builder.append(s);
    }

    public void append(String format, Object... params) {
        builder.append(String.format(format, params));
    }

    public void append(String[] strings) {
        builder.append(String.join(",", strings));
    }

    public <T> void append(T[] ts, FixSQLCommandHander<T> fixSQLCommandHander) {
        for (int i = 0; i < ts.length; i++) {
            builder.append(fixSQLCommandHander.getSql(ts[i]));
            if (i < ts.length - 1) {
                builder.append(",");
            }
        }
    }

    public void appendPlaceholder() {
        builder.append(PLACE_HOLDER);
    }

    public void appendPlaceolder(int count) {
        for (int i = 0; i < count; i++) {
            builder.append((i == count - 1) ? "?" : "?,");
        }
    }

    public void appendParam(String param, Object value) {
        paramNames.add(param);
        params.add(value);
    }

    public void appendParam(Map<String, Object> params) {
        for (String k : params.keySet()) {
            paramNames.add(k);
            this.params.add(params.get(k));
        }
    }

    /***
     * append params with anonymous param name
     * @param params
     */
    public void appendParam(Object[] params) {
        appendParam("p", params);
    }

    /***
     * append muti-params for one param-name
     * eg:use for "in" or "not in" operator
     * @param param
     * @param values
     */
    public void appendParam(String param, Object[] values) {
        for (int i = 1; i <= values.length; i++) {
            paramNames.add(param + i);
            this.params.add(values[i]);
        }

    }

    public void appendParam(String[] keys, Map<String, Object> params) {
        for (String k : keys) {
            paramNames.add(k);
            this.params.add(params.get(k));
        }
    }

    public PreparedSQLCommand build() {
        PreparedSQLCommand command = new PreparedSQLCommand();
        String[] paramNameArray = new String[paramNames.size()];
        paramNames.toArray(paramNameArray);
        Object[] paramArray = new Object[params.size()];
        params.toArray(paramArray);
        command.setParamNames(paramNameArray);
        command.setParams(paramArray);
        command.setCommandText(builder.toString());
        return command;
    }
}
