package com.lsz.crm.settings.serivce.impl;

import com.lsz.crm.settings.dao.DicTypeDao;
import com.lsz.crm.settings.dao.DicValueDao;
import com.lsz.crm.settings.domain.DicType;
import com.lsz.crm.settings.domain.DicValue;
import com.lsz.crm.settings.serivce.DicService;
import com.lsz.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService  {
private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {

        Map<String, List<DicValue>> map=new HashMap<String, List<DicValue>>() ;

        //将字典类型列表取出
       List<DicType > dtList=dicTypeDao.getTypeList();
        //将数据字典列表遍历
        for(DicType dt:dtList ){
            //取得每一种类型的字典编码
            String code=dt.getCode() ;
            //根据每一个字典类型来取得字典值列表
            List<DicValue> dvList=dicValueDao.getListByCode(code);
            map.put(code+"List",dvList );
        }

        return map;
    }
}
