package com.tastegood.master.data.source.oeder;

import android.support.annotation.NonNull;

import com.tastegood.master.data.source.oeder.model.GetOrderParams;
import com.tastegood.master.data.source.oeder.model.PrintOrderAgainParams;
import com.tastegood.master.data.source.oeder.model.PrintOrderParams;
import com.tastegood.master.data.source.oeder.model.StartDistributeOrderParams;
import com.tastegood.master.order.domain.model.Order;

import java.util.List;

/**
 * 数据源接口
 *
 * Created by surandy on 2016/10/13.
 */

public interface OrderDataSource {

    interface LoadOrdersCallback {

        void onOrdersLoaded(List<Order> tasks);

        void onOrdersBusinessFail(String errorMessage);

        void onDataNotAvailable();
    }

    interface PrintOrdersBatchCallback {

        void onPrintOrdersResult(String resultMsg);

        void onOrdersBusinessFail(String errorMessage);

        void onDataNotAvailable();
    }

    interface PrintOrderAgainCallback {

        void onPrintOrderAgainResult(String resultMsg);

        void onOrdersBusinessFail(String errorMessage);

        void onDataNotAvailable();

    }

    interface DistributOrderCallback {

        void onDistributeOrderResult(String resultMsg);

        void onOrdersBusinessFail(String errorMessage);

        void onDataNotAvailable();
    }

    void getOrders(GetOrderParams params, @NonNull LoadOrdersCallback callback);

    void printOrdersBatch(PrintOrderParams params, @NonNull PrintOrdersBatchCallback callback);

    void printOrderAgain(PrintOrderAgainParams params, @NonNull PrintOrderAgainCallback callback);

    void distributOrder(StartDistributeOrderParams params, @NonNull DistributOrderCallback callback);

}
