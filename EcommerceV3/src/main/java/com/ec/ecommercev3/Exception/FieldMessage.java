package com.ec.ecommercev3.Exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class FieldMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String fieldName;
    private String message;

    public FieldMessage(){}

    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

}