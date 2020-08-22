package com.lsz.crm.settings.serivce;

import com.lsz.crm.exception.LoginException;
import com.lsz.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
