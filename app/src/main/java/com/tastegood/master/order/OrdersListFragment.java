package com.tastegood.master.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import com.tastegood.master.R;
import com.tastegood.master.gobal.BaseFragment;
import com.tastegood.master.gobal.Constants;
import com.tastegood.master.gobal.Injection;
import com.tastegood.master.order.domain.model.Order;
import com.tastegood.master.order.event.NewOrderListEvent;
import com.tastegood.master.order.event.RefreshListEvent;
import com.tastegood.master.order.event.SwitchFragmentEvent;
import com.tastegood.master.order.view.OrderRecordOutworkView;
import com.tastegood.master.order.view.OrderRecordReceviveView;
import com.tastegood.master.order.view.OrderRecordSendingView;
import com.tastegood.master.order.view.OrderRecordWaitDistributeView;
import com.tastegood.master.order.view.OrderRecordWaitHandleView;
import com.tastegood.master.user.LoginActivity;
import com.tastegood.master.user.domain.model.UserInfo;
import com.tastegood.master.util.NetworkUtils;
import com.tastegood.master.util.SPUtils;
import com.tastegood.master.util.UIUtils;
import com.tastegood.master.view.LoadRecyclerView;
import com.tastegood.master.view.LoadRecycleviewAdapter;
import com.tastegood.master.view.animation.AllSelectAnimation;
import com.tastegood.master.view.animation.ViewExpandAnimation;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 订单列表界面
 *
 * Created by surandy on 2016/10/13.
 */

