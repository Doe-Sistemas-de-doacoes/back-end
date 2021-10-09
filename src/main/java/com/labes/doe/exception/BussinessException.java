package com.labes.doe.exception;

import com.labes.doe.util.MessageUtil;

public class BussinessException extends RuntimeException {

    public BussinessException(MessageUtil message) {
        super(message.getMessage());
    }
}
