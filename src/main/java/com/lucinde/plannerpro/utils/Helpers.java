package com.lucinde.plannerpro.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class Helpers {
    // in deze klasse staan algemene helpers die in diverse classes gebruikt worden, geen business logica verwerken en op public mogen blijven staan

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
