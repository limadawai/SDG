package com.jica.sdg.service;

import com.jica.sdg.model.EntryGriojk;

import java.util.List;
import java.util.Optional;

public interface IEntryGriOjkService {

    List<EntryGriojk> findAll();

    void saveEntryGriOjk(EntryGriojk sdg);
    
    Optional<EntryGriojk> findOne(Integer id);
    
    void deleteEntryEntryGriOjk(Integer id);
}