public class OrdersListFragment extends BaseFragment implements OrderContract.OrderView,
        LoadRecycleviewAdapter.ILoadListener {

    public static final String WAITHANDLE_FRAGMENT_TAG = "waithandle_fragment_tag";

    private static final String ORDER_STATUS = "order_status";
    private static final int LISTTYPE_WAITHANDLE = 1;
    private static final int LISTTYPE_WAITDISTRIBUTE = 2;
    private static final int LISTTYPE_SEDING = 3;
    private static final int LISTTYPE_RECEVIVE = 4;
    private static final int LISTTYPE_OUTWORK = 5;

    private OrderContract.Presenter mPresenter;
    private boolean currentedit_status = false;

    @Bind(R.id.cartListViewLayout)
    View cartListViewLayout;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;
    @Bind(R.id.recyclerview)
    LoadRecyclerView recyclerview;
    @Bind(R.id.view_nodata)
    View view_nodata;
    @Bind(R.id.view_nonetwork)
    View view_nonetwork;
    @Bind(R.id.allPrintLayout)
    View allPrintLayout;
    @Bind(R.id.all_checkbox)
    ImageView all_checkbox;
    @Bind(R.id.allPrintBtn)
    Button allPrintBtn;

    private UserInfo userInfo;
    private OrderRecordAdapter orderRecordAdapter;
    private boolean isAllSelected;
    private boolean isRefreshing;
    private boolean isLoading;
    private boolean isLoaded;
    private int current_page;
    private int status;
    List<Order> items = new ArrayList();
    LinearLayoutManager linearLayoutManager;
    OrderRecordWaitHandleView.ItemClickListener itemClickListener;
    OrderRecordWaitDistributeView.ItemClickListener distributeItemClickListener;

    public static OrdersListFragment newInstance(int orderStatus) {
        Bundle args = new Bundle();
        args.putInt(ORDER_STATUS, orderStatus);
        OrdersListFragment fragment = new OrdersListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_orderslist;
    }

    @Override
    protected void initViews() {
        current_page = 1;
        linearLayoutManager = new LinearLayoutManager(pActivity);
        recyclerview.setLayoutManager(linearLayoutManager);
        orderRecordAdapter = new OrderRecordAdapter(true, this);

        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (!NetworkUtils.isConnected(pActivity)) {
                    UIUtils.showShort(pActivity, "网络连接出了问题，请检查网络");
                    mPtrFrame.refreshComplete();
                    return;
                }
                if (!isRefreshing) {
                    current_page = 1;
                    isRefreshing = true;
                    isLoading = false;
                    isLoaded = false;
                    mPresenter.getOrderList(userInfo.getStorefrontId(), current_page, Constants.PAGE_SIZE, status);
                    if (currentedit_status) {
                        changeEditStatus(false);
                    }
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                //设置下拉刷新的条件
                return linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0;
            }
        });

        itemClickListener = new OrderRecordWaitHandleView.ItemClickListener() {

            @Override
            public void checkBoxClick() {
                changeAllCheckBoxStatus();
            }

            @Override
            public void itemheaderClick(View actionView, final int position) {
                ViewExpandAnimation expandAnimation = new ViewExpandAnimation(actionView);
                expandAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        linearLayoutManager.scrollToPosition(position);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                actionView.startAnimation(expandAnimation);
            }

            @Override
            public void printClick(Order order) {
                showLoadingView("打印中...");
                mPresenter.printOrders(order.getStorefrontId(), new int[]{order.getId()});
            }

        };

        distributeItemClickListener = new OrderRecordWaitDistributeView.ItemClickListener() {
            @Override
            public void itemheaderClick(View actionView, final int position) {
                ViewExpandAnimation expandAnimation = new ViewExpandAnimation(actionView);
                expandAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        linearLayoutManager.scrollToPosition(position);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                actionView.startAnimation(expandAnimation);
            }

            @Override
            public void printAgainClick(Order order) {
                showLoadingView("再次打印中...");
                mPresenter.printOrderAgain(order.getStorefrontId(), order.getId());
            }

            @Override
            public void startDistributeClick(Order order) {
                showLoadingView("开始配送...");
                mPresenter.startDistributeOrder(order.getStorefrontId(), order.getId());
            }
        };

    }

    @Override
    protected void initDatas() {
        mPresenter = new OrderPresenter(
                Injection.provideUseCaseHandler(),
                this,
                Injection.provideOrderTask(),
                Injection.providePrintOrderTask(),
                Injection.providePrintOrderAgainTask(),
                Injection.provideStartDistributeOrderTask());

        if (NetworkUtils.isConnected(pActivity)) {
            userInfo = (UserInfo) SPUtils.get(Constants.USER_INFO_KEY, UserInfo.class);
            if (userInfo != null) {
                isRefreshing = true;
                showLoadingView();
                status = getArguments().getInt(ORDER_STATUS);
                mPresenter.getOrderList(userInfo.getStorefrontId(), current_page, Constants.PAGE_SIZE, status);
            } else {
                UIUtils.showShort(pActivity, "请先登录");
                UIUtils.openActivity(pActivity, LoginActivity.class);
                pActivity.finish();
            }
        } else {
            showNetErrorView(true);
        }
    }

    @OnClick({R.id.all_checkbox, R.id.allPrintBtn, R.id.nodate_tv})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.all_checkbox:
                for (Order order : items) {
                    order.setChecked(!isAllSelected);
                }
                orderRecordAdapter.notifyDataSetChanged();
                isAllSelected = !isAllSelected;
                break;
            case R.id.allPrintBtn:
                if (items.size() <= 0) {
                    UIUtils.showShort(pActivity, "暂无可打印订单");
                    return;
                }
                List<Integer> selectedOrderIds = new ArrayList();
                for (Order order : items) {
                    if (order.isChecked()) {
                        selectedOrderIds.add(order.getId());
                    }
                }
                if (selectedOrderIds.size() <= 0) {
                    UIUtils.showShort(pActivity, "请勾选订单");
                    return;
                }
                int[] orderIds = new int[selectedOrderIds.size()];
                for (int i = 0; i < selectedOrderIds.size(); i++) {
                    orderIds[i] = selectedOrderIds.get(i);
                }
                showLoadingView("打印中...");
                mPresenter.printOrders(userInfo.getStorefrontId(), orderIds);
                break;
            case R.id.nodate_tv:
                showLoadingView();
                current_page = 1;
                isRefreshing = true;
                isLoading = false;
                isLoaded = false;
                mPresenter.getOrderList(userInfo.getStorefrontId(), current_page, Constants.PAGE_SIZE, status);
                break;
        }
    }

    @Override
    public void showLoadingView() {
        showProgressDialog("加载中...");
    }

    @Override
    public void showLoadingView(String message) {
        showProgressDialog(message);
    }

    @Override
    public void hideLoadingView() {
        dismissProgressDialog();
    }

    @Override
    public void showNetErrorView(boolean isFirst) {
        if (isFirst) {
            view_nonetwork.setVisibility(View.VISIBLE);
            view_nodata.setVisibility(View.GONE);
            mPtrFrame.setVisibility(View.GONE);
        } else {
            UIUtils.showShort(pActivity, "网络连接错误，请重试");
        }
    }

    @Override
    public void hideNetErrorView() {
        view_nonetwork.setVisibility(View.GONE);
        view_nodata.setVisibility(View.GONE);
        mPtrFrame.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoDataView(boolean isFirst) {
        if (isFirst) {
            mPtrFrame.setVisibility(View.GONE);
            view_nonetwork.setVisibility(View.GONE);
            view_nodata.setVisibility(View.VISIBLE);
        } else {
            UIUtils.showShort(pActivity, "暂无记录");
        }
    }

    @Override
    public void hideNoDataView() {
        view_nonetwork.setVisibility(View.GONE);
        view_nodata.setVisibility(View.GONE);
        mPtrFrame.setVisibility(View.VISIBLE);
    }

    @Override
    public void showBusinessError(String businessErrorMessage) {
        UIUtils.showShort(pActivity, businessErrorMessage);
    }

    @Override
    public void showOrderList(List<Order> orderList) {
        if (orderList != null) {
            if (current_page == 1) {
                if (orderList != null && orderList.size() > 0) {
                    if (recyclerview.getRecyclerView().getAdapter() == null) {
                        recyclerview.setAdapter(orderRecordAdapter);
                    }
                    items.clear();
                    items.addAll(orderList);
                    orderRecordAdapter.notifyDataSetChanged();
                    if (status == 0) {
                        EventBus.getDefault().post(new RefreshListEvent(WAITHANDLE_FRAGMENT_TAG));
                    }
                } else {
                    showNoDataView(true);
                }
            } else {
                if (orderList != null && orderList.size() > 0) {
                    items.addAll(orderList);
                    orderRecordAdapter.notifyDataSetChanged();
                } else {
                    isLoaded = true;
                    orderRecordAdapter.setLoadFooterState(LoadRecycleviewAdapter.LoadState.LoadFinish);
                    return;
                }
            }

            if (orderList.size() < Constants.PAGE_SIZE) {
                isLoaded = true;
                orderRecordAdapter.setLoadFooterState(LoadRecycleviewAdapter.LoadState.LoadFinish);
            } else {
                isLoading = false;
                orderRecordAdapter.setLoadFooterState(LoadRecycleviewAdapter.LoadState.LoadAgain);
            }
        } else {
            if (current_page == 1) {
                if (items.isEmpty()) {
                    showNoDataView(true);
                }
            }
            if (isLoading) {
                current_page--;
                orderRecordAdapter.setLoadFooterState(LoadRecycleviewAdapter.LoadState.LoadFailed);
            }
        }
        if (isRefreshing) {
            isRefreshing = false;
            mPtrFrame.refreshComplete();
        }
        if (isLoading) {
            isLoading = false;
        }

        changeAllCheckBoxStatus();

    }

    @Override
    public void showPrintResult(String result) {
        if ("true".equals(result)) {
            current_page = 1;
            mPresenter.getOrderList(userInfo.getStorefrontId(), current_page, Constants.PAGE_SIZE, status);
            changeEditStatus(false);
            UIUtils.showLong(pActivity, "打印成功");
        } else {
            UIUtils.showLong(pActivity, "打印失败");
        }
    }

    @Override
    public void showPrintAgainResult(String result) {
        if ("true".equals(result)) {
            current_page = 1;
            mPresenter.getOrderList(userInfo.getStorefrontId(), current_page, Constants.PAGE_SIZE, status);
            UIUtils.showLong(pActivity, "打印成功");
        } else {
            UIUtils.showLong(pActivity, "打印失败");
        }
    }

    @Override
    public void showDistributeOrderResult(String result) {
        if ("配送成功".equals(result)) {
            current_page = 1;
            mPresenter.getOrderList(userInfo.getStorefrontId(), current_page, Constants.PAGE_SIZE, status);
            UIUtils.showLong(pActivity, "开始配送成功");
            EventBus.getDefault().post(new SwitchFragmentEvent(new OrderManageFragment()));
        } else {
            UIUtils.showLong(pActivity, "开始配送失败");
        }
    }

    @Override
    public void setPresenter(OrderContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void loading() {
        if (!isLoading && !isLoaded) {
            current_page++;
            isLoading = true;
            mPresenter.getOrderList(userInfo.getStorefrontId(), current_page, Constants.PAGE_SIZE, status);
        }
    }

    @Override
    public void onEventMainThread(Object event) {
        if (event instanceof NewOrderListEvent) {
            showLoadingView();
            current_page = 1;
            isRefreshing = true;
            isLoading = false;
            isLoaded = false;
            mPresenter.getOrderList(userInfo.getStorefrontId(), current_page, Constants.PAGE_SIZE, status);
        }
    }

    /**
     * 改变列表编辑状态
     *
     * @param editFlag
     */
    public void changeEditStatus(boolean editFlag) {
        currentedit_status = editFlag;
        for (Order order : items) {
            order.setEditStatus(editFlag);
        }
        orderRecordAdapter.notifyDataSetChanged();
        AllSelectAnimation animation = new AllSelectAnimation(pActivity, cartListViewLayout, editFlag);
        cartListViewLayout.startAnimation(animation);
    }

    private void changeAllCheckBoxStatus() {
        int checkedCount = 0;
        for (Order order : items) {
            if (order.isChecked()) {
                checkedCount++;
            }
        }
        if (checkedCount >= items.size() && checkedCount != 0) {
            isAllSelected = true;
            all_checkbox.setImageResource(R.drawable.ic_checkbox_p);
        } else {
            isAllSelected = false;
            all_checkbox.setImageResource(R.drawable.ic_checkbox_n);
        }
    }

    class OrderRecordAdapter extends LoadRecycleviewAdapter {

        public OrderRecordAdapter(boolean isEnableLoad, ILoadListener listener) {
            super(isEnableLoad, listener);
        }

        @Override
        public int getSubItemCount() {
            return items == null ? 0 : items.size();
        }

        @Override
        public void onBindSubViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof OrderRecordWaitHandleView) {
                ((OrderRecordWaitHandleView) holder).setData(items.get(position), position);
            } else if (holder instanceof OrderRecordWaitDistributeView) {
                ((OrderRecordWaitDistributeView) holder).setData(items.get(position), position);
            } else if (holder instanceof OrderRecordSendingView) {
                ((OrderRecordSendingView) holder).setData(items.get(position));
            } else if (holder instanceof OrderRecordReceviveView) {
                ((OrderRecordReceviveView) holder).setData(items.get(position));
            } else if (holder instanceof OrderRecordOutworkView) {
                ((OrderRecordOutworkView) holder).setData(items.get(position));
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateSubViewHolder(ViewGroup parent, int viewType) {
            if (viewType == LISTTYPE_WAITHANDLE) {
                return new OrderRecordWaitHandleView(parent, itemClickListener);
            } else if (viewType == LISTTYPE_WAITDISTRIBUTE) {
                return new OrderRecordWaitDistributeView(parent, distributeItemClickListener);
            } else if (viewType == LISTTYPE_SEDING) {
                return new OrderRecordSendingView(parent);
            } else if (viewType == LISTTYPE_RECEVIVE) {
                return  new OrderRecordReceviveView(parent);
            } else if (viewType == LISTTYPE_OUTWORK) {
                return  new OrderRecordOutworkView(parent);
            } else {
                return new OrderRecordWaitHandleView(parent, itemClickListener);
            }
        }

        @Override
        public int getSubItemViewType(int position) {
            if (status == Constants.ORDER_STATUS_WAITHANDLE) {
                return LISTTYPE_WAITHANDLE;
            } else if (status == Constants.ORDER_STATUS_WAITDISTRIBUTE) {
                return LISTTYPE_WAITDISTRIBUTE;
            } else if (status == Constants.ORDER_STATUS_SENDING) {
                return LISTTYPE_SEDING;
            } else if (status == Constants.ORDER_STATUS_RECEVIVE) {
                return LISTTYPE_RECEVIVE;
            } else if (status == Constants.ORDER_STATUS_OUTWORK) {
                return LISTTYPE_OUTWORK;
            }  else {
                return 0;
            }
        }
    }

}