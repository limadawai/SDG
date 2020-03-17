package com.jica.sdg.controller;

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
import com.jica.sdg.service.SdgFundingService;
import com.jica.sdg.service.SdgGoalsService;
import com.jica.sdg.service.SdgTargetService;
import com.jica.sdg.service.UnitService;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RateController {
	
    @Autowired
    IProvinsiService provinsiService;
    
    @Autowired
    IEntrySdgService entrySdgService;
    
    @Autowired
    private EntityManager em;

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

    //entry SDG
    @GetMapping("admin/rate")
    public String entri_sdg(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Rate");
        
        Integer id_role = (Integer) session.getAttribute("id_role");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("id_role", session.getAttribute("id_role"));
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov      = list.get().getId_prov();
    	String privilege    = list.get().getPrivilege();
    	if(id_prov.equals("000")) {
            model.addAttribute("listprov", provinsiService.findAllProvinsi());
    	}else {
            Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
            list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
    	}
    	model.addAttribute("listNsaProfile", roleService.findByProvince(id_prov));
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);
        return "admin/rate/rate_index";
    }
    
    @GetMapping("admin/x/val/{id_prov}/{period}/{id_monper}/{year}/{id_role}")
    public @ResponseBody Map<String, Object>  val_rate_sdg(@PathVariable("id_prov") String id_prov, @PathVariable("period") String period, @PathVariable("id_monper") String id_monper,@PathVariable("year") String year, @PathVariable("id_role") String id_role) {
        Query query = em.createNativeQuery("");
//        System.out.println("id "+id_role);
        if(id_role.equals("999999")){
            String sql  = "select '00000' as id_role, 'Government' as nm_role, 'Government' as cat_role, '1' as kode, \n" +
                        "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_gov_indicator' and period = :period) as show_report \n" +
                        "union all\n" +
                        "select a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report\n" +
                        "from ref_role a\n" +
                        "where a.cat_role = 'Government' and a.id_role <> '1' and a.id_prov = :id_prov \n" +
                        "union all\n" +
                        "select '11111' as id, 'Non Government' as nm, 'NSA' as ket, '1' as kode, \n" +
                        "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_gov_indicator' and period = :period) as show_report \n" +
                        "union all\n" +
                        "select a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report \n" +
                        "from ref_role a\n" +
                        "where a.cat_role = 'NSA' and a.id_role <> '1' and a.id_prov = :id_prov ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_prov", id_prov);
            query.setParameter("period", period);
            query.setParameter("id_monper", id_monper);
            query.setParameter("year", year);
        }else{
            String sql  = "select '00000' as id_role, 'Government' as nm_role, 'Government' as cat_role, '1' as kode, \n" +
                        "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_gov_indicator' and period = :period) as show_report \n" +
                        "union all\n" +
                        "select a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report\n" +
                        "from ref_role a\n" +
                        "where a.cat_role = 'Government' and a.id_prov = :id_prov and a.id_role = :id_role \n" +
                        "union all\n" +
                        "select '11111' as id, 'Non Government' as nm, 'NSA' as ket, '1' as kode, \n" +
                        "(select count(*) as nn from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_gov_indicator' and period = :period) as show_report \n" +
                        "union all\n" +
                        "select a.id_role, a.nm_role, a.cat_role, '2' as kode, '111' as show_report \n" +
                        "from ref_role a\n" +
                        "where a.cat_role = 'NSA' and a.id_prov = :id_prov and a.id_role = :id_role ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_prov", id_prov);
            query.setParameter("period", period);
            query.setParameter("id_monper", id_monper);
            query.setParameter("year", year);
            query.setParameter("id_role", id_role);
        }
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    @GetMapping("admin/get-cek-app/{id_role}/{id_monper}/{year}/{period}")
    public @ResponseBody Map<String, Object>  cek_app(@PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper,@PathVariable("year") String year, @PathVariable("period") String period) {
        String sql  = "select count(*) as cek from entry_approval where (type = 'entry_gov_indicator' or type = 'entry_nsa_indicator') and id_role = :id_role and id_monper = :id_monper and year = :year and periode = :period ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_role", id_role);
        query.setParameter("period", period);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    @GetMapping("admin/get-cek-sho/{id_monper}/{year}/{period}/{type}")
    public @ResponseBody Map<String, Object>  cek_sho(@PathVariable("id_monper") String id_monper,@PathVariable("year") String year, @PathVariable("period") String period, @PathVariable("type") String type) {
        String sql  = "select count(*) as cek from entry_show_report where id_monper = :id_monper and year = :year and type = :type and period = :period ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("period", period);
        query.setParameter("id_monper", id_monper);
        query.setParameter("year", year);
        query.setParameter("type", type);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    @GetMapping("admin/get-cek-data-all/{id_role}/{year}/{period}/{type}/{tb}/{tb2}/{isi_time}")
    public @ResponseBody Map<String, Object>  cek_data_all(@PathVariable("id_role") String id_role,@PathVariable("year") String year, @PathVariable("period") String period, @PathVariable("type") String type, @PathVariable("tb") String tb, @PathVariable("tb2") String tb2, @PathVariable("isi_time") String isi_time) {
        String tg_date = "";
        if(period.equals("1")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created <= '"+isi_time+"' ";
            }
        }else if(period.equals("2")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created2 <= '"+isi_time+"' ";
            }
        }else if(period.equals("3")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created3 <= '"+isi_time+"' ";
            }
        }else if(period.equals("4")){
            if(isi_time.equals("777777")){
                tg_date = "";
            }else{
                tg_date = "and date_created4 <= '"+isi_time+"' ";
            }
        }else{}
        String sql  = "select \n" +
                    "(\n" +
                    "select count(*) as total_all from\n" +
                    "(\n" +
                    "select b. id, b.id_activity, c.id_role, b.nm_indicator, b.nm_indicator_eng, d.achievement"+period+" \n" +
                    "from "+tb2+" b\n" +
                    "left join "+tb+" c on b.id_activity = c.id\n" +
                    "inner join (select * from "+type+" where year_entry = :year and achievement"+period+" != 0 "+tg_date+" ) d on b.id = d.id_assign\n" +
                    "where c.id_role = :id_role \n" +
                    ") as a\n" +
                    ") as isi,\n" +
                    "(\n" +
                    "select count(*) as total_all from\n" +
                    "(\n" +
                    "select b. id, b.id_activity, c.id_role, b.nm_indicator, b.nm_indicator_eng, d.achievement"+period+" \n" +
                    "from "+tb2+" b\n" +
                    "left join "+tb+" c on b.id_activity = c.id\n" +
                    "left join (select * from "+type+" where year_entry = :year "+tg_date+") d on b.id = d.id_assign\n" +
                    "where c.id_role = :id_role\n" +
                    ") as a\n" +
                    ") as semua";
        Query query = em.createNativeQuery(sql);
