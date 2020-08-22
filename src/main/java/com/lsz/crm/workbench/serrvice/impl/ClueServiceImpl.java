package com.lsz.crm.workbench.serrvice.impl;

import com.lsz.crm.utils.DateTimeUtil;
import com.lsz.crm.utils.SqlSessionUtil;
import com.lsz.crm.utils.UUIDUtil;
import com.lsz.crm.workbench.dao.*;
import com.lsz.crm.workbench.domain.*;
import com.lsz.crm.workbench.serrvice.ClueService;

import java.util.List;

public class ClueServiceImpl implements ClueService  {
   //线索相关表
    private ClueDao clueDao= SqlSessionUtil.getSqlSession().getMapper(ClueDao.class) ;
   private ClueActivityRelationDao clueActivityRelationDao= SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class) ;
   private ClueRemarkDao clueRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
   //客户相关表
    private CustomerDao customerDao=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao=SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
   //联系人相关表
    private ContactsDao contactsDao =SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao=SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
   //交易相关表
    private TranDao tranDao=SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao =SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao .class);
   @Override
   public boolean save(Clue c) {
      boolean flag=true;
      int count=clueDao.save(c);
      if(count!=1){
         flag=false;
      }
     return flag;

   }

   @Override
   public Clue datil(String id) {
      Clue c=clueDao.datil(id);
      return c;


   }

   @Override
   public boolean unbund(String id) {

      boolean flag=true;
      int count=clueActivityRelationDao.unbund(id);
      if(count!=1){
         flag=false;
      }
      return flag;
   }

   @Override
   public boolean bund(String cid, String[] aids) {
      boolean flag=true;
        for(String aid:aids){
           //取得的每一做关联
           ClueActivityRelation  car=new ClueActivityRelation() ;
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(cid);

                //  添加关联关系表中的数据
          int count= clueActivityRelationDao.bund(car);
           if(count!=1){
              flag=false;
           }
        }
      return flag;
   }

    @Override
    public boolean convertt(String clueId, Tran t, String createBy) {
       String cresteTime= DateTimeUtil.getSysTime();

        boolean flag=true;
        //(1)通过线索id查单条获取对象（线索对象中封装了线索的信息）
        Clue c=clueDao.getById(clueId);
        //(2)通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
       String company = c.getCompany();
       Customer cus=customerDao.getCustomerByName(company);
      //如果cus null ,说明以前没有这个客户，需要创建一个
       if(cus==null){
           cus=new Customer();
           cus.setId(UUIDUtil.getUUID());
           cus.setAddress(c.getAddress());
           cus.setOwner(c.getOwner());
           cus.setPhone(c.getPhone());
           cus.setWebsite(c.getWebsite());
           cus.setNextContactTime(c.getNextContactTime());
           cus.setName(company);
           cus.setDescription(c.getDescription());
           cus.setCreateBy(createBy);
           cus.setCreateTime(cresteTime);
           cus.setContactSummary(c.getContactSummary());
           //添加客户
          int count1= customerDao.save(cus);
          if(count1!=1){
              flag=false;
          }
       }
       //(3)通过线索对象提取联系人信息，保存联系人
        Contacts con=new Contacts();
       con.setId(UUIDUtil.getUUID());
       con.setSource(c.getSource());
       con.setOwner(c.getOwner());
       con.setNextContactTime(c.getNextContactTime());
       con.setMphone(c.getMphone());
       con.setJob(c.getJob());
       con.setFullname(c.getFullname());
       con.setEmail(c.getEmail());
       con.setDescription(c.getDescription());
       con.setCustomerId(cus.getId());
       con.setCreateBy(createBy);
       con.setCreateTime(cresteTime);
       con.setContactSummary(c.getContactSummary());
       con.setAppellation(c.getAppellation());
       con.setAddress(c.getAddress());
       //添加联系人
         int count2= contactsDao.save(con);
        if(count2!=1){
            flag=false;
        }
        //经过第三步联系人以经有了如果要使用联系人的id直接使用con.id();
        //(4)线索备注转换到客户备注以及联系人备注
        //查询出与该线索有关的线索的备注信息列表
        List<ClueRemark>  clueRemarkList=clueRemarkDao.getListByClueId(clueId);
            for(ClueRemark clueRemark:clueRemarkList) {
                //取出备注信息（主要转化备注信息到客户备注和联系人备注中去
                String noteContent = clueRemark.getNoteContent();
                //创建客户备注对象，添加客户备注
                CustomerRemark customerRemark = new CustomerRemark();
                customerRemark.setId(UUIDUtil.getUUID());
                customerRemark.setCreateBy(createBy);
                customerRemark.setCreateTime(cresteTime);
                customerRemark.setCustomerId(cus.getId());
                customerRemark.setEditFlag("0");
                customerRemark.setNoteContent(noteContent);
                int count3 = customerRemarkDao.save(customerRemark);

                if (count3 != 1) {
                    flag = false;
                }
                //创建联系人备注对象，添加联系人备注
                ContactsRemark contactsRemark = new ContactsRemark();
                contactsRemark.setId(UUIDUtil.getUUID());
                contactsRemark.setCreateBy(createBy);
                contactsRemark.setCreateTime(cresteTime);
                contactsRemark.setContactsId(con.getId());
                contactsRemark.setEditFlag("0");
                contactsRemark.setNoteContent(noteContent);
                int count4 = contactsRemarkDao.save(contactsRemark);

                if (count4 != 1) {
                    flag = false;
                }
            }
                //(5)"线索和市场活动的关系转换到联系人和市场活动的关系
                //查询出与该条线索关联的市场活动，查询与市场活动的关联关系列表
               List<ClueActivityRelation> clueActivityRelationList= clueActivityRelationDao.getListByClueId(clueId);
              //遍历出每一条与市场活动关联关系表记录
                for(ClueActivityRelation clueActivityRelation:clueActivityRelationList){
                   //从每一条遍历出来的记录中取出关联的市场活动的id
                   String activityId= clueActivityRelation.getActivityId();
                   //创建 联系人与市场活动的关联关系表对象 让第三步生成的联系人与市场活动做关联
                    ContactsActivityRelation contactsActivityRelation=new ContactsActivityRelation();
                   contactsActivityRelation.setId(UUIDUtil.getUUID());
                    contactsActivityRelation.setActivityId(activityId);
                    contactsActivityRelation.setContactsId(con.getId());
                    //添加联系人与市场活动的关联关系表
                    int count5=contactsActivityRelationDao.save(contactsActivityRelation);


                    if(count5!=1){
                        flag=false;
                    }
                }
             //(6) 如果有创建交易需求，创建一条交易

           if(t!=null){
               /*
                t对象在controller里面封装好的信息如下
                id,money,expectedDate,stage,activityId,createBy,createTime
                通过c 完善对t 对象的封装
               */
               t.setSource(c.getSource());
               t.setOwner(c.getOwner());
               t.setNextContactTime(c.getNextContactTime());
               t.setDescription(c.getDescription());
               t.setContactsId(con.getId());
               t.setContactSummary(c.getContactSummary());
               t.setCustomerId(cus.getId());
               //添加交易
               int count6= tranDao.save(t);
               if(count6!=1){
                   flag=false;
               }
               //(7) 如果创建了交易，则创建一条该交易下的交易历史
               TranHistory th=new TranHistory();
               th.setId(UUIDUtil.getUUID());
               th.setCreateBy(createBy);
               th.setCreateTime(cresteTime);
               th.setExpectedDate(t.getExpectedDate());
               th.setMoney(t.getMoney());
               th.setStage(t.getStage());
               th.setTranId(t.getId());
               //添加交易历史
              int count7= tranHistoryDao.save(th);
               if(count7!=1){
                   flag=false;
               }
           }
           //(8) 删除线索备注
        for(ClueRemark clueRemark:clueRemarkList){
         int count8= clueRemarkDao.delete(clueRemark);
            if(count8!=1){
                flag=false;
            }
        }
        //(9) 删除线索和市场活动的关系
        for(ClueActivityRelation clueActivityRelation:clueActivityRelationList) {

            int count9= clueActivityRelationDao.delete(clueActivityRelation);
            if(count9!=1){
                flag=false;
            }
        }
        //(10) 删除线索
        int count10=clueDao.delete(clueId);
        if(count10!=1){
            flag=false;
        }
        return flag;
    }
}
