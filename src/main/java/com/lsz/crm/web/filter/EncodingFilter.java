package com.lsz.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter  {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        System.out.println("进入到过滤字符编码的过滤器");
        //过滤post,请求参数的乱码
         req.setCharacterEncoding("UTF-8");
         //过滤响应,请求参数的乱码
        resp.setContentType("text/html;charset=utf-8");
        //请求放行
        chain.doFilter(req,resp) ;
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }
}
