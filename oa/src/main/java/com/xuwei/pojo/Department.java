package com.xuwei.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门表
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Department {
    private String sn;
    private String name;
    private String address;
}