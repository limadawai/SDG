package com.jica.sdg.repository;

import com.jica.sdg.model.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends CrudRepository<Menu, Integer> {

    @Query(value = "select * from menu", nativeQuery = true)
    List<Menu> findAllMenu();

}
