package com.jica.sdg.service;

import com.jica.sdg.model.EntryProblemIdentify;
import com.jica.sdg.repository.EntryProblemIdentifyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface IEntryProblemIdentifyService {

    List<EntryProblemIdentify> findAllProblem();

    List<EntryProblemIdentify> findGoals();

//    List<EntryProblemIdentity> findGoalsByRole(String id_role);

}
