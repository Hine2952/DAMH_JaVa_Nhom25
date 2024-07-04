package com.example.ChatWeb.exception;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ErrorDetail {
    private String error;
    private String message;
    private LocalDateTime time;

    public ErrorDetail() {
    }

    public ErrorDetail(String error, String message, LocalDateTime time) {
        this.error = error;
        this.message = message;
        this.time = time;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
