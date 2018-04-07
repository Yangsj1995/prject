package com.shsxt.xmjf.service.impl;

import com.shsxt.xmjf.api.po.SysPicture;
import com.shsxt.xmjf.api.service.ISysPictureService;
import com.shsxt.xmjf.db.dao.SysPictureMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lp on 2018/3/5.
 */
@Service
public class SysPictureServiceImpl implements  ISysPictureService{

    @Resource
    private SysPictureMapper sysPictureMapper;
    @Override
    public List<SysPicture> querySysPicturesByItemId(Integer itemId) {
        return sysPictureMapper.querySysPicturesByItemId(itemId);
    }
}
