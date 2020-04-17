package com.xuwei.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报销单条目
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClaimVoucherItem {
    private Integer id;
    private Integer claimVoucherId; //报销单编号，用来关联报销单
    private String item; //费用类型
    private Double amount; //金额
    private String comment; //说明
}