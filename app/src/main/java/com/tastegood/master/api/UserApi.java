package com.tastegood.master.api;

import com.tastegood.master.gobal.BaseApiParams;
import com.tastegood.master.gobal.BaseApiResponEntity;
import com.tastegood.master.gobal.Constants;
import com.tastegood.master.user.domain.model.UserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by surandy on 2016/10/18.
 */

public interface UserApi {

    @Headers({
            "Content-type: application/json"
    })

    @POST(Constants.DZ_LOGIN)
    public Call<BaseApiResponEntity<UserInfo>> userLogin(@Body BaseApiParams baseApiParams);

    @POST(Constants.DZ_LOGOUT)
    public Call<BaseApiResponEntity<Boolean>> userLogout(@Body BaseApiParams baseApiParams);

}
