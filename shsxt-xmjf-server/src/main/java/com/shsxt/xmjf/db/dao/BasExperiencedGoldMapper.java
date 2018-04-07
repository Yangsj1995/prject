package com.shsxt.xmjf.db.dao;

import com.shsxt.xmjf.api.po.BasExperiencedGold;

public interface BasExperiencedGoldMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BasExperiencedGold record);

    int insertSelective(BasExperiencedGold record);

    BasExperiencedGold selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BasExperiencedGold record);

    int updateByPrimaryKey(BasExperiencedGold record);
}