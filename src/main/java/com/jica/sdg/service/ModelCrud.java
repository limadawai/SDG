/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jica.sdg.service;

import com.jica.sdg.model.RefCategory;
import com.jica.sdg.model.SdgGoals;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Etrio Widodo
 */
@Service
public class ModelCrud {
    private String sql;
    private List<Object[]> rows;
    private Map<String, Object> hasil;
    private List<Class> result;
    @Autowired
    private EntityManager em;
    
    public  Map<String, Object> getRefCategory(){  
        String sql = "select * from ref_category";
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<RefCategory> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(new RefCategory((String)row[0], (String) row[1]));
        };
        hasil.put("content",result);
        return hasil;
    }
    public  Map<String, Object> getRefCategory(String sql){  
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<RefCategory> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(new RefCategory((String)row[0], (String) row[1]));
        };
        hasil.put("content",result);
        return hasil;
    }
    
    public void hallo(){
        System.out.println("hallo");
    }
    
    public Map<String, Object> getMap(Map<String, Object> get){
        this.hasil= get;
        return this.hasil;
    }
    
    
    
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Object[]> getRows() {
        return rows;
    }

    public void setRows(List<Object[]> rows) {
        this.rows = rows;
    }

    public Map<String, Object> getHasil() {
        return hasil;
    }

    public void setHasil(Map<String, Object> hasil) {
        this.hasil = hasil;
    }

    public List<Class> getResult() {
        return result;
    }

    public void setResult(List<Class> result) {
        this.result = result;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
    
    
    
    
}
