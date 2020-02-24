package com.jica.sdg.service;

import com.jica.sdg.model.BestPractice;

import java.util.List;
import java.util.Optional;

public interface IBestPracticeService {

    List<BestPractice> findAll();

    void saveBestPractice(BestPractice sdg);
    
    Optional<BestPractice> findOne(Integer id);
    
    void deleteBestPractice(Integer id);
}
