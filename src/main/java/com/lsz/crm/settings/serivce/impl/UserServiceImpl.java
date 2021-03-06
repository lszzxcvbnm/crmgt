package com.lsz.crm.settings.serivce.impl;

import com.lsz.crm.exception.LoginException;
import com.lsz.crm.settings.dao.UserDao;
import com.lsz.crm.settings.domain.User;
import com.lsz.crm.settings.serivce.UserService;
import com.lsz.crm.utils.DateTimeUtil;
import com.lsz.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

  private UserDao userDao =SqlSessionUtil.getSqlSession().getMapper(UserDao.class );
public void str(){

    if(userDao==null){
        System.out.println("usdao为空");
    }
}

  @Override
  public User login(String loginAct, String loginPwd, String ip) throws LoginException {
    Map<String,String> map=new HashMap<String,String>();
    map.put("loginAct",loginAct);
    map.put("loginPwd",loginPwd);

    User user=userDao.login(map);

   if(user==null){
     throw new LoginException("账号密码错误");
   }
   //如果程序能够执行到该行，说明账号密码正确，，
    //需要继续向下验证其它三项信息

    //验证失效时间
    String expireTime=user.getExpireTime() ;
   String currentTime= DateTimeUtil.getSysTime();
    if(expireTime.compareTo(currentTime)<0){
      throw new LoginException("账号已失效");
    }

    //判断锁定状态
    String lockStste=user.getLockState();
    if("0".equals(lockStste)){
      throw new LoginException("账号已锁定");
    }

    //判断ip状态
    String allowIps=user.getAllowIps() ;
    if(!allowIps.contains(ip) ){
      throw new LoginException("ip地址受限");
    }
    return user ;
  }

    @Override
    public List<User> getUserList() {

      List<User> ulist=userDao.getUserList();
        return ulist;
    }
}
