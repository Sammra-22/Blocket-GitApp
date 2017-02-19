package com.schibsted.blockapp.model;


/**
 * Created by Sam on 2/18/17.
 */
public class ApiResponse{
    boolean success = false;


    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}