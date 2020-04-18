package com.jica.sdg.service;

import com.jica.sdg.model.UserRequestList;

import java.util.List;
import java.util.Optional;

public interface IUserRequestListService {

    List<UserRequestList> findAllNew();
    
    List<UserRequestList> findAllNewProv(String id_prov);
    
    List<UserRequestList> findAll();
    
    List<UserRequestList> findAllProv(String id_prov);

    void saveUserRequestList(UserRequestList u);
    
    Optional<UserRequestList> findOne(String id);
    
    void deleteUserRequestList(String id);
}
