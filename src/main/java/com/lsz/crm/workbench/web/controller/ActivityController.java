package com.lsz.crm.workbench.web.controller;

import com.lsz.crm.settings.domain.User;
import com.lsz.crm.settings.serivce.UserService;
import com.lsz.crm.settings.serivce.impl.UserServiceImpl;
import com.lsz.crm.utils.*;
import com.lsz.crm.vo.PageationVO;
import com.lsz.crm.workbench.domain.Activity;
import com.lsz.crm.workbench.domain.ActivityRemark;
import com.lsz.crm.workbench.serrvice.ActivityService;
import com.lsz.crm.workbench.serrvice.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet{

  @Override()
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到市场活动控制器");
        String path=request.getServletPath();
        if("/workbench/activity/getUserList.dao".equals(path)){

            getUserList(request,response);

        }else if("/workbench/activity/save.dao".equals(path)){
           save(request,response);

        }else if("/workbench/activity/pageList.dao".equals(path)){
            pageList(request,response);

        }else if("/workbench/activity/delete.dao".equals(path)){
            delete(request,response);

        }else if("/workbench/activity/getUserListActivity.dao".equals(path)){
            getUserListActivity(request,response);

        }else if("/workbench/activity/update.dao".equals(path)){
            update(request,response);

        }else if("/workbench/activity/detail.dao".equals(path)){
            detail(request,response);

        }else if("/workbench/activity/getRemarkListByAid.dao".equals(path)){
            getRemarkListByAid(request,response);

        }else if("/workbench/activity/deleteRemark.dao".equals(path)){
            deleteRemark(request,response);

        }else if("/workbench/activity/saveRemark.dao".equals(path)){
            saveRemark(request,response);

        }else if("/workbench/activity/updateRemark.dao".equals(path)){
            updateRemark(request,response);

        }


    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入修改备注的模态窗口");
        String id=request.getParameter("id") ;
        String noteContent=request.getParameter("noteContent") ;
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "1";
        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent );
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl() );
        boolean flag=as.updateRemark(ar);
        Map<String,Object > map=new HashMap<String,Object>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.printJsonObj(response ,map);

  }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到添加活动备注操作");
        String activityId = request.getParameter("activityId");
        String noteContent = request.getParameter("noteContent");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setActivityId(activityId);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);
        ar.setEditFlag(editFlag);
        ar.setNoteContent(noteContent);
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl() );
       boolean flag=as.saveRemark(ar);
       Map<String,Object > map=new HashMap<String,Object>();
       map.put("success",flag);
       map.put("ar",ar);
       PrintJson.printJsonObj(response ,map);
  }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("删除备注操作");
        String id=request.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl() );
         boolean flag= as.deleteRemark(id);
        PrintJson.printJsonFlag(response ,flag ) ;

  }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据市场活动，取得备注信息里表");
        String activityId=request.getParameter("activityId") ;
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl() );
        List<ActivityRemark> arList=as.getRemarkListByAid(activityId);
        PrintJson.printJsonObj(response ,arList ) ;
  }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到详细信息页的操作");
        String id=request.getParameter("id") ;
        System.out.println(id);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl() );
         Activity a= as.detail(id);
         request.setAttribute("a",a);
         request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的修改操作");
        String  id=request.getParameter("id");
        String  owner=request.getParameter("owner");
        String  name=request.getParameter("name");
        String  startDate=request.getParameter("startDate") ;

        String  endDate=request.getParameter("endDate") ;
        System.out.println("开始时间"+startDate+"结束时间"+endDate);
        String  cost=request.getParameter("cost");
        System.out.println(cost);
        String  description=request.getParameter("description") ;

        //修改时间，当前系统时间
        String  editTime= DateTimeUtil.getSysTime() ;
        //修改人，当前系统用户
        String  editBy=((User)request.getSession().getAttribute("user")).getName();
        Activity a=new Activity() ;
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setEditTime(editTime);
        a.setEditBy(editBy);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl() );
        boolean flag=as.update(a);
        PrintJson.printJsonFlag(response,flag) ;
  }

    private void getUserListActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询用户信息列表和根据市场活动id查询单条记录");
        String id=request.getParameter("id");
        System.out.println(id);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl() );
         Map<String,Object>map=as.getUserListActivity(id);
         PrintJson.printJsonObj(response ,map);
  }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行删除操作");
        String ids[]=request.getParameterValues("id") ;
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=as.delete(ids);
        PrintJson.printJsonFlag(response ,flag);
  }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询市场活动信息列表的操作（结合条件查询+分页查询）");
        String name=request.getParameter("name");
        String owner=request.getParameter("owner");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String pageNoStr=request.getParameter("pageNo");
        int  pageNo=Integer.valueOf(pageNoStr);
        String pageSizeStr=request.getParameter("pageSize");
        int pageSize=Integer.valueOf(pageSizeStr);
        int skipCount=(pageNo-1)*pageSize;
        Map<String,Object > map=new HashMap<String,Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        PageationVO<Activity> vo=as.pageList(map);

        PrintJson.printJsonObj(response,vo);

       // System.out.println(vo.getTotal() +"  "+vo.getDataList().indexO +"123");
  }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动添加操作");
       String  id= UUIDUtil.getUUID() ;
       String  owner=request.getParameter("owner") ;
       String  name=request.getParameter("name") ;
       String  startDate=request.getParameter("startDate") ;
       String  endDate=request.getParameter("endDate") ;
       String  cost=request.getParameter("cost") ;
       String  description=request.getParameter("description") ;

       //创建时间，当前系统时间
       String  createTime= DateTimeUtil.getSysTime() ;
       //创建人，当前系统用户
       String  createBy=((User)request.getSession().getAttribute("user")).getName();
        Activity a=new Activity() ;
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setCreateTime(createTime);
        a.setEditBy(createBy);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl() );
          boolean flag=as.save(a);
          PrintJson.printJsonFlag(response,flag) ;
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得用户信息列表");
        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl() );
        List<User> uList= us.getUserList();

          PrintJson.printJsonObj(response,uList ) ;



  }


}
