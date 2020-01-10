package com.jica.sdg.service;

import com.jica.sdg.model.Unit;

import java.util.List;
import java.util.Optional;

public interface IUnitService {

    List<Unit> findAll();

    void saveUnit(Unit unit);
    
    Optional<Unit> findOne(Integer id);
    
    void deleteUnit(Integer id);
}
