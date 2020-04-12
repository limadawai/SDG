package com.jica.sdg.service;

import com.jica.sdg.model.PhilanthropyCollaboration;

import java.util.List;
import java.util.Optional;

public interface IPhilanthropyService {

//    List<NsaCollaboration> findAll();
//    
//    List<NsaCollaboration> findId(String id);

    void savePhilanthropyCollaboration(PhilanthropyCollaboration phy);
    
//    Optional<NsaCollaboration> findOne(Long id);
//    
    void deletePhilantropi(Integer id);
}
