package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.po.SysPicture;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lp on 2018/3/5.
 */
public interface ISysPictureService  {
    public List<SysPicture> querySysPicturesByItemId(Integer itemId);
}
