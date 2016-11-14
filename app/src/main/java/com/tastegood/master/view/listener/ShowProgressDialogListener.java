package com.tastegood.master.view.listener;

/**
 * Created by surandy on 2016/10/28.
 */

public interface ShowProgressDialogListener {

    void showProgressDialog(String textString);

    void showProgressDialog(String textString, boolean cancleable);

    void dismissProgressDialog();

}
