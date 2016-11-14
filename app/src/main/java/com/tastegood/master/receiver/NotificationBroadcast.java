package com.tastegood.master.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.tastegood.master.MainActivity;
import com.tastegood.master.MasterApplication;
import com.tastegood.master.gobal.BaseActivity;
import com.tastegood.master.order.domain.model.JpushMessageNewOrder;
import com.tastegood.master.order.event.NewOrderActionEvent;
import com.tastegood.master.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by surandy on 2016/11/14.
 */

public class NotificationBroadcast extends BroadcastReceiver {

    public static final String JPUSH_ACTION = "cn.jpush.notification";
    public static final int GO_ORDERS = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null) return;
        Bundle bundle = intent.getExtras();
        if (bundle == null) return;
        Gson gson = new Gson();
        String action = intent.getAction();
        if (JPUSH_ACTION.equals(action)) {
            String messageType = bundle.getString(JpushMessageOperater.MESSAGE_TYPE);
            if (JpushMessageOperater.CONTENT_TYPE_NEWORDER.equals(messageType)) {
                String messagedate = bundle.getString(JpushMessageOperater.MESSAGE_DATE);
                JpushMessageNewOrder message = gson.fromJson(messagedate, JpushMessageNewOrder.class) ;
                goMainActivity(context, message);
            }
        }
    }

    private void goMainActivity(Context context, JpushMessageNewOrder message) {
        BaseActivity currentActivity = MasterApplication.getApplication(context).getCurrentActivity();
        Bundle bundle = new Bundle();
        bundle.putBoolean(MainActivity.NEWORDER_FLAG, true);
        if (currentActivity != null) {
            if (currentActivity instanceof MainActivity) {

            } else {
                UIUtils.openActivity(currentActivity, MainActivity.class);
            }
            EventBus.getDefault().post(new NewOrderActionEvent());
        } else {
            Intent in = new Intent(context, MainActivity.class);
            in.putExtras(bundle);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
        }
    }
}
