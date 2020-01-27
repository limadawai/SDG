package com.jica.sdg.service;

import com.jica.sdg.model.RanRad;

import java.util.List;

public interface IRanRadService {

    List<RanRad> findAll();

    List<RanRad> findAllByIdProv(String id);

}
