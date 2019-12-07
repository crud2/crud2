package org.crud2.core.query.result;

import lombok.Data;

@Data
public class PagerResult<T> extends PagerSerializable {
    private T data;
}