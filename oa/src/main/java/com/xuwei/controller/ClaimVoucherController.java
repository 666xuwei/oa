package com.xuwei.controller;


import com.xuwei.dto.ClaimVoucherInfo;
import com.xuwei.pojo.DealRecord;
import com.xuwei.pojo.Employee;
import com.xuwei.service.ClaimVoucherService;
import com.xuwei.util.Contant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 报销单处理
 */
@Controller("claimVoucherController")
@RequestMapping("/claim_voucher")
public class ClaimVoucherController {

    @Autowired
    private ClaimVoucherService claimVoucherService;

    /**
     * 显示当前报销单的详细信息
     * @return
     */
    @RequestMapping("/detail")
    public String detail(int id, Map<String, Object> map){
        map.put("claimVoucher",claimVoucherService.get(id));
        map.put("items",claimVoucherService.getItems(id));
        map.put("records",claimVoucherService.getRecords(id));
        return "claim_voucher_detail";
    }

    /**
     * 显示个人所有报销单
     * @param session
     * @param map
     * @return
     */
    @RequestMapping("/self")
    public String self(HttpSession session, Map<String, Object> map){
        Employee employee = (Employee)session.getAttribute("employee");
        map.put("list",claimVoucherService.getForSelf(employee.getSn()));
        return "claim_voucher_self";
    }

    /**
     * 显示所有待处理报销单: Select	事由	状态	创建人	金额	创建时间	操作
     * @param session
     * @return
     */
    @RequestMapping("/deal")
    public String deal(HttpSession session, Map<String, Object> map){
        Employee employee = (Employee)session.getAttribute("employee");
        map.put("list",claimVoucherService.getForDeal(employee.getSn()));
        return "claim_voucher_deal";
    }

    /**
     * 跳往填写报销单页面,需要传入费用类别，报销单对象和条目对象，使用dto创建的报销单处理对象，包含了这两个
     * @return
     */
    @RequestMapping("/to_add")
    public String toAdd(Map<String, Object> map){
        map.put("items", Contant.getItems());
        map.put("info",new ClaimVoucherInfo());
        return "claim_voucher_add";
    }

    /**
     * 添加报销单+条目
     * @param session
     * @param info
     * @return
     */
    @RequestMapping("/add")
    public String add(HttpSession session, ClaimVoucherInfo info){
        Employee employee = (Employee)session.getAttribute("employee");
        info.getClaimVoucher().setCreateSn(employee.getSn()); //设置报销单的创建者编号
        claimVoucherService.save(info.getClaimVoucher(),info.getItems()); //保存报销单和条目信息
        return "redirect:deal";
    }


    /**
     * 跳往修改页面
     * @param id
     * @return
     */
    @RequestMapping(value = "/to_update")
    public String toUpdate(int id, Map<String, Object> map){
        map.put("items", Contant.getItems());
        ClaimVoucherInfo info =new ClaimVoucherInfo();

        info.setClaimVoucher(claimVoucherService.get(id));
        info.setItems(claimVoucherService.getItems(id));
        //将原来的报销单条目信息封装到map中
        map.put("info",info);
        return "claim_voucher_update";
    }

    /**
     * 修改报销单，需要获取要修改报销单编号
     * @param session
     * @param info
     * @return
     */
    @RequestMapping("/update")
    public String update(HttpSession session, ClaimVoucherInfo info){
        Employee employee = (Employee)session.getAttribute("employee");
        info.getClaimVoucher().setCreateSn(employee.getSn());
        claimVoucherService.update(info.getClaimVoucher(),info.getItems());
        return "redirect:deal";
    }

    /**
     * 提交报销单
     * @param id
     * @return
     */
    @RequestMapping("/submit")
    public String submit(int id){
        claimVoucherService.submit(id);
        return "redirect:deal";
    }

    /**
     * 跳往报销单审核页面
     * @param id
     * @param map
     * @return
     */
    @RequestMapping("/to_check")
    public String toCheck(int id, Map<String, Object> map){
        map.put("claimVoucher",claimVoucherService.get(id));
        map.put("items",claimVoucherService.getItems(id));
        map.put("records",claimVoucherService.getRecords(id));

        DealRecord dealRecord =new DealRecord();
        dealRecord.setClaimVoucherId(id);

        map.put("record",dealRecord);
        return "claim_voucher_check";
    }

    /**
     * 审核报销单
     * @param session
     * @param dealRecord
     * @return
     */
    @RequestMapping("/check")
    public String check(HttpSession session, DealRecord dealRecord){
        Employee employee = (Employee)session.getAttribute("employee");
        dealRecord.setDealSn(employee.getSn()); //设置了待处理人编号
        claimVoucherService.deal(dealRecord);
        return "redirect:deal";
    }
}
