package com.tastegood.master.order;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.tastegood.master.R;
import com.tastegood.master.gobal.BaseFragment;
import com.tastegood.master.order.view.OrderManagerAdapter;
import com.tastegood.master.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 订单管理界面
 *
 * Created by surandy on 2016/10/31.
 */

public class OrderManageFragment extends BaseFragment {

    public static final String TAG = OrderManageFragment.class.getName();

    String[] titles = { "配送中", "已送达", "已失效"};

    @Bind(R.id.id_stickynavlayout_indicator)
    PagerSlidingTabStrip viewPagerIndicator;
    @Bind(R.id.id_stickynavlayout_viewpager)
    ViewPager viewPager;

    private List<Fragment> fragmentList;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_ordermanager;
    }

    @Override
    protected void initViews() {
        fragmentList = new ArrayList<>();
        for (int i = 2; i < 5; i++) {
            Fragment f1 = OrdersListFragment.newInstance(i);
            fragmentList.add(f1);
        }

        OrderManagerAdapter adapter = new OrderManagerAdapter(getChildFragmentManager(), titles, fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPagerIndicator.setTextSize(40);
        viewPagerIndicator.setViewPager(viewPager);
    }

    @Override
    protected void initDatas() {

    }
}
