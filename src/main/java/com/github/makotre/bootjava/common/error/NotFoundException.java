package com.github.makotre.bootjava.common.error;

import static com.github.makotre.bootjava.common.error.ErrorType.NOT_FOUND;

public class NotFoundException extends AppException {
    public NotFoundException(String msg) {
        super(msg, NOT_FOUND);
    }
}