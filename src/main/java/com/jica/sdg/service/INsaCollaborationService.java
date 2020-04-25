package com.jica.sdg.service;

import com.jica.sdg.model.NsaCollaboration;

import java.util.List;
import java.util.Optional;

public interface INsaCollaborationService {

    List<NsaCollaboration> findAll();
    
    List<NsaCollaboration> findId(String id);
    
    List<NsaCollaboration> findByProgram(String id);

    void saveNsaCollaboration(NsaCollaboration col);
    
    void updateIdPhilanthropy(int id_philanthropy, int id);
    
    Optional<NsaCollaboration> findOne(Long id);
    
    void deleteNsaCollaboration(Long id);
}
