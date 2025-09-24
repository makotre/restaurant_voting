package com.github.makotre.bootjava.common.error;

import static com.github.makotre.bootjava.common.error.ErrorType.DATA_CONFLICT;

public class DataConflictException extends AppException {
    public DataConflictException(String msg) {
        super(msg, DATA_CONFLICT);
    }
}