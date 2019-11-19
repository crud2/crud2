package org.crud2.autoengine.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SqlTextMacroParameterResolver implements SqlTextParameterResolver {
    private char macroStr = '~';

    @Override
    public String resolve(String sql, Map<String, Object> params) {
        for (String s : params.keySet()) {
            sql = sql.replaceAll(macroStr + s + macroStr, params.get(s) == null ? "" : params.get(s).toString());
        }
        return sql;
    }

    @Override
    public String[] getNames(String sql) {
        List<String> resultList = new ArrayList<>();
        StringBuilder nameBuilder = new StringBuilder();
        boolean start = false;
        char[] chars = sql.toCharArray();
        for (char c : chars) {

            if (macroStr == c) {
                if (start) {
                    resultList.add(nameBuilder.toString());
                    nameBuilder.setLength(0);
                    start = false;
                } else {
                    start = true;
                }
            } else if (start) {
                nameBuilder.append(c);
            }
        }
        String[] result = new String[resultList.size()];
        return resultList.toArray(result);
    }
}
