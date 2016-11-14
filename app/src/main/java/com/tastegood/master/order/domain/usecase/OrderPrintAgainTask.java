package com.tastegood.master.order.domain.usecase;

import android.support.annotation.NonNull;

import com.tastegood.master.data.source.oeder.OrderDataSource;
import com.tastegood.master.data.source.oeder.OrderRepository;
import com.tastegood.master.data.source.oeder.model.PrintOrderAgainParams;
import com.tastegood.master.gobal.UseCase;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by surandy on 2016/10/28.
 */

public class OrderPrintAgainTask extends UseCase<OrderPrintAgainTask.RequestValues, OrderPrintAgainTask.ResponseValue> {

    private final OrderRepository mOrderRepository;

    public OrderPrintAgainTask(@NonNull OrderRepository orderRepository) {
        mOrderRepository = checkNotNull(orderRepository, "orderRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        PrintOrderAgainParams params = new PrintOrderAgainParams();
        params.setStorefrontId(requestValues.mStorefrontId);
        params.setOrderId(requestValues.mOrderid);

        mOrderRepository.printOrderAgain(params, new OrderDataSource.PrintOrderAgainCallback() {

            @Override
            public void onPrintOrderAgainResult(String resultMsg) {
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

        private final int mOrderid;

        public RequestValues(long storefrontId, int orderid) {
            mStorefrontId = storefrontId;
            mOrderid = orderid;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final String mOrderPrintAgainResult;

        public ResponseValue(@NonNull String orderPrintAgainResult) {
            mOrderPrintAgainResult = checkNotNull(orderPrintAgainResult, "orderPrintAgainResult cannot be null!");
        }

        public String getOrderPrintResult() {
            return mOrderPrintAgainResult;
        }
    }

}
