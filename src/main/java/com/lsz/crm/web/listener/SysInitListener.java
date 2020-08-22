package com.lsz.crm.web.listener;

import com.lsz.crm.settings.domain.DicValue;
import com.lsz.crm.settings.serivce.DicService;
import com.lsz.crm.settings.serivce.impl.DicServiceImpl;
import com.lsz.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;


//监听器
public class SysInitListener implements ServletContextListener  {
   /*
   * event；该参数能够取得监听对象，监听什么取什么对象
   * */

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
      System.out.println("上下文域对象");
      ServletContext application =servletContextEvent.getServletContext();
       //取数据字典
       DicService ds= (DicService) ServiceFactory.getService(new DicServiceImpl());
      Map<String, List<DicValue >> map= ds.getAll();
      Set<String> set=map.keySet();
      for(String key:set){
          application.setAttribute(key,map.get(key));
      }
      //------
        //数据处理完毕后处理stage2Possibility.properties文件
       Map<String,String> pMap=new HashMap<String,String>();
      //解析properties文件
        ResourceBundle rb=ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e=  rb.getKeys();// Enumeration<String>枚举类型
        while (e.hasMoreElements()){
           //阶段
            String key =e.nextElement();//Element迭代器
            //可能性
            String value=rb.getString(key);
            pMap.put(key,value);

        }
        //将pMap保存到服务器
        application.setAttribute("pMap",pMap);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("关闭监听器");
    }
}
