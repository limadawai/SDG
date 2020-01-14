package com.jica.sdg.service;

import com.jica.sdg.model.EntryProblemIdentity;
import com.jica.sdg.repository.EntryProblemIdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryProblemIdentityService implements IEntryProblemIdentityService {

    @Autowired
    private EntryProblemIdentityRepository repository;

    @Override
    public List<EntryProblemIdentity> findAllProblem() {
        List problem = (List<EntryProblemIdentity>) repository.findAll();
        return problem;
    }
}
