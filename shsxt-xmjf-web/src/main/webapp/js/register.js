/**
 * Created by lp on 2018/3/1.
 */

$(function () {
    $(".validImg").click(function () {
       $(".validImg").attr("src",ctx+"/getImage?time="+new Date());
    });


    $("#clickMes").click(function () {
        var phone=$("#phone").val();
        var code=$("#code").val();
        if(isNull(phone)){
            layer.tips("手机号不合法!","#phone");
            return;
        }
        if(isNull(code)){
            layer.tips("图形验证码非法!","#verification");
            $(".validImg").attr("src",ctx+"/getImage?time="+new Date());
            return;
        }

        var _this=$(this);
        $.ajax({
            type:"post",
            url:ctx+"/sms/sendPhoneSms",
            data:{
                phone:phone,
                type:1,
                picCode:code
            },
            dataType:"json",
            success:function (data) {
                if(data.code==200){
                    time(_this);
                }else{
                    layer.tips(data.msg,"#clickMes");
                    $(".validImg").attr("src",ctx+"/getImage?time="+new Date());
                }
            }
        })

    })






    // 注册点击事件
    $("#register").click(function () {
        var phone=$("#phone").val();
        var code=$("#verification").val();
        var password=$("#password").val();
        if(isNull(phone)){
            layer.tips("手机号不合法!","#phone");
            return;
        }
        if(isNull(code)){
            layer.tips("短信验证码不合法!","#verification");
            return;
        }
        if(isNull(password)){
            layer.tips("登录密码不合法!","#password");
            return;
        }
        $.ajax({
            type:"post",
            url:ctx+"/user/register",
            data:{
                phone:phone,
                code:code,
                password:password
            },
            dataType:"json",
            success:function (data) {
                if(data.code==200){
                    window.location.href=ctx+"/login";
                }else{
                    layer.tips(data.msg,"#register");
                }
            }
        })



    })








});



var wait=6;
function time(o) {
    if (wait == 0) {
        o.removeAttr("disabled");
        o.val('获取验证码');
        o.css("color", '#ffffff');
        o.css("background","#fcb22f");

        wait = 6;
    } else {
        o.attr("disabled", true);
        o.css("color", '#fff');
        o.css("background", '#ddd');
        o.val("重新发送(" + wait + "s)");
        wait--;
        setTimeout(function() {
            time(o)
        }, 1000)
    }
}

