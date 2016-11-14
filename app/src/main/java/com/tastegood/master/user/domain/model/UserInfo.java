package com.tastegood.master.user.domain.model;

import java.io.Serializable;

/**
 * 用户信息实体
 *
 * Created by surandy on 2016/10/17.
 */

public class UserInfo implements Serializable {

    private int status;
    private int storefrontId;
    private String nickname;
    private String storefront;
    private String telephone;
    private String imgUrl;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStorefrontId() {
        return storefrontId;
    }

    public void setStorefrontId(int storefrontId) {
        this.storefrontId = storefrontId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStorefront() {
        return storefront;
    }

    public void setStorefront(String storefront) {
        this.storefront = storefront;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
