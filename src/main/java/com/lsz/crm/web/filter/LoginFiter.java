package com.lsz.crm.web.filter;

import com.lsz.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFiter implements Filter  {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain  chain) throws IOException, ServletException {
        System.out.println("进入到验证有没有登陆过的过滤器");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String path=request.getServletPath();
        if("/login.jsp".equals(path)||"/settings/user/login.dao".equals(path) ) {
            chain.doFilter(req,resp) ;
        }else{
            HttpSession session=request.getSession();
            User user= (User) session.getAttribute("user");

            if(user!=null){
                chain.doFilter(req,resp) ;
            }else{
                //重定向到登录页
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }

        }

    }

    @Override
    public void destroy() {

    }
}
