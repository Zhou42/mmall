package com.mmall.common;

/**
 * Created by yangzhou on 2017-06-15.
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role { // 比enum轻量级的做法
        int ROLE_CUSTOMER = 0; // normal user
        int ROLE_ADMIN = 1; // admin
    }

}
