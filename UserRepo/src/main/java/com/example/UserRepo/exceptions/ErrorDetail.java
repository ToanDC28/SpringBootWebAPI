package com.example.UserRepo.exceptions;

import java.util.Date;

public class ErrorDetail {
    private Date timestamp;
    private String mesage;
    private String details;

    public ErrorDetail(Date timestamp, String mesage, String details) {
        this.timestamp = timestamp;
        this.mesage = mesage;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMesage() {
        return mesage;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
