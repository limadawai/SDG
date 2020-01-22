package com.jica.sdg.service;

import com.jica.sdg.model.SdgDisaggreDetail;

import java.util.List;
import java.util.Optional;

public interface ISdgDisaggreDetailService {

    List<SdgDisaggreDetail> findAll(Integer id);

    void saveSdgDisaggreDetail(SdgDisaggreDetail sdg);
    
    Optional<SdgDisaggreDetail> findOne(Integer id);
    
    void deleteSdgDisaggreDetail(Integer id);
}
