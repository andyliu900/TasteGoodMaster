package com.tastegood.master.api;

import com.tastegood.master.gobal.BaseApiParams;
import com.tastegood.master.gobal.BaseApiResponEntity;
import com.tastegood.master.gobal.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by surandy on 2016/10/17.
 */

public interface DeviceApi {

    @Headers({
            "Content-type: application/json"
    })

    @POST(Constants.DEVICE_SIGNIN)
    public Call<BaseApiResponEntity<String>> signDeviceInfo(@Body BaseApiParams baseApiParams);

    @POST(Constants.UPLOAD_JPUSHTOKEN)
    public Call<BaseApiResponEntity<Integer>> uploadJpushTokenInfo(@Body BaseApiParams baseApiParams);

}
