package com.xuwei.dao;


import com.xuwei.pojo.ClaimVoucher;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 报销单接口
 */
@Repository("claimVoucherDao")
public interface ClaimVoucherDao {
    void insert(ClaimVoucher claimVoucher); //填写报销单
    void update(ClaimVoucher claimVoucher);
    void delete(int id);
    ClaimVoucher select(int id);

    List<ClaimVoucher> selectByCreateSn(String csn); //查询某个创建者的所有报销单
    List<ClaimVoucher> selectByNextDealSn(String ndsn); //查询某个人可以处理的报销单
}
