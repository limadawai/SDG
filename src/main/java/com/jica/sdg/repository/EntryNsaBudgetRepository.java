package com.jica.sdg.repository;

import com.jica.sdg.model.EntryNsaBudget;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

public interface EntryNsaBudgetRepository extends CrudRepository<EntryNsaBudget, Integer> {

    
}
