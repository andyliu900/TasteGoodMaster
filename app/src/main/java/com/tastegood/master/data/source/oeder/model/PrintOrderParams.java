package com.tastegood.master.data.source.oeder.model;

import com.tastegood.master.data.source.BaseParams;

/**
 * Created by surandy on 2016/11/7.
 */

public class PrintOrderParams extends BaseParams {

    private long storefrontId;

    private int[] orderIds;

    public long getStorefrontId() {
        return storefrontId;
    }

    public void setStorefrontId(long storefrontId) {
        this.storefrontId = storefrontId;
    }

    public int[] getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(int[] orderIds) {
        this.orderIds = orderIds;
    }
}
