package com.gabia.gyebalja.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Author : 정태균
 * Part : All
 */

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotExistTagException extends RuntimeException {
    public NotExistTagException(String msg) {
        super(msg);
    }
}
