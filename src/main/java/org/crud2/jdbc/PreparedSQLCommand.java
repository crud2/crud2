package org.crud2.jdbc;

import lombok.Data;
import org.crud2.util.ArrayUtil;
import org.slf4j.Logger;

@Data
public class PreparedSQLCommand {
    private String[] paramNames;
    private Object[] params;
    private String commandText;

    public void debug(Logger logger) {
        if (logger.isDebugEnabled()) {
            logger.debug("Prepare:" + commandText);
            logger.debug("Parameters:" + ArrayUtil.toString(paramNames));
            logger.debug("Values:" + ArrayUtil.toString(params));
        }
    }
}
