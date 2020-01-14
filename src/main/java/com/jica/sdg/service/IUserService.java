package com.jica.sdg.service;

import com.jica.sdg.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IUserService {
	
	List<User> findAll();

    List<User> findOne(String userName);
    
    Optional<User> findOne(Integer id);
    
    List findByProvince(String id_prov);
    
    List findAllGrid();
    
    void saveUsere(User rol);
    
    void deleteUser(Integer id);
    
    Integer cekUsername(String username);

}
