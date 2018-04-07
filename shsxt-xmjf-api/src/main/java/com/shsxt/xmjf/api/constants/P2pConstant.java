package com.shsxt.xmjf.api.constants;

/**
 * Created by lp on 2018/3/1.
 * 常量类
 */
public class P2pConstant {
    public static final String PICTURE_VERIFY_KEY="image";
    public static final Integer OPT_FAILED_CODE=300;
    public static final String OPT_FAILED_MSG="操作失败";
    public static final Integer OPT_SUCCESS_CODE=200;
    public static final String OPT_SUCCESS_MSG="操作成功";


    /////////////////////////////////////////////
    /**
     * 短信发送常量
     */
    ////////////////////////////////////////////
    public static final String SMS_REGISTER_TYPE="1";
    public static final String SMS_LOGIN_TYPE="2";
    public static final String SMS_URL="http://gw.api.taobao.com/router/rest";
    public static final String SMS_APP_KEY="24664902";
    public static final String SMS_APP_SECRET="04e5d0670a772219984bf206cb85c55b";
    public static final String SMS_TYPE="normal";
    public static final String SMS_FREE_SIGN_NAME="小马金服";
    public static final String SMS_TEMPLATE_REGISTER_CODE="SMS_109450111";// 注册code
    public static final String SMS_TEMPLATE_LOGIN_CODE="SMS_115100107";//登录code

    public static final String SESSION_USER="userInfo";


    //  充值类型 常量
    public static final Integer RECHARGE_APP=1;
    public static final Integer RECHARGE_ADMIN=2;
    public static final Integer RECHARGE_PC=3;
    public static final Integer RECHARGE_WX=4;


    //  支付请求常量参数
    public static final String PAY_USER_SELLER="74754404";//商户号
    public static final String PAY_PID="9796636105929474";
    public static final String PAY_KEY="23fd93167d2141708cdd67ef0ca5531d";
    public static final String PAY_NOTIFY_URL="http://www.shsxt.com/account/callBack";//异步回调地址
    public static final String PAY_RETURN_URL="http://www.shsxt.com/account/callBack";//同步回调地址
    public static final String PAY_SUCCESS="TRADE_SUCCESS";// 订单支付成功常量串











}
