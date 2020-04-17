package com.xuwei.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 员工表
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employee {
    private String sn;
    private String password;
    private String name;
    private String departmentSn; //部门编号
    private String post; //职位
    private Department department;
}