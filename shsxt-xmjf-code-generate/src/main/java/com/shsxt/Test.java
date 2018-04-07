package com.shsxt;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * Created by lp on 2018/3/1.
 */
public class Test {
    public static void main(String[] args) throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "24664902", "04e5d0670a772219984bf206cb85c55b");
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setSmsType("normal");
        req.setSmsFreeSignName("小马金服");
        req.setSmsParamString("{\"code\":\"168574\"}");
        req.setRecNum("18019338577");
        req.setSmsTemplateCode("SMS_115100107");
        AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
        System.out.println(rsp.getResult().getSuccess());
    }
}
