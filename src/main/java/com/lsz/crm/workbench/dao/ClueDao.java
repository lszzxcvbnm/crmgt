package com.lsz.crm.workbench.dao;

import com.lsz.crm.workbench.domain.Clue;

public interface ClueDao {


    int save(Clue c);

    Clue datil(String id);

    Clue getById(String clueId);

    int delete(String clueId);
}
