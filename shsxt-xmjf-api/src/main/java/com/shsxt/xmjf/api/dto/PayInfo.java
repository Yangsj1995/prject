package com.shsxt.xmjf.api.dto;

import com.shsxt.xmjf.api.constants.P2pConstant;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lp on 2018/3/6.
 *
 *
         * partner	商户PID	必填
         user_seller	商户号	必填
         out_order_no	商户网站订单号（唯一）	必填
         subject	订单名称	必填
         total_fee	订单价格	必填
         body	订单描述	选填
         notify_url	异步回调地址	必填
         return_url	同步回调地址	必填
         sign	校验码	必填
 */
public class PayInfo  implements Serializable{
    private static final long serialVersionUID = 4756337104934384710L;

    private String pid= P2pConstant.PAY_PID;// pid
    private String userSeller=P2pConstant.PAY_USER_SELLER;  //商户号
    private String outOrderNo;// 订单号
    private String subject;//订单名称
    private BigDecimal totalFee;//总价格
    private String body;//订单描述信息
    private String notifyUrl=P2pConstant.PAY_NOTIFY_URL;// 常量  异步回调地址
    private String returnUrl=P2pConstant.PAY_RETURN_URL;// 同步回调地址
    private String sign;// 校验码
    private String key=P2pConstant.PAY_KEY;
    private String gatewayNew= "http://payment.passpay.net/PayOrder/payorder";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUserSeller() {
        return userSeller;
    }

    public void setUserSeller(String userSeller) {
        this.userSeller = userSeller;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getGatewayNew() {
        return gatewayNew;
    }

    public void setGatewayNew(String gatewayNew) {
        this.gatewayNew = gatewayNew;
    }
}
