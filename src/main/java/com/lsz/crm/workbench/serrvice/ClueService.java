package com.lsz.crm.workbench.serrvice;

import com.lsz.crm.workbench.domain.Clue;
import com.lsz.crm.workbench.domain.Tran;

public interface ClueService {
    boolean save(Clue c);

    Clue datil(String id);

    boolean unbund(String id);

    boolean bund(String cid, String[] aids);

    boolean convertt(String clueId, Tran t, String createBy);
}
