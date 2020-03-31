package com.jica.sdg.service;

import com.jica.sdg.model.RanRad;

import java.util.List;
import java.util.Optional;

public interface IRanRadService {

    List<RanRad> findAll();

    List<RanRad> findAllByIdProv(String id);
    
    Optional<RanRad> findOne(Integer id);

}
