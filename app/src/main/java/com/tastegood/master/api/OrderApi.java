package com.tastegood.master.api;

import com.tastegood.master.gobal.BaseApiParams;
import com.tastegood.master.gobal.BaseApiResponEntity;
import com.tastegood.master.gobal.Constants;
import com.tastegood.master.order.domain.model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


/**
 * 订单Api
 *
 * Created by surandy on 2016/10/13.
 */

public interface OrderApi {

    @Headers({
            "Content-type: application/json"
    })

    @POST(Constants.GET_ORDERS)
    public Call<BaseApiResponEntity<List<Order>>> getOrders(@Body BaseApiParams baseApiParams);

    @POST(Constants.PRINT_ORDERS)
    public Call<BaseApiResponEntity<String>> printOrdersBatch(@Body BaseApiParams baseApiParams);

    @POST(Constants.PRINT_ORDERS_AGAIN)
    public Call<BaseApiResponEntity<String>> printOrderAgain(@Body BaseApiParams baseApiParams);

    @POST(Constants.START_DISTRIBUTE_ORDER)
    public Call<BaseApiResponEntity<String>> startDistributeOrder(@Body BaseApiParams baseApiParams);

}
