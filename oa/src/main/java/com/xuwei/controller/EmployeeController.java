package com.xuwei.controller;


import com.xuwei.pojo.Employee;
import com.xuwei.service.DepartmentService;
import com.xuwei.service.EmployeeService;
import com.xuwei.util.Contant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 员工管理
 */
@Controller("employeeController")
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private EmployeeService employeeService;

    /**
     *
     * @param map
     * @return
     */
    @RequestMapping("/list")
    public String list(Map<String, Object> map){
        map.put("list",employeeService.getAll());
        return "employee_list";
    }

    @RequestMapping("/to_add")
    public String toAdd(Map<String, Object> map){
        map.put("employee",new Employee());
        map.put("dlist",departmentService.getAll()); //部门列表
        map.put("plist", Contant.getPosts()); //职位
        return "employee_add";
    }
    @RequestMapping("/add")
    public String add(Employee employee){
        employeeService.add(employee);
        return "redirect:list";
    }

    @RequestMapping(value = "/to_update",params = "sn")
    public String toUpdate(String sn, Map<String, Object> map){
        map.put("employee",employeeService.get(sn));
        map.put("dlist",departmentService.getAll());
        map.put("plist", Contant.getPosts());
        return "employee_update";
    }

    @RequestMapping("/update")
    public String update(Employee employee){
        employeeService.edit(employee);
        return "redirect:list";
    }

    @RequestMapping(value = "/remove",params = "sn")
    public String remove(String sn){
        employeeService.remove(sn);
        return "redirect:list";
    }
}
