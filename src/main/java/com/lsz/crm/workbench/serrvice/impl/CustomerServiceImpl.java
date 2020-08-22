package com.lsz.crm.workbench.serrvice.impl;

import com.lsz.crm.utils.SqlSessionUtil;
import com.lsz.crm.workbench.dao.CustomerDao;
import com.lsz.crm.workbench.serrvice.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao= SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public List<String> getCustomerName(String name) {
        List<String> slist= customerDao.getCustomerName(name);
        return slist;
    }
}
