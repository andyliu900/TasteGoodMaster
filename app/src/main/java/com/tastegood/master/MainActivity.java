package com.tastegood.master;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tastegood.master.gobal.BaseToolbarActivity;
import com.tastegood.master.gobal.Constants;
import com.tastegood.master.gobal.Injection;
import com.tastegood.master.order.OrderManageFragment;
import com.tastegood.master.order.OrdersListFragment;
import com.tastegood.master.order.event.NewOrderActionEvent;
import com.tastegood.master.order.event.NewOrderListEvent;
import com.tastegood.master.order.event.RefreshListEvent;
import com.tastegood.master.order.event.SwitchFragmentEvent;
import com.tastegood.master.user.LoginActivity;
import com.tastegood.master.user.UserContract;
import com.tastegood.master.user.UserPresenter;
import com.tastegood.master.user.domain.model.UserInfo;
import com.tastegood.master.util.SPUtils;
import com.tastegood.master.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends BaseToolbarActivity implements UserContract.LogoutView {

    public static final String NEWORDER_FLAG = "neworder_flag";

    private UserContract.Presenter mPresenter;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)
    RelativeLayout navigationView;
    @Bind(R.id.shop_name)
    TextView shop_name;
    @Bind(R.id.logoutbtn)
    Button logoutbtn;
    @Bind(R.id.main_waithandle)
    Button main_waithandle;
    @Bind(R.id.main_waitdistribute)
    Button main_waitdistribute;
    @Bind(R.id.main_billmanage)
    Button main_billmanage;

    private static final int TAB_WAITHANDLE = 1;
    private static final int TAB_WAITDISTRIBUTE = 2;
    private static final int TAB_BILLMANAGE = 3;
    private OrdersListFragment waitHandleFragment;
    private OrdersListFragment waitDistributeFragment;
    private OrderManageFragment orderManageFragment;
    Fragment currentFragment = null;
    int currentFragmentIndex = 0;
    boolean editable = false; // 列表编辑状态

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar("今日订单", R.drawable.ic_menu, new onNavigationOnClickListener() {
            @Override
            public void setOnNavigationOnClickListener() {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        }, "编辑", new onRightImgOnClickListener() {
            @Override
            public void setOnRightImgOnClickListener() {
                if (waitHandleFragment != null) {
                    editable = !editable;
                    waitHandleFragment.changeEditStatus(editable);
                    if (editable) { // 可编辑，显示"完成"
                        setToolbarRightTextContent("完成");
                    } else { // 不可编辑，显示"编辑"
                        setToolbarRightTextContent("编辑");
                    }
                }
            }
        });

        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);

        newOrderAction();
    }

    @Override
    protected void initViews() {
        changeFragment(TAB_WAITHANDLE);
        main_waithandle.setSelected(true);
        main_waithandle.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected void initDatas() {
        UserInfo userInfo= (UserInfo) SPUtils.get(Constants.USER_INFO_KEY, UserInfo.class);
        if (userInfo != null) {
            shop_name.setText(userInfo.getNickname());
            logoutbtn.setVisibility(View.VISIBLE);
        } else {
            logoutbtn.setVisibility(View.GONE);
        }

        mPresenter = new UserPresenter(
                Injection.provideUseCaseHandler(),
                this,
                Injection.provideLogoutTask());
    }

    @OnClick({R.id.main_waithandle, R.id.main_waitdistribute, R.id.main_billmanage,
                R.id.logoutbtn})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.main_waithandle:
                setToolbarRightTextVisable(View.VISIBLE);
                switchButtonBG(main_waithandle);
                changeFragment(TAB_WAITHANDLE);
                break;
            case R.id.main_waitdistribute:
                setToolbarRightTextVisable(View.GONE);
                switchButtonBG(main_waitdistribute);
                changeFragment(TAB_WAITDISTRIBUTE);
                break;
            case R.id.main_billmanage:
                setToolbarRightTextVisable(View.GONE);
                switchButtonBG(main_billmanage);
                changeFragment(TAB_BILLMANAGE);
                break;
            case R.id.logoutbtn:
                showdialog("", "你确定要注销？", new String[]{"取消", "确认"});
                break;
            default:
                break;
        }
    }

    long lastBackPressTime = 0;

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(navigationView)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        if (lastBackPressTime < System.currentTimeMillis() - 3000L) {
            UIUtils.showShort(this, "再按一次返回键退出");
            lastBackPressTime = System.currentTimeMillis();
        } else {
            if (application != null) {
                application.exit();
            }
        }
    }

    private void switchButtonBG(Button clickedButton) {
        main_waithandle.setSelected(false);
        main_waithandle.setTextColor(getResources().getColor(R.color.colorAccent));
        main_waitdistribute.setSelected(false);
        main_waitdistribute.setTextColor(getResources().getColor(R.color.colorAccent));
        main_billmanage.setSelected(false);
        main_billmanage.setTextColor(getResources().getColor(R.color.colorAccent));

        clickedButton.setSelected(true);
        clickedButton.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void changeFragment(int index) {
        if (index == currentFragmentIndex) {
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        currentFragmentIndex = index;
        switch (currentFragmentIndex) {
            case TAB_WAITHANDLE:
                if (waitHandleFragment == null) {
                    waitHandleFragment = OrdersListFragment.newInstance(Constants.ORDER_STATUS_WAITHANDLE);
                    ft.add(R.id.contentFrame, waitHandleFragment, "WAITHANDLEFRAGMENT");
                }
                currentFragment = waitHandleFragment;
                break;
            case TAB_WAITDISTRIBUTE:
                if (waitDistributeFragment == null) {
                    waitDistributeFragment = OrdersListFragment.newInstance(Constants.ORDER_STATUS_WAITDISTRIBUTE);
                    ft.add(R.id.contentFrame, waitDistributeFragment, "WAITDISTRIBUTEFRAGMENT");
                }
                currentFragment = waitDistributeFragment;
                break;
            case TAB_BILLMANAGE:
                if (orderManageFragment == null) {
                    orderManageFragment = new OrderManageFragment();
                    ft.add(R.id.contentFrame, orderManageFragment, OrderManageFragment.TAG);
                }
                currentFragment = orderManageFragment;
                break;
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void goLoginActivity() {
        UIUtils.openActivity(this, LoginActivity.class);
        finish();
    }

    @Override
    public void showLogoutSuccessToast() {

    }

    @Override
    public void showLogoutFailToast() {
        UIUtils.showShort(this, "账号退出失败");
    }

    @Override
    public void showLoadingView() {
        showProgressDialog("请稍等...");
    }

    @Override
    public void hideLoadingView() {
        dismissProgressDialog();
    }

    @Override
    public void showBusinessError(String businessErrorMessage) {
        UIUtils.showShort(this, businessErrorMessage);
    }

    @Override
    public void SureListener() {
        super.SureListener();
        showLoadingView();
        UserInfo userInfo = (UserInfo) SPUtils.get(Constants.USER_INFO_KEY, UserInfo.class);
        mPresenter.logout(userInfo.getNickname());
    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onEventMainThread(Object event) {
        if (event instanceof RefreshListEvent) {
            String fragmentTag = ((RefreshListEvent)event).getFragmentTag();
            if (fragmentTag.equals(OrdersListFragment.WAITHANDLE_FRAGMENT_TAG)) {
                editable = false;
                setToolbarRightTextContent("编辑");
            }
        } else if (event instanceof SwitchFragmentEvent) {
            setToolbarRightTextVisable(View.GONE);
            switchButtonBG(main_billmanage);
            changeFragment(TAB_BILLMANAGE);
        } else if (event instanceof NewOrderActionEvent) {
            switchButtonBG(main_waithandle);
            changeFragment(TAB_WAITHANDLE);
            newOrderAction();
        }
    }

    private void newOrderAction() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            boolean hasNewOrder = bundle.getBoolean(NEWORDER_FLAG);
            if (hasNewOrder) {
                switchButtonBG(main_waithandle);
                changeFragment(TAB_WAITHANDLE);
                EventBus.getDefault().post(new NewOrderListEvent());
            }
        }
    }
}
