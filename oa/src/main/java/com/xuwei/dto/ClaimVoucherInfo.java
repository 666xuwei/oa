package com.xuwei.dto;


import com.xuwei.pojo.ClaimVoucher;
import com.xuwei.pojo.ClaimVoucherItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 定义要存储的报销单和条目信息
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClaimVoucherInfo {
    private ClaimVoucher claimVoucher;
    private List<ClaimVoucherItem> items;
}
