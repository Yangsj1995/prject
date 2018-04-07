package com.shsxt.xmjf.api.service;


import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.BasUser;
import com.shsxt.xmjf.api.po.User;

/**
 * Created by lp on 2018/3/1.
 */
public interface IBasUserService {


    /**
     * 用户注册
     * @param phone
     * @param password
     * @param code
     */
    public  void saveUser(String phone,String password,String code);


    /**
     * 用户登录
     * @param phone
     * @param password
     * @return
     */
    public UserModel userLogin(String phone,String password);


    /**
     * 快捷登录
     * @param phone
     * @param code
     * @return
     */
    public  UserModel quickLogin(String phone,String code);

    public BasUser queryBasUserByUserId(Integer userId);

}
