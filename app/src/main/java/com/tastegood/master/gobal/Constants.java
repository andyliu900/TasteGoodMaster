package com.tastegood.master.gobal;

/**
 * 常量
 *
 * Created by surandy on 2016/10/13.
 */

public interface Constants {

    int ORDER_STATUS_WAITHANDLE = 0;
    int ORDER_STATUS_WAITDISTRIBUTE = 1;
    int ORDER_STATUS_SENDING = 2;
    int ORDER_STATUS_RECEVIVE = 3;
    int ORDER_STATUS_OUTWORK = 4;

    String CLIENT_TYPE = "Android";

    String API_CALLER = "DZAndroid";

    String APP_TYPE = "DZ";

    String API_SECRET_KEY = "9e615c8a150a21f44a0eac71d82e19f20c1a49ff";

    String LOGGER_TAG = "TasteGoodMaster";

    int PAGE_SIZE = 20;

    /** share文件key值 */
    String DEVICE_NO_KEY = "device_no_key";
    String JPUSH_TOKEN_KEY = "jpush_token_key";
    String USER_INFO_KEY = "user_info_key";

    String JGPUSH_TEST = "PROD";

    /** 接口相关 */
    String BASE_URL = "http://www.meiyinheung.com/api/app/";

    String DEVICE_SIGNIN = "device/signIn";

    String UPLOAD_JPUSHTOKEN = "device/sumitJgToken";

    String DZ_LOGIN = "storefront/login";

    String DZ_LOGOUT = "storefront/logout";

    String GET_ORDERS = "storefront/orderList";

    String PRINT_ORDERS = "storefront/orderPrint";

    String PRINT_ORDERS_AGAIN = "storefront/updatePrintNumAgain";

    String START_DISTRIBUTE_ORDER = "storefront/orderStartSend";

}
