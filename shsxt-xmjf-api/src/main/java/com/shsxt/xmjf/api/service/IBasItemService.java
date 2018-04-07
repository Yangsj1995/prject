package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.dto.BasItemDto;
import com.shsxt.xmjf.api.po.BasItem;
import com.shsxt.xmjf.api.query.BasItemQuery;
import com.shsxt.xmjf.api.utils.PageList;

/**
 * Created by lp on 2018/3/3.
 */
public interface IBasItemService {
    public PageList queryBasItemsByParams(BasItemQuery basItemQuery);

    public  void updateBasItemToOpen(Integer basItemId);

    public BasItemDto queryBasItemByItemId(Integer itemId);

    public  Integer updateBasItem(BasItemDto basItemDto);
}
