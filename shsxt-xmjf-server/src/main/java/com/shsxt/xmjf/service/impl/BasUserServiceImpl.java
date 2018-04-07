package com.shsxt.xmjf.service.impl;


import com.shsxt.xmjf.api.constants.P2pConstant;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.BasUser;
import com.shsxt.xmjf.api.po.BasUserInfo;
import com.shsxt.xmjf.api.po.BasUserSecurity;
import com.shsxt.xmjf.api.po.User;
import com.shsxt.xmjf.api.service.IBasUserService;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.shsxt.xmjf.api.utils.RandomCodesUtils;
import com.shsxt.xmjf.db.dao.BasUserInfoMapper;
import com.shsxt.xmjf.db.dao.BasUserMapper;
import com.shsxt.xmjf.db.dao.BasUserSecurityMapper;
import com.shsxt.xmjf.db.dao.UserDao;
import com.shsxt.xmjf.utils.MD5;
import com.sun.corba.se.impl.encoding.CDROutputObject;
import org.apache.commons.codec.language.bm.Rule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lp on 2018/3/1.
 */
@Service
public class BasUserServiceImpl implements IBasUserService {
    @Resource
    private BasUserMapper basUserMapper;


    @Resource
    private BasUserInfoMapper basUserInfoMapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private BasUserSecurityMapper basUserSecurityMapper;


    @Override
    public void saveUser(String phone, String password, String code) {
        /**
         * 保存用户记录
         *  1.基本校验
         *     phone :非空  手机号合法 手机号唯一
         *     password:非空  至少6位
         *     code:短信验证码 非空  有效 值与缓存中值相等
         *  2.执行记录添加
         *       bas_user	用户基本信息
                 bas_user_info	用户信息扩展表记录添加
                 bas_user_security	用户安全信息表
                 bus_account	用户账户表记录信息
                 bus_user_integral	用户积分记录
                 bus_income_stat	用户收益表记录
                 bus_user_stat	用户统计表
                 bas_experienced_gold	注册体验金表
                 sys_app_settings
                 sys_log 系统日志
         */
        checkParams(phone,password,code);
        Integer userId= initBasUser(phone,password);
        initBasUserInfo(userId);
        initBasUserSecurity(userId);



    }

    @Override
    public UserModel userLogin(String phone, String password) {
        /**
         * 1.参数基本校验
         * 2.根据手机号查询用户记录
         * 3.匹配密码
         * 4.构建用户信息并返回
         */
        AssertUtil.isTrue(StringUtils.isBlank(phone)||phone.length()!=11,"手机号非法!");
        AssertUtil.isTrue(StringUtils.isBlank(password),"密码非空!");
        BasUser basUser=basUserMapper.queryBasUserByPhone(phone);
        AssertUtil.isTrue(null==basUser,"该用户已注销或未注册!");
        password=MD5.toMD5(password+basUser.getSalt());// 加盐进行加密
        AssertUtil.isTrue(!password.equals(basUser.getPassword()),"密码错误");
        return buildUserModel(basUser);
    }

    private UserModel buildUserModel(BasUser basUser) {
        UserModel userModel=new UserModel();
        userModel.setId(basUser.getId());
        userModel.setPhone(basUser.getMobile());
        return userModel;
    }

    @Override
    public UserModel quickLogin(String phone, String code) {
        AssertUtil.isTrue(StringUtils.isBlank(phone)||phone.length()!=11,"手机号非法!");
        AssertUtil.isTrue(StringUtils.isBlank(code),"验证码非空!");
        String key=phone+":"+P2pConstant.SMS_LOGIN_TYPE;
        AssertUtil.isTrue(!redisTemplate.hasKey(key),"验证码已失效!");
        String val= (String) redisTemplate.opsForValue().get(key);
        AssertUtil.isTrue(!val.equals(code),"验证码不正确!");
        BasUser basUser=basUserMapper.queryBasUserByPhone(phone);
        AssertUtil.isTrue(null==basUser,"该用户不存在!");
        return buildUserModel(basUser);
    }

    @Override
    public BasUser queryBasUserByUserId(Integer userId) {
        return basUserMapper.queryById(userId);
    }

    private void initBasUserSecurity(Integer userId) {
        BasUserSecurity basUserSecurity=new BasUserSecurity();
        basUserSecurity.setUserId(userId);
        basUserSecurity.setPhoneStatus(1);
        AssertUtil.isTrue(basUserSecurityMapper.insert(basUserSecurity)<1,P2pConstant.OPT_FAILED_MSG);
    }

    private void initBasUserInfo(Integer userId) {
        BasUserInfo basUserInfo=new BasUserInfo();
        basUserInfo.setUserId(userId);
        // 奖池
        basUserInfo.setInviteCode(RandomCodesUtils.createRandom(false,6));
        AssertUtil.isTrue(basUserInfoMapper.insert(basUserInfo)<1,P2pConstant.OPT_FAILED_MSG);
    }

    private Integer initBasUser(String phone, String password) {
        BasUser basUser=new BasUser();
        basUser.setAddtime(new Date());
        basUser.setMobile(phone);
        //  md5+盐进行加密操作
        String salt= RandomCodesUtils.createRandom(false,6);
        basUser.setSalt(salt);// 设置盐值
        password= MD5.toMD5(password+salt);
        basUser.setPassword(password);
        basUser.setReferer("PC");
        basUser.setStatus(1);
        basUser.setType(1);
        AssertUtil.isTrue(basUserMapper.insert(basUser)<1,P2pConstant.OPT_FAILED_MSG);
        Integer userId= basUser.getId();// 获取主键
        basUser.setUsername("SXT_P2P"+new SimpleDateFormat("yyyy").format(new Date())+userId);
        AssertUtil.isTrue(basUserMapper.update(basUser)<1,P2pConstant.OPT_FAILED_MSG);
        return  userId;
    }

    private void checkParams(String phone, String password, String code) {
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号非空!");
        AssertUtil.isTrue(phone.length()!=11,"手机号非法!");
        AssertUtil.isTrue(null!=basUserMapper.queryBasUserByPhone(phone),"该手机号已注册!");
        AssertUtil.isTrue(!redisTemplate.hasKey(phone+":"+ P2pConstant.SMS_REGISTER_TYPE),"验证码已失效!");
        ValueOperations<String,Object> valueOperations= redisTemplate.opsForValue();
        String val= (String) valueOperations.get(phone+":"+ P2pConstant.SMS_REGISTER_TYPE);
        AssertUtil.isTrue(!val.equals(code),"手机验证码不正确!");
    }
}
