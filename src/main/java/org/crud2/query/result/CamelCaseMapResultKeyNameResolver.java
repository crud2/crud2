package org.crud2.query.result;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/***
 * eg：LOGIN_USER_NAME->LoginUserName
 */
public class CamelCaseMapResultKeyNameResolver implements MapResultKeyNameResolver {
    @Override
    public String resolve(String orignal) {
        StringBuilder result = new StringBuilder();
        String[] tempArray = orignal.split("_");
        for (String s : tempArray) {
            result.append(s.substring(0, 1).toUpperCase()).append(s.substring(1).toLowerCase());
        }
        return result.toString();
    }
}
