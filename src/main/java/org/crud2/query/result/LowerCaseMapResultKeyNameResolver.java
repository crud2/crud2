package org.crud2.query.result;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/***
 * eg:LOGIN_USER_NAME->login_user_name
 */
public class LowerCaseMapResultKeyNameResolver implements MapResultKeyNameResolver {
    @Override
    public String resolve(String orignal) {
        return orignal.toLowerCase();
    }
}
