package com.jica.sdg.service;

import com.jica.sdg.model.UserRequestList;

import java.util.List;
import java.util.Optional;

public interface IUserRequestListService {

    List<UserRequestList> findAllNew();
    
    List<UserRequestList> findAll();

    void saveUserRequestList(UserRequestList u);
    
    Optional<UserRequestList> findOne(String id);
    
    void deleteUserRequestList(String id);
}
