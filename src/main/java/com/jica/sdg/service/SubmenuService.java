package com.jica.sdg.service;

import com.jica.sdg.model.Submenu;
import com.jica.sdg.repository.SubmenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmenuService implements ISubmenuService {

    @Autowired
    private SubmenuRepository repository;

    @Override
    public List<Submenu> findSubmenu(int id) {
        List submenu = (List<Submenu>) repository.findSubmenu(id);
        return submenu;
    }
}
