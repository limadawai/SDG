package com.jica.sdg.service;

import com.jica.sdg.model.User;

import java.util.List;

public interface IUserService {

    List<User> findOne(String userName);

}
