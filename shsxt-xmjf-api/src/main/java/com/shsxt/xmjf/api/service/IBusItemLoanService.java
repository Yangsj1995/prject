package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.po.BusItemLoan;

/**
 * Created by lp on 2018/3/5.
 */
public interface IBusItemLoanService {
    public BusItemLoan queryBusItemLoanByItemId(Integer itemId);
}
