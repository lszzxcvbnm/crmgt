package com.lsz.crm.workbench.web.controller;



import com.lsz.crm.settings.domain.User;
import com.lsz.crm.settings.serivce.UserService;
import com.lsz.crm.settings.serivce.impl.UserServiceImpl;
import com.lsz.crm.utils.DateTimeUtil;
import com.lsz.crm.utils.PrintJson;
import com.lsz.crm.utils.ServiceFactory;
import com.lsz.crm.utils.UUIDUtil;
import com.lsz.crm.workbench.domain.Activity;
import com.lsz.crm.workbench.domain.Clue;
import com.lsz.crm.workbench.domain.Tran;
import com.lsz.crm.workbench.serrvice.ActivityService;
import com.lsz.crm.workbench.serrvice.ClueService;
import com.lsz.crm.workbench.serrvice.impl.ActivityServiceImpl;
import com.lsz.crm.workbench.serrvice.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClueController extends HttpServlet {


    @Override()
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到线索活动控制器");
        String path = request.getServletPath();
        if ("/workbench/clue/getUserList.dao".equals(path)) {

            getUserLis(request,response);

        } else if ("/workbench/clue/save.dao".equals(path)) {
            save(request,response);

        }else if ("/workbench/clue/detail.dao".equals(path)) {
            detail(request,response);

        }else if ("/workbench/clue/getActivityListByClueId.dao".equals(path)) {
            getActivityListByClueId(request,response);

        }else if ("/workbench/clue/unbund.dao".equals(path)) {
            unbund(request,response);

        }else if ("/workbench/clue/getActivityListByNameNotById.dao".equals(path)) {
            getActivityListByNameNotById(request,response);

        }else if ("/workbench/clue/bund.dao".equals(path)) {
            bund(request,response);

        }else if ("/workbench/clue/getActivityListByName.dao".equals(path)) {
            getActivityListByName(request,response);

        }else if ("/workbench/clue/convert.dao".equals(path)) {
            convertt(request,response);

        }

    }

    private void convertt(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("执行线索转换的操作");
        //接收是否需要接收表单
        String clueId =request.getParameter("clueId");
        String flag=request.getParameter("flag");
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        Tran t=null;
        //如果需要接受表单数据
       if("a".equals(flag)){
          t=new Tran();
         String money=request.getParameter("money");
         String name=request.getParameter("name");
         String expectedDate=request.getParameter("expectedDate");
         String stage=request.getParameter("stage");
         String activityId= request.getParameter("activityId");
         String id=UUIDUtil.getUUID();
        //String createBy=((User)request.getSession().getAttribute("user")).getName();
         String createTime= DateTimeUtil.getSysTime();
         t.setActivityId(activityId);
         t.setMoney(money);
         t.setExpectedDate(expectedDate);
         t.setStage(stage);
         t.setCreateBy(createBy);
         t.setCreateTime(createTime);
         t.setId(id);
         t.setName(name);
       }

        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl() );
       /*
         1必须传递cludeid参数
         2必须传递的参数t，因为在线索转换的过程中，有可能会临时创建一笔交易，也有可能是为空，
        */


        boolean flag1;
        flag1 = cs.convertt(clueId,t,createBy);
        if (flag1){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }

    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询市场活动列表（根据name模糊查");
        String aname=request.getParameter("aname");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl() );
        List<Activity> aList=as.getActivityListByName(aname);
        PrintJson.printJsonObj(response,aList);


    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行关联市场活动的操作");
        String cid=request.getParameter("cid");
        String aids[]=request.getParameterValues("aid");
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl() );
        boolean flag=cs.bund(cid,aids);
        PrintJson.printJsonFlag(response,flag) ;
    }

    private void getActivityListByNameNotById(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询市场活动列表（根据名称模糊查+排除掉已经关联的线索的活动");
        String clueId=request.getParameter("clueId") ;
        String aname=request.getParameter("aname") ;
        Map<String,String > map=new HashMap<String,String>() ;
        map.put("aname",aname );
        map.put("clueId",clueId );
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl() );
        List<Activity> aList=as.getActivityListByNameNotById(map);
        PrintJson.printJsonObj(response,aList);
    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("解除关联操作");
        String id=request.getParameter("id") ;
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl() );
        boolean flag=cs.unbund(id);
        PrintJson.printJsonFlag(response,flag) ;
    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据线索id来查询关联关系市场活动表");
        String clueId=request.getParameter("clueId") ;

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl() );
        List<Activity> aList=as.getActivityListByClueId(clueId );

       PrintJson.printJsonObj(response,aList );
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到线索的详细信息页");
        String id=request.getParameter("id") ;
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl() );
       Clue  c=cs.datil(id);
       request.setAttribute("c",c);
       request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request ,response ) ;

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索的添加");
       String id= UUIDUtil.getUUID();
       String fullname=request.getParameter("fullname") ;
       String appellation=request.getParameter("appellation") ;
       String owner=request.getParameter("owner") ;
       String company=request.getParameter("company") ;
       String job=request.getParameter("job") ;
       String email=request.getParameter("email") ;
       String phone=request.getParameter("phone") ;
       String website=request.getParameter("website") ;
       String mphone=request.getParameter("mphone") ;
       String state=request.getParameter("state") ;
       String source=request.getParameter("source") ;
       String createBy=((User)request.getSession().getAttribute("user")).getName();
       String createTime= DateTimeUtil.getSysTime();
       String description=request.getParameter("description") ;
       String contactSummary=request.getParameter("contactSummary") ;
       String nextContactTime=request.getParameter("nextContactTime") ;
       String address=request.getParameter("address") ;
         Clue c =new Clue();
        c.setId(id);
        c.setFullname(fullname);
        c.setAppellation(appellation);
        c.setOwner(owner);
        c.setCompany(company);
        c.setJob(job);
        c.setEmail(email);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setMphone(mphone);
        c.setState(state);
        c.setSource(source);
        c.setCreateBy(createBy);
        c.setCreateTime(createTime);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
       c.setNextContactTime(nextContactTime);
       c.setAddress(address) ;
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl() );
          boolean flag=cs.save(c);
          PrintJson.printJsonFlag(response ,flag) ;

    }

    private void getUserLis(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");
        UserService US= (UserService) ServiceFactory.getService(new UserServiceImpl() );
         List<User > uList=US.getUserList();
        PrintJson.printJsonObj(response,uList ) ;
    }
}

