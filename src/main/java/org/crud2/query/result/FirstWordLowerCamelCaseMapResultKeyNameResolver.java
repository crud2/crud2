package org.crud2.query.result;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/***
 * eg:LOGIN_USER_NAME->loginUserName
 */
public class FirstWordLowerCamelCaseMapResultKeyNameResolver implements MapResultKeyNameResolver {
    @Override
    public String resolve(String orignal) {
        StringBuilder result = new StringBuilder();
        String[] tempArray = orignal.split("_");
        result.append(tempArray[0].toLowerCase());
        for (int i = 1; i < tempArray.length; i++) {
            result.append(tempArray[i].substring(0, 1).toUpperCase()).append(tempArray[i].substring(1).toLowerCase());
        }
        return result.toString();
    }
}
