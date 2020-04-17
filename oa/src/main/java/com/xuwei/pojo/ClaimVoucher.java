package com.xuwei.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 报销单
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClaimVoucher {
    private Integer id;
    private String cause; //事由
    private String createSn; //创建者编号
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date createTime; //创建时间
    private String nextDealSn; //待处理人编号
    private Double totalAmount; //总金额
    private String status; //状态

    private Employee creater;
    private Employee dealer;
}