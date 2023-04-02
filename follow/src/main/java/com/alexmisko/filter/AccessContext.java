package com.alexmisko.filter;

import com.alexmisko.vo.LoginUserInfo;

// 使用ThreadLocal （线程安全，需要及时清理，保证没有内存泄露，保证线程在重用时不会数据混乱）
public class AccessContext {
    private static final ThreadLocal<LoginUserInfo> loginUserInfo = new ThreadLocal<>();
    public static LoginUserInfo getLoginUserInfo(){
        return loginUserInfo.get();
    }
    public static void setLoginUserInfo(LoginUserInfo loginUserInfo_){
        loginUserInfo.set(loginUserInfo_);
    }
    public static void clearLoginUserInfo(){
        loginUserInfo.remove();
    }
}
