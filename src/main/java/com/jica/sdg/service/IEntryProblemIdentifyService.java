package com.jica.sdg.service;

import com.jica.sdg.model.EntryProblemIdentify;

import java.util.List;

public interface IEntryProblemIdentifyService {

    List<EntryProblemIdentify> findAllProblem();

    List<EntryProblemIdentify> findGoals();

//    List<EntryProblemIdentity> findGoalsByRole(String id_role);

}
