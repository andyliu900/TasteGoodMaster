package com.tastegood.master.data.source.device.model;


import com.tastegood.master.data.source.BaseParams;

/**
 * Created by surandy on 2016/10/17.
 */

public class UploadJpushTokenParams extends BaseParams {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
