package com.shsxt.xmjf.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.constants.P2pConstant;
import com.shsxt.xmjf.api.dto.BasItemDto;
import com.shsxt.xmjf.api.po.BasItem;
import com.shsxt.xmjf.api.query.BasItemQuery;
import com.shsxt.xmjf.api.service.IBasItemService;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.shsxt.xmjf.api.utils.PageList;
import com.shsxt.xmjf.db.dao.BasItemMapper;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.prefs.BackingStoreException;

/**
 * Created by lp on 2018/3/3.
 */
@Service
public class BasItemServiceImpl implements IBasItemService {
    @Resource
    private BasItemMapper basItemMapper;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public PageList queryBasItemsByParams(BasItemQuery basItemQuery) {

        // 获取 状态值=1 开放剩余时间
        // itemCycle,itemType,isHistory ,pageNum ,pageSize
       String queryKey="basItemList:itemCycle:"+basItemQuery.getItemCycle()
                +":itemType:"+basItemQuery.getItemType()
                +":isHistory:"+basItemQuery.getIsHistory()
                +":pageNum:"+basItemQuery.getPageNum()
                +":pageSize:"+basItemQuery.getPageSize();
        // 获取缓存数
        ValueOperations<String,Object> valueOperations=redisTemplate.opsForValue();
        Page<BasItemDto> basItemDtos= (Page<BasItemDto>) valueOperations.get(queryKey);
        if(CollectionUtils.isEmpty(basItemDtos)){
            PageHelper.startPage(basItemQuery.getPageNum(),basItemQuery.getPageSize());
           // PageInfo
            basItemDtos= (Page<BasItemDto>) basItemMapper.queryByParams(basItemQuery);
            if(!CollectionUtils.isEmpty(basItemDtos)){
                for(BasItemDto basItemDto:basItemDtos){
                    if(basItemDto.getItemStatus().equals(1)){
                        basItemDto.setSyTime((basItemDto.getReleaseTime().getTime()-new Date().getTime())/1000);
                    }
                    basItemDto.setSyAmount(basItemDto.getItemAccount().add(basItemDto.getItemOngoingAccount().negate()));
                }
                // 数据库中结果存在  放入缓存
                valueOperations.set(queryKey,basItemDtos);
            }
        }
        return new PageList(basItemDtos);
    }

    @Override
    public void updateBasItemToOpen(Integer basItemId) {
        BasItemDto basItem=basItemMapper.queryById(basItemId);
        AssertUtil.isTrue(null==basItem,"该记录不存在!");
        basItem.setItemStatus(10);
        AssertUtil.isTrue(basItemMapper.update(basItem)<1, P2pConstant.OPT_FAILED_MSG);
    }

    @Override
    public BasItemDto queryBasItemByItemId(Integer itemId) {
        return basItemMapper.queryById(itemId);
    }

    @Override
    public Integer updateBasItem(BasItemDto basItemDto) {
        return basItemMapper.update(basItemDto);
    }
}
