package de.micromata.jira.rest.core.domain;

import java.util.*;

import com.google.gson.annotations.*;

public class ErrorBean {
    @Expose
    private List<String> errorMessages = new ArrayList<>();
    @Expose
    private ErrorsBean   errors;

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(ErrorsBean errors) {
        this.errors = errors;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public void addMessage(String msg) {
        errorMessages.add(msg);
    }

    @Override
    public String toString() {
        return "[" + String.join(" & ", errorMessages) + "]";
    }
}
