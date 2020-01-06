package com.jica.sdg.service;

import com.jica.sdg.model.Menu;

import java.util.List;

public interface IMenuService {

    List<Menu> findAllMenu();
    
    List<Menu> findAllByList(Iterable<Integer> ids);

}
