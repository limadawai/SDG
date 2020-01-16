package com.jica.sdg.service;

import com.jica.sdg.model.Submenu;

import java.util.List;
import java.util.Set;

public interface ISubmenuService {

    List<Submenu> findSubmenu(int id);
    
    List<Submenu> findSubmenuByRole(int id, List<String> ids);
}
