package com.jica.sdg.service;

import com.jica.sdg.model.NsaActivity;

import java.util.List;
import java.util.Optional;

public interface INsaActivityService {

    List<NsaActivity> findAll(String id);

    void saveNsaActivity(NsaActivity gov);
    
    Optional<NsaActivity> findOne(String id);
    
    void deleteNsaActivity(String id);
}
