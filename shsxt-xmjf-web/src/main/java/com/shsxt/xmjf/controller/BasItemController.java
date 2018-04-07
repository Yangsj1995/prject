package com.shsxt.xmjf.controller;

import com.shsxt.xmjf.api.constants.P2pConstant;
import com.shsxt.xmjf.api.dto.BasItemDto;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.BasUserSecurity;
import com.shsxt.xmjf.api.po.BusAccount;
import com.shsxt.xmjf.api.po.BusItemLoan;
import com.shsxt.xmjf.api.po.SysPicture;
import com.shsxt.xmjf.api.query.BasItemQuery;
import com.shsxt.xmjf.api.service.*;
import com.shsxt.xmjf.api.utils.PageList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by lp on 2018/3/3.
 */
@Controller
@RequestMapping("item")
public class BasItemController extends  BaseController{

    @Resource
    private IBasItemService basItemService;

    @Resource
    private IBusAccountService accountService;

    @Resource
    private IBasUserSecurityService basUserSecurityService;

    @Resource
    private IBusItemLoanService busItemLoanService;

    @Resource
    private ISysPictureService sysPictureService;
    @RequestMapping("list")
    @ResponseBody
    public PageList queryBasItemList(BasItemQuery basItemQuery){
       return basItemService.queryBasItemsByParams(basItemQuery);
    }

    @RequestMapping("index")
    public  String index(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        return "invest_list";
    }


    @RequestMapping("updateBasItemToOpen")
    @ResponseBody
    public ResultInfo updateBasItemToOpen(Integer basItemId){
        basItemService.updateBasItemToOpen(basItemId);
        return success("项目开发成功!");
    }


    @RequestMapping("details")
    public  String details(Integer itemId, HttpSession session,Model model){
        BasItemDto basItemDto= basItemService.queryBasItemByItemId(itemId);
        basItemDto.setSyAmount(basItemDto.getItemAccount().add(basItemDto.getItemOngoingAccount().negate()));
        model.addAttribute("item",basItemDto);
        UserModel userModel= (UserModel) session.getAttribute(P2pConstant.SESSION_USER);
        // 用户是否登录
        if(null !=userModel){
           BusAccount busAccount= accountService.queryBusAccountInfoByUserId(userModel.getId());
           model.addAttribute("busAccount",busAccount);
        }

        // 贷款人用户信息
        BasUserSecurity basUserSecurity=basUserSecurityService.queryBasUserSecurityByUserId(basItemDto.getItemUserId());
        basUserSecurity.setIdentifyCard(basUserSecurity.getIdentifyCard().replaceAll("(\\d{4})\\d{10}(\\w{4})","$1*****$2"));
        model.addAttribute("userSecurity",basUserSecurity);
        // 贷款人车辆信息
        BusItemLoan busItemLoan=busItemLoanService.queryBusItemLoanByItemId(itemId);
        model.addAttribute("itemLoan",busItemLoan);

        // 审核图片信息查询
        List<SysPicture> pics=sysPictureService.querySysPicturesByItemId(itemId);
        model.addAttribute("pics",pics);
        return "details";
    }

}
