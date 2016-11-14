package com.tastegood.master.order.domain.usecase;

import android.support.annotation.NonNull;

import com.tastegood.master.data.source.oeder.OrderDataSource;
import com.tastegood.master.data.source.oeder.OrderRepository;
import com.tastegood.master.data.source.oeder.model.GetOrderParams;
import com.tastegood.master.gobal.UseCase;
import com.tastegood.master.order.domain.model.Order;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by surandy on 2016/10/28.
 */

public class OrderTask extends UseCase<OrderTask.RequestValues, OrderTask.ResponseValue> {

    private final OrderRepository mOrderRepository;

    public OrderTask(@NonNull OrderRepository orderRepository) {
        mOrderRepository = checkNotNull(orderRepository, "orderRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(OrderTask.RequestValues requestValues) {
        GetOrderParams params = new GetOrderParams();
        params.setStorefrontId(requestValues.mStorefrontId);
        params.setPageNumber(requestValues.mPageNumber);
        params.setPageSize(requestValues.mPageSize);
        params.setStatus(requestValues.mStatus);

        mOrderRepository.getOrders(params, new OrderDataSource.LoadOrdersCallback() {

            @Override
            public void onOrdersLoaded(List<Order> tasks) {
                OrderTask.ResponseValue responseValue = new OrderTask.ResponseValue(tasks);
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
        private final int mPageNumber;
        private final int mPageSize;
        private final int mStatus;

        public RequestValues(long storefrontId, int pageNumber, int pageSize, int status) {
            mStorefrontId = storefrontId;
            mPageNumber = pageNumber;
            mPageSize = pageSize;
            mStatus = status;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final List<Order> mOrderList;

        public ResponseValue(@NonNull List<Order> orderList) {
            mOrderList = checkNotNull(orderList, "order cannot be null!");
        }

        public List<Order> getOrders() {
            return mOrderList;
        }
    }

}
