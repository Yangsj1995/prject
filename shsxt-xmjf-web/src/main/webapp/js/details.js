/**
 * Created by lp on 2018/3/5.
 */

$(function () {
   initItemScaleData();



    //tab切换
    $('#tabs div').click(function () {
        $(this).addClass('tab_active');
        var show=$('#contents .tab_content').eq($(this).index());
        show.show();
        $('#tabs div').not($(this)).removeClass('tab_active');
        $('#contents .tab_content').not(show).hide();
        if($(this).index()==2){
        /**
         * 获取项目投资记录
         *   ajax 拼接tr
         *    追加tr 到 recordList
         */
        loadInvestRecodesList($("#itemId").val());
       
    }
});
});






function initItemScaleData() {
    $('#rate').radialIndicator({
        barColor: 'orange',
        barWidth: 5,
        roundCorner : true,
        percentage: true,
        radius: 24
    });
    $("#rate").data('radialIndicator').animate($("#rate").attr("data-val"));
}


/**
 * 加载投资信息列表数据
 * @param itemId
 * @param pageNum
 * @param pageSize
 */
function loadInvestRecodesList(itemId,pageNum,pageSize) {
    var params={};
    params.itemId=itemId;
    params.pageNum=1;
    params.pageSize=10;
    if(!isNull(pageNum)&&pageNum >1){
        params.pageNum=pageNum;
    }
    if(!isNull(pageSize)&& pageSize !=10){
        params.pageSize=pageSize;
    }

    $.ajax({
        type:"post",
        url:ctx+"/busItemInvest/queryBusItemInvestsByItemId",
        data:params,
        dataType:"json",
        success:function (data) {
            var pageInfo=data.paginator;
            var list=data.list;
            if(pageInfo.total>0){
                initTrHtml(list);
                initPageInfoHtml(pageInfo);
            }else{
               // alert("暂无投资记录!");
            }

        }
    })
    
}

function initTrHtml(list) {
    if(null !=list&&list.length>0){
        var trs="";
        for(var i=0;i<list.length;i++){
            var temp=list[i];
            trs=trs+"<tr>";
            trs=trs+"<td>"+temp.mobile.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')+"</td>";
            trs=trs+"<td>"+temp.amount+"</td>";
            trs=trs+"<td>"+temp.time+"</td>";
            trs=trs+"</tr>";
        }
        $("#recordList").html(trs);
    }
}

function initPageInfoHtml(pageInfo) {
    // <li class="active"><a>1</a></li>
    if(pageInfo.total>0){
        var pageArry=pageInfo.navigatepageNums;
        var lis="";
        for(var j=0;j<pageArry.length;j++){
            var href="javascript:getCurrentPageData("+pageArry[j]+")";
            if(pageInfo.pageNum==pageArry[j]){
                lis=lis+"<li class='active'><a href='"+href+"'>"+pageArry[j]+"</a></li>";
            }else{
                lis=lis+"<li><a href='"+href+"'>"+pageArry[j]+"</a></li>";
            }
        }
        $("#pages").html(lis);
    }
}


function getCurrentPageData(pageNum) {
    loadInvestRecodesList($("#itemId").val(),pageNum);
}


function toRecharge() {
    $.ajax({
        type:"post",
        url:ctx+"/user/checkUserIsAuth",
        dataType:"json",
        success:function (data) {
            if(data.code==200){
                window.location.href=ctx+"/account/toRecharge";
            }else{
                layer.confirm(data.msg, {
                    btn: ['执行认证','暂时不认证'] //按钮
                }, function(){
                    window.location.href=ctx+"/user/auth";
                }, function(){
                    alert("暂时不认证");
                });
            }
        }
    })
    
}

/**
 * 执行投资
 */
function doInvest() {
   var amount=parseFloat($("#usableMoney").val());
   var itemId=$("#itemId").val();
    /**
     * 参数校验
     */
    var usableMoney=parseFloat($("#ye").attr("data-value"));
    var singleMinInvestMoney=parseFloat($("#minInvestMoney").attr("data-value"));
    var singleMaxInvestMoney=parseFloat($("#maxInvestMoney").attr("data-value"));
    if(isNull(itemId)){
        layer.msg("待投资记录不存在!");
        return;
    }
    if(isNull(amount)){
        layer.msg("请输入投资金额!");
        return;
    }
    if(usableMoney==0){
        layer.msg("账户余额不足，请先进行充值操作!");
        return;
    }
    if(amount>usableMoney){
        layer.msg("充值金额不能大于账户可用余额!");
        return;
    }
   if(singleMinInvestMoney>0 && amount<singleMinInvestMoney){
       layer.msg("充值金额不能小于单笔最小投资金额!");
       return;
   }
    if(singleMaxInvestMoney>0 && amount>singleMaxInvestMoney){
        layer.msg("充值金额不能大于单笔最大投资金额!");
        return;
    }

    layer.prompt(
        {title: '请输入交易密码', formType: 1},
        function(pass, index){
        layer.close(index);
        $.ajax({
            type:"post",
            url:ctx+"/busItemInvest/doInvest",
            data:{
                amount:amount,
                itemId:itemId,
                busiPassword:pass
            },
            dataType:"json",
            success:function (data) {
                if(data.code==200){
                    layer.msg("项目投标成功");
                    setTimeout(function () {
                        window.location.href=ctx+"/account/accountInfo";
                    },2000);
                }else{
                    layer.msg(data.msg)
                }
            }
        })

    });









    
}