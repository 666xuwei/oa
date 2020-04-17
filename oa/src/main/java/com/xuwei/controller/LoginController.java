package com.xuwei.controller;


import com.xuwei.pojo.Employee;
import com.xuwei.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * 登陆功能，使用拦截器
 */
@Controller("loginController")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 跳往登陆界面
     */
    @RequestMapping("/to_login")
    public String to_Login() {
        return "login";
    }

    /**
     * @RequestParam 将请求参数绑定到你控制器的方法参数上（是springmvc中接收普通参数的注解）
     *
     * @param session spring会自动的把session给注入进来，直接用就可以了
     * @param sn
     * @param password
     */
    @RequestMapping("/login")
    public String login(HttpSession session, @RequestParam String sn, @RequestParam String password) {
        Employee employee = loginService.login(sn, password);
        if (employee == null) { //登陆失败，重新登陆
            return "redirect:to_login";
        }
        //登陆成功，使用session保存登陆信息
        session.setAttribute("employee", employee);
        return "redirect:self";
    }

    /**
     * 登陆成功后，跳转到个人信息页面
     */
    @RequestMapping("/self")
    public String self() {
        return "self";
    }

    /**
     * 退出登陆
     * @param session
     */
    @RequestMapping("/quit")
    public String quit(HttpSession session) {
        session.setAttribute("employee", null);
        return "redirect:to_login";
    }


    @RequestMapping("/to_change_password")
    public String toChangePassword() {
        return "change_password";
    }

    /**
     * 修改密码
     * @param session
     * @param old
     * @param new1
     * @param new2
     * @return
     */
    @RequestMapping("/change_password")
    public String changePassword(HttpSession session, @RequestParam String old, @RequestParam String new1, @RequestParam String new2) {
        Employee employee = (Employee) session.getAttribute("employee");
        if (employee.getPassword().equals(old)) { //当前登陆用户的密码是不是输入的密码
            if (new1.equals(new2)) { //两次输入的密码是否一致
                employee.setPassword(new1);
                loginService.changePassword(employee);
                return "redirect:self";
            }
        }
        return "redirect:to_change_password";
    }
}
