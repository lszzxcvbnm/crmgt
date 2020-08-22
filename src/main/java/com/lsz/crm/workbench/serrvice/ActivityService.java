package com.lsz.crm.workbench.serrvice;

import com.lsz.crm.vo.PageationVO;
import com.lsz.crm.workbench.domain.Activity;
import com.lsz.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity a);



    boolean delete(String[] ids);

    Map<String, Object> getUserListActivity(String id);

    boolean update(Activity a);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark ar);

    boolean updateRemark(ActivityRemark ar);

    PageationVO<Activity> pageList(Map<String, Object> map);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameNotById(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
