package com.tastegood.master.data.source.device;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.tastegood.master.api.BaseCallBack;
import com.tastegood.master.api.DeviceApi;
import com.tastegood.master.api.RetrofitInstance;
import com.tastegood.master.data.source.device.model.DeviceSignParams;
import com.tastegood.master.data.source.device.model.UploadJpushTokenParams;
import com.tastegood.master.gobal.BaseApiParams;
import com.tastegood.master.gobal.BaseApiResponEntity;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by surandy on 2016/10/18.
 */

public class DeviceDataSourceImpl implements DeviceDataSource {

    private static final String TAG = DeviceDataSourceImpl.class.getName();

    private static DeviceDataSourceImpl INSTANCE;

    private DeviceApi deviceApi;

    public static DeviceDataSourceImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DeviceDataSourceImpl();
        }
        return INSTANCE;
    }

    private DeviceDataSourceImpl() {
        if (deviceApi == null) {
            deviceApi = RetrofitInstance.getInstance().create(DeviceApi.class);
        }
    }

    @Override
    public void deviceSignIn(DeviceSignParams params, final @NonNull DeviceSignInCallback callback) {
        BaseApiParams baseApiParams = new BaseApiParams(params);
        Call<BaseApiResponEntity<String>> call = deviceApi.signDeviceInfo(baseApiParams);

        call.enqueue(new BaseCallBack<BaseApiResponEntity<String>>() {

            @Override
            public void doSuccess(Response<BaseApiResponEntity<String>> response) {
                callback.onSignSuccess(response.body().getData());
            }

            @Override
            public void doBusinessFail(Response<BaseApiResponEntity<String>> response) {
                Logger.e(TAG, response.body().getCode() + "   " +response.body().getMessage());
            }

            @Override
            public void doFail(Throwable error) {
                callback.onSignFail();
            }
        });
    }

    @Override
    public void uploadJpushToken(UploadJpushTokenParams params, final @NonNull UploadJpushTokenCallback callback) {
        BaseApiParams baseApiParams = new BaseApiParams(params);
        Call<BaseApiResponEntity<Integer>> call = deviceApi.uploadJpushTokenInfo(baseApiParams);

        call.enqueue(new BaseCallBack<BaseApiResponEntity<Integer>>() {

            @Override
            public void doSuccess(Response<BaseApiResponEntity<Integer>> response) {
                callback.onUploadSuccess(response.body().getData());
            }

            @Override
            public void doBusinessFail(Response<BaseApiResponEntity<Integer>> response) {
                Logger.e(TAG, response.body().getCode() + "   " +response.body().getMessage());
            }

            @Override
            public void doFail(Throwable error) {
                callback.onUploadFail();
            }
        });
    }
}
