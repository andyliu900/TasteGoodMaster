package com.tastegood.master.order.domain.usecase;

import android.support.annotation.NonNull;

import com.tastegood.master.data.source.oeder.OrderDataSource;
import com.tastegood.master.data.source.oeder.OrderRepository;
import com.tastegood.master.data.source.oeder.model.PrintOrderParams;
import com.tastegood.master.gobal.UseCase;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by surandy on 2016/10/28.
 */

public class OrderPrintTask extends UseCase<OrderPrintTask.RequestValues, OrderPrintTask.ResponseValue> {

    private final OrderRepository mOrderRepository;

    public OrderPrintTask(@NonNull OrderRepository orderRepository) {
        mOrderRepository = checkNotNull(orderRepository, "orderRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        PrintOrderParams params = new PrintOrderParams();
        params.setStorefrontId(requestValues.mStorefrontId);
        params.setOrderIds(requestValues.mOrderids);

        mOrderRepository.printOrdersBatch(params, new OrderDataSource.PrintOrdersBatchCallback() {

            @Override
            public void onPrintOrdersResult(String resultMsg) {
                ResponseValue responseValue = new ResponseValue(resultMsg);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onOrdersBusinessFail(String errorMessage) {
                getUseCaseCallback().onBusinessError(errorMessage);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }

        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final long mStorefrontId;

        private final int[] mOrderids;

        public RequestValues(long storefrontId, int[] orderids) {
            mStorefrontId = storefrontId;
            mOrderids = orderids;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final String mOrderPrintResult;

        public ResponseValue(@NonNull String orderPrintResult) {
            mOrderPrintResult = checkNotNull(orderPrintResult, "orderPrintResult cannot be null!");
        }

        public String getOrderPrintResult() {
            return mOrderPrintResult;
        }
    }

}
