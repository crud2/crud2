package org.crud2.edit.impl.inner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PreparedSQLCommandBuilder {
    private StringBuilder builder;
    private List<String> paramNames;

    private static final String PLACE_HOLDER = "?";

    public PreparedSQLCommandBuilder() {
        builder = new StringBuilder();
        paramNames = new ArrayList<>();
    }

    public static PreparedSQLCommandBuilder newInstance() {
        return new PreparedSQLCommandBuilder();
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

    public void appendParam(String param) {
        paramNames.add(param);
    }

    public void appendParam(String[] params) {
        for (String param : params) {
            paramNames.add(param);
        }
    }

    public PreparedSQLCommand build() {
        PreparedSQLCommand command = new PreparedSQLCommand();
        String[] paramNameArray = new String[paramNames.size()];
        paramNames.toArray(paramNameArray);
        command.setParamNames(paramNameArray);
        command.setCommandText(builder.toString());
        return command;
    }
}
