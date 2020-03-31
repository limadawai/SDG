package com.jica.sdg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jica.sdg.model.EntryApproval;
import com.jica.sdg.model.EntryShowReport;
import com.jica.sdg.model.SdgDisaggre;
import com.jica.sdg.model.SdgDisaggreDetail;
import com.jica.sdg.repository.EntryApprovalRepository;
import com.jica.sdg.repository.EntryShowReportRepository;
import com.jica.sdg.repository.SdgDisaggreDetailRepository;
import com.jica.sdg.repository.SdgDisaggreRepository;

@Service
public class EntryApprovalService implements IEntryApprovalService{
	
	@Autowired
	EntryApprovalRepository repo;
	
	@Autowired
	EntryShowReportRepository repo_report;

	@Override
	public List<EntryApproval> findAll() {
		return (List<EntryApproval>) repo.findAll();
	}

	@Override
	public void save(EntryApproval app) {
		repo.save(app);
	}

	@Override
	public Optional<EntryApproval> findOne(Integer id) {
		return (Optional<EntryApproval>) repo.findById(id);
	}

	@Override
	public void deleteEntryApproval(Integer id) {
		repo.deleteById(id);
	}

	@Override
	public void updateApproval(String approval, String description, Integer id) {
		repo.updateApproval(approval, description, id);
	}

	@Override
	public void deleteApproveGovBudget(Integer id_role, Integer id_monper, Integer year, String type, String periode) {
		repo.deleteApproval(id_role, id_monper, year, type, periode);
	}

	@Override
	public void updatedoneApproveGovBudget(Integer id_monper, Integer year, String type, String periode) {
		repo.updatedoneApproval(id_monper, year, type, periode);
	}

	@Override
	public void updateundoneApproveGovBudget(Integer id_monper, Integer year, String type, String periode) {
		repo.updateUNdoneApproval(id_monper, year, type, periode);
	}

	@Override
	public void saveshow(EntryShowReport app1) {
		repo_report.save(app1);
	}

	@Override
	public void deleteshow(Integer year, Integer id_monper, String type, String periode) {
		repo_report.deleteShowreport(year, id_monper, type, periode);
	}

	@Override
	public List<EntryApproval> getAllMessage() {
		return repo.getAllMessage();
	}

	@Override
	public List<EntryApproval> getMessageByRole(Integer id_role) {
		return repo.getMessageByRole(id_role);
	}

	@Override
	public List<EntryApproval> getMessageByProv(Integer id_prov) {
		return repo.getMessageByProv(id_prov);
	}
	
}
