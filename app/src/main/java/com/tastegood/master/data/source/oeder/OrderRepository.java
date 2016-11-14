package com.tastegood.master.data.source.oeder;

import android.support.annotation.NonNull;

import com.tastegood.master.data.source.oeder.model.GetOrderParams;
import com.tastegood.master.data.source.oeder.model.PrintOrderAgainParams;
import com.tastegood.master.data.source.oeder.model.PrintOrderParams;
import com.tastegood.master.data.source.oeder.model.StartDistributeOrderParams;
import com.tastegood.master.order.domain.model.Order;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 订单模块获取数据功能
 *
 * Created by surandy on 2016/10/13.
 */

public class OrderRepository implements OrderDataSource {

    private static OrderRepository INSTANCE = null;

    private final OrderDataSource mOrderRemoteDataSource;

    private OrderRepository(@NonNull OrderDataSource orderDataSource) {
        mOrderRemoteDataSource = checkNotNull(orderDataSource);
    }

    public static OrderRepository getInstance(OrderDataSource orderDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new OrderRepository(orderDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getOrders(@NonNull GetOrderParams orderParams, @NonNull final LoadOrdersCallback callback) {
        checkNotNull(callback);

        mOrderRemoteDataSource.getOrders(orderParams, new LoadOrdersCallback() {
            @Override
            public void onOrdersLoaded(List<Order> tasks) {
                callback.onOrdersLoaded(tasks);
            }

            @Override
            public void onOrdersBusinessFail(String errorMessage) {
                callback.onOrdersBusinessFail(errorMessage);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void printOrdersBatch(PrintOrderParams params, final @NonNull PrintOrdersBatchCallback callback) {
        checkNotNull(callback);

        mOrderRemoteDataSource.printOrdersBatch(params, new PrintOrdersBatchCallback() {

            @Override
            public void onPrintOrdersResult(String resultMsg) {
                callback.onPrintOrdersResult(resultMsg);
            }

            @Override
            public void onOrdersBusinessFail(String errorMessage) {
                callback.onOrdersBusinessFail(errorMessage);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }

        });

    }

    @Override
    public void printOrderAgain(PrintOrderAgainParams params, final @NonNull PrintOrderAgainCallback callback) {
        checkNotNull(callback);

        mOrderRemoteDataSource.printOrderAgain(params, new PrintOrderAgainCallback() {

            @Override
            public void onPrintOrderAgainResult(String resultMsg) {
                callback.onPrintOrderAgainResult(resultMsg);
            }

            @Override
            public void onOrdersBusinessFail(String errorMessage) {
                callback.onOrdersBusinessFail(errorMessage);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }

        });
    }

    @Override
    public void distributOrder(StartDistributeOrderParams params, final @NonNull DistributOrderCallback callback) {
        checkNotNull(callback);

        mOrderRemoteDataSource.distributOrder(params, new DistributOrderCallback() {
            @Override
            public void onDistributeOrderResult(String resultMsg) {
                callback.onDistributeOrderResult(resultMsg);
            }

            @Override
            public void onOrdersBusinessFail(String errorMessage) {
                callback.onOrdersBusinessFail(errorMessage);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

}
