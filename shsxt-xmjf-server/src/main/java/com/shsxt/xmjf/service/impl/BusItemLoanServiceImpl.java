package com.shsxt.xmjf.service.impl;

import com.shsxt.xmjf.api.po.BusItemLoan;
import com.shsxt.xmjf.api.service.IBusItemLoanService;
import com.shsxt.xmjf.db.dao.BusItemLoanMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by lp on 2018/3/5.
 */
@Service
public class BusItemLoanServiceImpl implements IBusItemLoanService {
    @Resource
    private BusItemLoanMapper busItemLoanMapper;
    @Override
    public BusItemLoan queryBusItemLoanByItemId(Integer itemId) {
        return  busItemLoanMapper.queryBusItemLoanByLoanItemId(itemId);
    }
}