//        query.setParameter("period", period);
        query.setParameter("id_role", id_role);
        query.setParameter("year", year);
//        query.setParameter("type", type);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }  
    
    @GetMapping("admin/get-cek-data-all-deadline/{id_role}/{year}/{period}/{type}/{tb}/{tb2}/{sts}/{isi_time}")
    public @ResponseBody Map<String, Object>  cek_data_all_deadline(@PathVariable("id_role") String id_role,@PathVariable("year") int year, @PathVariable("period") String period, @PathVariable("type") String type, @PathVariable("tb") String tb, @PathVariable("tb2") String tb2, @PathVariable("sts") String sts_monper, @PathVariable("isi_time") String isi_time) {
        String tg_date = "";
        if(sts_monper.equals("yearly")){
            if(period.equals("1")){
                tg_date = "and date_created <= '"+(year+1)+"-01-15' ";
            }else{}
        }else if(sts_monper.equals("semesterly")){
            if(period.equals("1")){
                tg_date = "and date_created <= '"+year+"-07-15' ";
            }else if(period.equals("2")){
                tg_date = "and date_created2 <= '"+(year+1)+"-01-15' ";
            }else {}
        }else if(sts_monper.equals("quarterly")){
            if(period.equals("1")){
                tg_date = "and date_created <= '"+year+"-04-15' ";
            }else if(period.equals("2")){
                tg_date = "and date_created2 <= '"+year+"-07-15' ";
            }else if(period.equals("3")){
                tg_date = "and date_created3 <= '"+year+"-10-15' ";
            }else if(period.equals("4")){
                tg_date = "and date_created4 <= '"+(year+1)+"-01-15' ";
            }else{}
        }else{}
        
        String tg_date_1 = "";
        if(period.equals("1")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created <= '"+isi_time+"' ";
            }
        }else if(period.equals("2")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created2 <= '"+isi_time+"' ";
            }
        }else if(period.equals("3")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created3 <= '"+isi_time+"' ";
            }
        }else if(period.equals("4")){
            if(isi_time.equals("777777")){
                tg_date_1 = "";
            }else{
                tg_date_1 = "and date_created4 <= '"+isi_time+"' ";
            }
        }else{}
        System.out.println("tgdate = "+tg_date);
        String sql  = "select \n" +
                    "(\n" +
                    "select count(*) as total_all from\n" +
                    "(\n" +
                    "select b. id, b.id_activity, c.id_role, b.nm_indicator, b.nm_indicator_eng, d.achievement"+period+" \n" +
                    "from "+tb2+" b\n" +
                    "left join "+tb+" c on b.id_activity = c.id\n" +
                    "inner join (select * from "+type+" where year_entry = :year "+tg_date+" "+tg_date_1+" ) d on b.id = d.id_assign\n" +
                    "where c.id_role = :id_role \n" +
                    ") as a\n" +
                    ") as isi,\n" +
                    "(\n" +
                    "select count(*) as total_all from\n" +
                    "(\n" +
                    "select b. id, b.id_activity, c.id_role, b.nm_indicator, b.nm_indicator_eng, d.achievement"+period+" \n" +
                    "from "+tb2+" b\n" +
                    "left join "+tb+" c on b.id_activity = c.id\n" +
                    "left join (select * from "+type+" where year_entry = :year "+tg_date_1+") d on b.id = d.id_assign\n" +
                    "where c.id_role = :id_role\n" +
                    ") as a\n" +
                    ") as semua";
        Query query = em.createNativeQuery(sql);
