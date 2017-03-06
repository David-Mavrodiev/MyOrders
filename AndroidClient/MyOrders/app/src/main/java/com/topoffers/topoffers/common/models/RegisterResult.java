package com.topoffers.topoffers.common.models;

import com.orm.SugarRecord;

import java.io.Serializable;

public class RegisterResult extends SugarRecord implements Serializable {
    private boolean success;
    private Error error;

    // Sugar need default constructor
    public RegisterResult() {
    }

    public RegisterResult(boolean success, Error error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
