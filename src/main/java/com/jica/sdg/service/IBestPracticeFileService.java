package com.jica.sdg.service;

import com.jica.sdg.model.BestPracticeFile;

import java.util.List;
import java.util.Optional;

public interface IBestPracticeFileService {

    List<BestPracticeFile> findAll();

    void saveBestPracticeFile(BestPracticeFile sdg);
    
    Optional<BestPracticeFile> findOne(Integer id);
    
    void deleteBestPracticeFile(Integer id);
}
