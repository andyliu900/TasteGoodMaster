package com.tastegood.master;

import com.tastegood.master.data.source.device.DeviceDataSourceImpl;
import com.tastegood.master.data.source.device.DeviceRepository;
import com.tastegood.master.data.source.oeder.OrderDataSourceImpl;
import com.tastegood.master.data.source.oeder.OrderRepository;
import com.tastegood.master.data.source.user.UserDataSourceImpl;
import com.tastegood.master.data.source.user.UserRepository;
import com.tastegood.master.order.domain.usecase.OrderTask;
import com.tastegood.master.user.domain.usecase.LoginTask;
import com.tastegood.master.user.domain.usecase.LogoutTask;
import com.tastegood.master.gobal.UseCaseHandler;

/**
 * Created by surandy on 2016/10/18.
 */

public class Injection {

    public static DeviceRepository provideDeviceRepository() {
        return DeviceRepository.getInstance(DeviceDataSourceImpl.getInstance());
    }

    public static UserRepository provideUserRepostitory() {
        return UserRepository.getInstance(UserDataSourceImpl.getInstance());
    }

    public static OrderRepository provideOrderRepostitory() {
        return OrderRepository.getInstance(OrderDataSourceImpl.getInstance());
    }

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }

    public static LoginTask provideLoginTask() {
        return new LoginTask(Injection.provideUserRepostitory());
    }

    public static LogoutTask provideLogoutTask() {
        return new LogoutTask(Injection.provideUserRepostitory());
    }

    public static OrderTask provideOrderTask() {
        return new OrderTask(Injection.provideOrderRepostitory());
    }

}
