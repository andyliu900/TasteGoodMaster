package com.tastegood.master.order;

import android.support.annotation.NonNull;

import com.tastegood.master.gobal.UseCase;
import com.tastegood.master.gobal.UseCaseHandler;
import com.tastegood.master.order.domain.model.Order;
import com.tastegood.master.order.domain.usecase.OrderPrintAgainTask;
import com.tastegood.master.order.domain.usecase.OrderPrintTask;
import com.tastegood.master.order.domain.usecase.OrderTask;
import com.tastegood.master.order.domain.usecase.StartDistributeOrderTask;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by surandy on 2016/10/28.
 */

public class OrderPresenter implements OrderContract.Presenter {

    private OrderContract.OrderView mOrderView;
    private OrderTask mOrderTask;
    private OrderPrintTask mOrderPrintTask;
    private OrderPrintAgainTask mOrderPrintAgainTask;
    private StartDistributeOrderTask mStartDistributeOrderTask;

    private final UseCaseHandler mUseCaseHandler;

    public OrderPresenter(@NonNull UseCaseHandler useCaseHandler,
                          @NonNull OrderContract.OrderView orderView,
                          @NonNull OrderTask orderTask,
                          @NonNull OrderPrintTask orderPrintTask,
                          @NonNull OrderPrintAgainTask orderPrintAgainTask,
                          @NonNull StartDistributeOrderTask startDistributeOrderTask) {
        mUseCaseHandler = checkNotNull(useCaseHandler, "usecaseHandler cannot be null");
        mOrderView = checkNotNull(orderView, "orderView cannot be null!");
        mOrderTask = checkNotNull(orderTask, "orderTask cannot be null!");
        mOrderPrintTask = checkNotNull(orderPrintTask, "orderPrintTask cannot be null!");
        mOrderPrintAgainTask = checkNotNull(orderPrintAgainTask, "orderPrintAgainTask cannot be null!");
        mStartDistributeOrderTask = checkNotNull(startDistributeOrderTask, "startDistributeOrderTask cannot be null!");

        mOrderView.setPresenter(this);
    }

    @Override
    public void getOrderList(long storefrontId, final int pageNumber, int pageSize, int status) {
        OrderTask.RequestValues requestValues = new OrderTask.RequestValues(storefrontId, pageNumber, pageSize, status);

        mUseCaseHandler.execute(mOrderTask, requestValues,
                new UseCase.UseCaseCallback<OrderTask.ResponseValue>() {
                    @Override
                    public void onSuccess(OrderTask.ResponseValue response) {
                        mOrderView.hideLoadingView();
                        List<Order> orderList = response.getOrders();
                        mOrderView.showOrderList(orderList);
                    }

                    @Override
                    public void onBusinessError(String businessErrorMessage) {
                        mOrderView.hideLoadingView();
                        mOrderView.showBusinessError(businessErrorMessage);
                    }

                    @Override
                    public void onError() {
                        if (pageNumber == 1) {
                            mOrderView.showNetErrorView(true);
                        } else {
                            mOrderView.showNetErrorView(false);
                        }
                        mOrderView.hideLoadingView();
                    }
                });
    }

    @Override
    public void printOrders(long storefrontId, int[] orderIds) {
        OrderPrintTask.RequestValues requestValues = new OrderPrintTask.RequestValues(storefrontId, orderIds);

        mUseCaseHandler.execute(mOrderPrintTask, requestValues,
                new UseCase.UseCaseCallback<OrderPrintTask.ResponseValue>() {
                    @Override
                    public void onSuccess(OrderPrintTask.ResponseValue response) {
                        mOrderView.hideLoadingView();
                        String printResult = response.getOrderPrintResult();
                        mOrderView.showPrintResult(printResult);
                    }

                    @Override
                    public void onBusinessError(String businessErrorMessage) {
                        mOrderView.hideLoadingView();
                        mOrderView.showBusinessError(businessErrorMessage);
                    }

                    @Override
                    public void onError() {
                        mOrderView.hideLoadingView();
                    }
                });

    }

    @Override
    public void printOrderAgain(long storefrontId, int orderId) {
        OrderPrintAgainTask.RequestValues requestValues = new OrderPrintAgainTask.RequestValues(storefrontId, orderId);

        mUseCaseHandler.execute(mOrderPrintAgainTask, requestValues,
                new UseCase.UseCaseCallback<OrderPrintAgainTask.ResponseValue>() {
                    @Override
                    public void onSuccess(OrderPrintAgainTask.ResponseValue response) {
                        mOrderView.hideLoadingView();
                        String printResult = response.getOrderPrintResult();
                        mOrderView.showPrintResult(printResult);
                    }

                    @Override
                    public void onBusinessError(String businessErrorMessage) {
                        mOrderView.hideLoadingView();
                        mOrderView.showBusinessError(businessErrorMessage);
                    }

                    @Override
                    public void onError() {
                        mOrderView.hideLoadingView();
                    }
                });
    }

    @Override
    public void startDistributeOrder(long storefrontId, int orderId) {
        StartDistributeOrderTask.RequestValues requestValues = new StartDistributeOrderTask.RequestValues(storefrontId, orderId);

        mUseCaseHandler.execute(mStartDistributeOrderTask, requestValues,
                new UseCase.UseCaseCallback<StartDistributeOrderTask.ResponseValue>() {
                    @Override
                    public void onSuccess(StartDistributeOrderTask.ResponseValue response) {
                        mOrderView.hideLoadingView();
                        String distributeResult = response.getDistributeOrderResult();
                        mOrderView.showDistributeOrderResult(distributeResult);
                    }

                    @Override
                    public void onBusinessError(String businessErrorMessage) {
                        mOrderView.hideLoadingView();
                        mOrderView.showBusinessError(businessErrorMessage);
                    }

                    @Override
                    public void onError() {
                        mOrderView.hideLoadingView();
                    }
                });
    }

}
