package com.jica.sdg.service;

import com.jica.sdg.model.EntryBestPractice;

import java.util.List;
import java.util.Optional;

public interface IEntryBestPracticeService {

    List<EntryBestPractice> findAll();

    void saveEntryBestPractice(EntryBestPractice sdg);
    
    Optional<EntryBestPractice> findOne(Integer id);
    
    void deleteEntryBestPractice(Integer id);
}
