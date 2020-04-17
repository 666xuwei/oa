package com.xuwei.service;


import com.xuwei.pojo.Employee;

/**
 * 登陆功能的实现
 */
public interface LoginService {

    Employee login(String sn, String password);
    void changePassword(Employee employee);
}
