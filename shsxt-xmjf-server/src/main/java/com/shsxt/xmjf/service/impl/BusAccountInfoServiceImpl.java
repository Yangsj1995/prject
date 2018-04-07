package com.shsxt.xmjf.service.impl;

import com.shsxt.xmjf.api.dto.AccountDto;
import com.shsxt.xmjf.api.po.BusAccount;
import com.shsxt.xmjf.api.service.IBusAccountService;
import com.shsxt.xmjf.db.dao.BusAccountMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lp on 2018/3/5.
 */
@Service
public class BusAccountInfoServiceImpl implements IBusAccountService {
    @Resource
    private BusAccountMapper busAccountMapper;
    @Override
    public BusAccount queryBusAccountInfoByUserId(Integer userId) {
        return busAccountMapper.queryBusAccountInfoByUserId(userId);
    }

    @Override
    public Integer updateBusAccount(BusAccount busAccount) {
        return busAccountMapper.update(busAccount);
    }

    @Override
    public List<AccountDto> queryAccountInfoByUserId(Integer userId) {
       Map<String,BigDecimal>  map=busAccountMapper.queryAccountInfoByUserId(userId);
       List<AccountDto> accountDtos=new ArrayList<AccountDto>();
       AccountDto accountDto=new AccountDto();
       accountDto.setName("总金额");
       accountDto.setY(map.get("total"));
       accountDtos.add(accountDto);
        AccountDto accountDto02=new AccountDto();
        accountDto02.setName("可用金额");
        accountDto02.setY(map.get("usable"));
        accountDtos.add(accountDto02);
        AccountDto accountDto03=new AccountDto();
        accountDto03.setName("可提现金额");
        accountDto03.setY(map.get("cash"));
        accountDtos.add(accountDto03);
        AccountDto accountDto04=new AccountDto();
        accountDto04.setName("冻结金额");
        accountDto04.setY(map.get("frozen"));
        accountDtos.add(accountDto04);
        AccountDto accountDto05=new AccountDto();
        accountDto05.setName("代收金额");
        accountDto05.setY(map.get("wait"));
        accountDtos.add(accountDto05);
       return accountDtos;
    }
}
