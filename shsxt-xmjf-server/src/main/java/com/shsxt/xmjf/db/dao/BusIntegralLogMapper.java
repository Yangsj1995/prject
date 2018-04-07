package com.shsxt.xmjf.db.dao;

import com.shsxt.xmjf.api.po.BusIntegralLog;

public interface BusIntegralLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusIntegralLog record);

    int insertSelective(BusIntegralLog record);

    BusIntegralLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BusIntegralLog record);

    int updateByPrimaryKey(BusIntegralLog record);
}