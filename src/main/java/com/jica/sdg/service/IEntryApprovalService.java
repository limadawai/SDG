package com.jica.sdg.service;

import com.jica.sdg.model.EntryApproval;
import com.jica.sdg.model.EntryShowReport;

import java.util.List;
import java.util.Optional;

public interface IEntryApprovalService {

    List<EntryApproval> findAll();
    
    List<EntryApproval> getAllMessage();
    
    List<EntryApproval> getMessageByRole(Integer id_role);
    
    List<EntryApproval> getMessageByProv(Integer id_prov);

    void save(EntryApproval app);
    
    
    Optional<EntryApproval> findOne(Integer id);
    
    void deleteEntryApproval(Integer id);
    
    void updateApproval(String approval, String description, Integer id);
    
    void deleteApproveGovBudget(Integer id_role, Integer id_monper, Integer year, String type, String periode);
    
    void updatedoneApproveGovBudget(Integer id_monper, Integer year, String type, String periode);
    
    void updateundoneApproveGovBudget(Integer id_monper, Integer year, String type, String periode);
    
    void saveshow(EntryShowReport app1);
    
    void deleteshow(Integer year, Integer id_monper, String type, String periode);
}
