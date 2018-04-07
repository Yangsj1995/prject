package com.shsxt.xmjf.db.dao;

import com.shsxt.xmjf.api.po.BusUserStat;
import com.shsxt.xmjf.api.po.BusUserStatKey;
import com.shsxt.xmjf.base.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface BusUserStatMapper extends BaseMapper<BusUserStat>{
    public  BusUserStat queryBusUserStatByUserId(@Param("userId")Integer userId);

}