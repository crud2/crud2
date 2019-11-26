package org.crud2.jdbc;

/***
 * eg:LOGIN_USER_NAME->login_user_name
 */
public class LowerCaseColumnKeyNameResolver implements ColumnKeyNameResolver {
    @Override
    public String resolve(String orignal) {
        return orignal.toLowerCase();
    }
}
