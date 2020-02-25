package com.example.lib.exception.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends HttpClientErrorException {

    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(String statusText) {
        super(HttpStatus.UNAUTHORIZED, statusText);
    }

    public UnauthorizedException(String statusText, byte[] body, Charset responseCharset) {
        super(HttpStatus.UNAUTHORIZED, statusText, body, responseCharset);
    }

    public UnauthorizedException(String statusText, HttpHeaders headers, byte[] body, Charset responseCharset) {
        super(HttpStatus.UNAUTHORIZED, statusText, headers, body, responseCharset);
    }
}
