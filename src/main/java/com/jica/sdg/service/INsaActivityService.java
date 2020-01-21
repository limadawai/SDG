package com.jica.sdg.service;

import com.jica.sdg.model.NsaActivity;

import java.util.List;
import java.util.Optional;

public interface INsaActivityService {

    List<NsaActivity> findAll(Integer id);

    void saveNsaActivity(NsaActivity gov);
    
    Optional<NsaActivity> findOne(Integer id);
    
    void deleteNsaActivity(Integer id);
}
