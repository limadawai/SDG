package com.jica.sdg.service;

import com.jica.sdg.model.EntryProblemIdentify;
import com.jica.sdg.repository.EntryProblemIdentifyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryProblemIdentifyService implements IEntryProblemIdentifyService {

    @Autowired
    private EntryProblemIdentifyRepository repository;

    @Override
    public List<EntryProblemIdentify> findAllProblem() {
        List problem = (List<EntryProblemIdentify>) repository.findAll();
        return problem;
    }

    @Override
    public List<EntryProblemIdentify> findGoals() {
        List goals = (List<EntryProblemIdentify>) repository.findGoals();
        return goals;
    }

}
