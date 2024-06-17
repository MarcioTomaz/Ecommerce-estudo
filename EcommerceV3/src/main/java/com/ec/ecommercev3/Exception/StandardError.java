package com.ec.ecommercev3.Exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
public class StandardError implements Serializable {

    private static final long serialVersionUID = 1L;
    private Instant timestemp;
    private Integer status;
    private String error;
    private String message;
    private String path;

    public StandardError() {
    }


}