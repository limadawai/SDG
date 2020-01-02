package com.jica.sdg.service;

import com.jica.sdg.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
	
	List<User> findAll();

    List<User> findOne(String userName);
    
    Optional<User> findOne(Integer id);
    
    List<User> findByProvince(String id_prov);
    
    void saveUsere(User rol);
    
    void deleteUser(Integer id);

}
