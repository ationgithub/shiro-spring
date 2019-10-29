package cn.wolfcode.shiro.web.controller;


import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;

//  认证页面
@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login(Model model, HttpServletRequest request) throws Exception {
        //如果登陆失败从request中获取认证异常信息，shiroLoginFailure就是shiro异常类的全限定名
        String exceptionClassName = (String) request.getAttribute("shiroLoginFailure");
        //根据shiro返回的异常类路径判断，抛出指定异常信息
        if (exceptionClassName != null) {
            if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
                request.setAttribute("errorMsg", "用户名不存在");
                model.addAttribute("errorMsg", "用户名不存在");
            } else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
                request.setAttribute("errorMsg", "用户名/密码不正确");
                model.addAttribute("errorMsg", "用户名/密码不正确");
            }else {
                request.setAttribute("errorMsg", "其他错误");
                model.addAttribute("errorMsg", "其他错误");
            }
        }
        // 此方法不处理登陆成功（认证成功），shiro认证成功会自动跳转到上一个请求路径
        // 登陆失败还到login页面
        request.setAttribute("username", request.getParameter("username"));
        return "forward:/login.jsp";

    }
}
