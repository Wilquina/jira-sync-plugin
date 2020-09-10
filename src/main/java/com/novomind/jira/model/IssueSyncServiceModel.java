package com.novomind.jira.model;

import javax.xml.bind.annotation.*;
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class IssueSyncServiceModel {

    @XmlElement(name = "value")
    private String message;

    public IssueSyncServiceModel() {
    }

    public IssueSyncServiceModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}