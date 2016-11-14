package com.tastegood.master.order.event;

/**
 * Created by surandy on 2016/11/3.
 */

public class RefreshListEvent {

    private String mFragmentTag;

    public RefreshListEvent(String fragmentTag) {
        this.mFragmentTag = fragmentTag;
    }

    public String getFragmentTag() {
        return mFragmentTag;
    }

}
