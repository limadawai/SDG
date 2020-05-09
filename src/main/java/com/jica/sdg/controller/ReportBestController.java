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
import com.jica.sdg.model.RanRad;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
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
    
    @GetMapping("admin/getbest_level2_ins/{sdg}")
    public @ResponseBody Map<String, Object> getbest_level2_ins(@RequestParam("id_monper") String id_monper, @RequestParam("year") String year, @RequestParam("id_role") String id_role, @RequestParam("id_prov") String id_prov, @PathVariable("sdg") String sdg) {
    	Query query;
        String sql = "";
        if(sdg.equals("0")) {
        	if(id_role.equals("131313")){
            	sql  = "select DISTINCT c.id_role, c.nm_role " +
                        "from best_map a " +
                        "join best_practice b on a.id_best_practice = b.id and b.id_role <> '999999' and b.id_monper = a.id_monper and b.year = :year "+
                        "join entry_approval d on a.id_monper = d.id_monper and d.year = b.year and d.type = 'entry_best_practice' and d.periode = '1' and d.approval != 3 " +
                        "left join ref_role c on c.id_role = d.id_role "+
                        "where a.id_prov = :id_prov and a.id_monper = :id_monper ";
            	query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_prov", id_prov);
            }else {
            	sql  = "select DISTINCT c.id_role, c.nm_role " +
                        "from best_map a " +
                        "join best_practice b on a.id_best_practice = b.id and b.id_role =:id_role and b.id_role <> '999999' and b.id_monper = a.id_monper and b.year = :year "+
                        "join entry_approval d on a.id_monper = d.id_monper and d.year = b.year and d.type = 'entry_best_practice' and d.periode = '1' and d.approval != 3 " +
                        "left join ref_role c on c.id_role = d.id_role " +
                        "where a.id_prov = :id_prov and a.id_monper = :id_monper and c.id_role = :id_role ";
            	query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_role", id_role);
                query.setParameter("id_prov", id_prov);
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
            	sql  = "select DISTINCT c.id_role, c.nm_role " +
                        "from best_map a " +
                        "join best_practice b on a.id_best_practice = b.id and b.id_role <> '999999' and b.id_monper = a.id_monper and b.year = :year "+
                        "join entry_approval d on a.id_monper = d.id_monper and d.year = b.year and d.type = 'entry_best_practice' and d.periode = '1' and d.approval != 3 " +
                        "left join ref_role c on c.id_role = d.id_role "+
                        "where a.id_prov = :id_prov and a.id_monper = :id_monper "+tar;
            	query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_prov", id_prov);
            }else {
            	sql  = "select DISTINCT c.id_role, c.nm_role " +
                        "from best_map a " +
                        "join best_practice b on a.id_best_practice = b.id and b.id_role =:id_role and b.id_role <> '999999' and b.id_monper = a.id_monper and b.year = :year "+
                        "join entry_approval d on a.id_monper = d.id_monper and d.year = b.year and d.type = 'entry_best_practice' and d.periode = '1' and d.approval != 3 " +
                        "left join ref_role c on c.id_role = d.id_role " +
                        "where a.id_prov = :id_prov and a.id_monper = :id_monper and c.id_role = :id_role "+tar;
            	query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_role", id_role);
                query.setParameter("id_prov", id_prov);
            }
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
        Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = monper.get().getStatus();
        String sql = "";
//        131313
        if(sdg.equals("0")) {
            if(id_role.equals("131313")){
                if(status.equals("completed")) {
                    sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "left join best_practice b on b.id_role <> '999999' and b.id_monper = :id_monper and b.year = :year and a.id_best_practice = b.id\n" +
                            "left join entry_approval d on b.id_monper = d.id_monper and d.year = b.year and d.type = 'entry_best_practice' and d.periode = '1' and b.id_role = d.id_role "+
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper and d.approval != 3 \n" +
                            ")as z\n" +
                            "left join (select * from history_sdg_goals where id_monper = '"+id_monper+"' ) y on z.id_goals = y.id_old";
                }else{
                    sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "left join best_practice b on b.id_role <> '999999' and b.id_monper = :id_monper and b.year = :year and a.id_best_practice = b.id\n" +
                            "left join entry_approval d on b.id_monper = d.id_monper and d.year = b.year and d.type = 'entry_best_practice' and d.periode = '1' and b.id_role = d.id_role "+
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper and d.approval != 3 \n" +
                            ")as z\n" +
                            "left join sdg_goals y on z.id_goals = y.id ";
                }
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_prov", id_prov);
            }else{
                if(status.equals("completed")) {
                    sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "left join best_practice b on b.id_role <> '999999' and b.id_monper = :id_monper and b.year = :year and a.id_best_practice = b.id\n" +
                            "left join entry_approval d on b.id_monper = d.id_monper and d.year = b.year and d.type = 'entry_best_practice' and d.periode = '1' and b.id_role = d.id_role "+
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper and d.approval != 3 and b.id_role = :id_role \n" +
                            ")as z\n" +
                            "left join (select * from history_sdg_goals where id_monper = '"+id_monper+"' ) y on z.id_goals = y.id_old";
                }else{
                    sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "left join best_practice b on b.id_role <> '999999' and b.id_monper = :id_monper and b.year = :year and a.id_best_practice = b.id\n" +
                            "left join entry_approval d on b.id_monper = d.id_monper and d.year = b.year and d.type = 'entry_best_practice' and d.periode = '1' and b.id_role = d.id_role "+
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper and d.approval != 3 and b.id_role = :id_role \n" +
                            ")as z\n" +
                            "left join sdg_goals y on z.id_goals = y.id";
                }
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
                if(status.equals("completed")) {
                    sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "left join best_practice b on b.id_role <> '999999' and b.id_monper = :id_monper and b.year = :year and a.id_best_practice = b.id\n" +
                            "left join entry_approval d on b.id_monper = d.id_monper and d.year = b.year and d.type = 'entry_best_practice' and d.periode = '1' and b.id_role = d.id_role "+
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper and d.approval != 3 "+tar+" \n" +
                            ")as z\n" +
                            "left join (select * from history_sdg_goals where id_monper = '"+id_monper+"' ) y on z.id_goals = y.id_old";
                }else{
                    sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "left join best_practice b on b.id_role <> '999999' and b.id_monper = :id_monper and b.year = :year and a.id_best_practice = b.id\n" +
                            "left join entry_approval d on b.id_monper = d.id_monper and d.year = b.year and d.type = 'entry_best_practice' and d.periode = '1' and b.id_role = d.id_role "+
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper and d.approval != 3 "+tar+"\n" +
                            ")as z\n" +
                            "left join sdg_goals y on z.id_goals = y.id";
                }
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_prov", id_prov);
            }else{
                if(status.equals("completed")) {
                    sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "left join best_practice b on b.id_role <> '999999' and b.id_monper = :id_monper and b.year = :year and a.id_best_practice = b.id\n" +
                            "left join entry_approval d on b.id_monper = d.id_monper and d.year = b.year and d.type = 'entry_best_practice' and d.periode = '1' and b.id_role = d.id_role "+
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper and d.approval != 3 and b.id_role = :id_role "+tar+" \n" +
                            ")as z\n" +
                            "left join (select * from history_sdg_goals where id_monper = '"+id_monper+"' ) y on z.id_goals = y.id_old";
                }else{
                    sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "left join best_practice b on b.id_role <> '999999' and b.id_monper = :id_monper and b.year = :year and a.id_best_practice = b.id\n" +
                            "left join entry_approval d on b.id_monper = d.id_monper and d.year = b.year and d.type = 'entry_best_practice' and d.periode = '1' and b.id_role = d.id_role "+
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper and d.approval != 3 and b.id_role = :id_role "+tar+" \n" +
                            ")as z\n" +
                            "left join sdg_goals y on z.id_goals = y.id";
                }
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
    
    @GetMapping("admin/getbest_level2_sum/{sdg}")
    public @ResponseBody Map<String, Object> getbest_level2_sum(@RequestParam("id_monper") String id_monper, @RequestParam("year") String year, @RequestParam("id_role") String id_role, @RequestParam("id_prov") String id_prov, @PathVariable("sdg") String sdg) {
    	Query query;
        System.out.println("id_monper = "+id_monper+" year = "+year+" id_prov = "+id_prov+" id_role = "+id_role);
        Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = monper.get().getStatus();
        String sql = "";
//        131313
        if(sdg.equals("0")) {
            if(id_role.equals("131313")){
                if(status.equals("completed")) {
                    sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper \n" +
                            ")as z\n" +
                            "left join (select * from history_sdg_goals where id_monper = '"+id_monper+"' ) y on z.id_goals = y.id_old ";
                }else{
                    sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper \n" +
                            ")as z\n" +
                            "left join sdg_goals y on z.id_goals = y.id";
                }
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_prov", id_prov);
            }else{
                if(status.equals("completed")) {
                    sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper \n" +
                            ")as z\n" +
                            "left join (select * from history_sdg_goals where id_monper = '"+id_monper+"' ) y on z.id_goals = y.id_old";
                }else{
                    sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper \n" +
                            ")as z\n" +
                            "left join sdg_goals y on z.id_goals = y.id";
                }
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
//                query.setParameter("id_role", id_role);
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
                if(status.equals("completed")) {
                    sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+tar+"\n" +
                            ")as z\n" +
                            "left join (select * from history_sdg_goals where id_monper = '"+id_monper+"' ) y on z.id_goals = y.id_old";
                }else{
                    sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+tar+"\n" +
                            ")as z\n" +
                            "left join sdg_goals y on z.id_goals = y.id";
                }
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_prov", id_prov);
            }else{
                if(status.equals("completed")) {
                    sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+tar+"\n" +
                            ")as z\n" +
                            "left join (select * from history_sdg_goals where id_monper = '"+id_monper+"' ) y on z.id_goals = y.id_old";
                }else{
                    sql  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                            "(\n" +
                            "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+tar+"\n" +
                            ")as z\n" +
                            "left join sdg_goals y on z.id_goals = y.id";
                }
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
//                query.setParameter("id_role", id_role);
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
        Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = monper.get().getStatus();
    	String goals = "";
    	if(id_goals.equals("null")) {
    		goals = " and a.id_goals is null ";
    	}else {
    		goals = " and a.id_goals = '"+id_goals+"' ";
    	}
        
//        String sqlcek  = "select count(*) as total from entry_approval where id_role = '8' and id_monper = '1' and year = '2020' and type = 'entry_best_practice' and periode = '1' and approval = '3'";
        
        String sql = "";
//        131313
        if(sdg.equals("0")) {
            if(id_role.equals("131313")){
                if(status.equals("completed")) {
                    sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role <> '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join (select * from history_sdg_target where id_monper = '"+id_monper+"') d on a.id_target = d.id_old\n" +
                            "left join (select * from history_sdg_indicator where id_monper = '"+id_monper+"') e on a.id_indicator = e.id_old\n "+
                            " join entry_approval f on b.id_monper = f.id_monper and f.year = b.year and f.type = 'entry_best_practice' and f.periode = '1' and b.id_role = f.id_role and f.approval != 3 " +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            goals+" order by c.nm_role";
                }else{
                    sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role <> '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join sdg_target d on a.id_target = d.id\n" +
                            "left join sdg_indicator e on a.id_indicator = e.id\n" +
                            " join entry_approval f on b.id_monper = f.id_monper and f.year = b.year and f.type = 'entry_best_practice' and f.periode = '1' and b.id_role = f.id_role and f.approval != 3 " +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            goals+" order by c.nm_role";
                }
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_prov", id_prov);
            }else{
                if(status.equals("completed")) {
                    sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role <> '999999' and id_role = :id_role and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join (select * from history_sdg_target where id_monper = '"+id_monper+"') d on a.id_target = d.id_old\n" +
                            "left join (select * from history_sdg_indicator where id_monper = '"+id_monper+"') e on a.id_indicator = e.id_old\n" +
                            " join entry_approval f on b.id_monper = f.id_monper and f.year = b.year and f.type = 'entry_best_practice' and f.periode = '1' and b.id_role = f.id_role and f.approval != 3 " +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            goals+" ";
                }else{
                    sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role <> '999999' and id_role = :id_role and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join sdg_target d on a.id_target = d.id\n" +
                            "left join sdg_indicator e on a.id_indicator = e.id\n" +
                            " join entry_approval f on b.id_monper = f.id_monper and f.year = b.year and f.type = 'entry_best_practice' and f.periode = '1' and b.id_role = f.id_role and f.approval != 3 " +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            goals+" ";
                }
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_role", id_role);
                query.setParameter("id_prov", id_prov);
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
                if(status.equals("completed")) {
                    sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                            "from best_map a\n" +
                            "inner join (select l.* from best_practice l inner join (select * from entry_approval where id_monper = :id_monper and year = :year and type = 'entry_best_practice' and periode = '1' and approval <> '3') b on l.id_role = b.id_role where l.id_role <> '999999' and l.id_monper = :id_monper and l.year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join (select * from history_sdg_target where id_monper = '"+id_monper+"') d on a.id_target = d.id_old\n" +
                            "left join (select * from history_sdg_indicator where id_monper = '"+id_monper+"') e on a.id_indicator = e.id_old\n" +
                            " join entry_approval f on b.id_monper = f.id_monper and f.year = b.year and f.type = 'entry_best_practice' and f.periode = '1' and b.id_role = f.id_role and f.approval != 3 " +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            goals+" "+tar;
                }else{
                    sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                            "from best_map a\n" +
                            "inner join (select l.* from best_practice l inner join (select * from entry_approval where id_monper = :id_monper and year = :year and type = 'entry_best_practice' and periode = '1' and approval <> '3') b on l.id_role = b.id_role where l.id_role <> '999999' and l.id_monper = :id_monper and l.year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join sdg_target d on a.id_target = d.id\n" +
                            "left join sdg_indicator e on a.id_indicator = e.id\n" +
                            " join entry_approval f on b.id_monper = f.id_monper and f.year = b.year and f.type = 'entry_best_practice' and f.periode = '1' and b.id_role = f.id_role and f.approval != 3 " +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            goals+" "+tar;
                }
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_prov", id_prov);
            }else{
                if(status.equals("completed")) {
                    sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                            "from best_map a\n" +
                            "inner join (select l.* from best_practice l inner join (select * from entry_approval where id_monper = :id_monper and year = :year and type = 'entry_best_practice' and periode = '1' and approval <> '3') b on l.id_role = b.id_role where l.id_role <> '999999' and l.id_role = :id_role and l.id_monper = :id_monper and l.year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join (select * from history_sdg_target where id_monper = '"+id_monper+"') d on a.id_target = d.id_old\n" +
                            "left join (select * from history_sdg_indicator where id_monper = '"+id_monper+"') e on a.id_indicator = e.id_old\n" +
                            " join entry_approval f on b.id_monper = f.id_monper and f.year = b.year and f.type = 'entry_best_practice' and f.periode = '1' and b.id_role = f.id_role and f.approval != 3 " +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            goals+" "+tar;
                }else{
                    sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                            "from best_map a\n" +
                            "inner join (select l.* from best_practice l inner join (select * from entry_approval where id_monper = :id_monper and year = :year and type = 'entry_best_practice' and periode = '1' and approval <> '3') b on l.id_role = b.id_role where l.id_role <> '999999' and l.id_role = :id_role and l.id_monper = :id_monper and l.year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join sdg_target d on a.id_target = d.id\n" +
                            "left join sdg_indicator e on a.id_indicator = e.id\n" +
                            " join entry_approval f on b.id_monper = f.id_monper and f.year = b.year and f.type = 'entry_best_practice' and f.periode = '1' and b.id_role = f.id_role and f.approval != 3 " +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            goals+" "+tar;
                }
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
    
    @GetMapping("admin/getbest_level3_ins/{sdg}")
    public @ResponseBody Map<String, Object> getbest_level3_ins(@RequestParam("id_monper") String id_monper, @RequestParam("year") String year, @RequestParam("id_role") String id_role, @RequestParam("id_prov") String id_prov, @PathVariable("sdg") String sdg) {
    	Query query;
        System.out.println("id_monper = "+id_monper+" year = "+year+" id_prov = "+id_prov+" id_role = "+id_role);
        Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = monper.get().getStatus();
        
//        String sqlcek  = "select count(*) as total from entry_approval where id_role = '8' and id_monper = '1' and year = '2020' and type = 'entry_best_practice' and periode = '1' and approval = '3'";
        
        String sql = "";
//        131313
        if(sdg.equals("0")) {
        	if(status.equals("completed")) {
                sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                        "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                        "b.challenges_learning, c.nm_role,\n" +
                        "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                        "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                        ",f.id_goals as kode_goals, f.nm_goals, f.nm_goals_eng\n" +
                        "from best_map a\n" +
                        "inner join (select * from best_practice where id_role <> '999999' and id_role = :id_role and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                        "left join ref_role c on b.id_role = c.id_role\n" +
                        "left join (select * from history_sdg_target where id_monper = '"+id_monper+"') d on a.id_target = d.id_old\n" +
                        "left join (select * from history_sdg_indicator where id_monper = '"+id_monper+"') e on a.id_indicator = e.id_old\n" +
                        "left join (select * from history_sdg_goals where id_monper = '"+id_monper+"') f on a.id_goals = f.id_old\n" +
                        "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                        " ";
            }else{
                sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                        "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                        "b.challenges_learning, c.nm_role,\n" +
                        "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                        "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id "+
                        ",f.id_goals as kode_goals, f.nm_goals, f.nm_goals_eng\n" +
                        "from best_map a\n" +
                        "inner join (select * from best_practice where id_role <> '999999' and id_role = :id_role and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                        "left join ref_role c on b.id_role = c.id_role\n" +
                        "left join sdg_target d on a.id_target = d.id\n" +
                        "left join sdg_indicator e on a.id_indicator = e.id\n" +
                        "left join sdg_goals f on a.id_goals = f.id\n" +
                        "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                        " ";
            }
            query = manager.createNativeQuery(sql);
            query.setParameter("id_monper", id_monper);
            query.setParameter("year", year);
            query.setParameter("id_role", id_role);
            query.setParameter("id_prov", id_prov);
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
            if(status.equals("completed")) {
                sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                        "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                        "b.challenges_learning, c.nm_role,\n" +
                        "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                        "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                        ",f.id_goals as kode_goals, f.nm_goals, f.nm_goals_eng\n" +
                        "from best_map a\n" +
                        "inner join (select l.* from best_practice l inner join (select * from entry_approval where id_monper = :id_monper and year = :year and type = 'entry_best_practice' and periode = '1' and approval <> '3') b on l.id_role = b.id_role where l.id_role <> '999999' and l.id_role = :id_role and l.id_monper = :id_monper and l.year = :year) b on a.id_best_practice = b.id\n" +
                        "left join ref_role c on b.id_role = c.id_role\n" +
                        "left join (select * from history_sdg_target where id_monper = '"+id_monper+"') d on a.id_target = d.id_old\n" +
                        "left join (select * from history_sdg_indicator where id_monper = '"+id_monper+"') e on a.id_indicator = e.id_old\n" +
                        "left join (select * from history_sdg_goals where id_monper = '"+id_monper+"') f on a.id_goals = f.id_old\n" +
                        "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                        " "+tar;
            }else{
                sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                        "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                        "b.challenges_learning, c.nm_role,\n" +
                        "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                        "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                        ",f.id_goals as kode_goals, f.nm_goals, f.nm_goals_eng\n" +
                        "from best_map a\n" +
                        "inner join (select l.* from best_practice l inner join (select * from entry_approval where id_monper = :id_monper and year = :year and type = 'entry_best_practice' and periode = '1' and approval <> '3') b on l.id_role = b.id_role where l.id_role <> '999999' and l.id_role = :id_role and l.id_monper = :id_monper and l.year = :year) b on a.id_best_practice = b.id\n" +
                        "left join ref_role c on b.id_role = c.id_role\n" +
                        "left join sdg_target d on a.id_target = d.id\n" +
                        "left join sdg_indicator e on a.id_indicator = e.id\n" +
                        "left join sdg_goals f on a.id_goals = f.id\n" +
                        "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                        " "+tar;
            }
            query = manager.createNativeQuery(sql);
            query.setParameter("id_monper", id_monper);
            query.setParameter("year", year);
            query.setParameter("id_role", id_role);
            query.setParameter("id_prov", id_prov);
        }
        
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/getbest_level3_sum/{sdg}")
    public @ResponseBody Map<String, Object> getbest_level3_sum(@RequestParam("id_monper") String id_monper, @RequestParam("year") String year, @RequestParam("id_role") String id_role, @RequestParam("id_prov") String id_prov, @RequestParam("id_goals") String id_goals, @PathVariable("sdg") String sdg) {
    	Query query;
        System.out.println("id_monper = "+id_monper+" year = "+year+" id_prov = "+id_prov+" id_role = "+id_role);
        Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = monper.get().getStatus();
        String sql = "";
//        131313
        if(sdg.equals("0")) {
            if(id_role.equals("131313")){
                if(status.equals("completed")) {
                    sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join (select * from history_sdg_target where id_monper = '"+id_monper+"') d on a.id_target = d.id_old\n" +
                            "left join (select * from history_sdg_indicator where id_monper = '"+id_monper+"') e on a.id_indicator = e.id_old\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            "and a.id_goals = :id_goals ";
                }else{
                    sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join sdg_target d on a.id_target = d.id\n" +
                            "left join sdg_indicator e on a.id_indicator = e.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            "and a.id_goals = :id_goals ";
                }
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_prov", id_prov);
                query.setParameter("id_goals", id_goals);
            }else{
                if(status.equals("completed")) {
                    sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join (select * from history_sdg_target where id_monper = '"+id_monper+"') d on a.id_target = d.id_old\n" +
                            "left join (select * from history_sdg_indicator where id_monper = '"+id_monper+"') e on a.id_indicator = e.id_old\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            "and a.id_goals = :id_goals ";
                }else{
                    sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join sdg_target d on a.id_target = d.id\n" +
                            "left join sdg_indicator e on a.id_indicator = e.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            "and a.id_goals = :id_goals ";
                }
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
//                query.setParameter("id_role", id_role);
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
                if(status.equals("completed")) {
                    sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join (select * from history_sdg_target where id_monper = '"+id_monper+"') d on a.id_target = d.id_old\n" +
                            "left join (select * from history_sdg_indicator where id_monper = '"+id_monper+"') e on a.id_indicator = e.id_old\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            "and a.id_goals = :id_goals "+tar;
                }else{
                    sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join sdg_target d on a.id_target = d.id\n" +
                            "left join sdg_indicator e on a.id_indicator = e.id\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            "and a.id_goals = :id_goals "+tar;
                }
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
                query.setParameter("id_prov", id_prov);
                query.setParameter("id_goals", id_goals);
            }else{
                if(status.equals("completed")) {
                    sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join (select * from history_sdg_target where id_monper = '"+id_monper+"') d on a.id_target = d.id_old\n" +
                            "left join (select * from history_sdg_indicator where id_monper = '"+id_monper+"') e on a.id_indicator = e.id_old\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            "and a.id_goals = :id_goals "+tar;
                }else{
                    sql  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                            "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                            "b.challenges_learning, c.nm_role,\n" +
                            "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                            "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng, b.id\n" +
                            "from best_map a\n" +
                            "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                            "left join ref_role c on b.id_role = c.id_role\n" +
                            "left join (select * from history_sdg_target where id_monper = '"+id_monper+"') d on a.id_target = d.id_old\n" +
                            "left join (select * from history_sdg_indicator where id_monper = '"+id_monper+"') e on a.id_indicator = e.id_old\n" +
                            "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                            "and a.id_goals = :id_goals "+tar;
                }
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", id_monper);
                query.setParameter("year", year);
//                query.setParameter("id_role", id_role);
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
    
    
    @GetMapping("admin/best/download_report-best/{id_prov}/{id_role}/{id_monper}/{year}/{sdg}/{sub}")
//    @GetMapping("admin/best/download_report-best/")
    @ResponseBody
    public void download_report_best(HttpServletResponse response, @PathVariable("id_prov") String id_prov, @PathVariable("id_role") String id_role, @PathVariable("id_monper") String id_monper, @PathVariable("year") String year, @PathVariable("sdg") String sdg, @PathVariable("sub") String sub) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=Report_Best_Practice-"+id_prov+".xlsx");
        ByteArrayInputStream stream = report_best(id_prov, id_role, id_monper, year, sdg, sub);
        IOUtils.copy(stream, response.getOutputStream());
    }
    
    private ByteArrayInputStream report_best(String id_prov, String id_role, String id_monper, String year, String sdg, String sub) {
        try(Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Report_Best_Practice");
            //baris 1
            int baris = 0;
            Row row = sheet.createRow(baris);
            CellStyle headerCellStyle = workbook.createCellStyle();
//            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle.setAlignment(HorizontalAlignment.LEFT);
            headerCellStyle.setWrapText(true);
            // Creating header
            Cell cell = row.createCell(0);
            cell.setCellValue("Summary");
            cell.setCellStyle(headerCellStyle);
            
            //baris 2
            baris += (baris+1);
            Row row1 = sheet.createRow(baris);
            CellStyle headerCellStyle1 = workbook.createCellStyle();
//            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle1.setAlignment(HorizontalAlignment.LEFT);
            headerCellStyle1.setWrapText(true);
            // Creating header
            Cell cell1 = row1.createCell(0);
            cell1.setCellValue("Total : 1");
            cell1.setCellStyle(headerCellStyle1);
            
            //baris 3
            String sql  = "select * from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_best_practice' and period = '1' ";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id_monper", id_monper);
            query.setParameter("year", year);
            Map<String, Object> mapDetail = new HashMap<>();
            mapDetail.put("mapDetail",query.getResultList());
            JSONObject objDetail = new JSONObject(mapDetail);
            JSONArray  arraylevel1 = objDetail.getJSONArray("mapDetail");
            
//            System.out.println("baris awal = "+baris);
//            System.out.println("leng = "+arraylevel1.length());
            int i=0;
            for (i=0; i<arraylevel1.length(); i++) {
//                System.out.println("baris = "+baris);
                baris = (baris+1);
                JSONArray detaillevel1 = arraylevel1.getJSONArray(i);
                Row row2 = sheet.createRow(baris);
                row2.createCell(0).setCellValue(detaillevel1.get(2).toString());
//                baris += (baris++);
            }
//            System.out.println("baris end = "+baris);
//            System.out.println("baris akhir = "+(baris+i));
            //baris awal = 1
            //leng = 4
	        
            //baris 4
            Query query1;
            System.out.println("id_monper = "+id_monper+" year = "+year+" id_prov = "+id_prov+" id_role = "+id_role);
    //        131313
            if(sdg.equals("0")) {
                if(id_role.equals("131313")){
                    String sql1  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                                "(\n" +
                                "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                                "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                                "b.challenges_learning\n" +
                                "from best_map a\n" +
                                "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                                "where a.id_prov = :id_prov and a.id_monper = :id_monper \n" +
                                ")as z\n" +
                                "left join sdg_goals y on z.id_goals = y.id";
                    query1 = manager.createNativeQuery(sql1);
                    query1.setParameter("id_monper", id_monper);
                    query1.setParameter("year", year);
                    query1.setParameter("id_prov", id_prov);
                }else{
                    String sql1  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                                "(\n" +
                                "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                                "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                                "b.challenges_learning\n" +
                                "from best_map a\n" +
                                "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                                "where a.id_prov = :id_prov and a.id_monper = :id_monper \n" +
                                ")as z\n" +
                                "left join sdg_goals y on z.id_goals = y.id";
                    query1 = manager.createNativeQuery(sql1);
                    query1.setParameter("id_monper", id_monper);
                    query1.setParameter("year", year);
    //                query.setParameter("id_role", id_role);
                    query1.setParameter("id_prov", id_prov);
        //            System.out.println("sql nya = "+sql);
                }
            }else{
                String[] arrOfStr = sdg.split(","); 
                StringBuffer target = new StringBuffer();
                if(arrOfStr.length>0) {
                    for (int m = 0; m < arrOfStr.length; m++) {
                        String[] arrOfStr1 = arrOfStr[m].split("---");
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
                    String sql1  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                                "(\n" +
                                "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                                "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                                "b.challenges_learning\n" +
                                "from best_map a\n" +
                                "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                                "where a.id_prov = :id_prov and a.id_monper = :id_monper "+tar+"\n" +
                                ")as z\n" +
                                "left join sdg_goals y on z.id_goals = y.id";
                    query1 = manager.createNativeQuery(sql1);
                    query1.setParameter("id_monper", id_monper);
                    query1.setParameter("year", year);
                    query1.setParameter("id_prov", id_prov);
                }else{
                    String sql1  = "select distinct z.id_goals, y.id_goals as kode_goals, y.nm_goals, y.nm_goals_eng from\n" +
                                "(\n" +
                                "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                                "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                                "b.challenges_learning\n" +
                                "from best_map a\n" +
                                "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                                "where a.id_prov = :id_prov and a.id_monper = :id_monper "+tar+"\n" +
                                ")as z\n" +
                                "left join sdg_goals y on z.id_goals = y.id";
                    query1 = manager.createNativeQuery(sql1);
                    query1.setParameter("id_monper", id_monper);
                    query1.setParameter("year", year);
    //                query.setParameter("id_role", id_role);
                    query1.setParameter("id_prov", id_prov);
        //            System.out.println("sql nya = "+sql);
                }
            }

            Map<String, Object> mapDetail1 = new HashMap<>();
            mapDetail1.put("mapDetail1",query1.getResultList());
            JSONObject objDetail1 = new JSONObject(mapDetail1);
            JSONArray  arraylevel2 = objDetail1.getJSONArray("mapDetail1");
            
            System.out.println("baris awal = "+baris);
            System.out.println("leng = "+arraylevel2.length());
            i=0;
            for (i=0; i<arraylevel2.length(); i++) {
                System.out.println("baris = "+baris);
                baris = (baris+1);
                JSONArray detaillevel2 = arraylevel2.getJSONArray(i);
                Row row3 = sheet.createRow(baris);
                String sum_goal = "";
                if(sub.equals("indo")){
                    sum_goal = detaillevel2.get(1).toString()+" "+detaillevel2.get(2).toString();
                }else{
                    sum_goal = detaillevel2.get(1).toString()+" "+detaillevel2.get(3).toString();
                }
                row3.createCell(0).setCellValue(sum_goal);
                
                //tabel
                //header tabel
                baris = (baris+1);
                Row row4 = sheet.createRow(baris);
                CellStyle headerCellStyle4 = workbook.createCellStyle();
                headerCellStyle4.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                headerCellStyle4.setBorderBottom(BorderStyle.MEDIUM);
                headerCellStyle4.setBorderTop(BorderStyle.MEDIUM);
                headerCellStyle4.setBorderLeft(BorderStyle.MEDIUM);
                headerCellStyle4.setBorderRight(BorderStyle.MEDIUM);
                headerCellStyle4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerCellStyle4.setAlignment(HorizontalAlignment.LEFT);
                headerCellStyle4.setWrapText(true);
                headerCellStyle4.setShrinkToFit(true);
                
                // Creating header
                String sum_head1 = "";
                String sum_head2 = "";
                String sum_head3 = "";
                String sum_head4 = "";
                String sum_head5 = "";
                String sum_head6 = "";
                if(sub.equals("indo")){
                    sum_head1 = "Indikator";
                    sum_head2 = "Institusi";
                    sum_head3 = "Lokasi dan Waktu Aktivitas";
                    sum_head4 = "Latar Belakang";
                    sum_head5 = "Proses Implementasi";
                    sum_head6 = "Tantangan dan Pembelajaran";

                }else{
                    sum_head1 = "Indicator";
                    sum_head2 = "Institution";
                    sum_head3 = "Location and Time of Activity";
                    sum_head4 = "Background";
                    sum_head5 = "Implementation Process";
                    sum_head6 = "Challenge and Learning";
                }
                Cell cell4 = row4.createCell(0);
                cell4.setCellValue(sum_head1);
                sheet.autoSizeColumn(0);
                cell4.setCellStyle(headerCellStyle4);
                
                cell4 = row4.createCell(1);
                cell4.setCellValue(sum_head2);
                sheet.autoSizeColumn(1);
                cell4.setCellStyle(headerCellStyle4);
                
                cell4 = row4.createCell(2);
                cell4.setCellValue("Program");
                sheet.autoSizeColumn(2);
                cell4.setCellStyle(headerCellStyle4);
                
                cell4 = row4.createCell(3);
                cell4.setCellValue(sum_head3);
                sheet.autoSizeColumn(3);
                cell4.setCellStyle(headerCellStyle4);
                
                cell4 = row4.createCell(4);
                cell4.setCellValue(sum_head4);
                sheet.autoSizeColumn(4);
                cell4.setCellStyle(headerCellStyle4);
                
                cell4 = row4.createCell(5);
                cell4.setCellValue(sum_head5);
                sheet.autoSizeColumn(5);
                cell4.setCellStyle(headerCellStyle4);
                
                cell4 = row4.createCell(6);
                cell4.setCellValue(sum_head6);
                sheet.autoSizeColumn(6);
                cell4.setCellStyle(headerCellStyle4);
                
                String id_goals = detaillevel2.get(0).toString();
                Query query2;
                System.out.println("id_monper = "+id_monper+" year = "+year+" id_prov = "+id_prov+" id_role = "+id_role);
        //        131313
                if(sdg.equals("0")) {
                    if(id_role.equals("131313")){
                        String sql2  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                                    "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                                    "b.challenges_learning, c.nm_role,\n" +
                                    "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                                    "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng\n" +
                                    "from best_map a\n" +
                                    "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                                    "left join ref_role c on b.id_role = c.id_role\n" +
                                    "left join sdg_target d on a.id_target = d.id\n" +
                                    "left join sdg_indicator e on a.id_indicator = e.id\n" +
                                    "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                                    "and a.id_goals = :id_goals ";
                        query2 = manager.createNativeQuery(sql2);
                        query2.setParameter("id_monper", id_monper);
                        query2.setParameter("year", year);
                        query2.setParameter("id_prov", id_prov);
                        query2.setParameter("id_goals", id_goals);
                    }else{
                        String sql2  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                                    "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                                    "b.challenges_learning, c.nm_role,\n" +
                                    "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                                    "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng\n" +
                                    "from best_map a\n" +
                                    "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                                    "left join ref_role c on b.id_role = c.id_role\n" +
                                    "left join sdg_target d on a.id_target = d.id\n" +
                                    "left join sdg_indicator e on a.id_indicator = e.id\n" +
                                    "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                                    "and a.id_goals = :id_goals ";
                        query2 = manager.createNativeQuery(sql2);
                        query2.setParameter("id_monper", id_monper);
                        query2.setParameter("year", year);
        //                query.setParameter("id_role", id_role);
                        query2.setParameter("id_prov", id_prov);
                        query2.setParameter("id_goals", id_goals);
            //            System.out.println("sql nya = "+sql);
                    }
                }else {
                    String[] arrOfStr2 = sdg.split(","); 
                    StringBuffer target2 = new StringBuffer();
                    if(arrOfStr2.length>0) {
                        for (int b = 0; b < arrOfStr2.length; b++) {
                            String[] arrOfStr21 = arrOfStr2[b].split("---");
                            int cek2=1;
                            for(int k=0;k<arrOfStr21.length;k++) {
                                cek2 = (cek2==4)?1:cek2;
                                if(!arrOfStr21[k].equals("0") && cek2==2) {
                                    target2.append("'"+arrOfStr21[k]+"',");
                                }
                                cek2 = cek2+1;
                            }
                        }
                    }else{
                        String[] arrOfStr21 = sdg.split("---");
                        int cek2=1;
                        for(int k=0;k<arrOfStr21.length;k++) {
                            cek2 = (cek2==4)?1:cek2;
                            if(!arrOfStr21[k].equals("0") && cek2==2) {
                                target2.append("'"+arrOfStr21[k]+"',");
                            }
                            cek2 = cek2+1;
                        }
                    }
                    String hasiltarget2 = (target2.length()==0)?"":target2.substring(0, target2.length() - 1);

                    String tar2 = (hasiltarget2.equals(""))?"":" and a.id_target in("+hasiltarget2+") ";
                    if(id_role.equals("131313")){
                        String sql2  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                                    "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                                    "b.challenges_learning, c.nm_role,\n" +
                                    "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                                    "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng\n" +
                                    "from best_map a\n" +
                                    "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                                    "left join ref_role c on b.id_role = c.id_role\n" +
                                    "left join sdg_target d on a.id_target = d.id\n" +
                                    "left join sdg_indicator e on a.id_indicator = e.id\n" +
                                    "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                                    "and a.id_goals = :id_goals "+tar2;
                        query2 = manager.createNativeQuery(sql2);
                        query2.setParameter("id_monper", id_monper);
                        query2.setParameter("year", year);
                        query2.setParameter("id_prov", id_prov);
                        query2.setParameter("id_goals", id_goals);
                    }else{
                        String sql2  = "select a.id as id_best_map, a.id_prov, a.id_monper, a.id_goals, a.id_target, a.id_indicator, a.id_best_practice, \n" +
                                    "b.id_role, b.program, b.location, b.time_activity, b.background, b.implementation_process,\n" +
                                    "b.challenges_learning, c.nm_role,\n" +
                                    "d.id_target as kode_target, d.nm_target, d.nm_target_eng,\n" +
                                    "e.id_indicator as kode_indicator, e.nm_indicator, e.nm_indicator_eng\n" +
                                    "from best_map a\n" +
                                    "inner join (select * from best_practice where id_role = '999999' and id_monper = :id_monper and year = :year) b on a.id_best_practice = b.id\n" +
                                    "left join ref_role c on b.id_role = c.id_role\n" +
                                    "left join sdg_target d on a.id_target = d.id\n" +
                                    "left join sdg_indicator e on a.id_indicator = e.id\n" +
                                    "where a.id_prov = :id_prov and a.id_monper = :id_monper "+
                                    "and a.id_goals = :id_goals "+tar2;
                        query2 = manager.createNativeQuery(sql2);
                        query2.setParameter("id_monper", id_monper);
                        query2.setParameter("year", year);
        //                query.setParameter("id_role", id_role);
                        query2.setParameter("id_prov", id_prov);
                        query2.setParameter("id_goals", id_goals);
            //            System.out.println("sql nya = "+sql);
                    }
                }
                Map<String, Object> mapDetail2 = new HashMap<>();
                mapDetail2.put("mapDetail2",query2.getResultList());
                JSONObject objDetail2 = new JSONObject(mapDetail2);
                JSONArray  arraylevel3 = objDetail2.getJSONArray("mapDetail2");

                int c=0;
                for (c=0; c<arraylevel3.length(); c++) {
                    System.out.println("baris = "+baris);
                    baris = (baris+1);
                    JSONArray detaillevel3 = arraylevel3.getJSONArray(c);
                    Row row5 = sheet.createRow(baris);
                    CellStyle headerCellStyle5 = workbook.createCellStyle();
                    headerCellStyle5.setFillForegroundColor(IndexedColors.WHITE1.getIndex());
                    headerCellStyle5.setBorderBottom(BorderStyle.MEDIUM);
                    headerCellStyle5.setBorderTop(BorderStyle.MEDIUM);
                    headerCellStyle5.setBorderLeft(BorderStyle.MEDIUM);
                    headerCellStyle5.setBorderRight(BorderStyle.MEDIUM);
                    headerCellStyle5.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    headerCellStyle5.setAlignment(HorizontalAlignment.LEFT);
                    headerCellStyle5.setWrapText(true);
                    headerCellStyle5.setShrinkToFit(true);

                    String sum_target = "";
                    String sum_indicator = "";
                    String sum_hasil = "";
                    if(sub.equals("indo")){
                        sum_target      = detaillevel3.get(15).toString()+" "+detaillevel3.get(16).toString();
                        sum_indicator   = detaillevel3.get(18).toString()+" "+detaillevel3.get(19).toString();
                        sum_hasil       = detaillevel3.get(15).toString()+" "+detaillevel3.get(16).toString()+"\n"+detaillevel3.get(18).toString()+" "+detaillevel3.get(19).toString();
                    }else{
                        sum_target      = detaillevel3.get(15).toString()+" "+detaillevel3.get(17).toString();
                        sum_indicator   = detaillevel3.get(18).toString()+" "+detaillevel3.get(20).toString();
                        sum_hasil       = detaillevel3.get(15).toString()+" "+detaillevel3.get(17).toString()+"\n"+detaillevel3.get(18).toString()+" "+detaillevel3.get(20).toString();
                    }
                    
                    Cell cell5 = row5.createCell(0);
                    cell5.setCellValue(sum_hasil);
//                    sheet.autoSizeColumn(0);
                    cell5.setCellStyle(headerCellStyle5);
                    
                    cell5 = row5.createCell(1);
                    cell5.setCellValue("");
//                    sheet.autoSizeColumn(0);
                    cell5.setCellStyle(headerCellStyle5);
                    
                    cell5 = row5.createCell(2);
                    cell5.setCellValue(detaillevel3.get(8).toString());
//                    sheet.autoSizeColumn(0);
                    cell5.setCellStyle(headerCellStyle5);
                    
                    
                }
                
                
                
//                baris += (baris++);
            }
            System.out.println("baris end = "+baris);
            
            
            //=================== Isi tabel atas =================
//            String sql = "SELECT '' as nm_program, b.sector, b.location, b.beneficiaries, b.ex_benefit, b.type_support, "
//                            + "'' as nm_philanthropy from nsa_collaboration b ";
//            Query query = em.createNativeQuery(sql);
////            query.setParameter("id_role", id_role);
//            Map<String, Object> mapDetail = new HashMap<>();
//            mapDetail.put("mapDetail",query.getResultList());
//            JSONObject objDetail = new JSONObject(mapDetail);
//            JSONArray  arrayDetail = objDetail.getJSONArray("mapDetail");
//
//            for (int i=0; i<arrayDetail.length(); i++) {
//                JSONArray finalDetail = arrayDetail.getJSONArray(i);
//                Row dataRow = sheet.createRow(i+1);
//                dataRow.createCell(0).setCellValue(i+1);
//                dataRow.createCell(1).setCellValue(finalDetail.get(1).toString());
//                dataRow.createCell(2).setCellValue(finalDetail.get(0).toString());
//                dataRow.createCell(3).setCellValue(finalDetail.get(2).toString());
//                dataRow.createCell(4).setCellValue(finalDetail.get(3).toString());
//                dataRow.createCell(5).setCellValue(finalDetail.get(4).toString());
//                dataRow.createCell(6).setCellValue(finalDetail.get(5).toString());
//                dataRow.createCell(7).setCellValue(finalDetail.get(6).toString());
//            }
//
//            sheet.autoSizeColumn(0);
//            sheet.autoSizeColumn(1);
//            sheet.autoSizeColumn(2);
//            sheet.autoSizeColumn(3);
//            sheet.autoSizeColumn(4);
//            sheet.autoSizeColumn(5);
//            sheet.autoSizeColumn(6);
//            sheet.autoSizeColumn(7);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
	}
    
}
