package com.jica.sdg.repository;

import com.jica.sdg.model.Submenu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SubmenuRepository extends CrudRepository<Submenu, Long> {

    @Query(value = "select * from submenu where id_menu=?1", nativeQuery = true)
    List<Submenu> findSubmenu(int idmenu);
    
    @Query(value = "select * from submenu where id_menu = :id and id_submenu in :ids ", nativeQuery = true)
    List<Submenu> findSubmenuByRole(@Param("id") int id, @Param("ids") List<String> ids);

}
