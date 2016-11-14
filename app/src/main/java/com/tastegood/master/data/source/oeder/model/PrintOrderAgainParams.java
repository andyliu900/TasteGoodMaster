package com.tastegood.master.data.source.oeder.model;

import com.tastegood.master.data.source.BaseParams;

/**
 * Created by surandy on 2016/11/7.
 */

public class PrintOrderAgainParams extends BaseParams {

    private long storefrontId;

    private int orderId;

    public long getStorefrontId() {
        return storefrontId;
    }

    public void setStorefrontId(long storefrontId) {
        this.storefrontId = storefrontId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
