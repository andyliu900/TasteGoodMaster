package com.tastegood.master.order;

import com.tastegood.master.gobal.BasePresenter;
import com.tastegood.master.gobal.BaseView;
import com.tastegood.master.order.domain.model.Order;

import java.util.List;

/**
 * Created by surandy on 2016/10/28.
 */

public interface OrderContract {

    interface OrderView extends BaseView<OrderContract.Presenter> {

        void showLoadingView();

        void showLoadingView(String message);

        void hideLoadingView();

        void showNetErrorView(boolean isFirst);

        void hideNetErrorView();

        void showNoDataView(boolean isFirst);

        void hideNoDataView();

        void showBusinessError(String businessErrorMessage);

        void showOrderList(List<Order> orderList);

        void showPrintResult(String result);

        void showPrintAgainResult(String result);

        void showDistributeOrderResult(String result);

    }

    interface Presenter extends BasePresenter {

        void getOrderList(long storefrontId, int pageNumber, int pageSize, int status);

        void printOrders(long storefrontId, int[] orderIds);

        void printOrderAgain(long storefrontId, int orderId);

        void startDistributeOrder(long storefrontId, int orderId);

    }

}
