package org.crud2.edit.impl.inner;

import lombok.Data;

@Data
public class PreparedSQLCommand {
    private String[] paramNames;
    private String commandText;
}
