package com.devsuperior.dscatalog.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

    private List<FieldMessage> fieldMessages = new ArrayList<>();

    public  ValidationError() {
        super();
    }

    public List<FieldMessage> getFieldMessages() {
        return fieldMessages;
    }

    public void addErrors(String fieldName, String message) {
        fieldMessages.add(new FieldMessage(fieldName, message));
    }
}
