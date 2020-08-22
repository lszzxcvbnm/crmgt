package com.lsz.crm.settings.serivce;

import com.lsz.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicService {


    Map<String, List<DicValue>> getAll();
}
