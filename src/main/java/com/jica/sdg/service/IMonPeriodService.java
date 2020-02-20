package com.jica.sdg.service;

import com.jica.sdg.model.RanRad;

import java.util.List;
import java.util.Optional;

public interface IMonPeriodService {

    List<RanRad> findAll(String id);

    void saveMonPeriod(RanRad sdg);
    
    Optional<RanRad> findOne(Integer id);
    
    void deleteMonPeriod(Integer id);
    
    Integer cekPeriode(String id_prov,Integer start,Integer end);
}
