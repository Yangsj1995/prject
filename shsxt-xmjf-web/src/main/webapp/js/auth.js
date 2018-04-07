/**
 * Created by lp on 2018/3/6.
 */

$(function () {
   $("#identityNext").click(function () {
       var realName=$("#realName").val();
       var idCard=$("#card").val();
       var busiPassword=$("#_ocx_password").val();
       var confirmBusiPassword=$("#_ocx_password1").val();
       if(isNull(realName)){
           layer.msg("请输入姓名","#realName");
           return;
       }
       if(isNull(idCard)||idCard.length!=18){
           layer.msg("身份证信息不合法!","#card");
           return;
       }

       if(isNull(busiPassword)){
           layer.msg("密码不能为空!","#_ocx_password");
           return;
       }

       if(isNull(confirmBusiPassword)){
           layer.msg("确认密码不能为空!","#_ocx_password1");
           return;
       }
       if(busiPassword !=confirmBusiPassword){
           layer.msg("密码输入不一致!","#_ocx_password1");
           return;
       }

       $.ajax({
           type:"post",
           url:ctx+"/user/doAuth",
           data:{
               realName:realName,
               idCard:idCard,
               confirmPassword:confirmBusiPassword,
               busiPassword:busiPassword
           },
           dataType:"json",
           success:function (data) {
               if(data.code==200){
                   alert("用户认证成功!");
               }else{
                   layer.msg(data.msg,"#identityNext");
               }
           }
       })




   })
});


