package com.lucinde.plannerpro.helpers;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class Helpers {

    public Helpers() {
    }

    public String fieldErrorBuilder(BindingResult br) {
        StringBuilder sb = new StringBuilder();
        for (FieldError fe : br.getFieldErrors()) {
            sb.append(fe.getField() + ": ");
            sb.append(fe.getDefaultMessage());
            sb.append("\n");
        }
        return sb.toString();
    }
}
