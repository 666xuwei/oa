package com.xuwei.service.impl;


import com.xuwei.dao.EmployeeDao;
import com.xuwei.pojo.Employee;
import com.xuwei.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    private EmployeeDao employeeDao;

    /**
     * 登陆校验
     * @param sn 用户名
     * @param password 密码
     * @return
     */
    public Employee login(String sn, String password) {
        Employee employee = employeeDao.select(sn);
        //如果登陆编号在数据库存在且对应密码与输入密码一致则认为登陆成功，否则登陆失败
        if (employee != null && employee.getPassword().equals(password)) {
            return employee;
        }
        return null;
    }

    /**
     * 修改密码
     * @param employee
     */
    public void changePassword(Employee employee) {
        employeeDao.update(employee);
    }
}
