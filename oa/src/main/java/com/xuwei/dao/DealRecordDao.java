package com.xuwei.dao;


import com.xuwei.pojo.DealRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理记录，类似于日志
 */
@Repository("dealRecordDao")
public interface DealRecordDao {
    void insert(DealRecord dealRecord);
    List<DealRecord> selectByClaimVoucher(int cvid); //针对于某个报销单我想查询处理流程
}
