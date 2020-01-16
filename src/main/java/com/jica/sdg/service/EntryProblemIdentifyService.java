package com.jica.sdg.service;

import com.jica.sdg.model.EntryProblemIdentity;
import com.jica.sdg.repository.EntryProblemIdentifyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryProblemIdentifyService implements IEntryProblemIdentifyService {

    @Autowired
    private EntryProblemIdentifyRepository repository;

    @Override
    public List<EntryProblemIdentity> findAllProblem() {
        List problem = (List<EntryProblemIdentity>) repository.findAll();
        return problem;
    }

    public List<EntryProblemIdentity> findGoals() {
        List goals = (List<EntryProblemIdentity>) repository.findGoals();
        return goals;
    }
}
