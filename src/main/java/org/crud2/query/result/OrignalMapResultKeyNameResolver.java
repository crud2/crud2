package org.crud2.query.result;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/***
 * eg:LOGIN_USER_NAME->LOGIN_USER_NAME
 */
public class OrignalMapResultKeyNameResolver implements MapResultKeyNameResolver {
    @Override
    public String resolve(String orignal) {
        return orignal;
    }
}
