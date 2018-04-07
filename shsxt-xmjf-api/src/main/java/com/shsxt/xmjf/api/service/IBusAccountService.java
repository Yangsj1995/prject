package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.dto.AccountDto;
import com.shsxt.xmjf.api.po.BusAccount;

import java.util.List;

/**
 * Created by lp on 2018/3/5.
 */
public interface IBusAccountService {

    public BusAccount queryBusAccountInfoByUserId(Integer userId);

    public  Integer updateBusAccount(BusAccount busAccount);


    public List<AccountDto> queryAccountInfoByUserId(Integer userId);
}
