package com.jica.sdg.service;

import com.jica.sdg.model.EntryShowReport;
import com.jica.sdg.repository.EntryShowReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryShowReportService implements IEntryShowReportService {

    @Autowired
    EntryShowReportRepository entryShowReportRepository;

    @Override
    public List<EntryShowReport> findAll() {
        return (List<EntryShowReport>) entryShowReportRepository.findAll();
    }
}
