package org.crud2.jdbc;

import lombok.Data;
import org.crud2.util.ArrayUtil;
import org.crud2.util.ClassNames;
import org.slf4j.Logger;

@Data
public class PreparedSQLCommand {
    private String[] paramNames;
    private Object[] params;
    private String commandText;

    void debug(Logger logger) {
        if (logger.isDebugEnabled()) {
            logger.debug("==>  Preparing: " + commandText);
            logger.debug("==> Parameters: " +
                    ArrayUtil.toString(
                            params,
                            (t, i) -> String.format("%s->%s(%s)", paramNames[i], t, ClassNames.simple(t))
                    )
            );
        }
    }
}
