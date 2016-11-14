package com.tastegood.master.order.domain.usecase;

import android.support.annotation.NonNull;

import com.tastegood.master.data.source.oeder.OrderDataSource;
import com.tastegood.master.data.source.oeder.OrderRepository;
import com.tastegood.master.data.source.oeder.model.StartDistributeOrderParams;
import com.tastegood.master.gobal.UseCase;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by surandy on 2016/10/28.
 */

public class StartDistributeOrderTask extends UseCase<StartDistributeOrderTask.RequestValues, StartDistributeOrderTask.ResponseValue> {

    private final OrderRepository mOrderRepository;

    public StartDistributeOrderTask(@NonNull OrderRepository orderRepository) {
        mOrderRepository = checkNotNull(orderRepository, "orderRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        StartDistributeOrderParams params = new StartDistributeOrderParams();
        params.setStorefrontId(requestValues.mStorefrontId);
        params.setOrderId(requestValues.mOrderid);

        mOrderRepository.distributOrder(params, new OrderDataSource.DistributOrderCallback() {

            @Override
            public void onDistributeOrderResult(String resultMsg) {
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

        private final String mOrderPrintResult;

        public ResponseValue(@NonNull String orderPrintResult) {
            mOrderPrintResult = checkNotNull(orderPrintResult, "orderPrintResult cannot be null!");
        }

        public String getDistributeOrderResult() {
            return mOrderPrintResult;
        }
    }

}
