package com.test.firebasetast.areaTemp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class memberData {


    private String account;
    private String password;
    private String address;


    public memberData(String account, String password, String address) {
        this.account = account;
        this.password = password;
        this.address = address;
    }

    public memberData(){}

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



}
