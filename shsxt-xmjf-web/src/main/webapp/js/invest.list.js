/**
 * Created by lp on 2018/3/3.
 */
$(function () {
    // 加载投资列表数据
   loadInvestListData();

   $(".tab").click(function () {
        $(this).addClass("list_active");
        $(".tab").not($(this)).removeClass("list_active");
        var isHistory=0;
        var itemCycle=$(this).index();
        if(itemCycle==4){
            isHistory=1;
        }
        var itemType=$("#itemType").val();
        loadInvestListData(itemCycle,itemType,isHistory);
   })

});


/**
 * 加载投资列表数据
 * @param itemCycle  项目期限
 * @param itemType   项目类别
 * @param isHistory   是否为历史3项目
 * @param pageNum     当前页页面
 * @param pageSize    每页大小
 */
function  loadInvestListData(itemCycle,itemType,isHistory,pageNum,pageSize) {
    var params={};
    if(!isNull(itemCycle)){
        params.itemCycle=itemCycle;
    }
    if(!isNull(itemType)){
        params.itemType=itemType;
    }
    params.isHistory=0;
    if(!isNull(isHistory)&&isHistory==1){
        params.isHistory=1;
    }
    params.pageNum=1;
    params.pageSize=10;
    if(!isNull(pageNum)&&pageNum!=1){
         params.pageNum=pageNum; 
    }
    if(!isNull(pageSize)&&pageSize!=10){
        params.pageSize=pageSize;
    }
    
    // 发送ajax 请求
    $.ajax({
        type:"post",
        url:ctx+"/item/list",
        data:params,
        dataType:"json",
        success:function (data) {
            var pageInfo=data.paginator;
            var list=data.list;
            if(pageInfo.total>0){
                // 填充行记录数据
                // 填充页码数据
                initRowData(list);
                initPageInfo(pageInfo);
                initItemScale();// 进度渲染
                initDjs();// 倒计时
            }else{
                //alert("暂无记录");
                $("#pages").html("<img style='margin-left: -70px;padding:40px;' " +
                    "src='/img/zanwushuju.png'>");
                $("#pcItemList").html("");


            }
        }
        
    })
    
   
    

}

function initRowData(list) {
    if(null!=list&&list.length>0){
        var trs="";
        for(var i=0;i<list.length;i++){
            var temp=list[i];
            trs=trs+"<tr>";
            // 年化率
            trs=trs+"<td><strong>"+temp.itemRate+"%</strong>";
            if(!isNull(temp.itemAddRate)&&temp.itemAddRate>0){
                trs=trs+"<span>+"+temp.itemAddRate+"%</span>";
            }
            trs=trs+"</td>";

            // 期限
            trs=trs+"<td>"+temp.itemCycle+"天</td>";
            // 项目名
            trs=trs+"<td>"+temp.itemName;
            if(temp.itemIsnew==1){
                trs=trs+"<strong class='colorful' new>NEW</strong>";
            }
            if(temp.itemIsnew==0 && temp.moveVip==1){
                trs=trs+"<strong class='colorful' app>APP</strong>";
            }
            if(temp.itemIsnew==0 && temp.moveVip==0 && temp.itemIsrecommend==1){
                trs=trs+"<strong class='colorful' hot>HOT</strong>";
            }
            if(temp.itemIsnew==0 && temp.moveVip==0 && temp.itemIsrecommend==0 &&(!isNull(temp.password))){
                trs=trs+"<strong class='colorful' psw>LOCK</strong>";
            }
            trs=trs+"</td>";

            //  等级
            trs=trs+"<td class='trust_range'>";
            if(temp.total>90){
                trs=trs+"A+";
            }
            if(temp.total <=90 && temp.total>85){
                trs=trs+"A";
            }
            if(temp.total<=85 && temp.total>=75){
                trs=trs+"A-";
            }
            if(temp.total<=75 && temp.total>65){
                trs=trs+"B";
            }
            trs=trs+"</td>";

            // 担保机构
            trs=trs+"<td class='company'><img src="+ctx+"/img/logo.png></td>";

            // 投资进度
            if(temp.itemStatus==1){
                // 倒计时效果
                trs=trs+"<td>" +
                    "<strong class='countdown time' data-time='"+temp.syTime+"' data-item='"+temp.id+"'>" +
                    "<time class='hour'></time>&nbsp;:" +
                    "<time class='min'></time>&nbsp;:" +
                    " <time class='sec'></time>" +
                    "</strong>" +
                    "</td>";
            }else{
                trs=trs+"<td class='scale' data-scale='"+temp.itemScale+"'></td>";
            }



            // 操作项
            var href=ctx+"/item/details?itemId="+temp.id;
            trs=trs+"<td>";
            // 即将开标
            if(temp.itemStatus==1){
                /**
                 * <p><a href="/item/details/{{id}}?{{id}}"><input class="countdownButton" valid type="button" value='即将开标'></a></p>
                 * @type {string}
                 */
             trs=trs+"<p><a href='"+href+"'>" +
                 "<input class='countdownButton' valid type='button' value='即将开标'>" +
                 "</a></p>";
           }
           // 立即投资
            if(temp.itemStatus==10 ||temp.itemStatus==13 ||temp.itemStatus==18){
                trs=trs+"<p class='left_money'>可投金额"+temp.syAmount+"元</p>" +
                    "<p><a href='"+href+"'>" +
                    "<input  valid type='button' value='立即投资'>" +
                    "</a></p>";
            }

            // 已抢完
            if(temp.itemStatus==20){
                trs=trs+ "<p><a href='"+href+"'>" +
                    "<input  not_valid type='button' value='已抢完'>" +
                    "</a></p>";
            }

            if(temp.itemStatus==30|| temp.itemStatus==31){
                trs=trs+ "<p><a href='"+href+"'>" +
                    "<input  not_valid type='button' value='还款中'>" +
                    "</a></p>";
            }

            if(temp.itemStatus==32){
                trs=trs+ "<p><a href='"+href+"'>" +
                    "<input  not_valid type='button' class='yihuankuan' value='已还款'>" +
                    "</a></p>";
            }
            if(temp.itemStatus==23){
                trs=trs+ "<p><a href='"+href+"'>" +
                    "<input  not_valid type='button'  value='已满标'>" +
                    "</a></p>";
            }
            trs=trs+"</td>";
            trs=trs+"</tr>";
        }



        $("#pcItemList").html(trs);

    }
}


