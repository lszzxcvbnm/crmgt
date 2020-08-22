package com.lsz.crm.settings.dao;

import com.lsz.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
