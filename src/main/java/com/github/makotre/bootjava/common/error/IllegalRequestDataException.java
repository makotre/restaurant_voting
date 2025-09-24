package com.github.makotre.bootjava.common.error;

import static com.github.makotre.bootjava.common.error.ErrorType.BAD_REQUEST;

public class IllegalRequestDataException extends AppException {
    public IllegalRequestDataException(String msg) {
        super(msg, BAD_REQUEST);
    }
}