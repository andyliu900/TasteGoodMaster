package com.tastegood.master.data.source.oeder;

import android.support.annotation.NonNull;

import com.tastegood.master.api.BaseCallBack;
import com.tastegood.master.api.OrderApi;
import com.tastegood.master.api.RetrofitInstance;
import com.tastegood.master.data.source.oeder.model.GetOrderParams;
import com.tastegood.master.data.source.oeder.model.PrintOrderAgainParams;
import com.tastegood.master.data.source.oeder.model.PrintOrderParams;
import com.tastegood.master.data.source.oeder.model.StartDistributeOrderParams;
import com.tastegood.master.gobal.BaseApiParams;
import com.tastegood.master.gobal.BaseApiResponEntity;
import com.tastegood.master.order.domain.model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


/**
 * 订单网络获取数据实现
 *
 * Created by surandy on 2016/10/13.
 */

public class OrderDataSourceImpl implements OrderDataSource {

    private static OrderDataSourceImpl INSTANCE;

    private OrderApi orderApi;

    public static OrderDataSourceImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OrderDataSourceImpl();
        }
        return INSTANCE;
    }

    private OrderDataSourceImpl() {
        if (orderApi == null) {
            orderApi = RetrofitInstance.getInstance().create(OrderApi.class);
        }
    }

    @Override
    public void getOrders(GetOrderParams params, final @NonNull LoadOrdersCallback callback) {
        BaseApiParams baseApiParams = new BaseApiParams(params);
        final Call<BaseApiResponEntity<List<Order>>> call = orderApi.getOrders(baseApiParams);

//        try {
//            Response<BaseApiResponEntity<List<Order>>> result =  call.execute();
//            System.out.println(result.body());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        call.enqueue(new BaseCallBack<BaseApiResponEntity<List<Order>>>() {
            @Override
            public void doSuccess(Response<BaseApiResponEntity<List<Order>>> response) {
                callback.onOrdersLoaded(response.body().getData());
            }

            @Override
            public void doBusinessFail(Response<BaseApiResponEntity<List<Order>>> response) {
                callback.onOrdersBusinessFail(response.body().getMessage());
            }

            @Override
            public void doFail(Throwable error) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void printOrdersBatch(PrintOrderParams params, final @NonNull PrintOrdersBatchCallback callback) {
        BaseApiParams baseApiParams = new BaseApiParams(params);
        final Call<BaseApiResponEntity<String>> call = orderApi.printOrdersBatch(baseApiParams);

//        try {
//            Response<BaseApiResponEntity<String>> result = call.execute();
//            System.out.println(result.body());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        call.enqueue(new BaseCallBack<BaseApiResponEntity<String>>() {
            @Override
            public void doSuccess(Response<BaseApiResponEntity<String>> response) {
                callback.onPrintOrdersResult(response.body().getData());
            }

            @Override
            public void doBusinessFail(Response<BaseApiResponEntity<String>> response) {
                callback.onOrdersBusinessFail(response.body().getMessage());
            }

            @Override
            public void doFail(Throwable error) {
                callback.onDataNotAvailable();
            }
        });

    }

    @Override
    public void printOrderAgain(PrintOrderAgainParams params, final @NonNull PrintOrderAgainCallback callback) {
        BaseApiParams baseApiParams = new BaseApiParams(params);
        final Call<BaseApiResponEntity<String>> call = orderApi.printOrderAgain(baseApiParams);

//        try {
//            Response<BaseApiResponEntity<String>> result = call.execute();
//            System.out.println(result.body());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        call.enqueue(new BaseCallBack<BaseApiResponEntity<String>>() {
            @Override
            public void doSuccess(Response<BaseApiResponEntity<String>> response) {
                callback.onPrintOrderAgainResult(response.body().getData());
            }

            @Override
            public void doBusinessFail(Response<BaseApiResponEntity<String>> response) {
                callback.onOrdersBusinessFail(response.body().getMessage());
            }

            @Override
            public void doFail(Throwable error) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void distributOrder(StartDistributeOrderParams params, final @NonNull DistributOrderCallback callback) {
        BaseApiParams baseApiParams = new BaseApiParams(params);
        final Call<BaseApiResponEntity<String>> call = orderApi.startDistributeOrder(baseApiParams);

//        try {
//            Response<BaseApiResponEntity<String>> result = call.execute();
//            System.out.println(result.body());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        call.enqueue(new BaseCallBack<BaseApiResponEntity<String>>() {
            @Override
            public void doSuccess(Response<BaseApiResponEntity<String>> response) {
                callback.onDistributeOrderResult(response.body().getData());
            }

            @Override
            public void doBusinessFail(Response<BaseApiResponEntity<String>> response) {
                callback.onOrdersBusinessFail(response.body().getMessage());
            }

            @Override
            public void doFail(Throwable error) {
                callback.onDataNotAvailable();
            }
        });
    }

}
