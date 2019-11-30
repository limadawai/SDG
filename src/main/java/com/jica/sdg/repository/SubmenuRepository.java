package com.jica.sdg.repository;

import com.jica.sdg.model.Submenu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmenuRepository extends CrudRepository<Submenu, Long> {

    @Query(value = "select * from submenu where id_menu=?1", nativeQuery = true)
    List<Submenu> findSubmenu(int idmenu);

}
