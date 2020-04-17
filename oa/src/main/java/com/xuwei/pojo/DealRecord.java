package com.xuwei.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 处理记录
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DealRecord {
    private Integer id;
    private Integer claimVoucherId; //报销单编号
    private String dealSn; //处理人编号
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date dealTime; //处理时间
    private String dealWay; //处理方式
    private String dealResult; //处理结果
    private String comment; //备注

    private Employee dealer;
}