function initPageInfo(pageInfo) {
    /**
     *  <li class="active"><a title="第一页" >1</a></li>
     <li><a title="第二页">2</a></li>
     <li><a title="第三页">3</a></li>
     */
    if(pageInfo.total>0){
        var lis="";
        if(pageInfo.pageNum>1){
            var href="javascript:getCurrentData(1)";
            lis=lis+"<li><a href='"+href+"' title='首页'>首页</a>";
        }
        if(pageInfo.pageNum>1){
            var href="javascript:getCurrentData("+(pageInfo.pageNum-1)+")";
            lis=lis+"<li><a  href='"+href+"' title='第"+(pageInfo.pageNum-1)+"页'>上一页</a>";
        }
        for(var j=0;j<pageInfo.navigatepageNums.length;j++){
            var page=pageInfo.navigatepageNums[j];
            var href="javascript:getCurrentData("+page+")";
            if(pageInfo.pageNum==page){
                lis=lis+"<li class='active'><a href='"+href+"' title='第"+page+"页'>"+page+"</a>";
            }else{
                lis=lis+"<li ><a  href='"+href+"' title='第"+page+"页'>"+page+"</a>";
            }
        }
        if(pageInfo.pageNum<pageInfo.pages){
            var href="javascript:getCurrentData("+(pageInfo.pageNum+1)+")";
            lis=lis+"<li><a href='"+href+"' title='第"+(pageInfo.pageNum+1)+"页'>下一页</a>";
        }
        if(pageInfo.pageNum<pageInfo.pages){
            var href="javascript:getCurrentData("+(pageInfo.pages)+")";
            lis=lis+"<li><a href='"+href+"' title='末页'>末页</a>";
        }
        $("#pages").html("");
        $("#pages").html(lis);
    }

}

/**
 * 渲染投资进度
 */
function initItemScale() {
    $('.scale').radialIndicator({
        barColor: 'orange',
        barWidth: 5,
        roundCorner : true,
        percentage: true,
        radius: 30
    });

    $('.scale').each(function(){
        $(this).data('radialIndicator').animate($(this).attr("data-scale"));$(this).data('radialIndicator').animate($(this).attr("data-scale"));
    })

}

/**
 * 切换项目类别时 刷新列表数据
 */
function initItemData(itemType) {
    var itemType=itemType;
    var itemCycle;
    var isHistory=0;
    $("#validItem .tab").each(function () {
        if($(this).hasClass("list_active")){
            itemCycle=$(this).index();
            if(itemCycle==4){
                isHistory=1;
            }
        }
    });
    loadInvestListData(itemCycle,itemType,isHistory);
}


function getCurrentData(pageNum) {
    var itemCycle;
    var isHistory=0;
    var itemType=$("#itemType").val();
    $("#validItem .tab").each(function () {
        if($(this).hasClass("list_active")){
            itemCycle=$(this).index();
            if(itemCycle==4){
                isHistory=1;
            }
        }
    });
    loadInvestListData(itemCycle,itemType,isHistory,pageNum);
}


function initDjs() {
    $(".countdown").each(function () {
        var time= $(this).attr("data-time");// 获取剩余时间
        var itemId=$(this).attr("data-item");
        timer(time,$(this),itemId);
    })
}


function timer(intDiff,obj,itemId){
    if( obj.timers){
        clearInterval( obj.timers);
    }

    obj.timers=setInterval(function(){
        var day=0,
            hour=0,
            minute=0,
            second=0;//时间默认值
        if(intDiff > 0){
            day = Math.floor(intDiff / (60 * 60 * 24));
            hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
            minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
            second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
        }
        if (minute <= 9) minute = '0' + minute;
        if (second <= 9) second = '0' + second;
        obj.find('.hour').html(hour);
        obj.find('.min').html(minute);
        obj.find('.sec').html(second);
        intDiff--;
        if(intDiff==-1){
            $.ajax({
                url : ctx+'/item/updateBasItemToOpen',
                dataType : 'json',
                type : 'post',
                data:{
                    basItemId:itemId
                },
                success : function(data) {
                    if(data.code==200){
                        window.location.reload()
                    }
                },
                error : function(textStatus, errorThrown) {

                }
            });
        }
    }, 1000);
}
