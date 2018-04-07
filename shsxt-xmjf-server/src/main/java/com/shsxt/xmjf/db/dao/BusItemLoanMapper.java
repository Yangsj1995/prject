package com.shsxt.xmjf.db.dao;

import com.shsxt.xmjf.api.po.BusItemLoan;
import com.shsxt.xmjf.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface BusItemLoanMapper  extends BaseMapper<BusItemLoan>{
    public  BusItemLoan queryBusItemLoanByLoanItemId(@Param("itemId") Integer itemId);

}