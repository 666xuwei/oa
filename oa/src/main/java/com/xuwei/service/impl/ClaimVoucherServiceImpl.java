package com.xuwei.service.impl;


import com.xuwei.dao.ClaimVoucherDao;
import com.xuwei.dao.ClaimVoucherItemDao;
import com.xuwei.dao.DealRecordDao;
import com.xuwei.dao.EmployeeDao;
import com.xuwei.pojo.ClaimVoucher;
import com.xuwei.pojo.ClaimVoucherItem;
import com.xuwei.pojo.DealRecord;
import com.xuwei.pojo.Employee;
import com.xuwei.service.ClaimVoucherService;
import com.xuwei.util.Contant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("claimVoucherService")
public class ClaimVoucherServiceImpl implements ClaimVoucherService {

    @Autowired
    private ClaimVoucherDao claimVoucherDao;
    @Autowired
    private ClaimVoucherItemDao claimVoucherItemDao;
    @Autowired
    private DealRecordDao dealRecordDao;
    @Autowired
    private EmployeeDao employeeDao;

    //保存报销单信息和条目信息
    @Override
    public void save(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        claimVoucher.setCreateTime(new Date()); //创建时间
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn()); //待处理人编号
        claimVoucher.setStatus(Contant.CLAIMVOUCHER_CREATED); //报销单状态，此处为新创建
        claimVoucherDao.insert(claimVoucher);

        for(ClaimVoucherItem item:items){ //条目可能不止一条
            item.setClaimVoucherId(claimVoucher.getId()); //设置当前条目所对应的报销单的编号
            claimVoucherItemDao.insert(item);
        }
    }

    //获取个人所有报销单
    @Override
    public List<ClaimVoucher> getForSelf(String sn) {
        return claimVoucherDao.selectByCreateSn(sn);
    }

    //获取待处理报销单
    @Override
    public List<ClaimVoucher> getForDeal(String sn) {
        return claimVoucherDao.selectByNextDealSn(sn);
    }

    //修改报销单
    @Override
    public void update(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn()); //设置处理人的编号
        claimVoucher.setStatus(Contant.CLAIMVOUCHER_CREATED); //设置报销单的状态
        claimVoucherDao.update(claimVoucher); //报销单修改

        //获取数据库中已有的报销的条目信息
        List<ClaimVoucherItem> olds = claimVoucherItemDao.selectByClaimVoucher(claimVoucher.getId());

        //从数据库中删除已经被删除的旧的报销单条目
        for(ClaimVoucherItem old:olds){
            boolean isHave=false;
            for(ClaimVoucherItem item:items){ //说明这个条目没有被删除，被修改了
                if(item.getId()==old.getId()){
                    isHave=true;
                    break;
                }
            }
            if(!isHave){ //这一步说明当前这个条目已经被删除了
                claimVoucherItemDao.delete(old.getId());
            }
        }

        //更新或者添加报销单条目信息
        for(ClaimVoucherItem item:items){
            item.setClaimVoucherId(claimVoucher.getId());
            if(item.getId()>0){ //有错误
                claimVoucherItemDao.update(item); //更新报销单条目
            }else{
                claimVoucherItemDao.insert(item); //添加报销单条目
            }
        }
    }

    //提交报销单
    @Override
    public void submit(int id) {
        //取出报销单和创建者
        ClaimVoucher claimVoucher = claimVoucherDao.select(id);
        Employee employee = employeeDao.select(claimVoucher.getCreateSn());

        //设置报销单属性
        claimVoucher.setStatus(Contant.CLAIMVOUCHER_SUBMIT);
        //待处理人，跟当前创建者是一个部门且是部门经理的
        claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(employee.getDepartmentSn(),Contant.POST_FM).get(0).getSn());

        claimVoucherDao.update(claimVoucher);

        //记录的保存
        DealRecord dealRecord = new DealRecord();
        dealRecord.setDealWay(Contant.DEAL_SUBMIT); //记录的方式
        dealRecord.setDealSn(employee.getSn()); //处理人编号
        dealRecord.setClaimVoucherId(id); //处理的报销单编号
        dealRecord.setDealResult(Contant.CLAIMVOUCHER_SUBMIT); //处理结果
        dealRecord.setDealTime(new Date()); //处理时间
        dealRecord.setComment("无"); //备注
        dealRecordDao.insert(dealRecord);
    }

    @Override
    public ClaimVoucher get(int id) {
        return claimVoucherDao.select(id);
    }

    @Override
    public List<ClaimVoucherItem> getItems(int cvid) {
        return claimVoucherItemDao.selectByClaimVoucher(cvid);
    }

    @Override
    public List<DealRecord> getRecords(int cvid) {
        return dealRecordDao.selectByClaimVoucher(cvid);
    }

    //审核报销单
    @Override
    public void deal(DealRecord dealRecord) {
        //获取报销单
        ClaimVoucher claimVoucher = claimVoucherDao.select(dealRecord.getClaimVoucherId());

        Employee employee = employeeDao.select(dealRecord.getDealSn());
        dealRecord.setDealTime(new Date());

        //根据“打回，拒绝，通过，打款”来做相应的判断
        if(dealRecord.getDealWay().equals(Contant.DEAL_PASS)){ //通过
            //报销单的金额小于等于5000或者处理人的职位是总经理
            if(claimVoucher.getTotalAmount()<=Contant.LIMIT_CHECK || employee.getPost().equals(Contant.POST_GM)){
                claimVoucher.setStatus(Contant.CLAIMVOUCHER_APPROVED); //设置报销单状态为已审核
                //设置待处理人为财务
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null,Contant.POST_CASHIER).get(0).getSn());

                dealRecord.setDealResult(Contant.CLAIMVOUCHER_APPROVED); //设置处理记录状态
            }else{ //需要复审
                claimVoucher.setStatus(Contant.CLAIMVOUCHER_RECHECK);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null,Contant.POST_GM).get(0).getSn()); //待处理人为总经理

                dealRecord.setDealResult(Contant.CLAIMVOUCHER_RECHECK);
            }
        }else if(dealRecord.getDealWay().equals(Contant.DEAL_BACK)){ //打回
            claimVoucher.setStatus(Contant.CLAIMVOUCHER_BACK);
            claimVoucher.setNextDealSn(claimVoucher.getCreateSn());

            dealRecord.setDealResult(Contant.CLAIMVOUCHER_BACK);
        }else if(dealRecord.getDealWay().equals(Contant.DEAL_REJECT)){ //拒绝
            claimVoucher.setStatus(Contant.CLAIMVOUCHER_TERMINATED);
            claimVoucher.setNextDealSn(null);

            dealRecord.setDealResult(Contant.CLAIMVOUCHER_TERMINATED);
        }else if(dealRecord.getDealWay().equals(Contant.DEAL_PAID)){ //打款
            claimVoucher.setStatus(Contant.CLAIMVOUCHER_PAID); //设置状态已打款
            claimVoucher.setNextDealSn(null);

            dealRecord.setDealResult(Contant.CLAIMVOUCHER_PAID);
        }
        claimVoucherDao.update(claimVoucher);
        dealRecordDao.insert(dealRecord);
    }
}
