package com.shsxt.xmjf.db.dao;

import com.shsxt.xmjf.api.po.User;

/**
 * Created by lp on 2018/3/1.
 */
public interface UserDao {
    public User queryUserById(Integer userId);
}
