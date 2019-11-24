package org.crud2.query.result;

import lombok.Data;

import java.util.List;

@Data
public class PagerResult<T> extends PagerSerializable {
    private List<T> data;
}