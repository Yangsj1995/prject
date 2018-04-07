package com.shsxt.service;

import com.shsxt.xmjf.api.constants.P2pConstant;
import com.shsxt.xmjf.api.service.ISmsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by lp on 2018/3/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
public class TestSmsService {
    @Resource
    private ISmsService smsService;

    @Test
    public  void test(){
        smsService.sendPhoneSms("18016273233", P2pConstant.SMS_REGISTER_TYPE,"1a3c");
    }
}
