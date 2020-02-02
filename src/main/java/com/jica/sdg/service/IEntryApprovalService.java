package com.jica.sdg.service;

import com.jica.sdg.model.EntryApproval;

import java.util.List;
import java.util.Optional;

public interface IEntryApprovalService {

    List<EntryApproval> findAll();

    void save(EntryApproval app);
    
    Optional<EntryApproval> findOne(Integer id);
    
    void deleteEntryApproval(Integer id);
    
    void updateApproval(String approval, String description, Integer id);
}
