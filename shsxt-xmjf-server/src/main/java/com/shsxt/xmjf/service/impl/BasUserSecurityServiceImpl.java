package com.shsxt.xmjf.service.impl;

import com.shsxt.xmjf.api.constants.P2pConstant;
import com.shsxt.xmjf.api.po.BasUserSecurity;
import com.shsxt.xmjf.api.service.IBasUserSecurityService;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.shsxt.xmjf.db.dao.BasUserSecurityMapper;
import com.shsxt.xmjf.utils.MD5;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by lp on 2018/3/5.
 */
@Service
public class BasUserSecurityServiceImpl implements IBasUserSecurityService {
    @Resource
    private BasUserSecurityMapper basUserSecurityMapper;
    @Override
    public BasUserSecurity queryBasUserSecurityByUserId(Integer userId) {
        return basUserSecurityMapper.queryBasUserSecurityByUserId(userId);
    }

    @Override
    public void updateBasUserSecurityByUserId(Integer userId,String realName, String idCard, String busiPassword,String confirmBusiPassword) {
        /**
         * 1.参数校验
         *     realName 非空
         *     idCard  非空  18位 不可重复
         *     busiPassword  非空  数字  至少6位
         *  2.执行更新
         */
        checkParams(realName,idCard,busiPassword,confirmBusiPassword);
        BasUserSecurity basUserSecurity=basUserSecurityMapper.queryBasUserSecurityByUserId(userId);
        basUserSecurity.setIdentifyCard(idCard);
        basUserSecurity.setPaymentPassword(MD5.toMD5(busiPassword));
        basUserSecurity.setRealname(realName);
        basUserSecurity.setRealnameStatus(1);
        basUserSecurity.setVerifyTime(new Date());
        AssertUtil.isTrue(basUserSecurityMapper.update(basUserSecurity)<1, P2pConstant.OPT_FAILED_MSG);
    }

    private void checkParams(String realName, String idCard, String busiPassword,String confirmBusiPassword) {
        AssertUtil.isTrue(StringUtils.isBlank(realName),"姓名不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(idCard)||idCard.length()!=18,"身份证长度不合法!");
        AssertUtil.isTrue(null !=basUserSecurityMapper.queryBasUserSecurityByIdCard(idCard),"身份证已存在!");
        AssertUtil.isTrue(StringUtils.isBlank(busiPassword)||busiPassword.length()<6,"交易密码过于简单");
        AssertUtil.isTrue(StringUtils.isBlank(confirmBusiPassword)||busiPassword.length()<6,"确认密码非法!");
        AssertUtil.isTrue(!busiPassword.equals(confirmBusiPassword),"密码输入不一致!");
        // 正则 校验字符串 为纯数字
        AssertUtil.isTrue(!(busiPassword.matches("[0-9]+")),"交易密码必须为数字!");
    }
}
