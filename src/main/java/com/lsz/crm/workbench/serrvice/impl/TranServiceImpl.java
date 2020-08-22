package com.lsz.crm.workbench.serrvice.impl;

import com.lsz.crm.utils.DateTimeUtil;
import com.lsz.crm.utils.SqlSessionUtil;
import com.lsz.crm.utils.UUIDUtil;
import com.lsz.crm.workbench.dao.CustomerDao;
import com.lsz.crm.workbench.dao.TranDao;
import com.lsz.crm.workbench.dao.TranHistoryDao;

import com.lsz.crm.workbench.domain.Customer;
import com.lsz.crm.workbench.domain.Tran;
import com.lsz.crm.workbench.domain.TranHistory;
import com.lsz.crm.workbench.serrvice.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    private TranHistoryDao tranHistoryDao=SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private TranDao tranDao= SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private CustomerDao customerDao= SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    @Override
    public boolean save(Tran t, String customerName) {
        /*
        在做添加之前，参数t里面就少了一项信息，就是客户的主键，customerId先处理客户相关的需求
        （1）判断customerName,根据客户名称在客户表进行精准查询
           如果有取id;
           如果没有新建客户并取id
           (2)经过以上信息t对象的信息就全了，需要执行添加交易的 操作
           (3)添加交易完毕以后，需要创建一条交易历史

         */
      boolean flag=true;
     Customer cus=customerDao.getCustomerByName(customerName);
         //如果为空，创建客户
          if(cus==null){
              cus=new Customer();
              cus.setId(UUIDUtil.getUUID());
              cus.setName(customerName);
              cus.setCreateBy(t.getCreateBy());
              cus.setCreateTime(t.getCreateTime());
              cus.setContactSummary(t.getContactSummary());
              cus.setNextContactTime(t.getNextContactTime());
              cus.setOwner(t.getOwner());
             //添加客户
              int count1=customerDao.save(cus);
              if(count1!=1){
                  flag=false;
              }
          }
          //cus已经存在了
        t.setCustomerId(cus.getId());
       //添加交易
        int count2=tranDao.save(t);;
        if(count2!=1){
            flag=false;
        }
        //添加交易历史
        TranHistory th=new TranHistory();
         th.setId(UUIDUtil.getUUID());
         th.setTranId(t.getId());
         th.setStage(t.getStage());
         th.setMoney(t.getMoney());
         th.setExpectedDate(t.getExpectedDate());
         th.setCreateTime(DateTimeUtil.getSysTime());
         th.setCreateBy(t.getCreateBy());
         //执行添加
        int count3=tranHistoryDao.save(th);;
        if(count3!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Tran detail(String id) {
        Tran t=tranDao.detail(id);
        return t;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> tList=  tranHistoryDao.getHistoryListByTranId(tranId);

        return tList;
    }

    @Override
    public boolean changeStage(Tran t) {
        boolean flag=true;
       //改变交易阶段
        int count1=tranDao.changeStage(t);
        if(count1!=1){
            flag=false;
        }
        //交易改变后生成历史
        TranHistory th=new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getEditBy());//此时的修改人为历史 的创建人
        //  th.setCreateBy(t.getCreateBy());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setTranId(t.getId());
        th.setStage(t.getStage());

        //添加交易历史
        int count2= tranHistoryDao.save(th);
        if(count2!=1){
            flag=false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {
       //取得tatal
         int total=tranDao.getTotal();
        //取得dataList
     List<Map<String,Object>> dataList=   tranDao.getCharts();

        //将total和dataList保存到map中
        Map<String, Object> map=new HashMap<String,Object>();
        map.put("total",total);
        map.put("dataList",dataList);
        //返回map
//        Map<String, Object>
        return map;
    }
}
