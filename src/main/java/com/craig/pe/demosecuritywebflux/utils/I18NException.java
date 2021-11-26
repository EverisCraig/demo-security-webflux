package com.craig.pe.demosecuritywebflux.utils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class I18NException extends RuntimeException {

    protected final transient Object[] params;

    public I18NException(String key, Object[] params) {
        this.params = params;
    }
}
