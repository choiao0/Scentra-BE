package com.apollo.scentraapi.auth.handler;

import io.jsonwebtoken.JwtException;

public class JwtExceptionHandler extends JwtException {
    public JwtExceptionHandler(String message) {
        super(message);
    }

    public JwtExceptionHandler(String message, Throwable cause) {
        super(message, cause);
    }
}