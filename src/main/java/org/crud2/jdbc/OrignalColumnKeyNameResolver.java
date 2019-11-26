package org.crud2.jdbc;

/***
 * eg:LOGIN_USER_NAME->LOGIN_USER_NAME
 */
public class OrignalColumnKeyNameResolver implements ColumnKeyNameResolver {
    @Override
    public String resolve(String orignal) {
        return orignal;
    }
}
