package com.craig.pe.demosecuritywebflux.exception;

import com.craig.pe.demosecuritywebflux.utils.I18NException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotFoundException extends I18NException {

    public UserNotFoundException(String key, Object... params) {
        super(key, params);
    }


}
