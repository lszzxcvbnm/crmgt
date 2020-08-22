package com.lsz.crm.workbench.dao;

import com.lsz.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity a);

    List<Activity> gerActivityListByCondition(Map<String, Object> map);


    int getTotalByCondition(Map<String, Object> map);

    int delete(String[] ids);

    Activity getById(String id);

    int update(Activity a);

    Activity detail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameNotById(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
