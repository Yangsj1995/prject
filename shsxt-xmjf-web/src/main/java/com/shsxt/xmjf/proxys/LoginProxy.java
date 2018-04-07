package com.shsxt.xmjf.proxys;

import com.shsxt.xmjf.api.constants.P2pConstant;
import com.shsxt.xmjf.api.exceptions.AuthException;
import com.shsxt.xmjf.api.model.UserModel;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by lp on 2018/3/2.
 */
@Component
@Aspect
public class LoginProxy {

    @Resource
    private HttpSession session;

    @Pointcut("@annotation(com.shsxt.xmjf.annotations.IsLogin)")
    public  void cut(){}

    @Before(value = "cut()")
    public void preMethod(){
        /**
         *
         */
        System.out.println("前置通知。。。");
        UserModel userModel= (UserModel) session.getAttribute(P2pConstant.SESSION_USER);
        if(null==userModel){
            throw new AuthException();
        }

    }

}
