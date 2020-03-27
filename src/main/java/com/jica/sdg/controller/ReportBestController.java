package com.jica.sdg.controller;

import com.jica.sdg.model.EntryGovIndicator;
import com.jica.sdg.model.EntryNsaIndicator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.Role;
import com.jica.sdg.repository.EntryProblemIdentifyRepository;
import com.jica.sdg.service.EntryProblemIdentifyService;
import com.jica.sdg.service.IEntryApprovalService;
import com.jica.sdg.service.IEntrySdgService;
import com.jica.sdg.service.IGovActivityService;
import com.jica.sdg.service.IGovProgramService;
import com.jica.sdg.service.INsaActivityService;
import com.jica.sdg.service.INsaProgramService;
import com.jica.sdg.service.IProvinsiService;
import com.jica.sdg.service.IRanRadService;
import com.jica.sdg.service.IRoleService;
import com.jica.sdg.service.ISdgIndicatorService;
import com.jica.sdg.service.NsaProfileService;
import com.jica.sdg.service.RanRadService;
import com.jica.sdg.service.SdgFundingService;
import com.jica.sdg.service.SdgGoalsService;
import com.jica.sdg.service.SdgTargetService;
import com.jica.sdg.service.UnitService;
import java.util.Date;
import javax.transaction.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReportBestController {
	
    @Autowired
    IProvinsiService provinsiService;
    
    @Autowired
    IEntrySdgService entrySdgService;
    
    @Autowired
    private EntityManager em;
    
    @Autowired
    EntityManager manager;

    @Autowired
    IRoleService roleService;

    @Autowired
    IRanRadService ranRadService;

    @Autowired
    EntryProblemIdentifyService identifyService;

    @Autowired
    NsaProfileService nsaProfilrService;

    @Autowired
    SdgGoalsService goalsService;

    @Autowired
    EntryProblemIdentifyRepository repository;
    
    @Autowired
    IGovProgramService govProgService;
    
    @Autowired
    IGovActivityService govActService;
    
    @Autowired
    INsaProgramService nsaProgService;
    
    @Autowired
    INsaActivityService nsaActService;
    
    @Autowired
    ISdgIndicatorService sdgIndicatorService;
    
    @Autowired
    SdgGoalsService sdgGoalsService;
    
    @Autowired
    SdgTargetService sdgTargetService;
//    
    @Autowired
    UnitService unitService;
    
    @Autowired
    SdgFundingService sdgFundingService;
    
    @Autowired
    IEntryApprovalService approvalService;
    
    @Autowired
    RanRadService radService;
    

    //entry SDG
    @GetMapping("admin/report-best-practice")
    public String report_best(Model model, HttpSession session) {
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("listrole", roleService.findAll());
        model.addAttribute("listranrad", radService.findAll());
        model.addAttribute("listgoals", goalsService.findAll());

        model.addAttribute("title", "SDG Problem Identification & Follow Up");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/report/best_practice";
    }
    
    @GetMapping("admin/getentryshowreport_best")
    public @ResponseBody Map<String, Object> getentryshowreport_best(@RequestParam("id_monper") String id_monper, @RequestParam("year") String year) {
    	Query query;
//        999999###111111
        if(id_monper.equals("999999")){
            String sql = "select * from entry_show_report where type = 'entry_best_practice' and period = '1' ";
            query = manager.createNativeQuery(sql);
//            query.setParameter("id_monper", id_monper);
//            query.setParameter("year", year);
        }else{
            String sql = "select * from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_best_practice' and period = '1' ";
            query = manager.createNativeQuery(sql);
            query.setParameter("id_monper", id_monper);
            query.setParameter("year", year);
        }
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/getbest_level2/{sdg}")
    public @ResponseBody Map<String, Object> getbest_level2(@RequestParam("id_monper") String id_monper, @RequestParam("year") String year, @RequestParam("id_role") String id_role, @RequestParam("id_prov") String id_prov, @PathVariable("sdg") String sdg) {
    	Query query;
        System.out.println("id_monper = "+id_monper+" year = "+year+" id_prov = "+id_prov+" id_role = "+id_role);
//        131313
        if(sdg.equals("0")) {
            if(id_role.equals("131313")){
                String sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role <> '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper \n" +
                            ")as z\n" +
                            "left join sdg_goals y on z.id_goals = y.id";
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_prov", id_prov);
            }else{
                String sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role <> '999999' and id_role = :id_role and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper \n" +
                            ")as z\n" +
                            "left join sdg_goals y on z.id_goals = y.id";
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_role", id_role);
                query.setParameter("id_prov", id_prov);
    //            System.out.println("sql nya = "+sql);
            }
        }else{
            String[] arrOfStr = sdg.split(","); 
            StringBuffer target = new StringBuffer();
            if(arrOfStr.length>0) {
                for (int i = 0; i < arrOfStr.length; i++) {
                    String[] arrOfStr1 = arrOfStr[i].split("---");
                    int cek=1;
                    for(int j=0;j<arrOfStr1.length;j++) {
                        cek = (cek==4)?1:cek;
                        if(!arrOfStr1[j].equals("0") && cek==1) {
                            target.append("'"+arrOfStr1[j]+"',");
                        }
                        cek = cek+1;
                    }
                }
            }else{
                String[] arrOfStr1 = sdg.split("---");
                int cek=1;
                for(int j=0;j<arrOfStr1.length;j++) {
                    cek = (cek==4)?1:cek;
                    if(!arrOfStr1[j].equals("0") && cek==1) {
                        target.append("'"+arrOfStr1[j]+"',");
                    }
                    cek = cek+1;
                }
            }
            String hasiltarget = (target.length()==0)?"":target.substring(0, target.length() - 1);

            String tar = (hasiltarget.equals(""))?"":" and a.id_goals in("+hasiltarget+") ";
            if(id_role.equals("131313")){
                String sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role <> '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+tar+"\n" +
                            ")as z\n" +
                            "left join sdg_goals y on z.id_goals = y.id";
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_prov", id_prov);
            }else{
                String sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role <> '999999' and id_role = :id_role and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+tar+"\n" +
                            ")as z\n" +
                            "left join sdg_goals y on z.id_goals = y.id";
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_role", id_role);
                query.setParameter("id_prov", id_prov);
    //            System.out.println("sql nya = "+sql);
            }
        }
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/getbest_level3/{sdg}")
    public @ResponseBody Map<String, Object> getbest_level3(@RequestParam("id_monper") String id_monper, @RequestParam("year") String year, @RequestParam("id_role") String id_role, @RequestParam("id_prov") String id_prov, @RequestParam("id_goals") String id_goals, @PathVariable("sdg") String sdg) {
    	Query query;
        System.out.println("id_monper = "+id_monper+" year = "+year+" id_prov = "+id_prov+" id_role = "+id_role);
//        131313
        if(sdg.equals("0")) {
            if(id_role.equals("131313")){
                String sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role <> '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join sdg_target d on a.id_target = d.id\n" +
                            "left join sdg_indicator e on a.id_indicator = e.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            "and a.id_goals = :id_goals ";
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_prov", id_prov);
                query.setParameter("id_goals", id_goals);
            }else{
                String sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role <> '999999' and id_role = :id_role and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join sdg_target d on a.id_target = d.id\n" +
                            "left join sdg_indicator e on a.id_indicator = e.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            "and a.id_goals = :id_goals ";
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_role", id_role);
                query.setParameter("id_prov", id_prov);
                query.setParameter("id_goals", id_goals);
    //            System.out.println("sql nya = "+sql);
            }
    	}else {
            String[] arrOfStr = sdg.split(","); 
            StringBuffer target = new StringBuffer();
            if(arrOfStr.length>0) {
                for (int i = 0; i < arrOfStr.length; i++) {
                    String[] arrOfStr1 = arrOfStr[i].split("---");
                    int cek=1;
                    for(int j=0;j<arrOfStr1.length;j++) {
                        cek = (cek==4)?1:cek;
                        if(!arrOfStr1[j].equals("0") && cek==2) {
                            target.append("'"+arrOfStr1[j]+"',");
                        }
                        cek = cek+1;
                    }
                }
            }else{
                String[] arrOfStr1 = sdg.split("---");
                int cek=1;
                for(int j=0;j<arrOfStr1.length;j++) {
                    cek = (cek==4)?1:cek;
                    if(!arrOfStr1[j].equals("0") && cek==2) {
                        target.append("'"+arrOfStr1[j]+"',");
                    }
                    cek = cek+1;
                }
            }
            String hasiltarget = (target.length()==0)?"":target.substring(0, target.length() - 1);

            String tar = (hasiltarget.equals(""))?"":" and a.id_target in("+hasiltarget+") ";
            if(id_role.equals("131313")){
                String sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role <> '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join sdg_target d on a.id_target = d.id\n" +
                            "left join sdg_indicator e on a.id_indicator = e.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            "and a.id_goals = :id_goals "+tar;
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_prov", id_prov);
                query.setParameter("id_goals", id_goals);
            }else{
                String sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role <> '999999' and id_role = :id_role and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join sdg_target d on a.id_target = d.id\n" +
                            "left join sdg_indicator e on a.id_indicator = e.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            "and a.id_goals = :id_goals "+tar;
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_role", id_role);
                query.setParameter("id_prov", id_prov);
                query.setParameter("id_goals", id_goals);
    //            System.out.println("sql nya = "+sql);
            }
        }
        
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
}
