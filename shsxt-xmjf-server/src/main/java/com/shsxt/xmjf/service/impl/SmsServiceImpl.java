package com.shsxt.xmjf.service.impl;

import com.alibaba.fastjson.JSON;
import com.shsxt.xmjf.api.constants.P2pConstant;
import com.shsxt.xmjf.api.service.ISmsService;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.BizResult;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by lp on 2018/3/1.
 */
@Service
public class SmsServiceImpl implements ISmsService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;


    @Override
    public void sendPhoneSms(String phone, String type, String code) {
        /**
         * 1.参数基本校验
         *     非空
         *     手机号 长度11位
         *     type:注册-1  登录-2
         *     code:六位 字符+数字
         * 2.业务具体实现-短信发送
         *     阿里大于sdk
         * 3.短信发送成功
         *     短信存储-缓存 redis 有效时间180秒(3分钟)
         * 4.短信发送失败(失败补偿措施)
         */
        checkSmParams(phone,type,code);
        try {
            TaobaoClient client = new DefaultTaobaoClient(P2pConstant.SMS_URL,P2pConstant.SMS_APP_KEY ,P2pConstant.SMS_APP_SECRET);
            AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
            req.setSmsType(P2pConstant.SMS_TYPE);
            req.setSmsFreeSignName(P2pConstant.SMS_FREE_SIGN_NAME);
            Map<String,String> map=new HashMap<String,String>();
            map.put("code",code);
            req.setSmsParamString(JSON.toJSONString(map));
            req.setRecNum(phone);
            if(type.equals(P2pConstant.SMS_LOGIN_TYPE)){
                req.setSmsTemplateCode(P2pConstant.SMS_TEMPLATE_LOGIN_CODE);
            }
            if(type.equals(P2pConstant.SMS_REGISTER_TYPE)){
                req.setSmsTemplateCode(P2pConstant.SMS_TEMPLATE_REGISTER_CODE);
            }
            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
            BizResult bizResult=rsp.getResult();
            System.out.println(bizResult.getSuccess());
            AssertUtil.isTrue(!bizResult.getSuccess(),"短信发送失败!");
            // 设置缓存
            ValueOperations<String,Object> valueOperations= redisTemplate.opsForValue();
            // key value
            // phone:type   code
            valueOperations.set(phone+":"+type,code);
            redisTemplate.expire(phone+":"+type,180L, TimeUnit.SECONDS);// 3分钟有效
        } catch (ApiException e) {
            e.printStackTrace();
        }


    }

    private void checkSmParams(String phone, String type, String code) {
        /**
         * 正则判断手机号合法性
         */
        AssertUtil.isTrue(StringUtils.isBlank(phone)||phone.length()!=11,"手机号非法!");
        AssertUtil.isTrue(StringUtils.isBlank(type),"短信类型不能为空!");
        AssertUtil.isTrue(!(type.equals(P2pConstant.SMS_LOGIN_TYPE)||type.equals(P2pConstant.SMS_REGISTER_TYPE)),"短信类型不匹配!");
        AssertUtil.isTrue(StringUtils.isBlank(code)||code.length()!=4,"短信验证码不合法!");
    }
}
