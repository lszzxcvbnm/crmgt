package com.lsz.crm.settings.web.controller;

import com.lsz.crm.settings.domain.User;
import com.lsz.crm.settings.serivce.UserService;
import com.lsz.crm.settings.serivce.impl.UserServiceImpl;
import com.lsz.crm.utils.MD5Util;
import com.lsz.crm.utils.PrintJson;
import com.lsz.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet{

  @Override()
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到用户控制器");
        String path=request.getServletPath();
        if("/settings/user/login.dao".equals(path)){

          login(request,response);

       }
//        else  if("/settings/user/xxdao".equals(path)){
////            xxxx(request.response);
//
//        }


  }
    private void login(HttpServletRequest request,HttpServletResponse response){
        System.out.println("进入登录操作");
        String loginAct=request.getParameter("loginAct") ;
        System.out.println(loginAct);
        String loginPwd=request.getParameter("loginPwd") ;
       //将密码明文转换为密文
       loginPwd= MD5Util.getMD5(loginPwd);
        //接收浏览器端ip地址

        String ip=request.getRemoteAddr();
        System.out.println("----ip---"+ip);
        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());
        System.out.println("123");

       try{

           System.out.println("456");
           User user=us.login(loginAct,loginPwd,ip);

           request.getSession().setAttribute("user",user);
           //执行到此处，业务层没有异常，，登录成功
           PrintJson.printJsonFlag(response ,true) ;
       }catch(Exception e){
           e.printStackTrace();
           System.out.println("789");
           //一旦程序执行了此处则登录失败
           String msg=e.getMessage();
           Map<String,Object> map=new HashMap<String,Object>();
           map.put("success",false);
           map.put("msg",msg);
           PrintJson.printJsonObj(response ,map) ;
       }
    }
}
