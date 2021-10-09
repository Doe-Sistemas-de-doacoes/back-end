package com.labes.doe.exception;

import com.labes.doe.util.MessageUtil;

public class NotFoundException extends RuntimeException {

    public NotFoundException(MessageUtil message) {
        super(message.getMessage());
    }
}
