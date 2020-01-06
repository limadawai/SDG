package com.jica.sdg.service;

import com.jica.sdg.model.Menu;
import com.jica.sdg.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService implements IMenuService {

    @Autowired
    private MenuRepository repository;

    @Override
    public List<Menu> findAllMenu() {
        List menu = (List<Menu>) repository.findAllMenu();
        return menu;
    }

	@Override
	public List<Menu> findAllByList(Iterable<Integer> ids) {
		List menu = (List<Menu>) repository.findAllById(ids);
		return menu;
	}
}
