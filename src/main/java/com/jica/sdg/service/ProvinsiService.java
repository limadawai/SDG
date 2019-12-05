package com.jica.sdg.service;

import com.jica.sdg.model.Provinsi;
import com.jica.sdg.repository.ProvinsiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinsiService implements IProvinsiService{

    @Autowired
    private ProvinsiRepository repository;

    public List<Provinsi> findAllProvinsi() {
        List prov = (List<Provinsi>) repository.findAllProvinsi();
        return prov;
    }
}