//        query.setParameter("period", period);
        query.setParameter("id_role", id_role);
        query.setParameter("year", year);
//        query.setParameter("type", type);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    @GetMapping("admin/get_sub_prog_level1/{id_monper}/{id_role}/{catrole}")
    public @ResponseBody Map<String, Object>  get_sub_prog_level1(@PathVariable("id_monper") String id_monper,@PathVariable("id_role") String id_role, @PathVariable("catrole") String catrole) {
        System.out.println("catrole = "+catrole+", id_monper = "+id_monper);
        Query query = em.createNativeQuery("");
        if(catrole.equals("Government")){
            String sql  = "select distinct a.id, a.nm_program, a.nm_program_eng from gov_program a \n" +
                        "left join gov_activity b on a.id = b.id_program\n" +
                        "where a.id_monper = :id_monper and b.id_role = :id_role ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_role", id_role);
            query.setParameter("id_monper", id_monper);
        }else if(catrole.equals("NSA")){
            String sql  = "select a.id, a.nm_program, a.nm_program_eng from nsa_program a \n" +
                        "where a.id_monper = :id_monper and a.id_role = :id_role ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_role", id_role);
            query.setParameter("id_monper", id_monper);
        }else{}
        
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    @GetMapping("admin/get_sub_prog_level2/{id_program}/{id_role}/{catrole}")
    public @ResponseBody Map<String, Object>  get_sub_prog_level2(@PathVariable("id_program") String id_program, @PathVariable("id_role") String id_role, @PathVariable("catrole") String catrole) {
        
        Query query = em.createNativeQuery("");
        if(catrole.equals("Government")){
            String sql  = "select a.id, a.nm_activity, a.nm_activity_eng from gov_activity a\n" +
                        "where a.id_program = :id_program and a.id_role = :id_role ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_role", id_role);
            query.setParameter("id_program", id_program);
        }else if(catrole.equals("NSA")){
            String sql  = "select a.id, a.nm_activity, a.nm_activity_eng from nsa_activity a\n" +
                        "where a.id_program = :id_program and a.id_role = :id_role ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_role", id_role);
            query.setParameter("id_program", id_program);
        }else{}
        
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    @GetMapping("admin/get_sub_prog_level3/{id_program}/{id_activity}/{id_role}/{catrole}/{period}/{id_monper}/{year}")
    public @ResponseBody Map<String, Object>  get_sub_prog_level3(@PathVariable("id_program") String id_program, @PathVariable("id_activity") String id_activity, @PathVariable("id_role") String id_role, @PathVariable("catrole") String catrole, @PathVariable("period") String period, @PathVariable("id_monper") String id_monper, @PathVariable("year") String year) {
        
        Query query = em.createNativeQuery("");
        if(catrole.equals("Government")){
            String sql  = "select a.id, a.nm_indicator, a.nm_indicator_eng, \n" +
                        "(SELECT nm_unit FROM ref_unit WHERE id_unit = a.unit) as nama_unit, \n" +
                        "c.id as id_entry, case when (c.achievement1 is null) then 0 else c.achievement1 end, case when (c.achievement2 is null) then 0 else c.achievement2 end, case when (c.achievement3 is null) then 0 else c.achievement3 end, case when (c.achievement4 is null) then 0 else c.achievement4 end\n" +
                        "from gov_indicator a\n" +
                        "left join gov_activity b on a.id_activity = b.id\n" +
                        "left join (select * from entry_gov_indicator where year_entry = :year and id_monper = :id_monper) c on a.id = c.id_assign\n" +
                        "where a.id_program = :id_program and a.id_activity = :id_activity and b.id_role = :id_role ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_role", id_role);
            query.setParameter("id_program", id_program);
            query.setParameter("id_activity", id_activity);
            query.setParameter("year", year);
            query.setParameter("id_monper", id_monper);
        }else if(catrole.equals("NSA")){
            String sql  = "select a.id, a.nm_indicator, a.nm_indicator_eng, \n" +
                        "(SELECT nm_unit FROM ref_unit WHERE id_unit = a.unit) as nama_unit,\n" +
                        "c.id as id_entry, case when (c.achievement1 is null) then 0 else c.achievement1 end, case when (c.achievement2 is null) then 0 else c.achievement2 end, case when (c.achievement3 is null) then 0 else c.achievement3 end, case when (c.achievement4 is null) then 0 else c.achievement4 end\n" +
                        "from nsa_indicator a\n" +
                        "left join nsa_activity b on a.id_activity = b.id\n" +
                        "left join (select * from entry_nsa_indicator where year_entry = :year and id_monper = :id_monper) c on a.id = c.id_assign\n" +
                        "where a.id_program = :id_program and a.id_activity = :id_activity and b.id_role = :id_role ";
            query = em.createNativeQuery(sql);
            query.setParameter("id_role", id_role);
            query.setParameter("id_program", id_program);
            query.setParameter("id_activity", id_activity);
            query.setParameter("year", year);
            query.setParameter("id_monper", id_monper);
        }else{}
        
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        
        hasil.put("content",list);
        return hasil;
    }    
    
    @PostMapping(path = "admin/save-submission/{dat_id_indicator}/{dat_achievement}", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public void saveBest(@PathVariable("dat_id_indicator") String dat_id_indicator,
			@PathVariable("dat_achievement") String dat_achievement) {
        System.out.println("data "+dat_id_indicator+" - "+dat_achievement);
//        if(!sdg_indicator.equals("0")) {
//            bestMapService.deleteGovMapByGovInd(best.getId());
//            String[] sdg = sdg_indicator.split(",");
//            for(int i=0;i<sdg.length;i++) {
//                String[] a = sdg[i].split("---");
//                Integer id_goals = Integer.parseInt(a[0]);
//                Integer id_target = Integer.parseInt(a[1]);
//                Integer id_indicator = Integer.parseInt(a[2]);
//                BestMap map = new BestMap();
//                map.setId_goals(id_goals);
//                if(id_target!=0) {
//                        map.setId_target(id_target);
//                }
//                if(id_indicator!=0) {
//                        map.setId_indicator(id_indicator);
//                }
//                map.setId_best_practice(best.getId());
//                map.setId_monper(id_monper);
//                map.setId_prov(id_prov);
//                bestMapService.saveGovMap(map);
//            }
//    	}
    }
    
}
