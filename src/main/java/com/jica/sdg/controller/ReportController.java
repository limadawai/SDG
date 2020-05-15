package com.jica.sdg.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jica.sdg.model.Problemlist;
import com.jica.sdg.model.Provinsi;
import com.jica.sdg.model.RanRad;
import com.jica.sdg.model.Role;
import com.jica.sdg.service.ISdgGoalsService;
import com.jica.sdg.service.ISdgIndicatorService;
import com.jica.sdg.service.ISdgTargetService;
import com.jica.sdg.service.ModelCrud;
import com.jica.sdg.service.NsaProfileService;
import com.jica.sdg.service.ProvinsiService;
import com.jica.sdg.service.RanRadService;
import com.jica.sdg.service.RoleService;
import com.jica.sdg.service.SdgFundingService;
import com.jica.sdg.service.SdgGoalsService;
import com.jica.sdg.service.SdgIndicatorService;
import com.jica.sdg.service.SdgTargetService;

@Controller
public class ReportController {

    @Autowired
    ProvinsiService provinsiService;
    @Autowired
    RoleService roleService;
    @Autowired
    RanRadService radService;
    @Autowired
    SdgGoalsService goalsService;
    @Autowired
    ISdgGoalsService sdgGoalsService;
    @Autowired
    SdgTargetService targetService;
    @Autowired
    SdgIndicatorService indicatorService;
    @Autowired
    EntityManager manager;
    @Autowired
    RanRadService ranRadService;
    @Autowired
    SdgFundingService sdgFundingService;
    @Autowired
    ISdgTargetService sdgTargetService;
    @Autowired
    NsaProfileService nsaProfilrService;
    @Autowired
    ISdgIndicatorService sdgIndicatorService;
    @Autowired
    ModelCrud modelCrud;
    @Autowired
    private EntityManager em;

    // ****************** Report Hasil Monitoring ******************
    @GetMapping("admin/report-monitoring")
    public String monitoring(Model model, HttpSession session) {
        model.addAttribute("listprov", provinsiService.findAllProvinsi());
        model.addAttribute("listrole", roleService.findAll());
        model.addAttribute("listranrad", radService.findAll());
        model.addAttribute("listgoals", goalsService.findAll());

        model.addAttribute("title", "SDG Problem Identification & Follow Up");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/report/monitoring";
    }

    @GetMapping("admin/getrolebyidprov")
    public @ResponseBody List<Object> getRoleByIdProv(@RequestParam("id_prov") String id) {
        List list = roleService.findByProvince(id);
        return list;
    }

    @GetMapping("admin/getranradbyidprov")
    public @ResponseBody List<Object> getranrad(@RequestParam("id_prov") String id) {
        List list = ranRadService.findAll();
        return list;
    }
    
    @GetMapping("admin/getentryshowreport")
    public @ResponseBody Map<String, Object> getentryshowreport(@RequestParam("id_monper") int idmonper) {
    	//String sql = "SELECT period FROM entry_show_report WHERE id_monper = :id_monper AND year = :year AND type = 'entry_sdg'";
    	String sql = "SELECT period, year FROM entry_show_report WHERE id_monper = :id_monper AND type = 'entry_sdg' order by year, period";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        //query.setParameter("year", year);
        List listSdg = query.getResultList();
        
        //String sql1 = "SELECT period FROM entry_show_report WHERE id_monper = :id_monper AND year = :year AND type = 'entry_gov_indicator'";
        String sql1 = "SELECT period, year FROM entry_show_report WHERE id_monper = :id_monper AND type = 'entry_gov_indicator' order by year, period";
        Query query1 = manager.createNativeQuery(sql1);
        query1.setParameter("id_monper", idmonper);
        //query1.setParameter("year", year);
        List listGovInd = query1.getResultList();
        
        //String sql2 = "SELECT period FROM entry_show_report WHERE id_monper = :id_monper AND year = :year AND type = 'entry_gov_budget'";
        String sql2 = "SELECT period, year FROM entry_show_report WHERE id_monper = :id_monper AND type = 'entry_gov_budget' order by year, period";
        Query query2 = manager.createNativeQuery(sql2);
        query2.setParameter("id_monper", idmonper);
        //query2.setParameter("year", year);
        List listGovBud = query2.getResultList();
        
        //String sql3 = "SELECT period, year FROM entry_show_report WHERE id_monper = :id_monper AND year = :year AND type = 'entry_nsa_budget'";
        String sql3 = "SELECT period, year FROM entry_show_report WHERE id_monper = :id_monper AND type = 'entry_nsa_budget' order by year, period";
        Query query3 = manager.createNativeQuery(sql3);
        query3.setParameter("id_monper", idmonper);
        //query3.setParameter("year", year);
        List listNsaBud = query3.getResultList();
        
        //String sql4 = "SELECT period FROM entry_show_report WHERE id_monper = :id_monper AND year = :year AND type = 'entry_nsa_indicator'";
        String sql4 = "SELECT period, year FROM entry_show_report WHERE id_monper = :id_monper AND type = 'entry_nsa_indicator' order by year, period";
        Query query4 = manager.createNativeQuery(sql4);
        query4.setParameter("id_monper", idmonper);
        //query4.setParameter("year", year);
        List listNsaInd = query4.getResultList();
        
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("sdg",listSdg);
        hasil.put("GovInd",listGovInd);
        hasil.put("GovBud",listGovBud);
        hasil.put("NsaInd",listNsaInd);
        hasil.put("NsaBud",listNsaBud);
        return hasil;
    }
    
    @GetMapping("admin/get-sdg-goals")
    public @ResponseBody Map<String, Object> getSdgGoals(@RequestParam("id_role") int id_role) {
    	String sql = "SELECT distinct a.id_goals as id, b.nm_goals, b.nm_goals_eng, b.id_goals FROM assign_sdg_indicator a "
    			+ " left join sdg_goals b on a.id_goals = b.id "
    			+ " WHERE a.id_role = :id_role order by CAST(a.id_goals AS UNSIGNED)";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_role", id_role);
        List listSdg = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("sdg",listSdg);
        return hasil;
    }
    
    @GetMapping("admin/get-sdg-goals/{sdg}")
    public @ResponseBody Map<String, Object> getSdgGoalsEva(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_monper") String id_monper,
    		@PathVariable("sdg") String sdg) {
    	Query query;
    	Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = (monper.isPresent())?monper.get().getStatus():"";
    	String role;
    	if(id_role.equals("all")) {
    		role = "";
    	}else if(id_role.equals("unassign")) {
    		role = " and a.id_role is null ";
    	}else {
    		role = " and a.id_role = '"+id_role+"'";
    	}
    	
    	if(sdg.equals("0")) {
    		String sql;
    		if(status.equals("completed")) {
    			sql = "SELECT distinct b.id_old, b.nm_goals, b.nm_goals_eng, b.id_goals "
        				+ " FROM history_sdg_indicator c "
        				+ " left join assign_sdg_indicator a on a.id_indicator = c.id_old "
            			+ " left join history_sdg_goals b on c.id_goals = b.id_old and b.id_monper = c.id_monper "
            			+ " WHERE c.id_old is not null and c.id_monper = '"+id_monper+"' "+role+" order by CAST(b.id_goals AS UNSIGNED)";
    		}else {
    			sql = "SELECT distinct b.id, b.nm_goals, b.nm_goals_eng, b.id_goals "
        				+ " FROM sdg_indicator c "
        				+ " left join assign_sdg_indicator a on a.id_indicator = c.id "
            			+ " left join sdg_goals b on c.id_goals = b.id "
            			+ " WHERE c.id is not null "+role+" order by CAST(b.id_goals AS UNSIGNED)";
    		}
            query = manager.createNativeQuery(sql);
    	}else {
    		String[] arrOfStr = sdg.split(","); 
    		StringBuffer goals = new StringBuffer();
    		if(arrOfStr.length>0) {
    			for (int i = 0; i < arrOfStr.length; i++) {
        			String[] arrOfStr1 = arrOfStr[i].split("---");
        			int cek=1;
        			for(int j=0;j<arrOfStr1.length;j++) {
        				cek = (cek==4)?1:cek;
        				if(!arrOfStr1[j].equals("0") && cek==1) {
        					goals.append("'"+arrOfStr1[j]+"',");
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
    					goals.append("'"+arrOfStr1[j]+"',");
    				}
    				cek = cek+1;
    			}
    		}
    		String hasilgoals = (goals.length()==0)?"":goals.substring(0, goals.length() - 1);
    		String gol = (hasilgoals.equals(""))?"":" and c.id_goals in("+hasilgoals+") ";
    		String sql;
    		if(status.equals("completed")) {
    			sql = "SELECT distinct b.id_old, b.nm_goals, b.nm_goals_eng, b.id_goals "
        				+ " FROM history_sdg_indicator c "
        				+ " left join assign_sdg_indicator a on a.id_indicator = c.id_old "
            			+ " left join history_sdg_goals b on c.id_goals = b.id_old and b.id_monper = c.id_monper "
            			+ " WHERE c.id_old is not null and c.id_monper = '"+id_monper+"' "+role+" "+gol+" order by b.id_old";
    		}else {
    			sql = "SELECT distinct b.id, b.nm_goals, b.nm_goals_eng, b.id_goals "
        				+ " FROM sdg_indicator c "
        				+ " left join assign_sdg_indicator a on a.id_indicator = c.id "
            			+ " left join sdg_goals b on c.id_goals = b.id "
            			+ " WHERE c.id is not null "+role+" "+gol+" order by b.id";
    		}
            query = manager.createNativeQuery(sql);
    	}
    	
        List listSdg = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("sdg",listSdg);
        return hasil;
    }
    
    @GetMapping("admin/get-sdg-goals-best")
    public @ResponseBody Map<String, Object> getSdgGoalsBest(
    		@RequestParam("id_monper") String id_monper) {
    	Query query;
    	Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = (monper.isPresent())?monper.get().getStatus():"";
    	
    	String sql;
		if(status.equals("completed")) {
			sql = "SELECT distinct b.id_old, b.nm_goals, b.nm_goals_eng, b.id_goals "
    				+ " FROM history_sdg_goals b "
        			+ " WHERE b.id_monper = '"+id_monper+"' order by CAST(b.id_goals AS UNSIGNED)";
		}else {
			sql = "SELECT distinct b.id, b.nm_goals, b.nm_goals_eng, b.id_goals "
    				+ " FROM sdg_goals b order by CAST(b.id_goals AS UNSIGNED)";
		}
        query = manager.createNativeQuery(sql);
    	
        List listSdg = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("sdg",listSdg);
        return hasil;
    }
    
    @GetMapping("admin/get-sdg-target-best")
    public @ResponseBody Map<String, Object> getSdgTargetBest(
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_goals") String id_goals) {
    	Query query;
    	Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = (monper.isPresent())?monper.get().getStatus():"";
    	
    	String sql;
		if(status.equals("completed")) {
			sql = "SELECT distinct b.id_old, b.nm_target, b.nm_target_eng, b.id_target "
    				+ " FROM history_sdg_target b "
        			+ " WHERE b.id_monper = '"+id_monper+"' and b.id_goals= '"+id_goals+"' order by CAST(b.id_target AS UNSIGNED)";
		}else {
			sql = "SELECT distinct b.id, b.nm_target, b.nm_target_eng, b.id_target "
    				+ " FROM sdg_target b where b.id_goals= '"+id_goals+"' order by CAST(b.id_target AS UNSIGNED)";
		}
        query = manager.createNativeQuery(sql);
    	
        List listSdg = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",listSdg);
        return hasil;
    }
    
    @GetMapping("admin/get-sdg-indicator-best")
    public @ResponseBody Map<String, Object> getSdgIndicatorBest(
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_target") String id_target) {
    	Query query;
    	Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = (monper.isPresent())?monper.get().getStatus():"";
    	
    	String sql;
		if(status.equals("completed")) {
			sql = "SELECT distinct b.id_old, b.nm_indicator, b.nm_indicator_eng, b.id_indicator "
    				+ " FROM history_sdg_indicator b "
        			+ " WHERE b.id_monper = '"+id_monper+"' and b.id_target= '"+id_target+"' order by CAST(b.id_indicator AS UNSIGNED)";
		}else {
			sql = "SELECT distinct b.id, b.nm_indicator, b.nm_indicator_eng, b.id_indicator "
    				+ " FROM sdg_indicator b where b.id_target= '"+id_target+"' order by CAST(b.id_indicator AS UNSIGNED)";
		}
        query = manager.createNativeQuery(sql);
    	
        List listSdg = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",listSdg);
        return hasil;
    }
    
    @GetMapping("admin/get-sdg-goals-report/{sdg}")
    public @ResponseBody Map<String, Object> getSdgGoalMon(
    		@RequestParam("id_role") String id_role,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@PathVariable("sdg") String sdg) {
    	Query query;
    	Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = monper.get().getStatus();
    	
    	if(sdg.equals("0")) {
    		String role=(!id_role.equals("all"))?" and d.id_role = '"+id_role+"'":"";
    		String sql;
    		if(status.equals("completed")) {
    			sql = "SELECT distinct a.id_old, a.nm_goals, a.nm_goals_eng, a.id_goals FROM history_sdg_goals a where a.id_monper = '"+id_monper+"' order by CAST(a.id_goals AS UNSIGNED)";
    			query = manager.createNativeQuery(sql);
    		}else {
    			sql = "SELECT distinct a.id, a.nm_goals, a.nm_goals_eng, a.id_goals FROM sdg_goals a order by CAST(a.id_goals AS UNSIGNED)";
    			query = manager.createNativeQuery(sql);
    		}
            
    	}else {
    		String[] arrOfStr = sdg.split(","); 
    		StringBuffer goals = new StringBuffer();
    		if(arrOfStr.length>0) {
    			for (int i = 0; i < arrOfStr.length; i++) {
        			String[] arrOfStr1 = arrOfStr[i].split("---");
        			int cek=1;
        			for(int j=0;j<arrOfStr1.length;j++) {
        				cek = (cek==4)?1:cek;
        				if(!arrOfStr1[j].equals("0") && cek==1) {
        					goals.append("'"+arrOfStr1[j]+"',");
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
    					goals.append("'"+arrOfStr1[j]+"',");
    				}
    				cek = cek+1;
    			}
    		}
    		String hasilgoals = (goals.length()==0)?"":goals.substring(0, goals.length() - 1);
    		//String gol = (hasilgoals.equals(""))?"":" and a.id_goals in("+hasilgoals+") ";
    		String golOld = (hasilgoals.equals(""))?"":" and a.id_old in("+hasilgoals+") ";
    		String gol = (hasilgoals.equals(""))?"":" where a.id in("+hasilgoals+") ";
            String role=(!id_role.equals("all"))?" and d.id_role = '"+id_role+"'":"";
            String sql;
            if(status.equals("completed")) {
    			sql = "SELECT distinct a.id_old, a.nm_goals, a.nm_goals_eng, a.id_goals FROM history_sdg_goals a where a.id_monper = '"+id_monper+"' "+golOld+" order by a.id_old";
    			query = manager.createNativeQuery(sql);
            }else {
    			sql = "SELECT distinct a.id, a.nm_goals, a.nm_goals_eng, a.id_goals FROM sdg_goals a "+gol+" order by a.id";
    			query = manager.createNativeQuery(sql);
            } 
    	}
    	
        List listSdg = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("sdg",listSdg);
        return hasil;
    }
    
    @GetMapping("admin/get-sdg-target-report/{sdg}")
    public @ResponseBody Map<String, Object> getSdgTargetMon(
    		@RequestParam("id_role") String id_role,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_goals") String id_goals,
    		@PathVariable("sdg") String sdg) {
    	Query query;
    	Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = monper.get().getStatus();
    	
    	if(sdg.equals("0")) {
    		String role=(!id_role.equals("all"))?" and d.id_role = '"+id_role+"'":"";
    		String sql;
            if(status.equals("completed")) {sql = "SELECT distinct a.id_old, a.nm_target, a.nm_target_eng, a.id_target FROM history_sdg_target a where a.id_goals = :id_goals and a.id_monper = '"+id_monper+"' order by CAST(a.id_target AS UNSIGNED)";
    			query = manager.createNativeQuery(sql);
    			query.setParameter("id_goals", id_goals);
            }else {sql = "SELECT distinct a.id, a.nm_target, a.nm_target_eng, a.id_target FROM sdg_target a where a.id_goals = :id_goals order by CAST(a.id_target AS UNSIGNED)";
    			query = manager.createNativeQuery(sql);
    			query.setParameter("id_goals", id_goals);
            }
    	}else {
    		String[] arrOfStr = sdg.split(","); 
    		StringBuffer goals = new StringBuffer();
    		StringBuffer target = new StringBuffer();
    		if(arrOfStr.length>0) {
    			for (int i = 0; i < arrOfStr.length; i++) {
        			String[] arrOfStr1 = arrOfStr[i].split("---");
        			int cek=1;
        			for(int j=0;j<arrOfStr1.length;j++) {
        				cek = (cek==4)?1:cek;
        				if(!arrOfStr1[j].equals("0") && cek==1) {
        					goals.append("'"+arrOfStr1[j]+"',");
        				}
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
    				if(!arrOfStr1[j].equals("0") && cek==1) {
    					goals.append("'"+arrOfStr1[j]+"',");
    				}
    				if(!arrOfStr1[j].equals("0") && cek==2) {
    					target.append("'"+arrOfStr1[j]+"',");
    				}
    				cek = cek+1;
    			}
    		}
    		String hasiltarget = (target.length()==0)?"":target.substring(0, target.length() - 1);
    		String hasilgoals = (goals.length()==0)?"":goals.substring(0, goals.length() - 1);
    		
    		String gol = (hasilgoals.equals(""))?"":" and a.id_goals in("+hasilgoals+") ";
    		
    		String tarOld = (hasiltarget.equals(""))?"":" and a.id_old in("+hasiltarget+") ";
    		String tar = (hasiltarget.equals(""))?"":" and a.id in("+hasiltarget+") ";

            String sql;
            if(status.equals("completed")) {
            	sql = "SELECT distinct a.id_old, a.nm_target, a.nm_target_eng, a.id_target FROM history_sdg_target a where a.id_goals = :id_goals and a.id_monper = '"+id_monper+"' "+tarOld+" order by CAST(a.id_target AS UNSIGNED)";
    			query = manager.createNativeQuery(sql);
    			query.setParameter("id_goals", id_goals);
            }else {
            	sql = "SELECT distinct a.id, a.nm_target, a.nm_target_eng, a.id_target FROM sdg_target a where a.id_goals = :id_goals "+tar+" order by CAST(a.id_target AS UNSIGNED)";
            	query = manager.createNativeQuery(sql);
            	query.setParameter("id_goals", id_goals);
            }
    	}
    	
        List listSdg = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("sdg",listSdg);
        return hasil;
    }
    
    @GetMapping("admin/get-sdg-indicator-report/{sdg}")
    public @ResponseBody Map<String, Object> getSdgIndicatorMon(
    		@RequestParam("id_role") String id_role,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_target") String id_target,
    		@PathVariable("sdg") String sdg) {
    	Query query;
    	Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = monper.get().getStatus();
    	
    	if(sdg.equals("0")) {
    		String role=(!id_role.equals("all"))?" and d.id_role = '"+id_role+"'":"";
    		String sql;
            if(status.equals("completed")) {sql = "SELECT distinct a.id_old, a.nm_indicator, a.nm_indicator_eng, a.id_indicator FROM history_sdg_indicator a WHERE a.id_goals = :id_goals and a.id_target = :id_target and a.id_monper = '"+id_monper+"' order by CAST(a.id_indicator AS UNSIGNED)";
        			query = manager.createNativeQuery(sql);
        			query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
            }else {sql = "SELECT distinct a.id, a.nm_indicator, a.nm_indicator_eng, a.id_indicator FROM sdg_indicator a  WHERE a.id_goals = :id_goals and a.id_target = :id_target order by CAST(a.id_indicator AS UNSIGNED)";
        			query = manager.createNativeQuery(sql);
        			query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
            }
    	}else {
    		String[] arrOfStr = sdg.split(","); 
    		StringBuffer indicator = new StringBuffer();
    		if(arrOfStr.length>0) {
    			for (int i = 0; i < arrOfStr.length; i++) {
        			String[] arrOfStr1 = arrOfStr[i].split("---");
        			int cek=1;
        			for(int j=0;j<arrOfStr1.length;j++) {
        				cek = (cek==4)?1:cek;
        				if(!arrOfStr1[j].equals("0") && cek==3) {
        					indicator.append("'"+arrOfStr1[j]+"',");
        				}
        				cek = cek+1;
        			}
        		}
    		}else{
    			String[] arrOfStr1 = sdg.split("---");
    			int cek=1;
    			for(int j=0;j<arrOfStr1.length;j++) {
    				cek = (cek==4)?1:cek;
    				if(!arrOfStr1[j].equals("0") && cek==3) {
    					indicator.append("'"+arrOfStr1[j]+"',");
    				}
    				cek = cek+1;
    			}
    		}
    		String hasilindicator = (indicator.length()==0)?"":indicator.substring(0, indicator.length() - 1);
    		String indOld = (hasilindicator.equals(""))?"":" and a.id_old in("+hasilindicator+") ";
    		String ind = (hasilindicator.equals(""))?"":" and a.id in("+hasilindicator+") ";
            String role=(!id_role.equals("all"))?" and d.id_role = '"+id_role+"'":"";
            String sql;
            if(status.equals("completed")) {sql = "SELECT distinct a.id_old, a.nm_indicator, a.nm_indicator_eng, a.id_indicator FROM history_sdg_indicator a WHERE a.id_goals = :id_goals and a.id_target = :id_target and a.id_monper = '"+id_monper+"' "+indOld+" order by CAST(a.id_indicator AS UNSIGNED)";
        			query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
            }else {sql = "SELECT distinct a.id, a.nm_indicator, a.nm_indicator_eng, a.id_indicator FROM sdg_indicator a  WHERE a.id_goals = :id_goals and a.id_target = :id_target "+ind+" order by CAST(a.id_indicator AS UNSIGNED)";
            	query = manager.createNativeQuery(sql);
                query.setParameter("id_goals", id_goals);
                query.setParameter("id_target", id_target);
            }
             
//            query = manager.createNativeQuery(sql);
//            query.setParameter("id_prov", id_prov);
//            query.setParameter("id_monper", id_monper);
//            query.setParameter("id_goals", id_goals);
//            query.setParameter("id_target", id_target);
    	}
    	
        List listSdg = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("sdg",listSdg);
        return hasil;
    }
    
    @GetMapping("admin/get-disaggre-report")
    public @ResponseBody Map<String, Object> getdisaggre(@RequestParam("id_indicator") int idindi,@RequestParam("id_monper") String id_monper) {
    	Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = monper.get().getStatus();
    	String sql;
        if(status.equals("completed")) {
        	sql = "SELECT a.id_old, b.id_old as id_det, a.id_disaggre, a.nm_disaggre, a.nm_disaggre_eng, b.desc_disaggre, b.desc_disaggre_eng "
        			+ "FROM history_sdg_ranrad_disaggre a LEFT JOIN history_sdg_ranrad_disaggre_detail b ON b.id_disaggre = a.id_old and b.id_monper = a.id_monper "
        			+ "WHERE a.id_indicator = :id_indicator and a.id_monper = '"+id_monper+"' ORDER BY a.id_old,b.id_old ASC";
        }else {
        	sql = "SELECT a.id, b.id as id_det, a.id_disaggre, a.nm_disaggre, a.nm_disaggre_eng, b.desc_disaggre, b.desc_disaggre_eng "
        			+ "FROM sdg_ranrad_disaggre a LEFT JOIN sdg_ranrad_disaggre_detail b ON b.id_disaggre = a.id "
        			+ "WHERE a.id_indicator = :id_indicator ORDER BY a.id,b.id ASC";
        }
    	 
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_indicator", idindi);
        List list = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("dis",list);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-goals-govProg")
    public @ResponseBody Map<String, Object> getMappingGoalsGovProg(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT f.id, f.id_program, f.nm_program, f.nm_program_eng, f.internal_code ");
    	sqlBud.append(" FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and (a.id_target is null or a.id_target = '') and (a.id_indicator is null or a.id_indicator = '') ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY f.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-related-nasProg")
    public @ResponseBody Map<String, Object> getMappingRelGovProg(
    		@RequestParam("id_program") String id_program,
    		@RequestParam("id_monper") String id_monper) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT b.id, b.id_program, b.nm_program, b.nm_program_eng, b.id_monper,d.nm_prov,b.internal_code FROM gov_program a\r\n" + 
    			"RIGHT JOIN gov_program b on a.id = b.rel_prog_id\r\n" + 
    			"LEFT JOIN ran_rad c on b.id_monper = c.id_monper\r\n" + 
    			"LEFT JOIN ref_province d on c.id_prov = d.id_prov\r\n" + 
    			"WHERE a.id_monper = :id_monper and a.id = :id_program\r\n" + 
    			"ORDER BY d.nm_prov,b.id");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_program", id_program);
        queryBudGov.setParameter("id_monper", id_monper);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-related-provProg")
    public @ResponseBody Map<String, Object> getMappingRelProvProg(
    		@RequestParam("id_program") String id_program,
    		@RequestParam("id_monper") String id_monper) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT a.id, a.id_program, a.nm_program, a.nm_program_eng, a.id_monper,'' as prov,a.internal_code FROM gov_program a\r\n" + 
    			"LEFT JOIN gov_program b on a.id = b.rel_prog_id\r\n" + 
    			"LEFT JOIN ran_rad c on b.id_monper = c.id_monper\r\n" + 
    			"LEFT JOIN ref_province d on c.id_prov = d.id_prov\r\n" + 
    			"WHERE b.id_monper = :id_monper and b.id = :id_program\r\n" + 
    			"ORDER BY a.id");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_program", id_program);
        queryBudGov.setParameter("id_monper", id_monper);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-target-govProg")
    public @ResponseBody Map<String, Object> getMappingTargetGovProg(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_target") String id_target) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT f.id, f.id_program, f.nm_program, f.nm_program_eng, f.internal_code ");
    	sqlBud.append(" FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and (a.id_indicator is null or a.id_indicator = '') ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY f.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_target", id_target);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-indicator-govProg")
    public @ResponseBody Map<String, Object> getMappingIndGovProg(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_target") String id_target,
    		@RequestParam("id_indicator") String id_indicator) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT f.id, f.id_program, f.nm_program, f.nm_program_eng, f.internal_code ");
    	sqlBud.append(" FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and a.id_indicator = :id_indicator ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY f.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_target", id_target);
        queryBudGov.setParameter("id_indicator", id_indicator);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-goals-nsaProg")
    public @ResponseBody Map<String, Object> getMappingGoalsNsaProg(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT f.id, f.id_program, f.nm_program, f.nm_program_eng, f.internal_code ");
    	sqlBud.append(" FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and (a.id_target is null or a.id_target = '') and (a.id_indicator is null or a.id_indicator = '') ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY f.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-target-nsaProg")
    public @ResponseBody Map<String, Object> getMappingTargetNsaProg(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_target") String id_target) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT f.id, f.id_program, f.nm_program, f.nm_program_eng, f.internal_code ");
    	sqlBud.append(" FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and (a.id_indicator is null or a.id_indicator = '') ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY f.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_target", id_target);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-indicator-nsaProg")
    public @ResponseBody Map<String, Object> getMappingIndNsaProg(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_target") String id_target,
    		@RequestParam("id_indicator") String id_indicator) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT f.id, f.id_program, f.nm_program, f.nm_program_eng, f.internal_code ");
    	sqlBud.append(" FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and a.id_indicator = :id_indicator ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY f.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_target", id_target);
        queryBudGov.setParameter("id_indicator", id_indicator);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-goals-govActivity")
    public @ResponseBody Map<String, Object> getMappingGoalsgovActivity(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_program") String id_program) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT d.id, d.id_activity, d.nm_activity, d.nm_activity_eng, d.internal_code, d.budget_allocation, g.nm_role ");
    	sqlBud.append(" FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and (a.id_target is null or a.id_target = '') and (a.id_indicator is null or a.id_indicator = '') AND c.id_program = :id_program ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY d.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_program", id_program);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-related-nasActivity")
    public @ResponseBody Map<String, Object> getMappingGoalsgovActivity(
    		@RequestParam("id_program") String id_program) {
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT a.id, a.id_activity, a.nm_activity, a.nm_activity_eng ");
    	sqlBud.append(" FROM gov_activity a\r\n" + 
    			" WHERE a.id_program = :id_program ");
    	sqlBud.append(" ORDER BY a.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_program", id_program);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-related-nasIndicator")
    public @ResponseBody Map<String, Object> getMappingGoalsgovInd(
    		@RequestParam("id_program") String id_program,
    		@RequestParam("id_activity") String id_activity) {
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT a.id, a.id_gov_indicator, a.nm_indicator, a.nm_indicator_eng ");
    	sqlBud.append(" FROM gov_indicator a\r\n" + 
    			" WHERE a.id_program = :id_program and a.id_activity = :id_activity");
    	sqlBud.append(" ORDER BY a.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_program", id_program);
        queryBudGov.setParameter("id_activity", id_activity);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-target-govActivity")
    public @ResponseBody Map<String, Object> getMappingTargetgovActivity(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_program") String id_program,
    		@RequestParam("id_target") String id_target) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT d.id, d.id_activity, d.nm_activity, d.nm_activity_eng, d.internal_code, d.budget_allocation, g.nm_role ");
    	sqlBud.append(" FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target  = :id_target and (a.id_indicator is null or a.id_indicator = '') AND c.id_program = :id_program ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY d.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_program", id_program);
        queryBudGov.setParameter("id_target", id_target);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-indicator-govActivity")
    public @ResponseBody Map<String, Object> getMappingIndgovActivity(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_program") String id_program,
    		@RequestParam("id_target") String id_target,
    		@RequestParam("id_indicator") String id_indicator) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT d.id, d.id_activity, d.nm_activity, d.nm_activity_eng, d.internal_code, d.budget_allocation, g.nm_role ");
    	sqlBud.append(" FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target  = :id_target and a.id_indicator = :id_indicator AND c.id_program = :id_program ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY d.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_program", id_program);
        queryBudGov.setParameter("id_target", id_target);
        queryBudGov.setParameter("id_indicator", id_indicator);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-goals-nsaActivity")
    public @ResponseBody Map<String, Object> getMappingGoalsnsaActivity(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_program") String id_program) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT d.id, d.id_activity, d.nm_activity, d.nm_activity_eng, d.internal_code, d.budget_allocation, g.nm_role ");
    	sqlBud.append(" FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and (a.id_target is null or a.id_target = '') and (a.id_indicator is null or a.id_indicator = '') AND c.id_program = :id_program ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY d.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_program", id_program);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-target-nsaActivity")
    public @ResponseBody Map<String, Object> getMappingTargetnsaActivity(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_program") String id_program,
    		@RequestParam("id_target") String id_target) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT d.id, d.id_activity, d.nm_activity, d.nm_activity_eng, d.internal_code, d.budget_allocation, g.nm_role ");
    	sqlBud.append(" FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and (a.id_indicator is null or a.id_indicator = '') AND c.id_program = :id_program ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY d.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_program", id_program);
        queryBudGov.setParameter("id_target", id_target);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-indicator-nsaActivity")
    public @ResponseBody Map<String, Object> getMappingIndnsaActivity(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_program") String id_program,
    		@RequestParam("id_target") String id_target,
    		@RequestParam("id_indicator") String id_indicator) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT d.id, d.id_activity, d.nm_activity, d.nm_activity_eng, d.internal_code, d.budget_allocation, g.nm_role ");
    	sqlBud.append(" FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and a.id_indicator = :id_indicator AND c.id_program = :id_program ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY d.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_program", id_program);
        queryBudGov.setParameter("id_target", id_target);
        queryBudGov.setParameter("id_indicator", id_indicator);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-goals-govIndicator")
    public @ResponseBody Map<String, Object> getMappingGoalsGovIndy(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_program") String id_program,
    		@RequestParam("id_activity") String id_activity) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT c.id, c.id_gov_indicator, c.nm_indicator, c.nm_indicator_eng,c.internal_code ");
    	sqlBud.append(" FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and (a.id_target is null or a.id_target = '') and (a.id_indicator is null or a.id_indicator = '') AND c.id_program = :id_program and c.id_activity = :id_activity ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY c.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_program", id_program);
        queryBudGov.setParameter("id_activity", id_activity);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-target-govIndicator")
    public @ResponseBody Map<String, Object> getMappingGoalsGovIndy(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_program") String id_program,
    		@RequestParam("id_activity") String id_activity,
    		@RequestParam("id_target") String id_target) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT c.id, c.id_gov_indicator, c.nm_indicator, c.nm_indicator_eng, c.internal_code ");
    	sqlBud.append(" FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and (a.id_indicator is null or a.id_indicator = '') AND c.id_program = :id_program and c.id_activity = :id_activity ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY c.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_program", id_program);
        queryBudGov.setParameter("id_activity", id_activity);
        queryBudGov.setParameter("id_target", id_target);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-indicator-govIndicator")
    public @ResponseBody Map<String, Object> getMappingGoalsGovIndy(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_program") String id_program,
    		@RequestParam("id_activity") String id_activity,
    		@RequestParam("id_target") String id_target,
    		@RequestParam("id_indicator") String id_indicator) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT c.id, c.id_gov_indicator, c.nm_indicator, c.nm_indicator_eng, c.internal_code ");
    	sqlBud.append(" FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and a.id_indicator = :id_indicator AND c.id_program = :id_program and c.id_activity = :id_activity ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY c.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_program", id_program);
        queryBudGov.setParameter("id_activity", id_activity);
        queryBudGov.setParameter("id_target", id_target);
        queryBudGov.setParameter("id_indicator", id_indicator);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-goals-nsaIndicator")
    public @ResponseBody Map<String, Object> getMappingGoalsNsaIndy(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_program") String id_program,
    		@RequestParam("id_activity") String id_activity) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT c.id, c.id_nsa_indicator, c.nm_indicator, c.nm_indicator_eng, c.interlan_code ");
    	sqlBud.append(" FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and (a.id_target is null or a.id_target = '') and (a.id_indicator is null or a.id_indicator = '') AND c.id_program = :id_program and c.id_activity = :id_activity ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY c.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_program", id_program);
        queryBudGov.setParameter("id_activity", id_activity);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-target-nsaIndicator")
    public @ResponseBody Map<String, Object> getMappingGoalsNsaIndy(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_program") String id_program,
    		@RequestParam("id_activity") String id_activity,
    		@RequestParam("id_target") String id_target) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT c.id, c.id_nsa_indicator, c.nm_indicator, c.nm_indicator_eng, c.internal_code ");
    	sqlBud.append(" FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and (a.id_indicator is null or a.id_indicator = '') AND c.id_program = :id_program and c.id_activity = :id_activity ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY c.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_program", id_program);
        queryBudGov.setParameter("id_activity", id_activity);
        queryBudGov.setParameter("id_target", id_target);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-indicator-nsaIndicator")
    public @ResponseBody Map<String, Object> getMappingGoalsNsaIndy(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_program") String id_program,
    		@RequestParam("id_activity") String id_activity,
    		@RequestParam("id_target") String id_target,
    		@RequestParam("id_indicator") String id_indicator) {
    		
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT c.id, c.id_nsa_indicator, c.nm_indicator, c.nm_indicator_eng, c.internal_code ");
    	sqlBud.append(" FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and a.id_indicator = :id_indicator AND c.id_program = :id_program and c.id_activity = :id_activity ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY c.id ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_program", id_program);
        queryBudGov.setParameter("id_activity", id_activity);
        queryBudGov.setParameter("id_target", id_target);
        queryBudGov.setParameter("id_indicator", id_indicator);
        List listProgGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("progGov",listProgGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-goals-govBudget")
    public @ResponseBody Map<String, Object> getMappingGoalsGovBudget(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("start_year") Integer start_year,
    		@RequestParam("end_year") Integer end_year,
    		@RequestParam("id_activity") String id_activity) {
    	
    	//gov
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT f.id_program, d.id_activity, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng,\r\n" + 
    			"d.budget_allocation,");
    	for(int i = start_year; i<=end_year;i++) {
    		//achievement
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '1') = 0 THEN '' \r\n "
    				+ " when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " + 
    				"ELSE (select achievement1 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement1_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement2 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement2_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement3 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement3_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement4 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement4_"+i+", ");
    	
    		//new value
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value1 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value1_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value2 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value2_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value3 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value3_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value4 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value4_"+i+", ");
    	}
    	sqlBud.append(" g.nm_role FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and (a.id_target is null or a.id_target = '') and (a.id_indicator is null or a.id_indicator = '') and d.id = :id_activity\r\n" + 
    			" ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY 1,2,3 ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_activity", id_activity);
        List listBudGov = queryBudGov.getResultList();
        
        
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("budgetGov",listBudGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-related-nasBudget")
    public @ResponseBody Map<String, Object> getMappingRelNasBudget(
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("start_year") Integer start_year,
    		@RequestParam("end_year") Integer end_year,
    		@RequestParam("id_activity") String id_activity) {
    	
    	//gov
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT f.id_program, d.id_activity, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng,\r\n" + 
    			"d.budget_allocation,");
    	for(int i = start_year; i<=end_year;i++) {
    		//achievement
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '1') = 0 THEN '' \r\n" + 
    				"ELSE (select achievement1 from entry_gov_budget where id_gov_activity = d.id and id_monper = f.id_monper and year_entry = "+i+") END as achievement1_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '2') = 0 THEN '' \r\n" + 
    				"ELSE (select achievement2 from entry_gov_budget where id_gov_activity = d.id and id_monper = f.id_monper and year_entry = "+i+") END as achievement2_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '3') = 0 THEN '' \r\n" + 
    				"ELSE (select achievement3 from entry_gov_budget where id_gov_activity = d.id and id_monper = f.id_monper and year_entry = "+i+") END as achievement3_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '4') = 0 THEN '' \r\n" + 
    				"ELSE (select achievement4 from entry_gov_budget where id_gov_activity = d.id and id_monper = f.id_monper and year_entry = "+i+") END as achievement4_"+i+", ");
    	
    		//new value
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '1') = 0 THEN '' \r\n" + 
    				"ELSE (select new_value1 from entry_gov_budget where id_gov_activity = d.id and id_monper = f.id_monper and year_entry = "+i+") END as new_value1_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '2') = 0 THEN '' \r\n" + 
    				"ELSE (select new_value2 from entry_gov_budget where id_gov_activity = d.id and id_monper = f.id_monper and year_entry = "+i+") END as new_value2_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '3') = 0 THEN '' \r\n" + 
    				"ELSE (select new_value3 from entry_gov_budget where id_gov_activity = d.id and id_monper = f.id_monper and year_entry = "+i+") END as new_value3_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '4') = 0 THEN '' \r\n" + 
    				"ELSE (select new_value4 from entry_gov_budget where id_gov_activity = d.id and id_monper = f.id_monper and year_entry = "+i+") END as new_value4_"+i+", ");
    	}
    	sqlBud.append(" g.nm_role FROM gov_activity d\r\n" + 
    			" left join gov_program f on f.id = d.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE f.id_monper = :id_monper and d.id = :id_activity\r\n" + 
    			" ");
    	sqlBud.append(" ORDER BY 1,2,3 ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_activity", id_activity);
        List listBudGov = queryBudGov.getResultList();
        
        
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("budgetGov",listBudGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-target-govBudget")
    public @ResponseBody Map<String, Object> getMappingGoalsGovBudget(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("start_year") Integer start_year,
    		@RequestParam("end_year") Integer end_year,
    		@RequestParam("id_activity") String id_activity,
    		@RequestParam("id_target") String id_target) {
    	
    	//gov
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT f.id_program, d.id_activity, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng,\r\n" + 
    			"d.budget_allocation,");
    	for(int i = start_year; i<=end_year;i++) {
    		//achievement
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement1 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement1_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement2 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement2_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement3 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement3_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement4 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement4_"+i+", ");
    	
    		//new value
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value1 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value1_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value2 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value2_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value3 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value3_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value4 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value4_"+i+", ");
    	}
    	sqlBud.append(" g.nm_role FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and (a.id_indicator is null or a.id_indicator = '') and d.id = :id_activity\r\n" + 
    			" ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY 1,2,3 ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_activity", id_activity);
        queryBudGov.setParameter("id_target", id_target);
        List listBudGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("budgetGov",listBudGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-indicator-govBudget")
    public @ResponseBody Map<String, Object> getMappingGoalsGovBudget(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("start_year") Integer start_year,
    		@RequestParam("end_year") Integer end_year,
    		@RequestParam("id_activity") String id_activity,
    		@RequestParam("id_target") String id_target,
    		@RequestParam("id_indicator") String id_indicator) {
    	
    	//gov
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT f.id_program, d.id_activity, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng,\r\n" + 
    			"d.budget_allocation,");
    	for(int i = start_year; i<=end_year;i++) {
    		//achievement
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement1 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement1_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement2 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement2_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement3 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement3_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement4 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement4_"+i+", ");
    	
    		//new value
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value1 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value1_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value2 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value2_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value3 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value3_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_budget' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value4 from entry_gov_budget where id_gov_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value4_"+i+", ");
    	}
    	sqlBud.append(" g.nm_role FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and a.id_indicator = :id_indicator and d.id = :id_activity\r\n" + 
    			" ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"' ");}
    	sqlBud.append(" ORDER BY 1,2,3 ");
    	Query queryBudGov = manager.createNativeQuery(sqlBud.toString());
        queryBudGov.setParameter("id_prov", id_prov);
        queryBudGov.setParameter("id_monper", id_monper);
        queryBudGov.setParameter("id_goals", id_goals);
        queryBudGov.setParameter("id_activity", id_activity);
        queryBudGov.setParameter("id_target", id_target);
        queryBudGov.setParameter("id_indicator", id_indicator);
        List listBudGov = queryBudGov.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("budgetGov",listBudGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-goals-nsaBudget")
    public @ResponseBody Map<String, Object> getMappingGoalsNsaBudget(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("start_year") Integer start_year,
    		@RequestParam("end_year") Integer end_year,
    		@RequestParam("id_activity") String id_activity) {
    	
    	//nsa
    	StringBuilder sqlBudNsa = new StringBuilder();
    	sqlBudNsa.append("SELECT DISTINCT f.id_program, d.id_activity, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng,\r\n" + 
    			"d.budget_allocation,");
    	for(int i = start_year; i<=end_year;i++) {
    		//achievement
    		sqlBudNsa.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement1 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement1_"+i+", ");
    		sqlBudNsa.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement2 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement2_"+i+", ");
    		sqlBudNsa.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '3') = 0 THEN '' \r\n" +
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement3 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement3_"+i+", ");
    		sqlBudNsa.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement4 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement4_"+i+", ");
    	
    		//new value
    		sqlBudNsa.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value1 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value1_"+i+", ");
    		sqlBudNsa.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value2 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value2_"+i+", ");
    		sqlBudNsa.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value3 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value3_"+i+", ");
    		sqlBudNsa.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value4 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value4_"+i+", ");
    	}
    	sqlBudNsa.append(" g.nm_role FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and (a.id_target is null or a.id_target = '') and (a.id_indicator is null or a.id_indicator = '') and d.id = :id_activity \r\n" + 
    			" ");
    	if(!id_role.equals("all")) {sqlBudNsa.append(" and d.id_role = '"+id_role+"'");}
    	sqlBudNsa.append(" ORDER BY 1,2,3 ");
    	Query queryBudNsa = manager.createNativeQuery(sqlBudNsa.toString());
    	queryBudNsa.setParameter("id_prov", id_prov);
    	queryBudNsa.setParameter("id_monper", id_monper);
    	queryBudNsa.setParameter("id_goals", id_goals);
    	queryBudNsa.setParameter("id_activity", id_activity);
        List listBudNsa = queryBudNsa.getResultList();
        
        
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("budgetGov",listBudNsa);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-target-nsaBudget")
    public @ResponseBody Map<String, Object> getMappingGoalsNsaBudget(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("start_year") Integer start_year,
    		@RequestParam("end_year") Integer end_year,
    		@RequestParam("id_activity") String id_activity,
    		@RequestParam("id_target") String id_target) {
    	
    	//nsa
    	StringBuilder sqlBudNsa = new StringBuilder();
    	sqlBudNsa.append("SELECT DISTINCT f.id_program, d.id_activity, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng,\r\n" + 
    			"d.budget_allocation,");
    	for(int i = start_year; i<=end_year;i++) {
    		//achievement
    		sqlBudNsa.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement1 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement1_"+i+", ");
    		sqlBudNsa.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement2 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement2_"+i+", ");
    		sqlBudNsa.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement3 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement3_"+i+", ");
    		sqlBudNsa.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement4 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement4_"+i+", ");
    	
    		//new value
    		sqlBudNsa.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value1 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value1_"+i+", ");
    		sqlBudNsa.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value2 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value2_"+i+", ");
    		sqlBudNsa.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value3 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value3_"+i+", ");
    		sqlBudNsa.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value4 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value4_"+i+", ");
    	}
    	sqlBudNsa.append(" g.nm_role FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and (a.id_indicator is null or a.id_indicator = '') and d.id = :id_activity \r\n" + 
    			" ");
    	if(!id_role.equals("all")) {sqlBudNsa.append(" and d.id_role = '"+id_role+"'");}
    	sqlBudNsa.append(" ORDER BY 1,2,3 ");
    	Query queryBudNsa = manager.createNativeQuery(sqlBudNsa.toString());
    	queryBudNsa.setParameter("id_prov", id_prov);
    	queryBudNsa.setParameter("id_monper", id_monper);
    	queryBudNsa.setParameter("id_goals", id_goals);
    	queryBudNsa.setParameter("id_target", id_target);
    	queryBudNsa.setParameter("id_activity", id_activity);
        List listBudNsa = queryBudNsa.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("budgetGov",listBudNsa);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-indicator-nsaBudget")
    public @ResponseBody Map<String, Object> getMappingGoalsNsaBudget(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("start_year") Integer start_year,
    		@RequestParam("end_year") Integer end_year,
    		@RequestParam("id_activity") String id_activity,
    		@RequestParam("id_target") String id_target,
    		@RequestParam("id_indicator") String id_indicator) {
    	
    	//nsa
    	StringBuilder sqlBud = new StringBuilder();
    	sqlBud.append("SELECT DISTINCT f.id_program, d.id_activity, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng,\r\n" + 
    			"d.budget_allocation,");
    	for(int i = start_year; i<=end_year;i++) {
    		//achievement
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement1 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement1_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement2 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement2_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement3 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement3_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement4 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as achievement4_"+i+", ");
    	
    		//new value
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value1 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value1_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value2 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value2_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value3 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value3_"+i+", ");
    		sqlBud.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_budget' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value4 from entry_nsa_budget where id_nsa_activity = d.id and id_monper = a.id_monper and year_entry = "+i+") END as new_value4_"+i+", ");
    	}
    	sqlBud.append(" g.nm_role FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and a.id_indicator = :id_indicator and d.id = :id_activity\r\n" + 
    			" ");
    	if(!id_role.equals("all")) {sqlBud.append(" and d.id_role = '"+id_role+"'");}
    	sqlBud.append(" ORDER BY 1,2,3 ");
    	Query queryBudNsa = manager.createNativeQuery(sqlBud.toString());
    	queryBudNsa.setParameter("id_prov", id_prov);
    	queryBudNsa.setParameter("id_monper", id_monper);
    	queryBudNsa.setParameter("id_goals", id_goals);
    	queryBudNsa.setParameter("id_target", id_target);
    	queryBudNsa.setParameter("id_indicator", id_indicator);
    	queryBudNsa.setParameter("id_activity", id_activity);
        List listBudNsa = queryBudNsa.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("budgetGov",listBudNsa);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-goals-relisasi")
    public @ResponseBody Map<String, Object> getMappingGoalsReal(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("start_year") Integer start_year,
    		@RequestParam("end_year") Integer end_year,
    		@RequestParam("id_gov_indicator") Integer id_gov_indicator) {
        
        //Indicator GOV
        StringBuilder sqlInd = new StringBuilder();
        sqlInd.append("SELECT DISTINCT f.id_program, d.id_activity, c.id_gov_indicator, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng, c.nm_indicator, c.nm_indicator_eng,h.nm_unit,i.funding_source,i.baseline,\r\n");
    	for(int i = start_year; i<=end_year;i++) {
    		//target
    		sqlInd.append("(select value from gov_target as target_"+i+" where target_"+i+".id_gov_indicator = a.id_gov_indicator and year = "+i+") as target_"+i+", ");
    		
    		//achievement
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement1 from entry_gov_indicator as achievement1_"+i+" where achievement1_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement1_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement2 from entry_gov_indicator as achievement2_"+i+" where achievement2_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement2_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement3 from entry_gov_indicator as achievement3_"+i+" where achievement3_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement3_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement4 from entry_gov_indicator as achievement4_"+i+" where achievement4_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement4_"+i+", ");
    	
    		//new value
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value1 from entry_gov_indicator as new_value1_"+i+" where new_value1_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value1_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value2 from entry_gov_indicator as new_value2_"+i+" where new_value2_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value2_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value3 from entry_gov_indicator as new_value3_"+i+" where new_value3_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value3_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value4 from entry_gov_indicator as new_value4_"+i+" where new_value4_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value4_"+i+", ");
    	}
    	sqlInd.append(" g.nm_role FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" left join ref_unit h on c.unit = h.id_unit\r\n" + 
    			" left join gov_funding i on a.id_gov_indicator = i.id_gov_indicator and a.id_monper = i.id_monper\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and (a.id_target is null or a.id_target = '') and (a.id_indicator is null or a.id_indicator = '') and a.id_gov_indicator = :id_gov_indicator \r\n" + 
    			" ");
    	if(!id_role.equals("all")) {sqlInd.append(" and d.id_role = '"+id_role+"' ");}
    	sqlInd.append(" ORDER BY 1,2,3 ");
    	Query queryIndGov = manager.createNativeQuery(sqlInd.toString());
    	queryIndGov.setParameter("id_prov", id_prov);
    	queryIndGov.setParameter("id_monper", id_monper);
    	queryIndGov.setParameter("id_goals", id_goals);
    	queryIndGov.setParameter("id_gov_indicator", id_gov_indicator);
        List listIndGov = queryIndGov.getResultList();
        
      //Indicator GOV
        StringBuilder sqlIndRan = new StringBuilder();
        sqlIndRan.append("SELECT DISTINCT f.id_program, d.id_activity, c.id_gov_indicator, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng, c.nm_indicator, c.nm_indicator_eng,h.nm_unit,i.funding_source,i.baseline,d.budget_allocation,\r\n");
    	for(int i = start_year; i<=end_year;i++) {
    		//target
    		sqlIndRan.append("(select value from gov_target as target_"+i+" where target_"+i+".id_gov_indicator = a.id_gov_indicator and year = "+i+") as target_"+i+", ");
    	}
    	sqlIndRan.append(" g.nm_role FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" left join ref_unit h on c.unit = h.id_unit\r\n" + 
    			" left join gov_funding i on a.id_gov_indicator = i.id_gov_indicator and a.id_monper = i.id_monper\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and (a.id_target is null or a.id_target = '') and (a.id_indicator is null or a.id_indicator = '') and a.id_gov_indicator = :id_gov_indicator \r\n" + 
    			" ");
    	sqlIndRan.append(" ORDER BY 1,2,3 ");
    	Query queryIndGovRan = manager.createNativeQuery(sqlIndRan.toString());
    	queryIndGovRan.setParameter("id_prov", id_prov);
    	queryIndGovRan.setParameter("id_monper", id_monper);
    	queryIndGovRan.setParameter("id_goals", id_goals);
    	queryIndGovRan.setParameter("id_gov_indicator", id_gov_indicator);
        List listIndGovRan = queryIndGovRan.getResultList();

        Map<String, Object> hasil = new HashMap<>();
        hasil.put("indGov",listIndGov);
        hasil.put("ranradGov",listIndGovRan);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-related-relisasi")
    public @ResponseBody Map<String, Object> getMappingRelReal(
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("start_year") Integer start_year,
    		@RequestParam("end_year") Integer end_year,
    		@RequestParam("id_gov_indicator") Integer id_gov_indicator) {
        
        //Indicator GOV
        StringBuilder sqlInd = new StringBuilder();
        sqlInd.append("SELECT DISTINCT f.id_program, d.id_activity, c.id_gov_indicator, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng, c.nm_indicator, c.nm_indicator_eng,h.nm_unit,i.funding_source,\r\n");
    	for(int i = start_year; i<=end_year;i++) {
    		//target
    		sqlInd.append("(select value from gov_target as target_"+i+" where target_"+i+".id_gov_indicator = c.id and year = "+i+") as target_"+i+", ");
    		
    		//achievement
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '1') = 0 THEN '' \r\n" + 
    				"ELSE (select achievement1 from entry_gov_indicator as achievement1_"+i+" where achievement1_"+i+".id_assign = c.id and id_monper = f.id_monper and year_entry = "+i+") END as achievement1_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '2') = 0 THEN '' \r\n" + 
    				"ELSE (select achievement2 from entry_gov_indicator as achievement2_"+i+" where achievement2_"+i+".id_assign = c.id and id_monper = f.id_monper and year_entry = "+i+") END as achievement2_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '3') = 0 THEN '' \r\n" + 
    				"ELSE (select achievement3 from entry_gov_indicator as achievement3_"+i+" where achievement3_"+i+".id_assign = c.id and id_monper = f.id_monper and year_entry = "+i+") END as achievement3_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '4') = 0 THEN '' \r\n" + 
    				"ELSE (select achievement4 from entry_gov_indicator as achievement4_"+i+" where achievement4_"+i+".id_assign = c.id and id_monper = f.id_monper and year_entry = "+i+") END as achievement4_"+i+", ");
    	
    		//new value
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '1') = 0 THEN '' \r\n" + 
    				"ELSE (select new_value1 from entry_gov_indicator as new_value1_"+i+" where new_value1_"+i+".id_assign = c.id and id_monper = f.id_monper and year_entry = "+i+") END as new_value1_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '2') = 0 THEN '' \r\n" + 
    				"ELSE (select new_value2 from entry_gov_indicator as new_value2_"+i+" where new_value2_"+i+".id_assign = c.id and id_monper = f.id_monper and year_entry = "+i+") END as new_value2_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '3') = 0 THEN '' \r\n" + 
    				"ELSE (select new_value3 from entry_gov_indicator as new_value3_"+i+" where new_value3_"+i+".id_assign = c.id and id_monper = f.id_monper and year_entry = "+i+") END as new_value3_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '4') = 0 THEN '' \r\n" + 
    				"ELSE (select new_value4 from entry_gov_indicator as new_value4_"+i+" where new_value4_"+i+".id_assign = c.id and id_monper = f.id_monper and year_entry = "+i+") END as new_value4_"+i+", ");
    	}
    	sqlInd.append(" g.nm_role FROM gov_indicator c \r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" left join ref_unit h on c.unit = h.id_unit\r\n" + 
    			" left join gov_funding i on c.id = i.id_gov_indicator and f.id_monper = i.id_monper\r\n" + 
    			" WHERE f.id_monper = :id_monper and c.id = :id_gov_indicator \r\n" + 
    			" ");
    	sqlInd.append(" ORDER BY 1,2,3 ");
    	Query queryIndGov = manager.createNativeQuery(sqlInd.toString());
    	queryIndGov.setParameter("id_monper", id_monper);
    	queryIndGov.setParameter("id_gov_indicator", id_gov_indicator);
        List listIndGov = queryIndGov.getResultList();

        Map<String, Object> hasil = new HashMap<>();
        hasil.put("indGov",listIndGov);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-target-relisasi")
    public @ResponseBody Map<String, Object> getMappingGoalsReal(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("start_year") Integer start_year,
    		@RequestParam("end_year") Integer end_year,
    		@RequestParam("id_gov_indicator") Integer id_gov_indicator,
    		@RequestParam("id_target") String id_target) {
        
        //Indicator GOV
        StringBuilder sqlInd = new StringBuilder();
        sqlInd.append("SELECT DISTINCT f.id_program, d.id_activity, c.id_gov_indicator, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng, c.nm_indicator, c.nm_indicator_eng,h.nm_unit,i.funding_source,i.baseline,\r\n");
    	for(int i = start_year; i<=end_year;i++) {
    		//target
    		sqlInd.append("(select value from gov_target as target_"+i+" where target_"+i+".id_gov_indicator = a.id_gov_indicator and year = "+i+") as target_"+i+", ");
    		
    		//achievement
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement1 from entry_gov_indicator as achievement1_"+i+" where achievement1_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement1_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement2 from entry_gov_indicator as achievement2_"+i+" where achievement2_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement2_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement3 from entry_gov_indicator as achievement3_"+i+" where achievement3_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement3_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement4 from entry_gov_indicator as achievement4_"+i+" where achievement4_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement4_"+i+", ");
    	
    		//new value
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value1 from entry_gov_indicator as new_value1_"+i+" where new_value1_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value1_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value2 from entry_gov_indicator as new_value2_"+i+" where new_value2_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value2_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value3 from entry_gov_indicator as new_value3_"+i+" where new_value3_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value3_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value4 from entry_gov_indicator as new_value4_"+i+" where new_value4_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value4_"+i+", ");
    	}
    	sqlInd.append(" g.nm_role FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" left join ref_unit h on c.unit = h.id_unit\r\n" + 
    			" left join gov_funding i on a.id_gov_indicator = i.id_gov_indicator and a.id_monper = i.id_monper\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and (a.id_indicator is null or a.id_indicator = '') and a.id_gov_indicator = :id_gov_indicator \r\n" + 
    			" ");
    	if(!id_role.equals("all")) {sqlInd.append(" and d.id_role = '"+id_role+"' ");}
    	sqlInd.append(" ORDER BY 1,2,3 ");
    	Query queryIndGov = manager.createNativeQuery(sqlInd.toString());
    	queryIndGov.setParameter("id_prov", id_prov);
    	queryIndGov.setParameter("id_monper", id_monper);
    	queryIndGov.setParameter("id_goals", id_goals);
    	queryIndGov.setParameter("id_target", id_target);
    	queryIndGov.setParameter("id_gov_indicator", id_gov_indicator);
        List listIndGov = queryIndGov.getResultList();
        
      //Indicator GOV
        StringBuilder sqlIndRan = new StringBuilder();
        sqlIndRan.append("SELECT DISTINCT f.id_program, d.id_activity, c.id_gov_indicator, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng, c.nm_indicator, c.nm_indicator_eng,h.nm_unit,i.funding_source,i.baseline,d.budget_allocation,\r\n");
    	for(int i = start_year; i<=end_year;i++) {
    		//target
    		sqlIndRan.append("(select value from gov_target as target_"+i+" where target_"+i+".id_gov_indicator = a.id_gov_indicator and year = "+i+") as target_"+i+", ");
    	}
    	sqlIndRan.append(" g.nm_role FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" left join ref_unit h on c.unit = h.id_unit\r\n" + 
    			" left join gov_funding i on a.id_gov_indicator = i.id_gov_indicator and a.id_monper = i.id_monper\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and (a.id_indicator is null or a.id_indicator = '') and a.id_gov_indicator = :id_gov_indicator \r\n" + 
    			" ");
    	sqlIndRan.append(" ORDER BY 1,2,3 ");
    	Query queryIndGovRan = manager.createNativeQuery(sqlIndRan.toString());
    	queryIndGovRan.setParameter("id_prov", id_prov);
    	queryIndGovRan.setParameter("id_monper", id_monper);
    	queryIndGovRan.setParameter("id_goals", id_goals);
    	queryIndGovRan.setParameter("id_target", id_target);
    	queryIndGovRan.setParameter("id_gov_indicator", id_gov_indicator);
        List listIndGovRan = queryIndGovRan.getResultList();

        Map<String, Object> hasil = new HashMap<>();
        hasil.put("indGov",listIndGov);
        hasil.put("ranradGov",listIndGovRan);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-indicator-relisasi")
    public @ResponseBody Map<String, Object> getMappingGoalsReal(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("start_year") Integer start_year,
    		@RequestParam("end_year") Integer end_year,
    		@RequestParam("id_gov_indicator") Integer id_gov_indicator,
    		@RequestParam("id_target") String id_target,
    		@RequestParam("id_indicator") String id_indicator) {
        
        //Indicator GOV
        StringBuilder sqlInd = new StringBuilder();
        sqlInd.append("SELECT DISTINCT f.id_program, d.id_activity, c.id_gov_indicator, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng, c.nm_indicator, c.nm_indicator_eng,h.nm_unit,i.funding_source,i.baseline,\r\n");
    	for(int i = start_year; i<=end_year;i++) {
    		//target
    		sqlInd.append("(select value from gov_target as target_"+i+" where target_"+i+".id_gov_indicator = a.id_gov_indicator and year = "+i+") as target_"+i+", ");
    		
    		//achievement
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement1 from entry_gov_indicator as achievement1_"+i+" where achievement1_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement1_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement2 from entry_gov_indicator as achievement2_"+i+" where achievement2_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement2_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement3 from entry_gov_indicator as achievement3_"+i+" where achievement3_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement3_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement4 from entry_gov_indicator as achievement4_"+i+" where achievement4_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement4_"+i+", ");
    	
    		//new value
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value1 from entry_gov_indicator as new_value1_"+i+" where new_value1_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value1_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value2 from entry_gov_indicator as new_value2_"+i+" where new_value2_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value2_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value3 from entry_gov_indicator as new_value3_"+i+" where new_value3_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value3_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_gov_indicator' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value4 from entry_gov_indicator as new_value4_"+i+" where new_value4_"+i+".id_assign = a.id_gov_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value4_"+i+", ");
    	}
    	sqlInd.append(" g.nm_role FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" left join ref_unit h on c.unit = h.id_unit\r\n" + 
    			" left join gov_funding i on a.id_gov_indicator = i.id_gov_indicator and a.id_monper = i.id_monper\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and a.id_indicator = :id_indicator and a.id_gov_indicator = :id_gov_indicator \r\n" + 
    			" ");
    	if(!id_role.equals("all")) {sqlInd.append(" and d.id_role = '"+id_role+"' ");}
    	sqlInd.append(" ORDER BY 1,2,3 ");
    	Query queryIndGov = manager.createNativeQuery(sqlInd.toString());
    	queryIndGov.setParameter("id_prov", id_prov);
    	queryIndGov.setParameter("id_monper", id_monper);
    	queryIndGov.setParameter("id_goals", id_goals);
    	queryIndGov.setParameter("id_target", id_target);
    	queryIndGov.setParameter("id_gov_indicator", id_gov_indicator);
    	queryIndGov.setParameter("id_indicator", id_indicator);
        List listIndGov = queryIndGov.getResultList();
        
      //Indicator GOV
        StringBuilder sqlIndRan = new StringBuilder();
        sqlIndRan.append("SELECT DISTINCT f.id_program, d.id_activity, c.id_gov_indicator, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng, c.nm_indicator, c.nm_indicator_eng,h.nm_unit,i.funding_source,i.baseline,d.budget_allocation,\r\n");
    	for(int i = start_year; i<=end_year;i++) {
    		//target
    		sqlIndRan.append("(select value from gov_target as target_"+i+" where target_"+i+".id_gov_indicator = a.id_gov_indicator and year = "+i+") as target_"+i+", ");
    	}
    	sqlIndRan.append(" g.nm_role FROM gov_map a\r\n" + 
    			" left join gov_indicator c on a.id_gov_indicator = c.id\r\n" + 
    			" left join gov_activity d on c.id_activity = d.id\r\n" + 
    			" left join gov_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" left join ref_unit h on c.unit = h.id_unit\r\n" + 
    			" left join gov_funding i on a.id_gov_indicator = i.id_gov_indicator and a.id_monper = i.id_monper\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and a.id_indicator = :id_indicator and a.id_gov_indicator = :id_gov_indicator \r\n" + 
    			" ");
    	sqlIndRan.append(" ORDER BY 1,2,3 ");
    	Query queryIndGovRan = manager.createNativeQuery(sqlIndRan.toString());
    	queryIndGovRan.setParameter("id_prov", id_prov);
    	queryIndGovRan.setParameter("id_monper", id_monper);
    	queryIndGovRan.setParameter("id_goals", id_goals);
    	queryIndGovRan.setParameter("id_target", id_target);
    	queryIndGovRan.setParameter("id_gov_indicator", id_gov_indicator);
    	queryIndGovRan.setParameter("id_indicator", id_indicator);
        List listIndGovRan = queryIndGovRan.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("indGov",listIndGov);
        hasil.put("ranradGov",listIndGovRan);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-goals-nsarelisasi")
    public @ResponseBody Map<String, Object> getMappingGoalsNsaReal(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("start_year") Integer start_year,
    		@RequestParam("end_year") Integer end_year,
    		@RequestParam("id_nsa_indicator") Integer id_nsa_indicator) {
        
    	StringBuilder sqlNsaInd = new StringBuilder();
        sqlNsaInd.append("SELECT DISTINCT f.id_program, d.id_activity, c.id_nsa_indicator, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng, c.nm_indicator, c.nm_indicator_eng,h.nm_unit,i.funding_source,i.baseline,\r\n");
    	for(int i = start_year; i<=end_year;i++) {
    		//target
    		sqlNsaInd.append("(select value from nsa_target as target_"+i+" where target_"+i+".id_nsa_indicator = a.id_nsa_indicator and year = "+i+") as target_"+i+", ");
    		
    		//achievement
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement1 from entry_nsa_indicator as achievement1_"+i+" where achievement1_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement1_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement2 from entry_nsa_indicator as achievement2_"+i+" where achievement2_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement2_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement3 from entry_nsa_indicator as achievement3_"+i+" where achievement3_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement3_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement4 from entry_nsa_indicator as achievement4_"+i+" where achievement4_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement4_"+i+", ");
    	
    		//new value
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value1 from entry_nsa_indicator as new_value1_"+i+" where new_value1_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value1_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value2 from entry_nsa_indicator as new_value2_"+i+" where new_value2_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value2_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value3 from entry_nsa_indicator as new_value3_"+i+" where new_value3_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value3_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value4 from entry_nsa_indicator as new_value4_"+i+" where new_value4_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value4_"+i+", ");
    	}
    	sqlNsaInd.append(" g.nm_role FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" left join ref_unit h on c.unit = h.id_unit\r\n" + 
    			" left join nsa_funding i on a.id_nsa_indicator = i.id_nsa_indicator and a.id_monper = i.id_monper\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and (a.id_target is null or a.id_target = '') and (a.id_indicator is null or a.id_indicator = '') and a.id_nsa_indicator = :id_nsa_indicator\r\n" + 
    			" ");
    	if(!id_role.equals("all")) {sqlNsaInd.append(" and d.id_role = '"+id_role+"' ");}
    	sqlNsaInd.append(" ORDER BY 1,2,3 ");
    	Query queryIndNsa = manager.createNativeQuery(sqlNsaInd.toString());
    	queryIndNsa.setParameter("id_prov", id_prov);
    	queryIndNsa.setParameter("id_monper", id_monper);
    	queryIndNsa.setParameter("id_goals", id_goals);
    	queryIndNsa.setParameter("id_nsa_indicator", id_nsa_indicator);
        List listIndNsa = queryIndNsa.getResultList();
        
        //Indicator GOV
        StringBuilder sqlIndRan = new StringBuilder();
        sqlIndRan.append("SELECT DISTINCT f.id_program, d.id_activity, c.id_nsa_indicator, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng, c.nm_indicator, c.nm_indicator_eng,h.nm_unit,i.funding_source,i.baseline,d.budget_allocation,\r\n");
    	for(int i = start_year; i<=end_year;i++) {
    		//target
    		sqlIndRan.append("(select value from nsa_target as target_"+i+" where target_"+i+".id_nsa_indicator = a.id_nsa_indicator and year = "+i+") as target_"+i+", ");
    	}
    	sqlIndRan.append(" g.nm_role FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" left join ref_unit h on c.unit = h.id_unit\r\n" + 
    			" left join nsa_funding i on a.id_nsa_indicator = i.id_nsa_indicator and a.id_monper = i.id_monper\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and (a.id_target is null or a.id_target = '') and (a.id_indicator is null or a.id_indicator = '') and a.id_nsa_indicator = :id_nsa_indicator \r\n" + 
    			" ");
    	sqlIndRan.append(" ORDER BY 1,2,3 ");
    	Query queryIndGovRan = manager.createNativeQuery(sqlIndRan.toString());
    	queryIndGovRan.setParameter("id_prov", id_prov);
    	queryIndGovRan.setParameter("id_monper", id_monper);
    	queryIndGovRan.setParameter("id_goals", id_goals);
    	queryIndGovRan.setParameter("id_nsa_indicator", id_nsa_indicator);
        List listIndGovRan = queryIndGovRan.getResultList();

        Map<String, Object> hasil = new HashMap<>();
        hasil.put("indGov",listIndNsa);
        hasil.put("ranradGov",listIndGovRan);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-target-nsarelisasi")
    public @ResponseBody Map<String, Object> getMappingGoalsNsaReal(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("start_year") Integer start_year,
    		@RequestParam("end_year") Integer end_year,
    		@RequestParam("id_nsa_indicator") Integer id_nsa_indicator,
    		@RequestParam("id_target") String id_target) {
        
    	StringBuilder sqlNsaInd = new StringBuilder();
        sqlNsaInd.append("SELECT DISTINCT f.id_program, d.id_activity, c.id_nsa_indicator, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng, c.nm_indicator, c.nm_indicator_eng,h.nm_unit,i.funding_source,i.baseline,\r\n");
    	for(int i = start_year; i<=end_year;i++) {
    		//target
    		sqlNsaInd.append("(select value from nsa_target as target_"+i+" where target_"+i+".id_nsa_indicator = a.id_nsa_indicator and year = "+i+") as target_"+i+", ");
    		
    		//achievement
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement1 from entry_nsa_indicator as achievement1_"+i+" where achievement1_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement1_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement2 from entry_nsa_indicator as achievement2_"+i+" where achievement2_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement2_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement3 from entry_nsa_indicator as achievement3_"+i+" where achievement3_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement3_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement4 from entry_nsa_indicator as achievement4_"+i+" where achievement4_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement4_"+i+", ");
    	
    		//new value
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value1 from entry_nsa_indicator as new_value1_"+i+" where new_value1_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value1_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value2 from entry_nsa_indicator as new_value2_"+i+" where new_value2_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value2_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value3 from entry_nsa_indicator as new_value3_"+i+" where new_value3_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value3_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value4 from entry_nsa_indicator as new_value4_"+i+" where new_value4_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value4_"+i+", ");
    	}
    	sqlNsaInd.append(" g.nm_role FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" left join ref_unit h on c.unit = h.id_unit\r\n" + 
    			" left join nsa_funding i on a.id_nsa_indicator = i.id_nsa_indicator and a.id_monper = i.id_monper\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and (a.id_indicator is null or a.id_indicator = '') and a.id_nsa_indicator = :id_nsa_indicator\r\n" + 
    			" ");
    	if(!id_role.equals("all")) {sqlNsaInd.append(" and d.id_role = '"+id_role+"' ");}
    	sqlNsaInd.append(" ORDER BY 1,2,3 ");
    	Query queryIndNsa = manager.createNativeQuery(sqlNsaInd.toString());
    	queryIndNsa.setParameter("id_prov", id_prov);
    	queryIndNsa.setParameter("id_monper", id_monper);
    	queryIndNsa.setParameter("id_goals", id_goals);
    	queryIndNsa.setParameter("id_nsa_indicator", id_nsa_indicator);
    	queryIndNsa.setParameter("id_target", id_target);
        List listIndNsa = queryIndNsa.getResultList();
        
      //Indicator GOV
        StringBuilder sqlIndRan = new StringBuilder();
        sqlIndRan.append("SELECT DISTINCT f.id_program, d.id_activity, c.id_nsa_indicator, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng, c.nm_indicator, c.nm_indicator_eng,h.nm_unit,i.funding_source,i.baseline,d.budget_allocation,\r\n");
    	for(int i = start_year; i<=end_year;i++) {
    		//target
    		sqlIndRan.append("(select value from nsa_target as target_"+i+" where target_"+i+".id_nsa_indicator = a.id_nsa_indicator and year = "+i+") as target_"+i+", ");
    	}
    	sqlIndRan.append(" g.nm_role FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" left join ref_unit h on c.unit = h.id_unit\r\n" + 
    			" left join nsa_funding i on a.id_nsa_indicator = i.id_nsa_indicator and a.id_monper = i.id_monper\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and (a.id_indicator is null or a.id_indicator = '') and a.id_nsa_indicator = :id_nsa_indicator \r\n" + 
    			" ");
    	sqlIndRan.append(" ORDER BY 1,2,3 ");
    	Query queryIndGovRan = manager.createNativeQuery(sqlIndRan.toString());
    	queryIndGovRan.setParameter("id_prov", id_prov);
    	queryIndGovRan.setParameter("id_monper", id_monper);
    	queryIndGovRan.setParameter("id_goals", id_goals);
    	queryIndGovRan.setParameter("id_nsa_indicator", id_nsa_indicator);
    	queryIndGovRan.setParameter("id_target", id_target);
        List listIndGovRan = queryIndGovRan.getResultList();

        Map<String, Object> hasil = new HashMap<>();
        hasil.put("indGov",listIndNsa);
        hasil.put("ranradGov",listIndGovRan);
        return hasil;
    }
    
    @GetMapping("admin/get-mapping-indicator-nsarelisasi")
    public @ResponseBody Map<String, Object> getMappingGoalsNsaReal(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") String id_goals,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("start_year") Integer start_year,
    		@RequestParam("end_year") Integer end_year,
    		@RequestParam("id_nsa_indicator") Integer id_nsa_indicator,
    		@RequestParam("id_target") String id_target,
    		@RequestParam("id_indicator") String id_indicator) {
        
    	StringBuilder sqlNsaInd = new StringBuilder();
        sqlNsaInd.append("SELECT DISTINCT f.id_program, d.id_activity, c.id_nsa_indicator, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng, c.nm_indicator, c.nm_indicator_eng,h.nm_unit,i.funding_source,i.baseline,\r\n");
    	for(int i = start_year; i<=end_year;i++) {
    		//target
    		sqlNsaInd.append("(select value from nsa_target as target_"+i+" where target_"+i+".id_nsa_indicator = a.id_nsa_indicator and year = "+i+") as target_"+i+", ");
    		
    		//achievement
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement1 from entry_nsa_indicator as achievement1_"+i+" where achievement1_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement1_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement2 from entry_nsa_indicator as achievement2_"+i+" where achievement2_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement2_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement3 from entry_nsa_indicator as achievement3_"+i+" where achievement3_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement3_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement4 from entry_nsa_indicator as achievement4_"+i+" where achievement4_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as achievement4_"+i+", ");
    	
    		//new value
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value1 from entry_nsa_indicator as new_value1_"+i+" where new_value1_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value1_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value2 from entry_nsa_indicator as new_value2_"+i+" where new_value2_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value2_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value3 from entry_nsa_indicator as new_value3_"+i+" where new_value3_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value3_"+i+", ");
    		sqlNsaInd.append("case when (select count(*) from entry_show_report where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = a.id_monper and year = "+i+" and type = 'entry_nsa_indicator' and id_role = d.id_role and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value4 from entry_nsa_indicator as new_value4_"+i+" where new_value4_"+i+".id_assign = a.id_nsa_indicator and id_monper = a.id_monper and year_entry = "+i+") END as new_value4_"+i+", ");
    	}
    	sqlNsaInd.append(" g.nm_role FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" left join ref_unit h on c.unit = h.id_unit\r\n" + 
    			" left join nsa_funding i on a.id_nsa_indicator = i.id_nsa_indicator and a.id_monper = i.id_monper\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and a.id_indicator = :id_indicator and a.id_nsa_indicator = :id_nsa_indicator\r\n" + 
    			" ");
    	if(!id_role.equals("all")) {sqlNsaInd.append(" and d.id_role = '"+id_role+"' ");}
    	sqlNsaInd.append(" ORDER BY 1,2,3 ");
    	Query queryIndNsa = manager.createNativeQuery(sqlNsaInd.toString());
    	queryIndNsa.setParameter("id_prov", id_prov);
    	queryIndNsa.setParameter("id_monper", id_monper);
    	queryIndNsa.setParameter("id_goals", id_goals);
    	queryIndNsa.setParameter("id_nsa_indicator", id_nsa_indicator);
    	queryIndNsa.setParameter("id_target", id_target);
    	queryIndNsa.setParameter("id_indicator", id_indicator);
        List listIndNsa = queryIndNsa.getResultList();
        
      //Indicator GOV
        StringBuilder sqlIndRan = new StringBuilder();
        sqlIndRan.append("SELECT DISTINCT f.id_program, d.id_activity, c.id_nsa_indicator, f.nm_program,\r\n" + 
    			"f.nm_program_eng, d.nm_activity, d.nm_activity_eng, c.nm_indicator, c.nm_indicator_eng,h.nm_unit,i.funding_source,i.baseline,d.budget_allocation,\r\n");
    	for(int i = start_year; i<=end_year;i++) {
    		//target
    		sqlIndRan.append("(select value from nsa_target as target_"+i+" where target_"+i+".id_nsa_indicator = a.id_nsa_indicator and year = "+i+") as target_"+i+", ");
    	}
    	sqlIndRan.append(" g.nm_role FROM nsa_map a\r\n" + 
    			" left join nsa_indicator c on a.id_nsa_indicator = c.id\r\n" + 
    			" left join nsa_activity d on c.id_activity = d.id\r\n" + 
    			" left join nsa_program f on f.id = c.id_program\r\n" + 
    			" left join ref_role g on d.id_role = g.id_role\r\n" + 
    			" left join ref_unit h on c.unit = h.id_unit\r\n" + 
    			" left join nsa_funding i on a.id_nsa_indicator = i.id_nsa_indicator and a.id_monper = i.id_monper\r\n" + 
    			" WHERE a.id_prov = :id_prov and a.id_monper = :id_monper and a.id_goals = :id_goals\r\n" + 
    			" and a.id_target = :id_target and a.id_indicator = :id_indicator and a.id_nsa_indicator = :id_nsa_indicator \r\n" + 
    			" ");
    	sqlIndRan.append(" ORDER BY 1,2,3 ");
    	Query queryIndGovRan = manager.createNativeQuery(sqlIndRan.toString());
    	queryIndGovRan.setParameter("id_prov", id_prov);
    	queryIndGovRan.setParameter("id_monper", id_monper);
    	queryIndGovRan.setParameter("id_goals", id_goals);
    	queryIndGovRan.setParameter("id_nsa_indicator", id_nsa_indicator);
    	queryIndGovRan.setParameter("id_target", id_target);
    	queryIndGovRan.setParameter("id_indicator", id_indicator);
        List listIndGovRan = queryIndGovRan.getResultList();

        Map<String, Object> hasil = new HashMap<>();
        hasil.put("indGov",listIndNsa);
        hasil.put("ranradGov",listIndGovRan);
        return hasil;
    }
    
    @GetMapping("admin/get-entry-sdg")
    public @ResponseBody Map<String, Object> getEntrySdg(
    		@RequestParam("id_indicator") String id_indicator,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("start_year") Integer start_year,
    		@RequestParam("end_year") Integer end_year) {

        StringBuilder sqlInd = new StringBuilder();
        sqlInd.append("SELECT DISTINCT c.nm_unit,d.baseline,b.increment_decrement,c.calculation,\r\n");
    	for(int i = start_year; i<=end_year;i++) {
    		//target
    		sqlInd.append("(select value from sdg_indicator_target as target_"+i+" where target_"+i+".id_monper = f.id_monper and target_"+i+".id_sdg_indicator = b.id and target_"+i+".id_role = a.id_role and year = "+i+") as target_"+i+", ");
    		
    		//achievement
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and (CASE WHEN e.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement1 from entry_sdg as achievement1_"+i+" where achievement1_"+i+".id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = "+i+") END as achievement1_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and (CASE WHEN e.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement2 from entry_sdg as achievement2_"+i+" where achievement2_"+i+".id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = "+i+") END as achievement2_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and (CASE WHEN e.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement3 from entry_sdg as achievement3_"+i+" where achievement3_"+i+".id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = "+i+") END as achievement3_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and (CASE WHEN e.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement4 from entry_sdg as achievement4_"+i+" where achievement4_"+i+".id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = "+i+") END as achievement4_"+i+", ");
    	
    		//new value
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and (CASE WHEN e.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value1 from entry_sdg as new_value1_"+i+" where new_value1_"+i+".id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = "+i+") END as new_value1_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and (CASE WHEN e.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value2 from entry_sdg as new_value2_"+i+" where new_value2_"+i+".id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = "+i+") END as new_value2_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and (CASE WHEN e.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value3 from entry_sdg as new_value3_"+i+" where new_value3_"+i+".id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = "+i+") END as new_value3_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and period = '4') = 0 THEN '' \r\n" +
    				" when (select count(*) from entry_approval where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and (CASE WHEN e.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value4 from entry_sdg as new_value4_"+i+" where new_value4_"+i+".id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = "+i+") END as new_value4_"+i+", ");
    	}
    	sqlInd.append(" CASE when e.nm_role is null then 'Unassigned' else e.nm_role end FROM sdg_indicator b\r\n" + 
    			" left join assign_sdg_indicator a on a.id_indicator = b.id and a.id_prov = :id_prov\r\n" +
    			" left join ref_unit c on b.unit = c.id_unit\r\n" + 
    			" left join sdg_funding d on d.id_monper = :id_monper and b.id = d.id_sdg_indicator\r\n" + 
    			" left join ref_role e on a.id_role = e.id_role\r\n" + 
    			" right join entry_sdg f on b.id = f.id_sdg_indicator and f.id_monper = :id_monper \r\n" + 
    			" WHERE b.id = :id_indicator \r\n" + 
    			" ");
    	Query queryIndGov = manager.createNativeQuery(sqlInd.toString());
    	queryIndGov.setParameter("id_prov", id_prov);
    	queryIndGov.setParameter("id_monper", id_monper);
    	queryIndGov.setParameter("id_indicator", id_indicator);
        List listIndGov = queryIndGov.getResultList();
        
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("indSdg",listIndGov);
        return hasil;
    }
    
    @GetMapping("admin/get-entry-sdg-detail")
    public @ResponseBody Map<String, Object> getEntrySdgDetail(
    		@RequestParam("id_indicator") String id_indicator,
    		@RequestParam("id_disaggre_detail") String id_disaggre_detail,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("start_year") Integer start_year,
    		@RequestParam("end_year") Integer end_year) {

        StringBuilder sqlInd = new StringBuilder();
        sqlInd.append("SELECT DISTINCT c.nm_unit,d.baseline,b.increment_decrement,c.calculation,\r\n");
    	for(int i = start_year; i<=end_year;i++) {
    		//target
    		sqlInd.append("(select value from sdg_indicator_target as target_"+i+" where target_"+i+".id_monper = f.id_monper and target_"+i+".id_sdg_indicator = b.id and target_"+i+".id_role = a.id_role and year = "+i+") as target_"+i+", ");
    		
    		//achievement
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and (CASE WHEN e.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement1 from entry_sdg_detail as achievement1_"+i+" where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = "+i+") END as achievement1_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and (CASE WHEN e.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement2 from entry_sdg_detail as achievement2_"+i+" where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = "+i+") END as achievement2_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and period = '3') = 0 THEN '' \r\n" +
    				" when (select count(*) from entry_approval where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and (CASE WHEN e.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement3 from entry_sdg_detail as achievement3_"+i+" where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = "+i+") END as achievement3_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and (CASE WHEN e.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select achievement4 from entry_sdg_detail as achievement4_"+i+" where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = "+i+") END as achievement4_"+i+", ");
    	
    		//new value
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and period = '1') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and (CASE WHEN e.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value1 from entry_sdg_detail as new_value1_"+i+" where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = "+i+") END as new_value1_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and period = '2') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and (CASE WHEN e.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value2 from entry_sdg_detail as new_value2_"+i+" where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = "+i+") END as new_value2_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and period = '3') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and (CASE WHEN e.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value3 from entry_sdg_detail as new_value3_"+i+" where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = "+i+") END as new_value3_"+i+", ");
    		sqlInd.append("case when (select count(*) from entry_show_report where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and period = '4') = 0 THEN '' \r\n" + 
    				" when (select count(*) from entry_approval where id_monper = f.id_monper and year = "+i+" and type = 'entry_sdg' and (CASE WHEN e.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' " +
    				"ELSE (select new_value4 from entry_sdg_detail as new_value4_"+i+" where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = "+i+") END as new_value4_"+i+", ");
    	}
    	sqlInd.append(" CASE when e.nm_role is null then 'Unassigned' else e.nm_role end FROM sdg_indicator b\r\n" + 
    			" left join assign_sdg_indicator a on a.id_indicator = b.id and a.id_prov = :id_prov\r\n" +
    			" left join ref_unit c on b.unit = c.id_unit\r\n" + 
    			" left join sdg_funding d on d.id_monper = :id_monper and b.id = d.id_sdg_indicator\r\n" + 
    			" left join ref_role e on a.id_role = e.id_role\r\n" + 
    			" left join sdg_ranrad_disaggre g on b.id = g.id_indicator\r\n" + 
    			" left join sdg_ranrad_disaggre_detail h on g.id = h.id_disaggre\r\n" + 
    			" right join entry_sdg_detail f on f.id_disaggre = g.id and f.id_disaggre_detail = h.id and f.id_monper = :id_monper \r\n" + 
    			" WHERE b.id = :id_indicator and h.id = :id_disaggre_detail \r\n" + 
    			" ");
    	Query queryIndGov = manager.createNativeQuery(sqlInd.toString());
    	queryIndGov.setParameter("id_prov", id_prov);
    	queryIndGov.setParameter("id_monper", id_monper);
    	queryIndGov.setParameter("id_indicator", id_indicator);
    	queryIndGov.setParameter("id_disaggre_detail", id_disaggre_detail);
        List listIndGov = queryIndGov.getResultList();
        
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("indSdg",listIndGov);
        return hasil;
    }
    
    @GetMapping("admin/get-sdg-target")
    public @ResponseBody Map<String, Object> getSdgTarget(@RequestParam("id_role") int id_role, @RequestParam("id_goals") int id_goals) {
    	String sql = "SELECT distinct a.id_target as id, b.nm_target, b.nm_target_eng, b.id_target FROM assign_sdg_indicator a "
    			+ " left join sdg_target b on a.id_target = b.id "
    			+ " WHERE a.id_role = :id_role and a.id_goals = :id_goals order by CAST(a.id_target AS UNSIGNED)";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_role", id_role);
        query.setParameter("id_goals", id_goals);
        List listSdg = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",listSdg);
        return hasil;
    }
    
    @GetMapping("admin/get-sdg-target/{sdg}")
    public @ResponseBody Map<String, Object> getSdgTargetEva(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_goals") int id_goals, 
    		@PathVariable("sdg") String sdg) {
    	Query query;
    	Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = (monper.isPresent())?monper.get().getStatus():"";
    	String role;
    	if(id_role.equals("all")) {
    		role = "";
    	}else if(id_role.equals("unassign")) {
    		role = " and a.id_role is null ";
    	}else {
    		role = " and a.id_role = '"+id_role+"'";
    	}
    	if(sdg.equals("0")) {
    		String sql;
    		if(status.equals("completed")) {
    			sql = "SELECT distinct d.id_old, d.nm_target, d.nm_target_eng, d.id_target "
        				+ " FROM history_sdg_indicator c "
        				+ " left join assign_sdg_indicator a on a.id_indicator = c.id_old "
            			+ " left join history_sdg_goals b on c.id_goals = b.id_old and b.id_monper = c.id_monper "
            			+ " left join history_sdg_target d on c.id_target = d.id_old and d.id_monper = c.id_monper "
            			+ " WHERE c.id_old is not null and c.id_goals = :id_goals and c.id_monper = '"+id_monper+"' "+role+" order by CAST(d.id_target AS UNSIGNED)";
    		}else {
    			sql = "SELECT distinct d.id, d.nm_target, d.nm_target_eng, d.id_target "
        				+ " FROM sdg_indicator c "
        				+ " left join assign_sdg_indicator a on a.id_indicator = c.id "
            			+ " left join sdg_goals b on c.id_goals = b.id "
            			+ " left join sdg_target d on c.id_target = d.id "
            			+ " WHERE c.id is not null and c.id_goals = :id_goals "+role+" order by CAST(d.id_target AS UNSIGNED)";
    		}
            query = manager.createNativeQuery(sql);
            query.setParameter("id_goals", id_goals);
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
    		
    		String tar = (hasiltarget.equals(""))?"":" and c.id_target in("+hasiltarget+") ";
    		String sql;
    		if(status.equals("completed")) {
    			sql = "SELECT distinct d.id_old, d.nm_target, d.nm_target_eng, d.id_target "
        				+ " FROM history_sdg_indicator c "
        				+ " left join assign_sdg_indicator a on a.id_indicator = c.id_old "
            			+ " left join history_sdg_goals b on c.id_goals = b.id_old and b.id_monper = c.id_monper "
            			+ " left join history_sdg_target d on c.id_target = d.id_old and d.id_monper = c.id_monper "
            			+ " WHERE c.id_old is not null and c.id_goals = :id_goals and c.id_monper = '"+id_monper+"' "+role+" "+tar+" order by CAST(d.id_target AS UNSIGNED)";
    		}else {
    			sql = "SELECT distinct d.id, d.nm_target, d.nm_target_eng, d.id_target "
        				+ " FROM sdg_indicator c "
        				+ " left join assign_sdg_indicator a on a.id_indicator = c.id "
            			+ " left join sdg_goals b on c.id_goals = b.id "
            			+ " left join sdg_target d on c.id_target = d.id "
            			+ " WHERE c.id is not null and c.id_goals = :id_goals "+role+" "+tar+" order by CAST(d.id_target AS UNSIGNED)";
    		}
            query = manager.createNativeQuery(sql);
            query.setParameter("id_goals", id_goals);
    	}
    	
        List listSdg = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",listSdg);
        return hasil;
    }
    
    @GetMapping("admin/get-sdg-indicator-utama/{sdg}")
    public @ResponseBody Map<String, Object> getSdgIndicatorUtama(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") int id_goals, 
    		@RequestParam("id_target") int id_target,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_prov") String id_prov,
    		@PathVariable("sdg") String sdg) {
    	Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = (monper.isPresent())?monper.get().getStatus():"";
    	String role;
    	if(id_role.equals("all") || id_role.equals("*")) {
    		role = "";
    	}else if(id_role.equals("unassign")) {
    		role = " and a.id_role is null ";
    	}else {
    		role = " and a.id_role = '"+id_role+"'";
    	}
    	String sql;
    	Query query;
    	
    	if(sdg.equals("0")) {
    		if(status.equals("completed")) {
    			sql = "SELECT distinct b.id_old, b.nm_indicator, "
            			+ " b.nm_indicator_eng, b.id_indicator, b.increment_decrement, c.nm_unit, "
            			+ " d.baseline, CASE when g.nm_role is null then 'Unassigned' else g.nm_role end "
            			+ " FROM history_sdg_indicator b "
            			+ " left join assign_sdg_indicator a on b.id_old = a.id_indicator and a.id_prov = :id_prov "
            			+ " left join ref_unit c on b.unit = c.id_unit "
            			+ " left join sdg_funding d on d.id_monper = '"+id_monper+"' and b.id_old = d.id_sdg_indicator "
            			+ " left join ref_role g on a.id_role = g.id_role "
            			+ " WHERE b.id_goals = :id_goals and b.id_target = :id_target and b.id_monper='"+id_monper+"' "+role+" order by CAST(b.id_indicator AS UNSIGNED)";
        	}else {
        		sql = "SELECT distinct b.id, b.nm_indicator, "
            			+ " b.nm_indicator_eng, b.id_indicator, b.increment_decrement, c.nm_unit, "
            			+ " d.baseline, CASE when g.nm_role is null then 'Unassigned' else g.nm_role end "
            			+ " FROM sdg_indicator b "
            			+ " left join assign_sdg_indicator a on b.id = a.id_indicator and a.id_prov = :id_prov "
            			+ " left join ref_unit c on b.unit = c.id_unit "
            			+ " left join sdg_funding d on d.id_monper = '"+id_monper+"' and b.id = d.id_sdg_indicator "
            			+ " left join ref_role g on a.id_role = g.id_role "
            			+ " WHERE b.id_goals = :id_goals and b.id_target = :id_target "+role+" order by CAST(b.id_indicator AS UNSIGNED)";
        	}        	
            query = manager.createNativeQuery(sql);
            query.setParameter("id_goals", id_goals);
            query.setParameter("id_target", id_target);
            query.setParameter("id_prov", id_prov);
    	}else {
    		String[] arrOfStr = sdg.split(","); 
    		StringBuffer indicator = new StringBuffer();
    		if(arrOfStr.length>0) {
    			for (int i = 0; i < arrOfStr.length; i++) {
        			String[] arrOfStr1 = arrOfStr[i].split("---");
        			int cek=1;
        			for(int j=0;j<arrOfStr1.length;j++) {
        				cek = (cek==4)?1:cek;
        				if(!arrOfStr1[j].equals("0") && cek==3) {
        					indicator.append("'"+arrOfStr1[j]+"',");
        				}
        				cek = cek+1;
        			}
        		}
    		}else{
    			String[] arrOfStr1 = sdg.split("---");
    			int cek=1;
    			for(int j=0;j<arrOfStr1.length;j++) {
    				cek = (cek==4)?1:cek;
    				if(!arrOfStr1[j].equals("0") && cek==3) {
    					indicator.append("'"+arrOfStr1[j]+"',");
    				}
    				cek = cek+1;
    			}
    		}
    		String hasilindicator = (indicator.length()==0)?"":indicator.substring(0, indicator.length() - 1);
    		String indOld = (hasilindicator.equals(""))?"":" and b.id_old in("+hasilindicator+") ";
    		String ind = (hasilindicator.equals(""))?"":" and b.id in("+hasilindicator+") ";
    		
    		if(status.equals("completed")) {
    			sql = "SELECT distinct b.id_old, b.nm_indicator, "
            			+ " b.nm_indicator_eng, b.id_indicator, b.increment_decrement, c.nm_unit, "
            			+ " d.baseline, CASE when g.nm_role is null then 'Unassigned' else g.nm_role end "
            			+ " FROM history_sdg_indicator b "
            			+ " left join assign_sdg_indicator a on b.id_old = a.id_indicator and a.id_prov = :id_prov "
            			+ " left join ref_unit c on b.unit = c.id_unit "
            			+ " left join sdg_funding d on d.id_monper = '"+id_monper+"' and b.id_old = d.id_sdg_indicator "
            			+ " left join ref_role g on a.id_role = g.id_role "
            			+ " WHERE b.id_goals = :id_goals and b.id_target = :id_target and b.id_monper='"+id_monper+"' "+role+" "+indOld+" order by CAST(b.id_indicator AS UNSIGNED)";
        	}else {
        		sql = "SELECT distinct b.id, b.nm_indicator, "
            			+ " b.nm_indicator_eng, b.id_indicator, b.increment_decrement, c.nm_unit, "
            			+ " d.baseline, CASE when g.nm_role is null then 'Unassigned' else g.nm_role end "
            			+ " FROM sdg_indicator b "
            			+ " left join assign_sdg_indicator a on b.id = a.id_indicator and a.id_prov = :id_prov "
            			+ " left join ref_unit c on b.unit = c.id_unit "
            			+ " left join sdg_funding d on d.id_monper = '"+id_monper+"' and b.id = d.id_sdg_indicator "
            			+ " left join ref_role g on a.id_role = g.id_role "
            			+ " WHERE b.id_goals = :id_goals and b.id_target = :id_target "+role+" "+ind+" order by CAST(b.id_indicator AS UNSIGNED)";
        	}        	
            query = manager.createNativeQuery(sql);
            query.setParameter("id_goals", id_goals);
            query.setParameter("id_target", id_target);
            query.setParameter("id_prov", id_prov);
    	}
        List listSdg = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",listSdg);
        return hasil;
    }
    
    @GetMapping("admin/get-sdg-indicator-slave")
    public @ResponseBody Map<String, Object> getSdgIndicatorSlave(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_indicator") int id_indicator, 
    		@RequestParam("year") int year,
    		@RequestParam("id_monper") int id_monper,
    		@RequestParam("id_prov") String id_prov) {
    	
    	String role;
    	if(id_role.equals("all")) {
    		role = "";
    	}else if(id_role.equals("unassign")) {
    		role = " and a.id_role is null ";
    	}else {
    		role = " and a.id_role = '"+id_role+"'";
    	}
    	    	
    	String sql = "SELECT distinct b.id, b.nm_indicator, "
    			+ " b.nm_indicator_eng, b.id_indicator, b.increment_decrement, c.nm_unit, "
    			+ " d.baseline, e.value, "
    			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
    			+ " ELSE (select achievement1 from entry_sdg as achievement1 where achievement1.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement1, "
    			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
    			+ " ELSE (select achievement2 from entry_sdg as achievement2 where achievement2.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement2, "
    			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
    			+ " ELSE (select achievement3 from entry_sdg as achievement3 where achievement3.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement3, "
    			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
    			+ " ELSE (select achievement1 from entry_sdg as achievement4 where achievement4.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement4, "
    			
    			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
    			+ " ELSE (select new_value1 from entry_sdg as achievement1 where achievement1.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value1, "
    			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
    			+ " ELSE (select new_value2 from entry_sdg as achievement2 where achievement2.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value2, "
    			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
    			+ " ELSE (select new_value3 from entry_sdg as achievement3 where achievement3.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value3, "
    			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
    			+ " ELSE (select new_value4 from entry_sdg as achievement4 where achievement4.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value4, "
    			
    			+ " c.calculation, CASE when g.nm_role is null then 'Unassigned' else g.nm_role end "
    			+ " FROM sdg_indicator b "
    			+ " left join assign_sdg_indicator a on b.id = a.id_indicator and a.id_prov = :id_prov "
    			+ " left join ref_unit c on b.unit = c.id_unit "
    			+ " left join sdg_funding d on d.id_monper = :id_monper and b.id = d.id_sdg_indicator "
    			+ " left join entry_sdg f on b.id = f.id_sdg_indicator and f.year_entry = :year and f.id_monper = :id_monper "
    			+ " left join sdg_indicator_target e on b.id = e.id_sdg_indicator and e.year = :year and e.id_monper = f.id_monper "
    			+ " left join ref_role g on a.id_role = g.id_role "
    			+ " WHERE b.id = :id_indicator "+role+" order by CAST(b.id_indicator AS UNSIGNED)";
    	
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_indicator", id_indicator);
        query.setParameter("year", year);
        query.setParameter("id_monper", id_monper);
        query.setParameter("id_prov", id_prov);
        List listSdg = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",listSdg);
        return hasil;
    }
    
    @GetMapping("admin/get-sdg-indicator/{sdg}")
    public @ResponseBody Map<String, Object> getSdgIndicator(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") int id_goals, 
    		@RequestParam("id_target") int id_target, 
    		@RequestParam("year") int year,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_prov") String id_prov,
    		@PathVariable("sdg") String sdg) {
    	
    	Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = monper.get().getStatus();
    	String role;
    	if(id_role.equals("all")) {
    		role = "";
    	}else if(id_role.equals("unassign")) {
    		role = " and a.id_role is null ";
    	}else {
    		role = " and a.id_role = '"+id_role+"'";
    	}
    	String sql;
    	Query query;
    	if(sdg.equals("0")) {
    		if(status.equals("completed")) {
    			sql = "SELECT distinct b.id_old, b.nm_indicator, "
            			+ " b.nm_indicator_eng, b.id_indicator, b.increment_decrement, c.nm_unit, "
            			+ " d.baseline, e.value, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select achievement1 from entry_sdg as achievement1 where achievement1.id_sdg_indicator = b.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement1, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select achievement2 from entry_sdg as achievement2 where achievement2.id_sdg_indicator = b.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement2, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select achievement3 from entry_sdg as achievement3 where achievement3.id_sdg_indicator = b.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement3, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select achievement1 from entry_sdg as achievement4 where achievement4.id_sdg_indicator = b.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement4, "
            			
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select new_value1 from entry_sdg as achievement1 where achievement1.id_sdg_indicator = b.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value1, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select new_value2 from entry_sdg as achievement2 where achievement2.id_sdg_indicator = b.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value2, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select new_value3 from entry_sdg as achievement3 where achievement3.id_sdg_indicator = b.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value3, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select new_value4 from entry_sdg as achievement4 where achievement4.id_sdg_indicator = b.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value4, "
            			
            			+ " c.calculation, CASE when g.nm_role is null then 'Unassigned' else g.nm_role end "
            			+ " FROM history_sdg_indicator b "
            			+ " left join assign_sdg_indicator a on b.id_old = a.id_indicator and a.id_prov = :id_prov "
            			+ " left join ref_unit c on b.unit = c.id_unit "
            			+ " left join sdg_funding d on d.id_monper = :id_monper and b.id_old = d.id_sdg_indicator "
            			+ " left join entry_sdg f on b.id_old = f.id_sdg_indicator and f.year_entry = :year and f.id_monper = :id_monper "
            			+ " left join sdg_indicator_target e on b.id_old = e.id_sdg_indicator and e.id_monper = f.id_monper and e.year = :year "
            			+ " left join ref_role g on a.id_role = g.id_role "
            			+ " WHERE b.id_goals = :id_goals and b.id_target = :id_target and b.id_monper='"+id_monper+"' "+role+" order by CAST(b.id_indicator AS UNSIGNED)";
        	}else {
        		sql = "SELECT distinct b.id, b.nm_indicator, "
            			+ " b.nm_indicator_eng, b.id_indicator, b.increment_decrement, c.nm_unit, "
            			+ " d.baseline, e.value, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select achievement1 from entry_sdg as achievement1 where achievement1.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement1, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select achievement2 from entry_sdg as achievement2 where achievement2.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement2, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select achievement3 from entry_sdg as achievement3 where achievement3.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement3, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select achievement1 from entry_sdg as achievement4 where achievement4.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement4, "
            			
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select new_value1 from entry_sdg as achievement1 where achievement1.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value1, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select new_value2 from entry_sdg as achievement2 where achievement2.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value2, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select new_value3 from entry_sdg as achievement3 where achievement3.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value3, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select new_value4 from entry_sdg as achievement4 where achievement4.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value4, "
            			
            			+ " c.calculation, CASE when g.nm_role is null then 'Unassigned' else g.nm_role end "
            			+ " FROM sdg_indicator b "
            			+ " left join assign_sdg_indicator a on b.id = a.id_indicator and a.id_prov = :id_prov "
            			+ " left join ref_unit c on b.unit = c.id_unit "
            			+ " left join sdg_funding d on d.id_monper = :id_monper and b.id = d.id_sdg_indicator "
            			+ " left join entry_sdg f on b.id = f.id_sdg_indicator and f.year_entry = :year and f.id_monper = :id_monper "
            			+ " left join sdg_indicator_target e on b.id = e.id_sdg_indicator and e.id_monper = f.id_monper and e.year = :year "
            			+ " left join ref_role g on a.id_role = g.id_role "
            			+ " WHERE b.id_goals = :id_goals and b.id_target = :id_target "+role+" order by CAST(b.id_indicator AS UNSIGNED)";
        	}        	
    		
            query = manager.createNativeQuery(sql);
            query.setParameter("id_goals", id_goals);
            query.setParameter("id_target", id_target);
            query.setParameter("year", year);
            query.setParameter("id_monper", id_monper);
            query.setParameter("id_prov", id_prov);
    	}else {
    		String[] arrOfStr = sdg.split(","); 
    		StringBuffer indicator = new StringBuffer();
    		if(arrOfStr.length>0) {
    			for (int i = 0; i < arrOfStr.length; i++) {
        			String[] arrOfStr1 = arrOfStr[i].split("---");
        			int cek=1;
        			for(int j=0;j<arrOfStr1.length;j++) {
        				cek = (cek==4)?1:cek;
        				if(!arrOfStr1[j].equals("0") && cek==3) {
        					indicator.append("'"+arrOfStr1[j]+"',");
        				}
        				cek = cek+1;
        			}
        		}
    		}else{
    			String[] arrOfStr1 = sdg.split("---");
    			int cek=1;
    			for(int j=0;j<arrOfStr1.length;j++) {
    				cek = (cek==4)?1:cek;
    				if(!arrOfStr1[j].equals("0") && cek==3) {
    					indicator.append("'"+arrOfStr1[j]+"',");
    				}
    				cek = cek+1;
    			}
    		}
    		String hasilindicator = (indicator.length()==0)?"":indicator.substring(0, indicator.length() - 1);
    		String indOld = (hasilindicator.equals(""))?"":" and b.id_old in("+hasilindicator+") ";
    		String ind = (hasilindicator.equals(""))?"":" and b.id in("+hasilindicator+") ";
    		
    		if(status.equals("completed")) {
    			sql = "SELECT distinct b.id_old, b.nm_indicator, "
            			+ " b.nm_indicator_eng, b.id_indicator, b.increment_decrement, c.nm_unit, "
            			+ " d.baseline, e.value, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select achievement1 from entry_sdg as achievement1 where achievement1.id_sdg_indicator = b.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement1, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select achievement2 from entry_sdg as achievement2 where achievement2.id_sdg_indicator = b.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement2, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select achievement3 from entry_sdg as achievement3 where achievement3.id_sdg_indicator = b.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement3, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select achievement1 from entry_sdg as achievement4 where achievement4.id_sdg_indicator = b.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement4, "
            			
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select new_value1 from entry_sdg as achievement1 where achievement1.id_sdg_indicator = b.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value1, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select new_value2 from entry_sdg as achievement2 where achievement2.id_sdg_indicator = b.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value2, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select new_value3 from entry_sdg as achievement3 where achievement3.id_sdg_indicator = b.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value3, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select new_value4 from entry_sdg as achievement4 where achievement4.id_sdg_indicator = b.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value4, "
            			
            			+ " c.calculation, CASE when g.nm_role is null then 'Unassigned' else g.nm_role end "
            			+ " FROM history_sdg_indicator b "
            			+ " left join assign_sdg_indicator a on b.id_old = a.id_indicator and a.id_prov = :id_prov "
            			+ " left join ref_unit c on b.unit = c.id_unit "
            			+ " left join sdg_funding d on d.id_monper = :id_monper and b.id_old = d.id_sdg_indicator "
            			+ " left join entry_sdg f on b.id_old = f.id_sdg_indicator and f.year_entry = :year and f.id_monper = :id_monper "
            			+ " left join sdg_indicator_target e on b.id_old = e.id_sdg_indicator and e.id_monper = f.id_monper and e.year = :year "
            			+ " left join ref_role g on a.id_role = g.id_role "
            			+ " WHERE b.id_goals = :id_goals and b.id_target = :id_target and b.id_monper='"+id_monper+"' "+role+" "+indOld+" order by CAST(b.id_indicator AS UNSIGNED)";
    			
        	}else {
        		sql = "SELECT distinct b.id, b.nm_indicator, "
            			+ " b.nm_indicator_eng, b.id_indicator, b.increment_decrement, c.nm_unit, "
            			+ " d.baseline, e.value, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select achievement1 from entry_sdg as achievement1 where achievement1.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement1, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select achievement2 from entry_sdg as achievement2 where achievement2.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement2, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select achievement3 from entry_sdg as achievement3 where achievement3.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement3, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select achievement1 from entry_sdg as achievement4 where achievement4.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement4, "
            			
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select new_value1 from entry_sdg as achievement1 where achievement1.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value1, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select new_value2 from entry_sdg as achievement2 where achievement2.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value2, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select new_value3 from entry_sdg as achievement3 where achievement3.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value3, "
            			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN g.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
            			+ " ELSE (select new_value4 from entry_sdg as achievement4 where achievement4.id_sdg_indicator = b.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value4, "
            			
            			+ " c.calculation, CASE when g.nm_role is null then 'Unassigned' else g.nm_role end "
            			+ " FROM sdg_indicator b "
            			+ " left join assign_sdg_indicator a on b.id = a.id_indicator and a.id_prov = :id_prov "
            			+ " left join ref_unit c on b.unit = c.id_unit "
            			+ " left join sdg_funding d on d.id_monper = :id_monper and b.id = d.id_sdg_indicator "
            			+ " left join entry_sdg f on b.id = f.id_sdg_indicator and f.year_entry = :year and f.id_monper = :id_monper "
            			+ " left join sdg_indicator_target e on b.id = e.id_sdg_indicator and e.id_monper = f.id_monper and e.year = :year "
            			+ " left join ref_role g on a.id_role = g.id_role "
            			+ " WHERE b.id_goals = :id_goals and b.id_target = :id_target "+role+" "+ind+" order by CAST(b.id_indicator AS UNSIGNED)";
        	}        	
            query = manager.createNativeQuery(sql);
            query.setParameter("id_goals", id_goals);
            query.setParameter("id_target", id_target);
            query.setParameter("year", year);
            query.setParameter("id_monper", id_monper);
            query.setParameter("id_prov", id_prov);
    	}
    	
        List listSdg = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",listSdg);
        return hasil;
    }
    
    @GetMapping("admin/get-sdg-disaggre")
    public @ResponseBody Map<String, Object> getSdgDisaggre(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") int id_goals, 
    		@RequestParam("id_target") int id_target, 
    		@RequestParam("year") int year,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_indicator") int id_indicator,
    		@RequestParam("id_prov") String id_prov) {
    	
    	Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = monper.get().getStatus();
    	String role;
    	if(id_role.equals("all")) {
    		role = "";
    	}else if(id_role.equals("unassign")) {
    		role = " and a.id_role is null ";
    	}else {
    		role = " and a.id_role = '"+id_role+"'";
    	}
    	String sql;
    	
    	if(status.equals("completed")) {
    		sql = "SELECT distinct b.id_old as id, b.nm_indicator, "
        			+ " b.nm_indicator_eng, b.id_indicator, b.increment_decrement, c.nm_unit, "
        			+ " d.baseline, e.value, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select achievement1 from entry_sdg_detail as achievement1 where id_disaggre = g.id_old and id_disaggre_detail = h.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement1, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select achievement2 from entry_sdg_detail as achievement2 where id_disaggre = g.id_old and id_disaggre_detail = h.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement2, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select achievement3 from entry_sdg_detail as achievement3 where id_disaggre = g.id_old and id_disaggre_detail = h.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement3, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select achievement1 from entry_sdg_detail as achievement4 where id_disaggre = g.id_old and id_disaggre_detail = h.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement4, "
        			
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select new_value1 from entry_sdg_detail as achievement1 where id_disaggre = g.id_old and id_disaggre_detail = h.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value1, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select new_value2 from entry_sdg_detail as achievement2 where id_disaggre = g.id_old and id_disaggre_detail = h.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value2, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select new_value3 from entry_sdg_detail as achievement3 where id_disaggre = g.id_old and id_disaggre_detail = h.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value3, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select new_value4 from entry_sdg_detail as achievement4 where id_disaggre = g.id_old and id_disaggre_detail = h.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value4, "
        			
        			+ " g.nm_disaggre, g.nm_disaggre_eng, "
        			+ " h.desc_disaggre, h.desc_disaggre_eng, c.calculation, CASE when i.nm_role is null then 'Unassigned' else i.nm_role end  "
        			+ " FROM history_sdg_indicator b "
        			+ " left join assign_sdg_indicator a on a.id_indicator = b.id_old and a.id_prov = :id_prov "
        			+ " left join ref_unit c on b.unit = c.id_unit "
        			+ " left join sdg_funding d on d.id_monper = :id_monper and b.id_old = d.id_sdg_indicator "
        			+ " left join sdg_indicator_target e on e.id_monper = :id_monper and b.id_old = e.id_sdg_indicator and a.id_role = e.id_role and e.year = :year "
        			+ " right join history_sdg_ranrad_disaggre g on b.id_old = g.id_indicator "
        			+ " left join history_sdg_ranrad_disaggre_detail h on g.id_old = h.id_disaggre "
        			+ " left join entry_sdg_detail f on h.id_disaggre = f.id_disaggre and h.id_old = f.id_disaggre_detail and a.id_role = f.id_role and f.year_entry = :year and f.id_monper = :id_monper "
        			+ " left join ref_role i on a.id_role = i.id_role "
        			+ " WHERE b.id_goals = :id_goals and b.id_target = :id_target and b.id_old = :id_indicator and b.id_monper='"+id_monper+"' "+role+" order by b.id_old";
    	}else {
    		sql = "SELECT distinct b.id as id, b.nm_indicator, "
        			+ " b.nm_indicator_eng, b.id_indicator, b.increment_decrement, c.nm_unit, "
        			+ " d.baseline, e.value, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select achievement1 from entry_sdg_detail as achievement1 where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement1, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select achievement2 from entry_sdg_detail as achievement2 where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement2, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select achievement3 from entry_sdg_detail as achievement3 where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement3, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select achievement1 from entry_sdg_detail as achievement4 where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement4, "
        			
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select new_value1 from entry_sdg_detail as achievement1 where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value1, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select new_value2 from entry_sdg_detail as achievement2 where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value2, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select new_value3 from entry_sdg_detail as achievement3 where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value3, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select new_value4 from entry_sdg_detail as achievement4 where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value4, "
        			
        			+ " g.nm_disaggre, g.nm_disaggre_eng, "
        			+ " h.desc_disaggre, h.desc_disaggre_eng, c.calculation, CASE when i.nm_role is null then 'Unassigned' else i.nm_role end  "
        			+ " FROM sdg_indicator b "
        			+ " left join assign_sdg_indicator a on a.id_indicator = b.id and a.id_prov = :id_prov "
        			+ " left join ref_unit c on b.unit = c.id_unit "
        			+ " left join sdg_funding d on d.id_monper = :id_monper and b.id = d.id_sdg_indicator "
        			+ " left join sdg_indicator_target e on e.id_monper = :id_monper and b.id = e.id_sdg_indicator and a.id_role = e.id_role and e.year = :year "
        			+ " right join sdg_ranrad_disaggre g on b.id = g.id_indicator "
        			+ " left join sdg_ranrad_disaggre_detail h on g.id = h.id_disaggre "
        			+ " left join entry_sdg_detail f on h.id_disaggre = f.id_disaggre and h.id = f.id_disaggre_detail and a.id_role = f.id_role and f.year_entry = :year and f.id_monper = :id_monper "
        			+ " left join ref_role i on a.id_role = i.id_role "
        			+ " WHERE b.id_goals = :id_goals and b.id_target = :id_target and b.id = :id_indicator "+role+" order by b.id";
    	}
    	
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", id_goals);
        query.setParameter("id_target", id_target);
        query.setParameter("year", year);
        query.setParameter("id_monper", id_monper);
        query.setParameter("id_indicator", id_indicator);
        query.setParameter("id_prov", id_prov);
        List listSdg = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",listSdg);
        return hasil;
    }
    
    @GetMapping("admin/get-sdg-disaggre-utama")
    public @ResponseBody Map<String, Object> getSdgDisaggreUtama(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") int id_goals, 
    		@RequestParam("id_target") int id_target, 
    		@RequestParam("id_indicator") int id_indicator,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_monper") String id_monper) {
    	
    	Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = (monper.isPresent())?monper.get().getStatus():"";
    	String role;
    	if(id_role.equals("all")) {
    		role = "";
    	}else if(id_role.equals("unassign")) {
    		role = " and a.id_role is null ";
    	}else {
    		role = " and a.id_role = '"+id_role+"'";
    	}
    	Query query;
    	if(status.equals("completed")) {
    		String sql = "SELECT distinct b.id_old as id, b.nm_indicator, "
        			+ " b.nm_indicator_eng, b.id_indicator, b.increment_decrement, c.nm_unit, "
        			+ " d.baseline, '' as value, '' as achievement1, '' as achievement2, '' as achievement3, '' as achievement4, "
        			+ " '' as new_value1, '' as new_value2, '' as new_value3, '' as new_value4, g.nm_disaggre, g.nm_disaggre_eng, "
        			+ " h.desc_disaggre, h.desc_disaggre_eng, c.calculation, h.id_disaggre, h.id_old as id_dis_detail, CASE when i.nm_role is null then 'Unassigned' else i.nm_role end "
        			+ " FROM history_sdg_indicator b "
        			+ " left join assign_sdg_indicator a on a.id_indicator = b.id_old and a.id_prov = :id_prov "
        			+ " left join ref_unit c on b.unit = c.id_unit "
        			+ " left join sdg_funding d on d.id_monper = '"+id_monper+"' and b.id_old = d.id_sdg_indicator "
        			+ " right join history_sdg_ranrad_disaggre g on g.id_monper = '"+id_monper+"' and b.id_old = g.id_indicator "
        			+ " left join history_sdg_ranrad_disaggre_detail h on h.id_monper = '"+id_monper+"' and g.id_old = h.id_disaggre "
        			+ " left join ref_role i on a.id_role = i.id_role "
        			+ " WHERE b.id_goals = :id_goals and b.id_target = :id_target and b.id_old = :id_indicator and and b.id_monper='"+id_monper+"' "+role+" order by b.id_old";
            query = manager.createNativeQuery(sql);
            query.setParameter("id_goals", id_goals);
            query.setParameter("id_target", id_target);
            query.setParameter("id_indicator", id_indicator);
            query.setParameter("id_prov", id_prov);
    	}else {
    		String sql = "SELECT distinct b.id, b.nm_indicator, "
        			+ " b.nm_indicator_eng, b.id_indicator, b.increment_decrement, c.nm_unit, "
        			+ " d.baseline, '' as value, '' as achievement1, '' as achievement2, '' as achievement3, '' as achievement4, "
        			+ " '' as new_value1, '' as new_value2, '' as new_value3, '' as new_value4, g.nm_disaggre, g.nm_disaggre_eng, "
        			+ " h.desc_disaggre, h.desc_disaggre_eng, c.calculation, h.id_disaggre, h.id as id_dis_detail, CASE when i.nm_role is null then 'Unassigned' else i.nm_role end "
        			+ " FROM sdg_indicator b "
        			+ " left join assign_sdg_indicator a on a.id_indicator = b.id and a.id_prov = :id_prov "
        			+ " left join ref_unit c on b.unit = c.id_unit "
        			+ " left join sdg_funding d on d.id_monper = '"+id_monper+"' and b.id = d.id_sdg_indicator "
        			+ " right join sdg_ranrad_disaggre g on b.id = g.id_indicator "
        			+ " left join sdg_ranrad_disaggre_detail h on g.id = h.id_disaggre "
        			+ " left join ref_role i on a.id_role = i.id_role "
        			+ " WHERE b.id_goals = :id_goals and b.id_target = :id_target and b.id = :id_indicator "+role+" order by b.id";
            query = manager.createNativeQuery(sql);
            query.setParameter("id_goals", id_goals);
            query.setParameter("id_target", id_target);
            query.setParameter("id_indicator", id_indicator);
            query.setParameter("id_prov", id_prov);
    	}
    	
        List listSdg = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",listSdg);
        return hasil;
    }
    
    @GetMapping("admin/get-sdg-disaggre-slave")
    public @ResponseBody Map<String, Object> getSdgDisaggreSlave(
    		@RequestParam("id_role") String id_role, 
    		@RequestParam("id_goals") int id_goals, 
    		@RequestParam("id_target") int id_target, 
    		@RequestParam("year") int year,
    		@RequestParam("id_monper") String id_monper,
    		@RequestParam("id_indicator") int id_indicator,
    		@RequestParam("id_prov") String id_prov,
    		@RequestParam("id_dis") String id_dis,
    		@RequestParam("id_dis_detail") String id_dis_detail) {
    	
    	Optional<RanRad> monper = radService.findOne(Integer.parseInt(id_monper));
    	String status = (monper.isPresent())?monper.get().getStatus():"";
    	String role;
    	if(id_role.equals("all")) {
    		role = "";
    	}else if(id_role.equals("unassign")) {
    		role = " and a.id_role is null ";
    	}else {
    		role = " and a.id_role = '"+id_role+"'";
    	}
    	Query query;
    	String sql;
    	if(status.equals("completed")) {
    		sql = "SELECT distinct b.id_old as id, b.nm_indicator, "
        			+ " b.nm_indicator_eng, b.id_indicator, b.increment_decrement, c.nm_unit, "
        			+ " d.baseline, e.value, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select achievement1 from entry_sdg_detail as achievement1 where id_disaggre = g.id_old and id_disaggre_detail = h.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement1, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select achievement2 from entry_sdg_detail as achievement2 where id_disaggre = g.id_old and id_disaggre_detail = h.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement2, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select achievement3 from entry_sdg_detail as achievement3 where id_disaggre = g.id_old and id_disaggre_detail = h.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement3, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select achievement1 from entry_sdg_detail as achievement4 where id_disaggre = g.id_old and id_disaggre_detail = h.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement4, "
        			
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select new_value1 from entry_sdg_detail as achievement1 where id_disaggre = g.id_old and id_disaggre_detail = h.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value1, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select new_value2 from entry_sdg_detail as achievement2 where id_disaggre = g.id_old and id_disaggre_detail = h.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value2, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select new_value3 from entry_sdg_detail as achievement3 where id_disaggre = g.id_old and id_disaggre_detail = h.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value3, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select new_value4 from entry_sdg_detail as achievement4 where id_disaggre = g.id_old and id_disaggre_detail = h.id_old and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value4, "
        			
        			+ " g.nm_disaggre, g.nm_disaggre_eng, "
        			+ " h.desc_disaggre, h.desc_disaggre_eng, c.calculation, CASE when i.nm_role is null then 'Unassigned' else i.nm_role end  "
        			+ " FROM history_sdg_indicator b "
        			+ " left join assign_sdg_indicator a on a.id_indicator = b.id_old and a.id_prov = :id_prov "
        			+ " left join ref_unit c on b.unit = c.id_unit "
        			+ " left join sdg_funding d on d.id_monper = :id_monper and b.id_old = d.id_sdg_indicator "
        			+ " left join sdg_indicator_target e on e.id_monper = :id_monper and b.id_old = e.id_sdg_indicator and a.id_role = e.id_role and e.year = :year "
        			+ " right join history_sdg_ranrad_disaggre g on b.id_old = g.id_indicator "
        			+ " left join history_sdg_ranrad_disaggre_detail h on g.id_old = h.id_disaggre "
        			+ " left join entry_sdg_detail f on h.id_disaggre = f.id_disaggre and h.id_old = f.id_disaggre_detail and a.id_role = f.id_role and f.year_entry = :year and f.id_monper = :id_monper "
        			+ " left join ref_role i on a.id_role = i.id_role "
        			+ " WHERE b.id_goals = :id_goals and b.id_target = :id_target and b.id_old = :id_indicator and g.id_old = :id_dis and h.id_old = :id_dis_detail and and b.id_monper='"+id_monper+"' "+role+" order by b.id_old";
    	}else {
    		sql = "SELECT distinct b.id, b.nm_indicator, "
        			+ " b.nm_indicator_eng, b.id_indicator, b.increment_decrement, c.nm_unit, "
        			+ " d.baseline, e.value, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select achievement1 from entry_sdg_detail as achievement1 where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement1, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select achievement2 from entry_sdg_detail as achievement2 where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement2, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select achievement3 from entry_sdg_detail as achievement3 where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement3, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select achievement1 from entry_sdg_detail as achievement4 where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = f.year_entry) END as achievement4, "
        			
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '1' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select new_value1 from entry_sdg_detail as achievement1 where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value1, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '2' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select new_value2 from entry_sdg_detail as achievement2 where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value2, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '3' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select new_value3 from entry_sdg_detail as achievement3 where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value3, "
        			+ " case when (select count(*) from entry_approval where id_monper = f.id_monper and year = f.year_entry and type = 'entry_sdg' and (CASE WHEN i.nm_role is null THEN id_role is null ELSE id_role = f.id_role END) and periode = '4' and approval <> 3) = 0 THEN '' "
        			+ " ELSE (select new_value4 from entry_sdg_detail as achievement4 where id_disaggre = g.id and id_disaggre_detail = h.id and id_monper = f.id_monper and year_entry = f.year_entry) END as new_value4, "
        			
        			+ " g.nm_disaggre, g.nm_disaggre_eng, "
        			+ " h.desc_disaggre, h.desc_disaggre_eng, c.calculation "
        			+ " FROM sdg_indicator b "
        			+ " left join assign_sdg_indicator a on a.id_indicator = b.id and a.id_prov = :id_prov "
        			+ " left join ref_unit c on b.unit = c.id_unit "
        			+ " left join sdg_funding d on d.id_monper = :id_monper and b.id = d.id_sdg_indicator "
        			+ " left join sdg_indicator_target e on e.id_monper = :id_monper and b.id = e.id_sdg_indicator and a.id_role = e.id_role and e.year = :year "
        			+ " right join sdg_ranrad_disaggre g on b.id = g.id_indicator "
        			+ " left join sdg_ranrad_disaggre_detail h on g.id = h.id_disaggre "
        			+ " left join entry_sdg_detail f on h.id_disaggre = f.id_disaggre and h.id = f.id_disaggre_detail and a.id_role = f.id_role and f.year_entry = :year and f.id_monper = :id_monper "
        			+ " left join ref_role i on a.id_role = i.id_role "
        			+ " WHERE b.id_goals = :id_goals and b.id_target = :id_target and b.id = :id_indicator and g.id = :id_dis and h.id = :id_dis_detail "+role+" order by b.id";
            
    	}
    	query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        query.setParameter("id_goals", id_goals);
        query.setParameter("id_target", id_target);
        query.setParameter("year", year);
        query.setParameter("id_monper", id_monper);
        query.setParameter("id_indicator", id_indicator);
        query.setParameter("id_dis", id_dis);
        query.setParameter("id_dis_detail", id_dis_detail);
        List listSdg = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",listSdg);
        return hasil;
    }
    
    @GetMapping("admin/getentrysdgyear")
    public @ResponseBody List<Object> getentrysdgyear(@RequestParam("id_role") int idrole, @RequestParam("id_monper") int idmonper, 
    		@RequestParam("year_entry") int year) {
    	String sql = "SELECT a.*, b.sdg_indicator, b.start_year, c.nm_goals, c.nm_goals_eng, "
    			+ "d.nm_target, d.nm_target_eng, e.nm_indicator, e.nm_indicator_eng, "
    			+ "f.funding_source, g.nm_unit, h.value AS val1, i.value AS val2, j.value AS val3, k.value AS val4, l.value AS val5, "
    			+ "m.achievement1 AS ach1, n.achievement1 AS ach2, o.achievement1 AS ach3, p.achievement1 as ach4 "
    			+ "FROM entry_sdg a LEFT JOIN "
    			+ "ran_rad b ON b.id_monper = :id_monper LEFT JOIN "
    			+ "sdg_goals c ON c.id = (SELECT id_goals FROM sdg_indicator WHERE id = a.id_sdg_indicator) LEFT JOIN "
    			+ "sdg_target d ON d.id = (SELECT id_target FROM sdg_indicator WHERE id = a.id_sdg_indicator) LEFT JOIN "
    			+ "sdg_indicator e ON e.id = a.id_sdg_indicator LEFT JOIN "
    			+ "sdg_funding f ON f.id_monper = :id_monper and f.id_sdg_indicator = a.id_sdg_indicator LEFT JOIN "
    			+ "ref_unit g ON g.id_unit = (SELECT unit FROM sdg_indicator WHERE id = a.id_sdg_indicator) LEFT JOIN "
    			+ "sdg_indicator_target h ON h.id_monper = :id_monper and h.id_sdg_indicator = a.id_sdg_indicator AND h.id_role = :id_role AND h.year = :year_entry+0 LEFT JOIN "
    			+ "sdg_indicator_target i ON i.id_monper = :id_monper and i.id_sdg_indicator = a.id_sdg_indicator AND i.id_role = :id_role AND i.year = :year_entry+1 LEFT JOIN "
    			+ "sdg_indicator_target j ON j.id_monper = :id_monper and j.id_sdg_indicator = a.id_sdg_indicator AND j.id_role = :id_role AND j.year = :year_entry+2 LEFT JOIN "
    			+ "sdg_indicator_target k ON k.id_monper = :id_monper and k.id_sdg_indicator = a.id_sdg_indicator AND k.id_role = :id_role AND k.year = :year_entry+3 LEFT JOIN "
    			+ "sdg_indicator_target l ON l.id_monper = :id_monper and l.id_sdg_indicator = a.id_sdg_indicator AND l.id_role = :id_role AND l.year = :year_entry+4 LEFT JOIN "
    			+ "entry_sdg m ON m.id_role = :id_role AND m.id_monper = :id_monper AND m.year_entry = :year_entry+1 LEFT JOIN "
    			+ "entry_sdg n ON n.id_role = :id_role AND n.id_monper = :id_monper AND n.year_entry = :year_entry+2 LEFT JOIN "
    			+ "entry_sdg o ON o.id_role = :id_role AND o.id_monper = :id_monper AND o.year_entry = :year_entry+3 LEFT JOIN "
    			+ "entry_sdg p ON p.id_role = :id_role AND p.id_monper = :id_monper AND p.year_entry = :year_entry+4 "
    			+ "WHERE a.id_role = :id_role AND a.id_monper = :id_monper AND a.year_entry = :year_entry";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_role", idrole);
        query.setParameter("id_monper", idmonper);
        query.setParameter("year_entry", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgovindicator")
    public @ResponseBody Map<String, Object> getgovindicator(@RequestParam("id_prov") String idprov, 
    		@RequestParam("id_goals") int id_goals,
    		@RequestParam("id_target") int id_target,
    		@RequestParam("id_sdg_indicator") int idsdgindikator, 
    		@RequestParam("id_monper") int idmonper, 
    		@RequestParam("id_role") int idrole,
    		@RequestParam("year") int year) {
    	String sql = "select e.nm_program, e.nm_program_eng, f.nm_activity, f.nm_activity_eng, "
    			+ "d.nm_indicator, d.nm_indicator_eng, g.nm_unit, h.value, b.achievement1, b.achievement2, b.achievement3, "
    			+ "b.achievement4, c.achievement1 as bud1, c.achievement2 as bud2, c.achievement3 as bud3, c.achievement4 as bud4, "
    			+ "i.funding_source, b.new_value1, b.new_value2, b.new_value3, b.new_value4, c.new_value1 as newbud1, "
    			+ "c.new_value2 as newbud2, c.new_value3 as newbud3, c.new_value4 as newbud4, a.id, j.nm_role, j.cat_role "
    			+ "from gov_map a "
    			+ "left join entry_gov_indicator b on a.id_gov_indicator = b.id_assign and b.year_entry = :year "
    			+ "left join gov_indicator d on a.id_gov_indicator = d.id "
    			+ "left join entry_gov_budget c on d.id_activity = c.id_gov_activity and c.year_entry = :year "
    			+ "left join gov_program e on d.id_program = e.id "
    			+ "left join gov_activity f on d.id_activity = f.id "
    			+ "left join ref_unit g on d.unit = g.id_unit "
    			+ "left join gov_target h on a.id_gov_indicator = h.id_gov_indicator and year = :year "
    			+ "left join gov_funding i on a.id_gov_indicator = i.id_gov_indicator and a.id_monper = i.id_monper "
    			+ "left join ref_role j on j.id_role = f.id_role "
    			+ "where a.id_prov = :id_prov and a.id_monper = :id_monper "
    			+ "and a.id_goals = :id_goals and (a.id_target = :id_target or a.id_target = '' or a.id_target is null) and (a.id_indicator = :id_indicator or a.id_indicator='' or a.id_indicator is null) "
    			+ "and f.id_role = :id_role ";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        query.setParameter("id_goals", id_goals);
        query.setParameter("id_target", id_target);
        query.setParameter("id_indicator", idsdgindikator);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_role", idrole);
        query.setParameter("year", year);
        List list = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/getgovindicatorisi")
    public @ResponseBody Map<String, Object> getgovindicatorisi(@RequestParam("id") Integer id, @RequestParam("year") Integer year) {
    	String sql = "select e.nm_program, e.nm_program_eng, f.nm_activity, f.nm_activity_eng, "
    			+ "d.nm_indicator, d.nm_indicator_eng, g.nm_unit, h.value, b.achievement1, b.achievement2, b.achievement3, "
    			+ "b.achievement4, c.achievement1 as bud1, c.achievement2 as bud2, c.achievement3 as bud3, c.achievement4 as bud4, "
    			+ "i.funding_source, b.new_value1, b.new_value2, b.new_value3, b.new_value4, c.new_value1 as newbud1, "
    			+ "c.new_value2 as newbud2, c.new_value3 as newbud3, c.new_value4 as newbud4 "
    			+ "from gov_map a "
    			+ "left join entry_gov_indicator b on a.id_gov_indicator = b.id_assign and b.year_entry = :year "
    			+ "left join gov_indicator d on a.id_gov_indicator = d.id "
    			+ "left join entry_gov_budget c on d.id_activity = c.id_gov_activity and c.year_entry = :year "
    			+ "left join gov_program e on d.id_program = e.id "
    			+ "left join gov_activity f on d.id_activity = f.id "
    			+ "left join ref_unit g on d.unit = g.id_unit "
    			+ "left join gov_target h on a.id_gov_indicator = h.id_gov_indicator and year = :year "
    			+ "left join gov_funding i on a.id_gov_indicator = i.id_gov_indicator and a.id_monper = i.id_monper "
    			+ "where a.id = :id ";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id", id);
        query.setParameter("year", year);
        List list = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/getnsaindicator")
    public @ResponseBody Map<String, Object> getnsaindicator(@RequestParam("id_prov") String idprov, 
    		@RequestParam("id_goals") int id_goals,
    		@RequestParam("id_target") int id_target,
    		@RequestParam("id_sdg_indicator") int idsdgindikator, 
    		@RequestParam("id_monper") int idmonper, 
    		@RequestParam("id_role") int idrole,
    		@RequestParam("year") int year) {
    	String sql = "select e.nm_program, e.nm_program_eng, f.nm_activity, f.nm_activity_eng, "
    			+ "d.nm_indicator, d.nm_indicator_eng, g.nm_unit, h.value, b.achievement1, b.achievement2, b.achievement3, "
    			+ "b.achievement4, c.achievement1 as bud1, c.achievement2 as bud2, c.achievement3 as bud3, c.achievement4 as bud4, "
    			+ "i.funding_source, b.new_value1, b.new_value2, b.new_value3, b.new_value4, c.new_value1 as newbud1, "
    			+ "c.new_value2 as newbud2, c.new_value3 as newbud3, c.new_value4 as newbud4, a.id, j.nm_role, j.cat_role "
    			+ "from nsa_map a "
    			+ "left join entry_nsa_indicator b on a.id_nsa_indicator = b.id_assign and b.year_entry = :year "
    			+ "left join nsa_indicator d on a.id_nsa_indicator = d.id "
    			+ "left join entry_nsa_budget c on d.id_activity = c.id_nsa_activity and c.year_entry = :year "
    			+ "left join nsa_program e on d.id_program = e.id "
    			+ "left join nsa_activity f on d.id_activity = f.id "
    			+ "left join ref_unit g on d.unit = g.id_unit "
    			+ "left join nsa_target h on a.id_nsa_indicator = h.id_nsa_indicator and year = :year "
    			+ "left join nsa_funding i on a.id_nsa_indicator = i.id_nsa_indicator and a.id_monper = i.id_monper "
    			+ "left join ref_role j on j.id_role = f.id_role "
    			+ "where a.id_prov = :id_prov and a.id_monper = :id_monper "
    			+ "and a.id_goals = :id_goals and (a.id_target = :id_target or a.id_target = '' or a.id_target is null) and (a.id_indicator = :id_indicator or a.id_indicator='' or a.id_indicator is null) "
    			+ "and f.id_role = :id_role ";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_prov", idprov);
        query.setParameter("id_goals", id_goals);
        query.setParameter("id_target", id_target);
        query.setParameter("id_indicator", idsdgindikator);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_role", idrole);
        query.setParameter("year", year);
        List list = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/getallindicatorreport")
    public @ResponseBody Map<String, Object> getallindicator(@RequestParam("id_prov") String idprov, 
    		@RequestParam("id_goals") int id_goals,
    		@RequestParam("id_target") int id_target,
    		@RequestParam("id_sdg_indicator") int idsdgindikator, 
    		@RequestParam("id_monper") int idmonper, 
    		@RequestParam("id_role") String idrole,
    		@RequestParam("year") int year) {
    	String sql = "select e.nm_program, e.nm_program_eng, f.nm_activity, f.nm_activity_eng, "
    			+ "d.nm_indicator, d.nm_indicator_eng, g.nm_unit, h.value, b.achievement1, b.achievement2, b.achievement3, "
    			+ "b.achievement4, c.achievement1 as bud1, c.achievement2 as bud2, c.achievement3 as bud3, c.achievement4 as bud4, "
    			+ "i.funding_source, b.new_value1, b.new_value2, b.new_value3, b.new_value4, c.new_value1 as newbud1, "
    			+ "c.new_value2 as newbud2, c.new_value3 as newbud3, c.new_value4 as newbud4, a.id, j.nm_role, j.cat_role "
    			+ "from gov_map a "
    			+ "left join entry_gov_indicator b on a.id_gov_indicator = b.id_assign and b.year_entry = :year "
    			+ "left join gov_indicator d on a.id_gov_indicator = d.id "
    			+ "left join entry_gov_budget c on d.id_activity = c.id_gov_activity and c.year_entry = :year "
    			+ "left join gov_program e on d.id_program = e.id "
    			+ "left join gov_activity f on d.id_activity = f.id "
    			+ "left join ref_unit g on d.unit = g.id_unit "
    			+ "left join gov_target h on a.id_gov_indicator = h.id_gov_indicator and year = :year "
    			+ "left join gov_funding i on a.id_gov_indicator = i.id_gov_indicator and a.id_monper = i.id_monper "
    			+ "left join ref_role j on j.id_role = f.id_role "
    			+ "where a.id_prov = :id_prov and a.id_monper = :id_monper "
    			+ "and a.id_goals = :id_goals and (a.id_target = :id_target or a.id_target = '' or a.id_target is null) and (a.id_indicator = :id_indicator or a.id_indicator='' or a.id_indicator is null) "
    			+ "and f.id_role = :id_role ";
    	sql += " UNION ALL ";
    	sql += "select e.nm_program, e.nm_program_eng, f.nm_activity, f.nm_activity_eng, "
    			+ "d.nm_indicator, d.nm_indicator_eng, g.nm_unit, h.value, b.achievement1, b.achievement2, b.achievement3, "
    			+ "b.achievement4, c.achievement1 as bud1, c.achievement2 as bud2, c.achievement3 as bud3, c.achievement4 as bud4, "
    			+ "i.funding_source, b.new_value1, b.new_value2, b.new_value3, b.new_value4, c.new_value1 as newbud1, "
    			+ "c.new_value2 as newbud2, c.new_value3 as newbud3, c.new_value4 as newbud4, a.id, j.nm_role, j.cat_role "
    			+ "from nsa_map a "
    			+ "left join entry_nsa_indicator b on a.id_nsa_indicator = b.id_assign and b.year_entry = :year "
    			+ "left join nsa_indicator d on a.id_nsa_indicator = d.id "
    			+ "left join entry_nsa_budget c on d.id_activity = c.id_nsa_activity and c.year_entry = :year "
    			+ "left join nsa_program e on d.id_program = e.id "
    			+ "left join nsa_activity f on d.id_activity = f.id "
    			+ "left join ref_unit g on d.unit = g.id_unit "
    			+ "left join nsa_target h on a.id_nsa_indicator = h.id_nsa_indicator and year = :year "
    			+ "left join nsa_funding i on a.id_nsa_indicator = i.id_nsa_indicator and a.id_monper = i.id_monper "
    			+ "left join ref_role j on j.id_role = f.id_role "
    			+ "where a.id_prov = :id_prov and a.id_monper = :id_monper "
    			+ "and a.id_goals = :id_goals and (a.id_target = :id_target or a.id_target = '' or a.id_target is null) and (a.id_indicator = :id_indicator or a.id_indicator='' or a.id_indicator is null) "
    			+ "and f.id_role = :id_role ";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_prov", idprov);
        query.setParameter("id_goals", id_goals);
        query.setParameter("id_target", id_target);
        query.setParameter("id_indicator", idsdgindikator);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_role", idrole);
        query.setParameter("year", year);
        List list = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/getnsaindicatorisi")
    public @ResponseBody Map<String, Object> getnsaindicatorisi(@RequestParam("id") Integer id, @RequestParam("year") Integer year) {
    	String sql = "select e.nm_program, e.nm_program_eng, f.nm_activity, f.nm_activity_eng, "
    			+ "d.nm_indicator, d.nm_indicator_eng, g.nm_unit, h.value, b.achievement1, b.achievement2, b.achievement3, "
    			+ "b.achievement4, c.achievement1 as bud1, c.achievement2 as bud2, c.achievement3 as bud3, c.achievement4 as bud4, "
    			+ "i.funding_source, b.new_value1, b.new_value2, b.new_value3, b.new_value4, c.new_value1 as newbud1, "
    			+ "c.new_value2 as newbud2, c.new_value3 as newbud3, c.new_value4 as newbud4 "
    			+ "from nsa_map a "
    			+ "left join entry_nsa_indicator b on a.id_nsa_indicator = b.id_assign and b.year_entry = :year "
    			+ "left join nsa_indicator d on a.id_nsa_indicator = d.id "
    			+ "left join entry_nsa_budget c on d.id_activity = c.id_nsa_activity and c.year_entry = :year "
    			+ "left join nsa_program e on d.id_program = e.id "
    			+ "left join nsa_activity f on d.id_activity = f.id "
    			+ "left join ref_unit g on d.unit = g.id_unit "
    			+ "left join nsa_target h on a.id_nsa_indicator = h.id_nsa_indicator and year = :year "
    			+ "left join nsa_funding i on a.id_nsa_indicator = i.id_nsa_indicator and a.id_monper = i.id_monper "
    			+ "where a.id = :id ";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id", id);
        query.setParameter("year", year);
        List list = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }    

    // ****************************** Report Grafik ******************************

    @GetMapping("admin/report-graph")
    public String grafik(Model model, HttpSession session) {
        model.addAttribute("listprov", provinsiService.findAllProvinsi());

        model.addAttribute("title", "Report Graphic");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("idrole", session.getAttribute("id_role"));
        
        Integer id_role = (Integer) session.getAttribute("id_role");
        Optional<Role> list = roleService.findOne(id_role);
    	String id_prov      = list.get().getId_prov();
    	String privilege    = list.get().getPrivilege();
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);
        model.addAttribute("id_role", session.getAttribute("id_role"));
        
        return "admin/report/graph";
    }
    
    @GetMapping("admin/getallgoals")
    public @ResponseBody List<Object> getallgoals(@RequestParam("id_monper") int id_monper, @RequestParam("id_role") String id_role, @RequestParam("id_prov") String id_prov) {
        System.out.println("monper = "+id_monper);
        Optional<RanRad> monper = radService.findOne(id_monper);
    	String status = monper.get().getStatus();
        String sql = "";
//        if(id_role.equals("111111")){
//            if(status.equals("completed")) {
////                System.out.println("comple - "+id_monper);
//                sql = "select distinct a.id_goals, b.nm_goals, b.nm_goals_eng, b.id_goals as kode_goals from assign_sdg_indicator a\n" +
//                    "left join (select * from history_sdg_goals where id_monper = '"+id_monper+"') b on a.id_goals = b.id_old\n" +
//                    "where a.id_monper = '"+id_monper+"' and a.id_prov = '"+id_prov+"' ";
//            }else{
//                System.out.println("no");
//                sql = "select distinct a.id_goals, b.nm_goals, b.nm_goals_eng, b.id_goals as kode_goals from assign_sdg_indicator a\n" +
//                    "left join sdg_goals b on a.id_goals = b.id\n" +
//                    "where a.id_monper = '"+id_monper+"' and a.id_prov = '"+id_prov+"' ";
//            }
//        }else{
//            if(status.equals("completed")) {
////                System.out.println("comple - "+id_monper);
//                sql = "select a.id_goals, b.nm_goals, b.nm_goals_eng, b.id_goals as kode_goals from assign_sdg_indicator a\n" +
//                    "left join (select * from history_sdg_goals where id_monper = '"+id_monper+"') b on a.id_goals = b.id_old\n" +
//                    "where a.id_monper = '"+id_monper+"' and a.id_prov = '"+id_prov+"' and a.id_role = '"+id_role+"' ";
//            }else{
//                System.out.println("role no mon: "+id_monper+" ,prov: "+id_prov+" ,role: "+id_role);
//                sql = "select a.id_goals, b.nm_goals, b.nm_goals_eng, b.id_goals as kode_goals from assign_sdg_indicator a\n" +
//                    "left join sdg_goals b on a.id_goals = b.id\n" +
//                    "where a.id_monper = '"+id_monper+"' and a.id_prov = '"+id_prov+"' and a.id_role = '"+id_role+"' ";
//            }
//        }
        if(status.equals("completed")) {
            System.out.println("comple - "+id_monper);
            sql = "SELECT distinct id_old, nm_goals, nm_goals_eng, id_goals FROM history_sdg_goals where id_monper = '"+id_monper+"' ORDER BY CAST(id_goals AS UNSIGNED)";
        }else{
            sql = "SELECT distinct id, nm_goals, nm_goals_eng, id_goals FROM sdg_goals ORDER BY CAST(id_goals AS UNSIGNED)";
        }
    	Query query = manager.createNativeQuery(sql);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getallgoals_graph")
    public @ResponseBody List<Object> getallgoals(@RequestParam("id_monper") int id_monper, @RequestParam("id_role") String id_role, @RequestParam("id_prov") String id_prov, @RequestParam("id_activity") String id_activity,@RequestParam("id_indicator") String id_indicator,@RequestParam("jenis") String jenis) {
        System.out.println("monper = "+id_monper);
        Optional<RanRad> monper = radService.findOne(id_monper);
    	String status = monper.get().getStatus();
        String sql = "";
        if(id_activity.equals("0") && id_indicator.equals("0")) {
        	if(status.equals("completed")) {
                sql = "SELECT distinct id_old, nm_goals, nm_goals_eng, id_goals FROM history_sdg_goals where id_monper = '"+id_monper+"' ORDER BY CAST(id_goals AS UNSIGNED)";
            }else{
                sql = "SELECT distinct id, nm_goals, nm_goals_eng, id_goals FROM sdg_goals ORDER BY CAST(id_goals AS UNSIGNED)";
            }
        }else if(!id_indicator.equals("0")) {
        	if(status.equals("completed")) {
                if(jenis.equals("gov")) {
            		sql = "select distinct b.id_old, b.nm_goals, b.nm_goals_eng, b.id_goals from gov_map a  join history_sdg_goals b on a.id_goals = b.id_old and b.id_monper = '"+id_monper+"' where a.id_gov_indicator = '"+id_indicator+"' ORDER BY CAST(b.id_goals AS UNSIGNED)";
            	}else {
            		sql = "select distinct b.id_old, b.nm_goals, b.nm_goals_eng, b.id_goals from nsa_map a  join history_sdg_goals b on a.id_goals = b.id_old and b.id_monper = '"+id_monper+"' where a.id_nsa_indicator = '"+id_indicator+"' ORDER BY CAST(b.id_goals AS UNSIGNED)";
            	}
        	}else{
            	if(jenis.equals("gov")) {
            		sql = "select distinct b.id, b.nm_goals, b.nm_goals_eng, b.id_goals from gov_map a  join sdg_goals b on a.id_goals = b.id where a.id_gov_indicator = '"+id_indicator+"' ORDER BY CAST(b.id_goals AS UNSIGNED)";
            	}else {
            		sql = "select distinct b.id, b.nm_goals, b.nm_goals_eng, b.id_goals from nsa_map a  join sdg_goals b on a.id_goals = b.id where a.id_nsa_indicator = '"+id_indicator+"' ORDER BY CAST(b.id_goals AS UNSIGNED)";
            	}
            }
        }else{
        	if(status.equals("completed")) {
                if(jenis.equals("gov")) {
            		sql = "select distinct b.id_old, b.nm_goals, b.nm_goals_eng, b.id_goals from gov_map a  join history_sdg_goals b on a.id_goals = b.id_old and b.id_monper = '"+id_monper+"' join gov_indicator c on a.id_gov_indicator = c.id where c.id_activity = '"+id_activity+"' ORDER BY CAST(b.id_goals AS UNSIGNED)";
            	}else {
            		sql = "select distinct b.id_old, b.nm_goals, b.nm_goals_eng, b.id_goals from nsa_map a  join history_sdg_goals b on a.id_goals = b.id_old and b.id_monper = '"+id_monper+"' join nsa_indicator c on a.id_nsa_indicator = c.id where c.id_activity = '"+id_activity+"' ORDER BY CAST(b.id_goals AS UNSIGNED)";
            	}
        	}else{
            	if(jenis.equals("gov")) {
            		sql = "select distinct b.id, b.nm_goals, b.nm_goals_eng, b.id_goals from gov_map a  join sdg_goals b on a.id_goals = b.id join gov_indicator c on a.id_gov_indicator = c.id where c.id_activity = '"+id_activity+"' ORDER BY CAST(b.id_goals AS UNSIGNED)";
            	}else {
            		sql = "select distinct b.id, b.nm_goals, b.nm_goals_eng, b.id_goals from nsa_map a  join sdg_goals b on a.id_goals = b.id join nsa_indicator c on a.id_nsa_indicator = c.id where c.id_activity = '"+id_activity+"' ORDER BY CAST(b.id_goals AS UNSIGNED)";
            	}
            }
        }
        
    	Query query = manager.createNativeQuery(sql);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getalltarget_graph")
    public @ResponseBody List<Object> getalltarget(@RequestParam("id_goals") int id_goals, @RequestParam("id_monper") int id_monper, @RequestParam("id_role") String id_role, @RequestParam("id_prov") String id_prov, @RequestParam("id_activity") String id_activity,@RequestParam("id_indicator") String id_indicator,@RequestParam("jenis") String jenis) {
        Optional<RanRad> monper = radService.findOne(id_monper);
    	String status = monper.get().getStatus();
        String sql="";
        if(id_activity.equals("0") && id_indicator.equals("0")) {
        	if(status.equals("completed")) {
                sql = "SELECT distinct id_old, nm_target, nm_target_eng, id_target FROM history_sdg_target WHERE id_goals = :id_goals and id_monper = '"+id_monper+"' ORDER BY CAST(id_target AS UNSIGNED)";
            }else{
                sql = "SELECT distinct id, nm_target, nm_target_eng, id_target FROM sdg_target WHERE id_goals = :id_goals ORDER BY CAST(id_target AS UNSIGNED)";
            }
        }else if(!id_indicator.equals("0")){
        	if(status.equals("completed")) {
                if(jenis.equals("gov")) {
            		sql = "select distinct b.id_old, b.nm_target, b.nm_target_eng, b.id_target from gov_map a  join history_sdg_target b on a.id_target = b.id_old and b.id_monper = '"+id_monper+"' where a.id_gov_indicator = '"+id_indicator+"' and a.id_goals = :id_goals and a.id_target is not null ORDER BY CAST(b.id_target AS UNSIGNED)";
            	}else {
            		sql = "select distinct b.id_old, b.nm_target, b.nm_target_eng, b.id_target from nsa_map a  join history_sdg_target b on a.id_target = b.id_old and b.id_monper = '"+id_monper+"' where a.id_nsa_indicator = '"+id_indicator+"' and a.id_goals = :id_goals and a.id_target is not null ORDER BY CAST(b.id_target AS UNSIGNED)";
            	}
        	}else{
        		if(jenis.equals("gov")) {
            		sql = "select distinct b.id, b.nm_target, b.nm_target_eng, b.id_target from gov_map a  join sdg_target b on a.id_target = b.id where a.id_gov_indicator = '"+id_indicator+"' and a.id_goals = :id_goals and a.id_target is not null ORDER BY CAST(b.id_target AS UNSIGNED)";
            	}else {
            		sql = "select distinct b.id, b.nm_target, b.nm_target_eng, b.id_target from nsa_map a  join sdg_target b on a.id_target = b.id where a.id_nsa_indicator = '"+id_indicator+"' and a.id_goals = :id_goals and a.id_target is not null ORDER BY CAST(b.id_target AS UNSIGNED)";
            	}
            }
        }else{
        	if(status.equals("completed")) {
                if(jenis.equals("gov")) {
            		sql = "select distinct b.id_old, b.nm_target, b.nm_target_eng, b.id_target from gov_map a  join history_sdg_target b on a.id_target = b.id_old and b.id_monper = '"+id_monper+"' join gov_indicator c on a.id_gov_indicator = c.id where a.id_goals = :id_goals and a.id_target is not null and c.id_activity = '"+id_activity+"' ORDER BY CAST(b.id_target AS UNSIGNED)";
            	}else {
            		sql = "select distinct b.id_old, b.nm_target, b.nm_target_eng, b.id_target from nsa_map a  join history_sdg_target b on a.id_target = b.id_old and b.id_monper = '"+id_monper+"' join nsa_indicator c on a.id_nsa_indicator = c.id where a.id_goals = :id_goals and a.id_target is not null and c.id_activity = '"+id_activity+"' ORDER BY CAST(b.id_target AS UNSIGNED)";
            	}
        	}else{
            	if(jenis.equals("gov")) {
            		sql = "select distinct b.id, b.nm_target, b.nm_target_eng, b.id_target from gov_map a  join sdg_target b on a.id_target = b.id join gov_indicator c on a.id_gov_indicator = c.id where a.id_goals = :id_goals and a.id_target is not null and c.id_activity = '"+id_activity+"' ORDER BY CAST(b.id_target AS UNSIGNED)";
            	}else {
            		sql = "select distinct b.id, b.nm_target, b.nm_target_eng, b.id_target from nsa_map a  join sdg_target b on a.id_target = b.id join nsa_indicator c on a.id_nsa_indicator = c.id where a.id_goals = :id_goals and a.id_target is not null and c.id_activity = '"+id_activity+"' ORDER BY CAST(b.id_target AS UNSIGNED)";
            	}
            }
        }
        
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", id_goals);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getallindicator_graph")
    public @ResponseBody List<Object> getallindicator(@RequestParam("id_goals") int id_goals, @RequestParam("id_target") int id_target, @RequestParam("id_monper") int id_monper, @RequestParam("id_role") String id_role, @RequestParam("id_prov") String id_prov, @RequestParam("id_activity") String id_activity,@RequestParam("id_indicator") String id_indicator,@RequestParam("jenis") String jenis) {
    	Optional<RanRad> monper = radService.findOne(id_monper);
    	String status = monper.get().getStatus();
        String sql="";
        if(id_activity.equals("0") && id_indicator.equals("0")) {
        	if(status.equals("completed")) {
                sql = "SELECT distinct id_old, nm_indicator, nm_indicator_eng, id_indicator FROM history_sdg_indicator WHERE id_goals = :id_goals AND "
        			+ "id_target = :id_target and id_monper = '"+id_monper+"' GROUP BY id, id_goals, id_target ORDER BY CAST(id_indicator AS UNSIGNED)";
            }else{
                sql = "SELECT distinct id, nm_indicator, nm_indicator_eng, id_indicator FROM sdg_indicator WHERE id_goals = :id_goals AND "
        			+ "id_target = :id_target GROUP BY id, id_goals, id_target ORDER BY CAST(id_indicator AS UNSIGNED)";
            }
        }else if(!id_indicator.equals("0")){
        	if(status.equals("completed")) {
                if(jenis.equals("gov")) {
            		sql = "select distinct b.id_old, b.nm_indicator, b.nm_indicator_eng, b.id_indicator from gov_map a  join history_sdg_indicator b on a.id_indicator = b.id_old and b.id_monper = '"+id_monper+"' where a.id_gov_indicator = '"+id_indicator+"' and a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is not null ORDER BY CAST(b.id_indicator AS UNSIGNED)";
            	}else {
            		sql = "select distinct b.id_old, b.nm_indicator, b.nm_indicator_eng, b.id_indicator from nsa_map a  join history_sdg_indicator b on a.id_indicator = b.id_old and b.id_monper = '"+id_monper+"' where a.id_nsa_indicator = '"+id_indicator+"' and a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is not null ORDER BY CAST(b.id_indicator AS UNSIGNED)";
            	}
        	}else{
        		if(jenis.equals("gov")) {
            		sql = "select distinct b.id, b.nm_indicator, b.nm_indicator_eng, b.id_indicator from gov_map a  join sdg_indicator b on a.id_indicator = b.id where a.id_gov_indicator = '"+id_indicator+"' and a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is not null ORDER BY CAST(b.id_indicator AS UNSIGNED)";
            	}else {
            		sql = "select distinct b.id, b.nm_indicator, b.nm_indicator_eng, b.id_indicator from nsa_map a  join sdg_indicator b on a.id_indicator = b.id where a.id_nsa_indicator = '"+id_indicator+"' and a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is not null ORDER BY CAST(b.id_indicator AS UNSIGNED)";
            	}
            }
        }else{
        	if(status.equals("completed")) {
                if(jenis.equals("gov")) {
            		sql = "select distinct b.id_old, b.nm_indicator, b.nm_indicator_eng, b.id_indicator from gov_map a  join history_sdg_indicator b on a.id_indicator = b.id_old and b.id_monper = '"+id_monper+"' join gov_indicator c on a.id_gov_indicator = c.id where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is not null and c.id_activity = '"+id_activity+"' ORDER BY CAST(b.id_indicator AS UNSIGNED)";
            	}else {
            		sql = "select distinct b.id_old, b.nm_indicator, b.nm_indicator_eng, b.id_indicator from nsa_map a  join history_sdg_indicator b on a.id_indicator = b.id_old and b.id_monper = '"+id_monper+"' join nsa_indicator c on a.id_nsa_indicator = c.id where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is not null and c.id_activity = '"+id_activity+"' ORDER BY CAST(b.id_indicator AS UNSIGNED)";
            	}
        	}else{
            	if(jenis.equals("gov")) {
            		sql = "select distinct b.id, b.nm_indicator, b.nm_indicator_eng, b.id_indicator from gov_map a  join sdg_indicator b on a.id_indicator = b.id join gov_indicator c on a.id_gov_indicator = c.id where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is not null and c.id_activity = '"+id_activity+"' ORDER BY CAST(b.id_indicator AS UNSIGNED)";
            	}else {
            		sql = "select distinct b.id, b.nm_indicator, b.nm_indicator_eng, b.id_indicator from nsa_map a  join sdg_indicator b on a.id_indicator = b.id join nsa_indicator c on a.id_nsa_indicator = c.id where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is not null and c.id_activity = '"+id_activity+"' ORDER BY CAST(b.id_indicator AS UNSIGNED)";
            	}
            }
        }
        
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", id_goals);
        query.setParameter("id_target", id_target);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getalltarget")
    public @ResponseBody List<Object> getalltarget(@RequestParam("id_goals") int id_goals, @RequestParam("id_monper") int id_monper, @RequestParam("id_role") String id_role, @RequestParam("id_prov") String id_prov) {
        Optional<RanRad> monper = radService.findOne(id_monper);
    	String status = monper.get().getStatus();
        String sql="";
//        if(id_role.equals("111111")){
//            if(status.equals("completed")) {
////                System.out.println("comple - "+id_monper);
//                sql = "select distinct a.id_target, b.nm_target, b.nm_target_eng, b.id_target as kode_target from assign_sdg_indicator a\n" +
//                    "left join (select * from history_sdg_target where id_monper = '"+id_monper+"') b on a.id_target = b.id_old\n" +
//                    "where a.id_monper = '"+id_monper+"' and a.id_prov = '"+id_prov+"' and b.id_goals = '"+id_goals+"' ";
//            }else{
//                sql = "select distinct a.id_target, b.nm_target, b.nm_target_eng, b.id_target as kode_target from assign_sdg_indicator a\n" +
//                    "left join sdg_target b on a.id_target = b.id\n" +
//                    "where a.id_monper = '"+id_monper+"' and a.id_prov = '"+id_prov+"' and b.id_goals = '"+id_goals+"' ";
//            }
//        }else{
//            if(status.equals("completed")) {
////                System.out.println("comple - "+id_monper);
//                sql = "select a.id_target, b.nm_target, b.nm_target_eng, b.id_target as kode_target from assign_sdg_indicator a\n" +
//                    "left join (select * from history_sdg_target where id_monper = '"+id_monper+"') b on a.id_target = b.id_old\n" +
//                    "where a.id_monper = '"+id_monper+"' and a.id_prov = '"+id_prov+"' and b.id_goals = '"+id_goals+"' and a.id_role = '"+id_role+"' ";
//            }else{
//                System.out.println("role no mon: "+id_monper+" ,prov: "+id_prov+" ,role: "+id_role+" ,goal: "+id_goals);
//                sql = "select a.id_target, b.nm_target, b.nm_target_eng, b.id_target as kode_target from assign_sdg_indicator a\n" +
//                    "left join sdg_target b on a.id_target = b.id\n" +
//                    "where a.id_monper = '"+id_monper+"' and a.id_prov = '"+id_prov+"' and b.id_goals = '"+id_goals+"' and a.id_role = '"+id_role+"' ";
//            }
//        }
//        
        if(status.equals("completed")) {
            sql = "SELECT distinct id_old, nm_target, nm_target_eng, id_target FROM history_sdg_target WHERE id_goals = :id_goals and id_monper = '"+id_monper+"' ORDER BY CAST(id_target AS UNSIGNED)";
        }else{
            sql = "SELECT distinct id, nm_target, nm_target_eng, id_target FROM sdg_target WHERE id_goals = :id_goals ORDER BY CAST(id_target AS UNSIGNED)";
        }
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", id_goals);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getallindicator")
    public @ResponseBody List<Object> getallindicator(@RequestParam("id_goals") int id_goals, @RequestParam("id_target") int id_target, @RequestParam("id_monper") int id_monper, @RequestParam("id_role") String id_role, @RequestParam("id_prov") String id_prov) {
    	Optional<RanRad> monper = radService.findOne(id_monper);
    	String status = monper.get().getStatus();
        String sql="";
//        if(id_role.equals("111111")){
//            if(status.equals("completed")) {
////                System.out.println("comple - "+id_monper);
//                sql = "select distinct a.id_indicator, b.nm_indicator, b.nm_indicator_eng, b.id_indicator as kode_indicator from assign_sdg_indicator a\n" +
//                    "left join (select * from history_sdg_indicator where id_monper = '"+id_monper+"') b on a.id_indicator = b.id_old\n" +
//                    "where a.id_monper = '"+id_monper+"' and a.id_prov = '"+id_prov+"' and b.id_goals = '"+id_goals+"' and b.id_target = '"+id_target+"'  ";
//            }else{
//                sql = "select distinct a.id_indicator, b.nm_indicator, b.nm_indicator_eng, b.id_indicator as kode_indicator from assign_sdg_indicator a\n" +
//                    "left join sdg_indicator b on a.id_indicator = b.id\n" +
//                    "where a.id_monper = '"+id_monper+"' and a.id_prov = '"+id_prov+"' and b.id_goals = '"+id_goals+"' and b.id_target = '"+id_target+"'  ";
//            }
//        }else{
//            if(status.equals("completed")) {
////                System.out.println("comple - "+id_monper);
//                sql = "select a.id_indicator, b.nm_indicator, b.nm_indicator_eng, b.id_indicator as kode_indicator from assign_sdg_indicator a\n" +
//                    "left join (select * from history_sdg_indicator where id_monper = '"+id_monper+"') b on a.id_indicator = b.id_old\n" +
//                    "where a.id_monper = '"+id_monper+"' and a.id_prov = '"+id_prov+"' and b.id_goals = '"+id_goals+"' and b.id_target = '"+id_target+"' and a.id_role = '"+id_role+"'  ";
//            }else{
//                sql = "select a.id_indicator, b.nm_indicator, b.nm_indicator_eng, b.id_indicator as kode_indicator from assign_sdg_indicator a\n" +
//                    "left join sdg_indicator b on a.id_indicator = b.id\n" +
//                    "where a.id_monper = '"+id_monper+"' and a.id_prov = '"+id_prov+"' and b.id_goals = '"+id_goals+"' and b.id_target = '"+id_target+"' and a.id_role = '"+id_role+"' ";
//            }
//        }
        if(status.equals("completed")) {
            sql = "SELECT distinct id_old, nm_indicator, nm_indicator_eng, id_indicator FROM history_sdg_indicator WHERE id_goals = :id_goals AND "
    			+ "id_target = :id_target and id_monper = '"+id_monper+"' GROUP BY id, id_goals, id_target ORDER BY CAST(id_indicator AS UNSIGNED)";
        }else{
            sql = "SELECT distinct id, nm_indicator, nm_indicator_eng, id_indicator FROM sdg_indicator WHERE id_goals = :id_goals AND "
    			+ "id_target = :id_target GROUP BY id, id_goals, id_target ORDER BY CAST(id_indicator AS UNSIGNED)";
        }
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", id_goals);
        query.setParameter("id_target", id_target);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getalldisaggre")
    public @ResponseBody List<Object> getalldisaggre(@RequestParam("id_indicator") int idindi) {
    	String sql = "SELECT a.id, a.nm_disaggre, a.nm_disaggre_eng, b.desc_disaggre, b.desc_disaggre_eng "
    			+ "FROM sdg_ranrad_disaggre a LEFT JOIN sdg_ranrad_disaggre_detail b ON b.id_disaggre = a.id "
    			+ "WHERE a.id = :id_indicator ORDER BY a.id ASC";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_indicator", idindi);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getallgovprogram")
    public @ResponseBody List<Object> getallgovprogram(@RequestParam("id_goals") String id_goals, @RequestParam("id_target") String id_target, @RequestParam("id_indicator") String id_indicator, @RequestParam("id_prov") String idprov, @RequestParam("id_role") String id_role, @RequestParam("id_monper") String id_monper) {
//    	String sql  = "SELECT c.id as id_gov_indicator, b.id, b.nm_program, b.nm_program_eng FROM gov_activity as a \n" +
//                    "left join gov_program as b on a.id_program = b.id\n" +
//                    "inner join gov_indicator as c on a.id = c.id_activity\n" +
//                    "inner join gov_map as d on c.id = d.id_gov_indicator\n" +
//                    "WHERE a.id_role = :id_role and d.id_prov = :id_prov and d.id_monper = :id_monper ";
        Query query = em.createNativeQuery("");
//        System.out.println("id "+id_role);
        if(id_role.equals("111111")){
            if(id_target.equals("null")){
                if(id_indicator.equals("null")){
                    String sql  = "select distinct c.id, c.nm_program, c.nm_program_eng, c.internal_code\n" +
                                "from gov_map as a\n" +
                                "left join gov_indicator as b on a.id_gov_indicator = b.id\n" +
                                "left join gov_program as c on b.id_program = c.id\n" +
                                "left join gov_activity as d on b.id_activity = d.id \n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.internal_code AS INT)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
//                    query.setParameter("id_role", id_role);
                }else{
                    String sql  = "select distinct c.id, c.nm_program, c.nm_program_eng, c.internal_code\n" +
                                "from gov_map as a\n" +
                                "left join gov_indicator as b on a.id_gov_indicator = b.id\n" +
                                "left join gov_program as c on b.id_program = c.id\n" +
                                "left join gov_activity as d on b.id_activity = d.id \n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.internal_code AS INT)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
//                    query.setParameter("id_role", id_role);
                }
            }else{
                if(id_indicator.equals("null")){
                    String sql  = "select distinct c.id, c.nm_program, c.nm_program_eng, c.internal_code\n" +
                                "from gov_map as a\n" +
                                "left join gov_indicator as b on a.id_gov_indicator = b.id\n" +
                                "left join gov_program as c on b.id_program = c.id\n" +
                                "left join gov_activity as d on b.id_activity = d.id \n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.internal_code AS INT)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
//                    query.setParameter("id_role", id_role);
                }else{
                    String sql  = "select distinct c.id, c.nm_program, c.nm_program_eng, c.internal_code\n" +
                                "from gov_map as a\n" +
                                "left join gov_indicator as b on a.id_gov_indicator = b.id\n" +
                                "left join gov_program as c on b.id_program = c.id\n" +
                                "left join gov_activity as d on b.id_activity = d.id \n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.internal_code AS INT)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
//                    query.setParameter("id_role", id_role);
                }
            }
        }else{
            if(id_target.equals("null")){
                if(id_indicator.equals("null")){
                    String sql  = "select distinct c.id, c.nm_program, c.nm_program_eng, c.internal_code\n" +
                                "from gov_map as a\n" +
                                "left join gov_indicator as b on a.id_gov_indicator = b.id\n" +
                                "left join gov_program as c on b.id_program = c.id\n" +
                                "left join gov_activity as d on b.id_activity = d.id \n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and d.id_role = :id_role and c.id IS NOT NULL order by CAST(c.internal_code AS INT)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
                    query.setParameter("id_role", id_role);
                }else{
                    String sql  = "select distinct c.id, c.nm_program, c.nm_program_eng, c.internal_code\n" +
                                "from gov_map as a\n" +
                                "left join gov_indicator as b on a.id_gov_indicator = b.id\n" +
                                "left join gov_program as c on b.id_program = c.id\n" +
                                "left join gov_activity as d on b.id_activity = d.id \n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and d.id_role = :id_role and c.id IS NOT NULL order by CAST(c.internal_code AS INT)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
                    query.setParameter("id_role", id_role);
                }
            }else{
                if(id_indicator.equals("null")){
                    String sql  = "select distinct c.id, c.nm_program, c.nm_program_eng, c.internal_code\n" +
                                "from gov_map as a\n" +
                                "left join gov_indicator as b on a.id_gov_indicator = b.id\n" +
                                "left join gov_program as c on b.id_program = c.id\n" +
                                "left join gov_activity as d on b.id_activity = d.id \n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and d.id_role = :id_role and c.id IS NOT NULL order by CAST(c.internal_code AS INT)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
                    query.setParameter("id_role", id_role);
                }else{
                    String sql  = "select distinct c.id, c.nm_program, c.nm_program_eng, c.internal_code\n" +
                                "from gov_map as a\n" +
                                "left join gov_indicator as b on a.id_gov_indicator = b.id\n" +
                                "left join gov_program as c on b.id_program = c.id\n" +
                                "left join gov_activity as d on b.id_activity = d.id \n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and d.id_role = :id_role and c.id IS NOT NULL order by CAST(c.internal_code AS INT)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
                    query.setParameter("id_role", id_role);
                }
            }
        }
        List list   = query.getResultList();
        return list;
    }
    
    
    @GetMapping("admin/getallgovactivity")
    public @ResponseBody List<Object> getallgovactivity(@RequestParam("id_goals") String id_goals, @RequestParam("id_target") String id_target, @RequestParam("id_indicator") String id_indicator, @RequestParam("id_prov") String idprov, @RequestParam("id_role") String id_role, @RequestParam("id_monper") String id_monper, @RequestParam("idprog") String idprog) {
    	Query query = em.createNativeQuery("");
//        System.out.println("id "+id_role);

        if(id_role.equals("111111")){
            if(id_target.equals("null")){
                if(id_indicator.equals("null")){
                    String sql  = "select distinct c.id, c.nm_activity, c.nm_activity_eng, c.internal_code, c.id_role\n" +
                                "from gov_map as a\n" +
                                "left join gov_indicator as b on a.id_gov_indicator = b.id\n" +
                                "left join (select * from gov_activity where id_program = :idprog ) as c on b.id_activity = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.id_activity AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
//                    query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                }else{
                    String sql  = "select distinct c.id, c.nm_activity, c.nm_activity_eng, c.internal_code, c.id_role\n" +
                                "from gov_map as a\n" +
                                "left join gov_indicator as b on a.id_gov_indicator = b.id\n" +
                                "left join (select * from gov_activity where id_program = :idprog) as c on b.id_activity = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.id_activity AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
//                    query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                }
            }else{
                if(id_indicator.equals("null")){
                    String sql  = "select distinct c.id, c.nm_activity, c.nm_activity_eng, c.internal_code, c.id_role\n" +
                                "from gov_map as a\n" +
                                "left join gov_indicator as b on a.id_gov_indicator = b.id\n" +
                                "left join (select * from gov_activity where id_program = :idprog) as c on b.id_activity = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.id_activity AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
//                    query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                }else{
                    String sql  = "select distinct c.id, c.nm_activity, c.nm_activity_eng, c.internal_code, c.id_role\n" +
                                "from gov_map as a\n" +
                                "left join gov_indicator as b on a.id_gov_indicator = b.id\n" +
                                "left join (select * from gov_activity where id_program = :idprog) as c on b.id_activity = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.id_activity AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
//                    query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                }
            }
        }else{
            if(id_target.equals("null")){
                if(id_indicator.equals("null")){
                    String sql  = "select distinct c.id, c.nm_activity, c.nm_activity_eng, c.internal_code, c.id_role\n" +
                                "from gov_map as a\n" +
                                "left join gov_indicator as b on a.id_gov_indicator = b.id\n" +
                                "left join (select * from gov_activity where id_program = :idprog and id_role = :id_role) as c on b.id_activity = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.id_activity AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
                    query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                }else{
                    String sql  = "select distinct c.id, c.nm_activity, c.nm_activity_eng, c.internal_code, c.id_role\n" +
                                "from gov_map as a\n" +
                                "left join gov_indicator as b on a.id_gov_indicator = b.id\n" +
                                "left join (select * from gov_activity where id_program = :idprog and id_role = :id_role) as c on b.id_activity = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.id_activity AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
                    query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                }
            }else{
                if(id_indicator.equals("null")){
                    String sql  = "select distinct c.id, c.nm_activity, c.nm_activity_eng, c.internal_code, c.id_role\n" +
                                "from gov_map as a\n" +
                                "left join gov_indicator as b on a.id_gov_indicator = b.id\n" +
                                "left join (select * from gov_activity where id_program = :idprog and id_role = :id_role) as c on b.id_activity = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.id_activity AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
                    query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                }else{
                    String sql  = "select distinct c.id, c.nm_activity, c.nm_activity_eng, c.internal_code, c.id_role\n" +
                                "from gov_map as a\n" +
                                "left join gov_indicator as b on a.id_gov_indicator = b.id\n" +
                                "left join (select * from gov_activity where id_program = :idprog and id_role = :id_role) as c on b.id_activity = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.id_activity AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
                    query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                }
            }
        }
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getallgovindi")
    public @ResponseBody List<Object> getallgovindi(@RequestParam("id_goals") String id_goals, @RequestParam("id_target") String id_target, @RequestParam("id_indicator") String id_indicator, @RequestParam("id_prov") String idprov, @RequestParam("id_role") String id_role, @RequestParam("id_monper") String id_monper, @RequestParam("idprog") String idprog, @RequestParam("idactivity") int idactivity) {
    	Query query = em.createNativeQuery("");
//        System.out.println("id "+id_role);
        if(id_role.equals("111111")){
            if(id_target.equals("null")){
                if(id_indicator.equals("null")){
                    String sql  = "select distinct b.id, b.nm_indicator, b.nm_indicator_eng, b.internal_code\n" +
                                "from gov_map as a\n" +
                                "left join (select * from gov_indicator where id_program = :idprog and id_activity = :idactivity) as b on a.id_gov_indicator = b.id\n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and b.id IS NOT NULL order by CAST(b.internal_code AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
            //        query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                    query.setParameter("idactivity", idactivity);
                }else{
                    String sql  = "select distinct b.id, b.nm_indicator, b.nm_indicator_eng, b.internal_code\n" +
                                "from gov_map as a\n" +
                                "left join (select * from gov_indicator where id_program = :idprog and id_activity = :idactivity) as b on a.id_gov_indicator = b.id\n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and b.id IS NOT NULL order by CAST(b.internal_code AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
            //        query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                    query.setParameter("idactivity", idactivity);
                }
            }else{
                if(id_indicator.equals("null")){
                    String sql  = "select distinct b.id, b.nm_indicator, b.nm_indicator_eng, b.internal_code\n" +
                                "from gov_map as a\n" +
                                "left join (select * from gov_indicator where id_program = :idprog and id_activity = :idactivity) as b on a.id_gov_indicator = b.id\n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and b.id IS NOT NULL order by CAST(b.internal_code AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
            //        query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                    query.setParameter("idactivity", idactivity);
                }else{
                    String sql  = "select distinct b.id, b.nm_indicator, b.nm_indicator_eng, b.internal_code\n" +
                                "from gov_map as a\n" +
                                "left join (select * from gov_indicator where id_program = :idprog and id_activity = :idactivity) as b on a.id_gov_indicator = b.id\n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and b.id IS NOT NULL order by CAST(b.internal_code AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
            //        query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                    query.setParameter("idactivity", idactivity);
                }
            }
        }else{
            if(id_target.equals("null")){
                if(id_indicator.equals("null")){
                    String sql  = "select distinct b.id, b.nm_indicator, b.nm_indicator_eng, b.internal_code\n" +
                                "from gov_map as a\n" +
                                "left join (select * from gov_indicator where id_program = :idprog and id_activity = :idactivity) as b on a.id_gov_indicator = b.id\n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and b.id IS NOT NULL order by CAST(b.internal_code AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
            //        query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                    query.setParameter("idactivity", idactivity);
                }else{
                    String sql  = "select distinct b.id, b.nm_indicator, b.nm_indicator_eng, b.internal_code\n" +
                                "from gov_map as a\n" +
                                "left join (select * from gov_indicator where id_program = :idprog and id_activity = :idactivity) as b on a.id_gov_indicator = b.id\n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and b.id IS NOT NULL order by CAST(b.internal_code AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
            //        query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                    query.setParameter("idactivity", idactivity);
                }
            }else{
                if(id_indicator.equals("null")){
                    String sql  = "select distinct b.id, b.nm_indicator, b.nm_indicator_eng, b.internal_code\n" +
                                "from gov_map as a\n" +
                                "left join (select * from gov_indicator where id_program = :idprog and id_activity = :idactivity) as b on a.id_gov_indicator = b.id\n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and b.id IS NOT NULL order by CAST(b.internal_code AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
            //        query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                    query.setParameter("idactivity", idactivity);
                }else{
                    String sql  = "select distinct b.id, b.nm_indicator, b.nm_indicator_eng, b.internal_code\n" +
                                "from gov_map as a\n" +
                                "left join (select * from gov_indicator where id_program = :idprog and id_activity = :idactivity) as b on a.id_gov_indicator = b.id\n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and b.id IS NOT NULL order by CAST(b.internal_code AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
            //        query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                    query.setParameter("idactivity", idactivity);
                }
            }
        }
        List list = query.getResultList();
        return list;
    }
    
    
    @GetMapping("admin/getallgovprogram_pertama")
    public @ResponseBody List<Object> getallgovprogram_pertama(@RequestParam("id_prov") String idprov, @RequestParam("id_role") String id_role, @RequestParam("id_monper") String id_monper) {
    	Query query = em.createNativeQuery("");
        if(id_role.equals("111111")){
            String sql  = "SELECT distinct b.id as id_prg, b.nm_program, b.nm_program_eng, b.internal_code FROM gov_activity as a \n" +
                        "left join gov_program as b on a.id_program = b.id\n" +
                        "inner join gov_indicator as c on a.id = c.id_activity\n" +
                        "WHERE b.id_monper = :id_monper order by CAST(b.internal_code AS INT) ";
            query = manager.createNativeQuery(sql);
//            query.setParameter("id_prov", idprov);
            query.setParameter("id_monper", id_monper);
//            query.setParameter("id_role", id_role);
        }else{
            String sql  = "SELECT distinct b.id as id_prg, b.nm_program, b.nm_program_eng, b.internal_code FROM gov_activity as a \n" +
                        "left join gov_program as b on a.id_program = b.id\n" +
                        "inner join gov_indicator as c on a.id = c.id_activity\n" +
                        "WHERE a.id_role = :id_role and b.id_monper = :id_monper order by CAST(b.internal_code AS INT) ";
            query = manager.createNativeQuery(sql);
//            query.setParameter("id_prov", idprov);
            query.setParameter("id_monper", id_monper);
            query.setParameter("id_role", id_role);
        }

        List list   = query.getResultList();
        return list;
    }
    
    
    @GetMapping("admin/getallgovactivity_pertama")
    public @ResponseBody List<Object> getallgovactivity_pertama(@RequestParam("id_prov") String idprov, @RequestParam("id_role") String id_role, @RequestParam("id_monper") String id_monper, @RequestParam("id_prog") String id_prog) {
    	Query query = em.createNativeQuery("");
        if(id_role.equals("111111")){
            String sql  = "SELECT distinct a.id as id_act, a.nm_activity, a.nm_activity_eng, a.internal_code, a.id_role FROM gov_activity as a \n" +
                        "left join gov_program as b on a.id_program = b.id\n" +
                        "inner join gov_indicator as c on a.id = c.id_activity\n" +
                        "WHERE b.id_monper = :id_monper and a.id_program = :id_prog order by CAST(a.internal_code AS UNSIGNED)";
            query = manager.createNativeQuery(sql);
//            query.setParameter("id_prov", idprov);
            query.setParameter("id_monper", id_monper);
            query.setParameter("id_prog", id_prog);
        }else{
            String sql  = "SELECT distinct a.id as id_act, a.nm_activity, a.nm_activity_eng, a.internal_code, a.id_role FROM gov_activity as a \n" +
                        "left join gov_program as b on a.id_program = b.id\n" +
                        "inner join gov_indicator as c on a.id = c.id_activity\n" +
                        "WHERE a.id_role = :id_role and b.id_monper = :id_monper and a.id_program = :id_prog order by CAST(a.internal_code AS UNSIGNED)";
            query = manager.createNativeQuery(sql);
//            query.setParameter("id_prov", idprov);
            query.setParameter("id_monper", id_monper);
            query.setParameter("id_prog", id_prog);
            query.setParameter("id_role", id_role);
        }

        List list   = query.getResultList();
        return list;
    }
    
    
    @GetMapping("admin/getallgovindi_pertama")
    public @ResponseBody List<Object> getallgovindi_pertama(@RequestParam("id_prov") String idprov, @RequestParam("id_role") String id_role, @RequestParam("id_monper") String id_monper, @RequestParam("id_prog") String id_prog, @RequestParam("id_activity") String id_activity) {
    	Query query = em.createNativeQuery("");
        if(id_role.equals("111111")){
            String sql  = "SELECT c.id as id_act, c.nm_indicator, c.nm_indicator_eng, c.internal_code, a.id_role FROM gov_activity as a \n" +
                        "left join gov_program as b on a.id_program = b.id\n" +
                        "inner join gov_indicator as c on a.id = c.id_activity\n" +
                        "WHERE b.id_monper = :id_monper and c.id_program = :id_prog and c.id_activity = :id_activity order by CAST(c.internal_code AS UNSIGNED)";
            query = manager.createNativeQuery(sql);
//            query.setParameter("id_prov", idprov);
            query.setParameter("id_monper", id_monper);
            query.setParameter("id_prog", id_prog);
            query.setParameter("id_activity", id_activity);
        }else{
            String sql  = "SELECT c.id as id_act, c.nm_indicator, c.nm_indicator_eng, c.internal_code, a.id_role FROM gov_activity as a \n" +
                        "left join gov_program as b on a.id_program = b.id\n" +
                        "inner join gov_indicator as c on a.id = c.id_activity\n" +
                        "WHERE a.id_role = :id_role and b.id_monper = :id_monper and c.id_program = :id_prog and c.id_activity = :id_activity order by CAST(c.internal_code AS UNSIGNED)";
            query = manager.createNativeQuery(sql);
//            query.setParameter("id_prov", idprov);
            query.setParameter("id_monper", id_monper);
            query.setParameter("id_prog", id_prog);
            query.setParameter("id_activity", id_activity);
            query.setParameter("id_role", id_role);
        }

        List list   = query.getResultList();
        return list;
    }
    
    
    
    @GetMapping("admin/getallnsaprogram_pertama")
    public @ResponseBody List<Object> getallnsaprogram_pertama(@RequestParam("id_prov") String idprov, @RequestParam("id_role") String id_role, @RequestParam("id_monper") String id_monper) {
    	Query query = em.createNativeQuery("");
        if(id_role.equals("111111")){
            String sql  = "SELECT distinct b.id as id_prg, b.nm_program, b.nm_program_eng, b.internal_code FROM nsa_activity as a \n" +
                        "left join nsa_program as b on a.id_program = b.id\n" +
                        "inner join nsa_indicator as c on a.id = c.id_activity\n" +
                        "WHERE b.id_monper = :id_monper order by CAST(b.internal_code AS INT)";
            query = manager.createNativeQuery(sql);
//            query.setParameter("id_prov", idprov);
            query.setParameter("id_monper", id_monper);
//            query.setParameter("id_role", id_role);
        }else{
            String sql  = "SELECT distinct b.id as id_prg, b.nm_program, b.nm_program_eng, b.internal_code FROM nsa_activity as a \n" +
                        "left join nsa_program as b on a.id_program = b.id\n" +
                        "inner join nsa_indicator as c on a.id = c.id_activity\n" +
                        "WHERE a.id_role = :id_role and b.id_monper = :id_monper  order by CAST(b.internal_code AS INT)";
            query = manager.createNativeQuery(sql);
//            query.setParameter("id_prov", idprov);
            query.setParameter("id_monper", id_monper);
            query.setParameter("id_role", id_role);
        }

        List list   = query.getResultList();
        return list;
    }
    
    
    @GetMapping("admin/getallnsaactivity_pertama")
    public @ResponseBody List<Object> getallnsaactivity_pertama(@RequestParam("id_prov") String idprov, @RequestParam("id_role") String id_role, @RequestParam("id_monper") String id_monper, @RequestParam("id_prog") String id_prog) {
    	Query query = em.createNativeQuery("");
        if(id_role.equals("111111")){
            String sql  = "SELECT distinct a.id as id_act, a.nm_activity, a.nm_activity_eng, a.internal_code, a.id_role FROM nsa_activity as a \n" +
                        "left join nsa_program as b on a.id_program = b.id\n" +
                        "inner join nsa_indicator as c on a.id = c.id_activity\n" +
                        "WHERE b.id_monper = :id_monper and a.id_program = :id_prog order by CAST(a.internal_code AS UNSIGNED)";
            query = manager.createNativeQuery(sql);
//            query.setParameter("id_prov", idprov);
            query.setParameter("id_monper", id_monper);
            query.setParameter("id_prog", id_prog);
        }else{
            String sql  = "SELECT distinct a.id as id_act, a.nm_activity, a.nm_activity_eng, a.internal_code, a.id_role FROM nsa_activity as a \n" +
                        "left join nsa_program as b on a.id_program = b.id\n" +
                        "inner join nsa_indicator as c on a.id = c.id_activity\n" +
                        "WHERE a.id_role = :id_role and b.id_monper = :id_monper and a.id_program = :id_prog order by CAST(a.internal_code AS UNSIGNED)";
            query = manager.createNativeQuery(sql);
//            query.setParameter("id_prov", idprov);
            query.setParameter("id_monper", id_monper);
            query.setParameter("id_prog", id_prog);
            query.setParameter("id_role", id_role);
        }

        List list   = query.getResultList();
        return list;
    }
    
    
    @GetMapping("admin/getallnsaindi_pertama")
    public @ResponseBody List<Object> getallnsaindi_pertama(@RequestParam("id_prov") String idprov, @RequestParam("id_role") String id_role, @RequestParam("id_monper") String id_monper, @RequestParam("id_prog") String id_prog, @RequestParam("id_activity") String id_activity) {
    	Query query = em.createNativeQuery("");
        if(id_role.equals("111111")){
            String sql  = "SELECT c.id as id_act, c.nm_indicator, c.nm_indicator_eng, c.internal_code, a.id_role FROM nsa_activity as a \n" +
                        "left join nsa_program as b on a.id_program = b.id\n" +
                        "inner join nsa_indicator as c on a.id = c.id_activity\n" +
                        "WHERE b.id_monper = :id_monper and c.id_program = :id_prog and c.id_activity = :id_activity order by CAST(c.internal_code AS UNSIGNED)";
            query = manager.createNativeQuery(sql);
//            query.setParameter("id_prov", idprov);
            query.setParameter("id_monper", id_monper);
            query.setParameter("id_prog", id_prog);
            query.setParameter("id_activity", id_activity);
        }else{
            String sql  = "SELECT c.id as id_act, c.nm_indicator, c.nm_indicator_eng, c.internal_code, a.id_role FROM nsa_activity as a \n" +
                        "left join nsa_program as b on a.id_program = b.id\n" +
                        "inner join nsa_indicator as c on a.id = c.id_activity\n" +
                        "WHERE a.id_role = :id_role and b.id_monper = :id_monper and c.id_program = :id_prog and c.id_activity = :id_activity order by CAST(c.internal_code AS UNSIGNED)";
            query = manager.createNativeQuery(sql);
//            query.setParameter("id_prov", idprov);
            query.setParameter("id_monper", id_monper);
            query.setParameter("id_prog", id_prog);
            query.setParameter("id_activity", id_activity);
            query.setParameter("id_role", id_role);
        }

        List list   = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getallnsaprogram")
    public @ResponseBody List<Object> getallnsaprogram(@RequestParam("id_goals") String id_goals, @RequestParam("id_target") String id_target, @RequestParam("id_indicator") String id_indicator, @RequestParam("id_prov") String idprov, @RequestParam("id_role") String id_role, @RequestParam("id_monper") String id_monper) {
    	Query query = em.createNativeQuery("");
        System.out.println("id indi = "+id_indicator);
        if(id_role.equals("111111")){
            if(id_target.equals("null")){
                if(id_indicator.equals("null")){
                    String sql  = "select distinct c.id, c.nm_program, c.nm_program_eng, c.internal_code\n" +
                                "from nsa_map as a\n" +
                                "left join nsa_indicator as b on a.id_nsa_indicator = b.id\n" +
                                "left join nsa_program as c on b.id_program = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.internal_code AS INT)";

                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
//                    query.setParameter("id_role", id_role);
                    query.setParameter("id_monper", id_monper);
                }else{
                    String sql  = "select distinct c.id, c.nm_program, c.nm_program_eng, c.internal_code\n" +
                                "from nsa_map as a\n" +
                                "left join nsa_indicator as b on a.id_nsa_indicator = b.id\n" +
                                "left join nsa_program as c on b.id_program = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.internal_code AS INT) ";

                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
//                    query.setParameter("id_role", id_role);
                    query.setParameter("id_monper", id_monper);
                }
            }else{
                if(id_indicator.equals("null")){
                    String sql  = "select distinct c.id, c.nm_program, c.nm_program_eng, c.internal_code\n" +
                                "from nsa_map as a\n" +
                                "left join nsa_indicator as b on a.id_nsa_indicator = b.id\n" +
                                "left join nsa_program as c on b.id_program = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.internal_code AS INT) ";

                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
//                    query.setParameter("id_role", id_role);
                    query.setParameter("id_monper", id_monper);
                }else{
                    String sql  = "select distinct c.id, c.nm_program, c.nm_program_eng, c.internal_code\n" +
                                "from nsa_map as a\n" +
                                "left join nsa_indicator as b on a.id_nsa_indicator = b.id\n" +
                                "left join nsa_program as c on b.id_program = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.internal_code AS INT) ";

                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
//                    query.setParameter("id_role", id_role);
                    query.setParameter("id_monper", id_monper);
                }
            }
        }else{
            if(id_target.equals("null")){
                if(id_indicator.equals("null")){
                    String sql  = "select distinct c.id, c.nm_program, c.nm_program_eng, c.internal_code\n" +
                                "from nsa_map as a\n" +
                                "left join nsa_indicator as b on a.id_nsa_indicator = b.id\n" +
                                "left join nsa_program as c on b.id_program = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id_role = :id_role and c.id IS NOT NULL order by CAST(c.internal_code AS INT) ";

                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_role", id_role);
                    query.setParameter("id_monper", id_monper);
                }else{
                    String sql  = "select distinct c.id, c.nm_program, c.nm_program_eng, c.internal_code\n" +
                                "from nsa_map as a\n" +
                                "left join nsa_indicator as b on a.id_nsa_indicator = b.id\n" +
                                "left join nsa_program as c on b.id_program = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id_role = :id_role and c.id IS NOT NULL order by CAST(c.internal_code AS INT) ";

                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_role", id_role);
                    query.setParameter("id_monper", id_monper);
                }
            }else{
                if(id_indicator.equals("null")){
                    String sql  = "select distinct c.id, c.nm_program, c.nm_program_eng, c.internal_code\n" +
                                "from nsa_map as a\n" +
                                "left join nsa_indicator as b on a.id_nsa_indicator = b.id\n" +
                                "left join nsa_program as c on b.id_program = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id_role = :id_role and c.id IS NOT NULL order by CAST(c.internal_code AS INT) ";

                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_role", id_role);
                    query.setParameter("id_monper", id_monper);
                }else{
                    String sql  = "select distinct c.id, c.nm_program, c.nm_program_eng, c.internal_code\n" +
                                "from nsa_map as a\n" +
                                "left join nsa_indicator as b on a.id_nsa_indicator = b.id\n" +
                                "left join nsa_program as c on b.id_program = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id_role = :id_role and c.id IS NOT NULL order by CAST(c.internal_code AS INT) ";

                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_role", id_role);
                    query.setParameter("id_monper", id_monper);
                }
            }
        }
        
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getallnsaactivity")
    public @ResponseBody List<Object> getallnsaactivity(@RequestParam("id_goals") String id_goals, @RequestParam("id_target") String id_target, @RequestParam("id_indicator") String id_indicator, @RequestParam("id_prov") String idprov, @RequestParam("id_role") String id_role, @RequestParam("id_monper") String id_monper, @RequestParam("idprog") String idprog) {
    	Query query = em.createNativeQuery("");
//        System.out.println("id "+id_role);
        if(id_role.equals("111111")){
            if(id_target.equals("null")){
                if(id_indicator.equals("null")){
                    String sql  = "select distinct c.id, c.nm_activity, c.nm_activity_eng, c.internal_code, c.id_role\n" +
                                "from nsa_map as a\n" +
                                "left join nsa_indicator as b on a.id_nsa_indicator = b.id\n" +
                                "left join (select * from nsa_activity where id_program = :idprog) as c on b.id_activity = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.internal_code AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
//                    query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                }else{
                    String sql  = "select distinct c.id, c.nm_activity, c.nm_activity_eng, c.internal_code, c.id_role\n" +
                                "from nsa_map as a\n" +
                                "left join nsa_indicator as b on a.id_nsa_indicator = b.id\n" +
                                "left join (select * from nsa_activity where id_program = :idprog) as c on b.id_activity = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.internal_code AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
//                    query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                }
            }else{
                if(id_indicator.equals("null")){
                    String sql  = "select distinct c.id, c.nm_activity, c.nm_activity_eng, c.internal_code, c.id_role\n" +
                                "from nsa_map as a\n" +
                                "left join nsa_indicator as b on a.id_nsa_indicator = b.id\n" +
                                "left join (select * from nsa_activity where id_program = :idprog) as c on b.id_activity = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.internal_code AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
//                    query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                }else{
                    String sql  = "select distinct c.id, c.nm_activity, c.nm_activity_eng, c.internal_code, c.id_role\n" +
                                "from nsa_map as a\n" +
                                "left join nsa_indicator as b on a.id_nsa_indicator = b.id\n" +
                                "left join (select * from nsa_activity where id_program = :idprog) as c on b.id_activity = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.internal_code AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
//                    query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                }
            }
        }else{
            if(id_target.equals("null")){
                if(id_indicator.equals("null")){
                    String sql  = "select distinct c.id, c.nm_activity, c.nm_activity_eng, c.internal_code, c.id_role\n" +
                                "from nsa_map as a\n" +
                                "left join nsa_indicator as b on a.id_nsa_indicator = b.id\n" +
                                "left join (select * from nsa_activity where id_program = :idprog and id_role = :id_role) as c on b.id_activity = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.internal_code AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
                    query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                }else{
                    String sql  = "select distinct c.id, c.nm_activity, c.nm_activity_eng, c.internal_code, c.id_role\n" +
                                "from nsa_map as a\n" +
                                "left join nsa_indicator as b on a.id_nsa_indicator = b.id\n" +
                                "left join (select * from nsa_activity where id_program = :idprog and id_role = :id_role) as c on b.id_activity = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.internal_code AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
    //                query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
                    query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                }
            }else{
                if(id_indicator.equals("null")){
                    String sql  = "select distinct c.id, c.nm_activity, c.nm_activity_eng, c.internal_code, c.id_role\n" +
                                "from nsa_map as a\n" +
                                "left join nsa_indicator as b on a.id_nsa_indicator = b.id\n" +
                                "left join (select * from nsa_activity where id_program = :idprog and id_role = :id_role) as c on b.id_activity = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.internal_code AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
    //                query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
                    query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                }else{
                    String sql  = "select distinct c.id, c.nm_activity, c.nm_activity_eng, c.internal_code, c.id_role\n" +
                                "from nsa_map as a\n" +
                                "left join nsa_indicator as b on a.id_nsa_indicator = b.id\n" +
                                "left join (select * from nsa_activity where id_program = :idprog and id_role = :id_role) as c on b.id_activity = c.id\n" +
                                "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper and c.id IS NOT NULL order by CAST(c.internal_code AS UNSIGNED)";
                    query = manager.createNativeQuery(sql);
                    query.setParameter("id_goals", id_goals);
                    query.setParameter("id_target", id_target);
                    query.setParameter("id_indicator", id_indicator);
                    query.setParameter("id_prov", idprov);
                    query.setParameter("id_monper", id_monper);
                    query.setParameter("id_role", id_role);
                    query.setParameter("idprog", idprog);
                }
            }
        }
        
        
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getallnsaindi")
    public @ResponseBody List<Object> getallnsaindi(@RequestParam("id_goals") String id_goals, @RequestParam("id_target") String id_target, @RequestParam("id_indicator") String id_indicator, @RequestParam("id_prov") String idprov, @RequestParam("id_role") String id_role, @RequestParam("id_monper") String id_monper, @RequestParam("idprog") String idprog, @RequestParam("idactivity") int idactivity) {
    	Query query = em.createNativeQuery("");
//        System.out.println("id "+id_role);
        if(id_target.equals("null")){
            if(id_indicator.equals("null")){
                String sql  = "select distinct b.id, b.nm_indicator, b.nm_indicator_eng, b.internal_code\n" +
                            "from nsa_map as a\n" +
                            "left join (select * from nsa_indicator where id_program = :idprog and id_activity = :idactivity) as b on a.id_nsa_indicator = b.id\n" +
                            "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper \n" +
                            "and b.id IS NOT NULL";
                query = manager.createNativeQuery(sql);
                query.setParameter("id_goals", id_goals);
//                query.setParameter("id_target", id_target);
//                query.setParameter("id_indicator", id_indicator);
                query.setParameter("id_prov", idprov);
                query.setParameter("id_monper", id_monper);
        //        query.setParameter("id_role", id_role);
                query.setParameter("idprog", idprog);
                query.setParameter("idactivity", idactivity);
            }else{
                String sql  = "select distinct b.id, b.nm_indicator, b.nm_indicator_eng, b.internal_code\n" +
                            "from nsa_map as a\n" +
                            "left join (select * from nsa_indicator where id_program = :idprog and id_activity = :idactivity) as b on a.id_nsa_indicator = b.id\n" +
                            "where a.id_goals = :id_goals and a.id_target is null and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper \n" +
                            "and b.id IS NOT NULL";
                query = manager.createNativeQuery(sql);
                query.setParameter("id_goals", id_goals);
//                query.setParameter("id_target", id_target);
                query.setParameter("id_indicator", id_indicator);
                query.setParameter("id_prov", idprov);
                query.setParameter("id_monper", id_monper);
        //        query.setParameter("id_role", id_role);
                query.setParameter("idprog", idprog);
                query.setParameter("idactivity", idactivity);
            }
        }else{
            if(id_indicator.equals("null")){
                String sql  = "select distinct b.id, b.nm_indicator, b.nm_indicator_eng, b.internal_code\n" +
                            "from nsa_map as a\n" +
                            "left join (select * from nsa_indicator where id_program = :idprog and id_activity = :idactivity) as b on a.id_nsa_indicator = b.id\n" +
                            "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator is null and a.id_prov = :id_prov and a.id_monper = :id_monper \n" +
                            "and b.id IS NOT NULL";
                query = manager.createNativeQuery(sql);
                query.setParameter("id_goals", id_goals);
                query.setParameter("id_target", id_target);
//                query.setParameter("id_indicator", id_indicator);
                query.setParameter("id_prov", idprov);
                query.setParameter("id_monper", id_monper);
        //        query.setParameter("id_role", id_role);
                query.setParameter("idprog", idprog);
                query.setParameter("idactivity", idactivity);
            }else{
                String sql  = "select distinct b.id, b.nm_indicator, b.nm_indicator_eng, b.internal_code\n" +
                            "from nsa_map as a\n" +
                            "left join (select * from nsa_indicator where id_program = :idprog and id_activity = :idactivity) as b on a.id_nsa_indicator = b.id\n" +
                            "where a.id_goals = :id_goals and a.id_target = :id_target and a.id_indicator = :id_indicator and a.id_prov = :id_prov and a.id_monper = :id_monper \n" +
                            "and b.id IS NOT NULL";
                query = manager.createNativeQuery(sql);
                query.setParameter("id_goals", id_goals);
                query.setParameter("id_target", id_target);
                query.setParameter("id_indicator", id_indicator);
                query.setParameter("id_prov", idprov);
                query.setParameter("id_monper", id_monper);
        //        query.setParameter("id_role", id_role);
                query.setParameter("idprog", idprog);
                query.setParameter("idactivity", idactivity);
            }
        }
        
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/cekshowreport")
    public @ResponseBody List<Object> cekshowreport(@RequestParam("id_monper") int idmonper, @RequestParam("year") int year) {
    	String sql = "SELECT EXISTS(SELECT * FROM entry_show_report WHERE id_monper = :id_monper AND year = :year)";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("year", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/get_data_gri_pt")
    public @ResponseBody Map<String, Object> get_data_gri_pt(@RequestParam("year") String year) {
    	String sql = "SELECT id, company_name, file3 from entry_gri_ojk where year = '"+year+"' ";
    	Query query = manager.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/get_data_gri_th")
    public @ResponseBody Map<String, Object> get_data_gri_th(@RequestParam("company") String company) {
    	String sql = "SELECT id, year, file3 from entry_gri_ojk where company_name = '"+company+"' ";
    	Query query = manager.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/getgovtarget")
    public @ResponseBody List<Object> getgovtarget(@RequestParam("id_gov_indicator") int idgovindi, @RequestParam("year") int year) {
    	String sql = "SELECT value FROM gov_target WHERE id_gov_indicator = :id_gov_indicator AND year = :year";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", idgovindi);
        query.setParameter("year", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getidassign")
    public @ResponseBody List<Object> getidassign(@RequestParam("id_monper") int idmonper, @RequestParam("id_prov") int idprov) {
    	String sql = "SELECT id FROM assign_sdg_indicator WHERE id_monper = :id_monper AND id_prov = :id_prov GROUP BY id";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_prov", idprov);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgovrealyear")
    public @ResponseBody List<Object> getgovrealyear(@RequestParam("id_monper") int idmonper, @RequestParam("year_entry") int year) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1) "
    			+ "FROM entry_gov_indicator WHERE id_monper = :id_monper AND year_entry = :year_entry";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("year_entry", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgovrealsemester")
    public @ResponseBody List<Object> getgovrealsemester(@RequestParam("id_monper") int idmonper, @RequestParam("year_entry") int year) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1), COALESCE(NULLIF(new_value2,''),achievement2) "
    			+ "FROM entry_gov_indicator WHERE id_monper = :id_monper AND year_entry = :year_entry";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("year_entry", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgovrealquarter")
    public @ResponseBody List<Object> getgovrealquarter(@RequestParam("id_monper") int idmonper, @RequestParam("year_entry") int year) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1), COALESCE(NULLIF(new_value2,''),achievement2), "
    			+ "SELECT COALESCE(NULLIF(new_value3,''),achievement3), COALESCE(NULLIF(new_value4,''),achievement4) "
    			+ "FROM entry_gov_indicator WHERE id_monper = :id_monper AND year_entry = :year_entry";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("year_entry", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgovunit")
    public @ResponseBody List<Object> getgovunit(@RequestParam("id_gov_indicator") int idindi) {
    	String sql = "SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM gov_indicator WHERE id = :id_gov_indicator)";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", idindi);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgovfund")
    public @ResponseBody List<Object> getgovfund(@RequestParam("id_gov_indicator") int idindi, @RequestParam("id_monper") int idmonper) {
    	String sql = "SELECT funding_source, baseline FROM gov_funding WHERE id_gov_indicator = :id_gov_indicator AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", idindi);
        query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgovgoals")
    public @ResponseBody List<Object> getgovgoals(@RequestParam("id_prov") String idprov, @RequestParam("id_monper") int idmonper, 
    		@RequestParam("id_gov_indicator") int idindi) {
    	String sql = "SELECT a.id_gov_indicator, b.id as idgoals, b.nm_goals, b.nm_goals_eng, "
    			+ "c.id as idtarget, c.nm_target, c.nm_target_eng, d.id as idindi, d.nm_indicator, d.nm_indicator_eng "
    			+ "FROM gov_map a LEFT JOIN sdg_goals b ON b.id = a.id_goals LEFT JOIN "
    			+ "sdg_target c ON c.id = a.id_target LEFT JOIN sdg_indicator d ON d.id = a.id_indicator "
    			+ "WHERE id_prov = :id_prov AND id_monper = :id_monper AND id_gov_indicator = :id_gov_indicator";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_gov_indicator", idindi);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/cekgovranrad")
    public @ResponseBody List<Object> cekgovranrad(@RequestParam("id_monper") int idmonper, @RequestParam("id_prov") String idprov) {
    	String sql = "SELECT sdg_indicator FROM ran_rad WHERE id_monper = :id_monper AND id_prov = :id_prov";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_prov", idprov);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getsdgtar")
    public @ResponseBody List<Object> getsdgtar(@RequestParam("id_gov_indicator") int idindi, @RequestParam("id_role") int valrole, 
    		@RequestParam("year") int year) {
    	String sql = "SELECT value FROM sdg_indicator_target WHERE id_sdg_indicator = (SELECT id_indicator FROM gov_map WHERE id_gov_indicator = :id_gov_indicator) AND "
    			+ "id_role = :id_role AND year = :year";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", idindi);
        query.setParameter("id_role", valrole);
        query.setParameter("year", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getsdgreal")
    public @ResponseBody List<Object> getsdgreal(@RequestParam("id_gov_indicator") int idindi, @RequestParam("id_role") int valrole, 
    		@RequestParam("year") int year, @RequestParam("id_monper") int idmonper) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1) FROM entry_sdg WHERE "
    			+ "id_sdg_indicator = (SELECT id_indicator FROM gov_map WHERE id_gov_indicator = :id_gov_indicator) AND "
    			+ "id_role = :id_role AND year_entry = :year_entry AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", idindi);
        query.setParameter("id_role", valrole);
        query.setParameter("year_entry", year);
        query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getsdgfund")
    public @ResponseBody List<Object> getsdgfund(@RequestParam("id_gov_indicator") int idindi, @RequestParam("id_monper") int idmonper) {
    	String sql = "SELECT funding_source, baseline FROM sdg_funding "
    			+ "WHERE id_sdg_indicator = (SELECT id_indicator FROM gov_map WHERE id_gov_indicator = :id_gov_indicator) "
    			+ "AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", idindi);
        query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getsdgunit")
    public @ResponseBody List<Object> getsdgunit(@RequestParam("id_gov_indicator") int idindi) {
    	String sql = "SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM sdg_indicator WHERE id = (SELECT id_indicator FROM gov_map WHERE id_gov_indicator = :id_gov_indicator))";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", idindi);
        List list = query.getResultList();
        return list;
    }
    
 // ****************************** End Of Report Grafik ******************************
    
    @GetMapping("admin/getrole")
    public @ResponseBody List<Object> getrole(@RequestParam("id_prov") String idprov) {
    	String sql = "SELECT id_role FROM ref_role WHERE id_prov = :id_prov";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getmonper")
    public @ResponseBody List<Object> getmonper(@RequestParam("id_prov") String idprov) {
    	String sql = "SELECT id_monper, start_year, end_year FROM ran_rad WHERE id_prov = :id_prov and (status = 'on Going' or status = 'completed')";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getallrolebyprov")
    public @ResponseBody List<Object> getallrolebyprov(@RequestParam("id_prov") String idprov) {
    	String sql = "SELECT id_role, nm_role FROM ref_role WHERE id_prov = :id_prov and id_role <> '1'";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/graphsdg")
    public @ResponseBody List<Object> graphSdg(@RequestParam("id_prov") String idprov, @RequestParam("id_role") int idrole) {
        String sql = "SELECT a.*, b.id AS idgoals, b.nm_goals, b.nm_goals_eng, c.id AS idtarget, c.nm_target, c.nm_target_eng, " +
                "d.id AS idindicator, d.nm_indicator, d.nm_indicator_eng "
                + "FROM assign_sdg_indicator a LEFT JOIN " +
                "sdg_goals b ON b.id = a.id_goals LEFT JOIN " +
                "sdg_target c ON c.id = a.id_target LEFT JOIN " +
                "sdg_indicator d ON d.id = a.id_indicator WHERE a.id_prov = :id_prov AND id_role = :id_role";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", idprov);
        query.setParameter("id_role", idrole);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/graphidgovindi")
    public @ResponseBody List<Object> idGovIndi(@RequestParam("id_indicator") int idindi, @RequestParam("id_monper") int idmonper) {
        String sql = "SELECT id_gov_indicator, id_monper FROM gov_map WHERE id_indicator = :id_indicator AND id_monper = :id_monper";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_indicator", idindi);
        query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/isigovmap")
    public @ResponseBody List<Object> isigovmap(@RequestParam("id") int id_gov_indicator) {
        String sql = "SELECT a.*, b.nm_program, b.nm_program_eng, b.internal_code as progcode, " +
                "c.nm_activity, c.nm_activity_eng, c.internal_code AS actcode FROM gov_indicator a LEFT JOIN " +
                "gov_program b ON b.id = a.id_program LEFT JOIN " +
                "gov_activity c ON c.id = a.id_activity WHERE a.id = :id_gov_indicator";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", id_gov_indicator);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/graphidnsaindi")
    public @ResponseBody List<Object> idNsaIndi(@RequestParam("id_indicator") int idindi, @RequestParam("id_monper") int idmonper) {
        String sql = "SELECT id_nsa_indicator, id_monper FROM nsa_map WHERE id_indicator = :id_indicator AND id_monper = :id_monper";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_indicator", idindi);
        query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/isinsamap")
    public @ResponseBody List<Object> isinsamap(@RequestParam("id") int id_nsa_indicator) {
        String sql = "SELECT a.*, b.nm_program, b.nm_program_eng, b.internal_code as progcode, " +
                "c.nm_activity, c.nm_activity_eng, c.internal_code AS actcode FROM nsa_indicator a LEFT JOIN " +
                "nsa_program b ON b.id = a.id_program LEFT JOIN " +
                "nsa_activity c ON c.id = a.id_activity WHERE a.id = :id_nsa_indicator";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_nsa_indicator", id_nsa_indicator);
        List list = query.getResultList();
        return list;
    }

    //====================== Grafik Detail ======================
    
    @GetMapping("admin/report-graph-detail/{idgoals}/{idtarget}/{idindicator}/{idprog}/{idacty}/{idindi}/{idmonper}/{flag}/{valdaerah}/{valrole}/{tipe_periode}/{kode_bud}")
    public String grafikdetail(Model model, HttpSession session, @PathVariable("idgoals") String idgoals, @PathVariable("idtarget") String idtarget, @PathVariable("idindicator") String idindicator, @PathVariable("idprog") String idprog, @PathVariable("idacty") String idacty,
        @PathVariable("idindi") String idindi, @PathVariable("idmonper") int idmonper, 
        @PathVariable("flag") int flag, @PathVariable("valdaerah") String valdaerah, @PathVariable("valrole") String valrole, @PathVariable("tipe_periode") String tipe_periode, @PathVariable("kode_bud") String kode_bud) {
        model.addAttribute("title", "Report Graphic Detail");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("idgoals", idgoals);
        model.addAttribute("idtarget", idtarget);
        model.addAttribute("idindicator", idindicator);
        model.addAttribute("idprog", idprog);
        model.addAttribute("idacty", idacty);
        model.addAttribute("idindi", idindi);
        model.addAttribute("idmonper", idmonper);
        model.addAttribute("flag", flag);
        model.addAttribute("valdaerah", valdaerah);
        model.addAttribute("valrole", valrole);
        model.addAttribute("tipe_periode", tipe_periode);
        model.addAttribute("kode_bud", kode_bud);
        return "admin/report/graphdetail_new_versi_bangyos";
    }

    @GetMapping("admin/report-graph-detail-ind/{idgoals}/{idtarget}/{idindicator}/{idprog}/{idacty}/{idindi}/{idmonper}/{flag}/{valdaerah}/{valrole}/{tipe_periode}/{kode_bud}")
    public String grafikdetailind(Model model, HttpSession session, @PathVariable("idgoals") String idgoals, @PathVariable("idtarget") String idtarget, @PathVariable("idindicator") String idindicator, @PathVariable("idprog") String idprog, @PathVariable("idacty") String idacty,
        @PathVariable("idindi") String idindi, @PathVariable("idmonper") int idmonper, 
        @PathVariable("flag") int flag, @PathVariable("valdaerah") String valdaerah, @PathVariable("valrole") String valrole, @PathVariable("tipe_periode") String tipe_periode, @PathVariable("kode_bud") String kode_bud) {
        model.addAttribute("title", "Report Graphic Detail");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("idgoals", idgoals);
        model.addAttribute("idtarget", idtarget);
        model.addAttribute("idindicator", idindicator);
        model.addAttribute("idprog", idprog);
        model.addAttribute("idacty", idacty);
        model.addAttribute("idindi", idindi);
        model.addAttribute("idmonper", idmonper);
        model.addAttribute("flag", flag);
        model.addAttribute("valdaerah", valdaerah);
        model.addAttribute("valrole", valrole);
        model.addAttribute("tipe_periode", tipe_periode);
        model.addAttribute("kode_bud", kode_bud);
        return "admin/report/graphdetail_new_versi_indikator";
    }    


    @GetMapping("admin/report-graph-pie/{valdaerah}/{valrole}/{idmonper}")
    public String grafikdetail(Model model, HttpSession session,  @PathVariable("idmonper") int idmonper, @PathVariable("valdaerah") String valdaerah, @PathVariable("valrole") String valrole) {
        model.addAttribute("title", "Report Graphic Pie");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("idmonper", idmonper);
        model.addAttribute("valdaerah", valdaerah);
        model.addAttribute("valrole", valrole);
        return "admin/report/graph_pie_2";
    }	

    @GetMapping("admin/getpievalue")
    public @ResponseBody List<Object> isinsamap(@RequestParam("idprov") String idProv, @RequestParam("idmonper") int idMonper, @RequestParam("idgoal") int idGoal) {
        String sql = "Select e.nm_role, count(a.id_goals) from gov_map a " +
						"Left join sdg_goals b on a.id_goals = b.id " +
						"join gov_indicator c on a.id_gov_indicator = c.id " +
						"Left join gov_activity d on c.id_activity = d.id " +
						"Left join ref_role e on d.id_role = e.id_role " +
						"where a.id_prov = :idProv and a.id_monper = :idMonper and a.id_goals = :idGoal " +
						"GROUP BY e.nm_role " +
						"UNION " +
						"Select e.nm_role, count(a.id_goals) from nsa_map a " +
						"Left join sdg_goals b on a.id_goals = b.id " +
						"join nsa_indicator c on a.id_nsa_indicator = c.id " +
						"Left join nsa_activity d on c.id_activity = d.id " +
						"Left join ref_role e on d.id_role = e.id_role " +
						"where a.id_prov = :idProv and a.id_monper = :idMonper and a.id_goals = :idGoal " +
						"GROUP BY e.nm_role";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("idProv", idProv);
		query.setParameter("idMonper", idMonper);
		query.setParameter("idGoal", idGoal);
        List list = query.getResultList();
        return list;
    }	

    @GetMapping("admin/getpievalueind")
    public @ResponseBody List<Object> isinsamap2(@RequestParam("idprov") String idProv, @RequestParam("idmonper") int idMonper, @RequestParam("idind") int idInd) {
        String sql = "Select e.nm_role, count(a.id_indicator) " +
        "from gov_map a Left join sdg_indicator b on a.id_indicator = b.id " +
        "join gov_indicator c on a.id_gov_indicator = c.id " + 
        "Left join gov_activity d on c.id_activity = d.id " +
        "Left join ref_role e on d.id_role = e.id_role " +
        "where a.id_prov = :idProv and a.id_monper = :idMonper and a.id_indicator = :idInd " +
        "GROUP BY e.nm_role UNION " +
        "Select e.nm_role, count(a.id_indicator) " +
        "from nsa_map a Left join sdg_indicator b on a.id_indicator = b.id " +
        "join nsa_indicator c on a.id_nsa_indicator = c.id " +
        "Left join nsa_activity d on c.id_activity = d.id " +
        "Left join ref_role e on d.id_role = e.id_role " +
        "where a.id_prov = :idProv and a.id_monper = :idMonper and a.id_indicator = :idInd "+
        "GROUP BY e.nm_role";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("idProv", idProv);
		query.setParameter("idMonper", idMonper);
		query.setParameter("idInd", idInd);
        List list = query.getResultList();
        return list;
    }	    
    
    @GetMapping("admin/getprov")
    public @ResponseBody List<Object> getprov(@RequestParam("valdaerah") String val) {
    	String sql = "SELECT nm_prov, acronym FROM ref_province WHERE id_prov = :id_prov";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_prov", val);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgoals")
    public @ResponseBody List<Object> getgoals(@RequestParam("id_goals") String idgoals, @RequestParam("id_target") String idtarget, 
    		@RequestParam("id_indicator") String id_indicator, @RequestParam("id_monper") int id_monper) {
        Optional<RanRad> monper = radService.findOne(id_monper);
    	String status = monper.get().getStatus();
        String sql;
//        System.out.println("goal = "+idgoals+" , target = "+idtarget+" , indicator = "+id_indicator);
//        if(id_indicator.equals("null")){
//            System.out.println("indicator null");
//        }else{
//            System.out.println("indicator gak null");
//        }
        if(status.equals("completed")) {
            sql = "SELECT a.id_old AS idindicator, a.nm_indicator, a.nm_indicator_eng, b.id AS idgoals, b.nm_goals, b.nm_goals_eng, "
    			+ "c.id AS idtarget, c.nm_target, c.nm_target_eng, a.id_indicator, b.id_goals, c.id_target "
    			+ "FROM history_sdg_indicator a LEFT JOIN "
    			+ "(select * from history_sdg_goals where id_monper = '"+id_monper+"') b ON b.id_old = a.id_goals LEFT JOIN "
    			+ "(select * from history_sdg_target where id_monper = '"+id_monper+"') c ON c.id_old = a.id_target "
    			+ "WHERE a.id_old = :id_indicator and and a.id_monper='"+id_monper+"'";
        }else{
            sql = "SELECT a.id AS idindicator, a.nm_indicator, a.nm_indicator_eng, b.id AS idgoals, b.nm_goals, b.nm_goals_eng, "
    			+ "c.id AS idtarget, c.nm_target, c.nm_target_eng, a.id_indicator, b.id_goals, c.id_target "
    			+ "FROM sdg_indicator a LEFT JOIN "
    			+ "sdg_goals b ON b.id = a.id_goals LEFT JOIN "
    			+ "sdg_target c ON c.id = a.id_target "
    			+ "WHERE a.id = :id_indicator";
        }
    	Query query = manager.createNativeQuery(sql);
//        query.setParameter("id_goals", idgoals);
//        query.setParameter("id_target", idtarget);
        query.setParameter("id_indicator", id_indicator);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/gettargetlabel")
    public @ResponseBody List<Object> gettargetlabel(@RequestParam("id_goals") String idgoals, @RequestParam("id_target") String idtarget, 
    		@RequestParam("id_indicator") String id_indicator, @RequestParam("id_monper") int id_monper) {
        Optional<RanRad> monper = radService.findOne(id_monper);
    	String status = monper.get().getStatus();
        String sql;
        System.out.println("goal = "+idgoals+" , target = "+idtarget+" , indicator = "+id_indicator);
//        if(id_indicator.equals("null")){
//            System.out.println("indicator null");
//        }else{
//            System.out.println("indicator gak null");
//        }
        if(status.equals("completed")) {
            sql = "select \n" +
                "(select id_goals from history_sdg_goals where id_old = :id_goals and id_monper = '"+id_monper+"' ) as kode_g,\n" +
                "(select nm_goals from history_sdg_goals where id_old = :id_goals and id_monper = '"+id_monper+"' ) as nm_g,\n" +
                "(select nm_goals_eng from history_sdg_goals where id_old = :id_goals and id_monper = '"+id_monper+"' ) as nm_g_e,\n" +
                "(select id_target from history_sdg_target where id_old = :id_target and id_monper = '"+id_monper+"' ) as kode_t,\n" +
                "(select nm_target from history_sdg_target where id_old = :id_target and id_monper = '"+id_monper+"' ) as nm_t,\n" +
                "(select nm_target_eng from history_sdg_target where id_old = :id_target and id_monper = '"+id_monper+"' ) as nm_t_e";
        }else{
            sql = "select \n" +
                "(select id_goals from sdg_goals where id = :id_goals ) as kode_g,\n" +
                "(select nm_goals from sdg_goals where id = :id_goals ) as nm_g,\n" +
                "(select nm_goals_eng from sdg_goals where id = :id_goals ) as nm_g_e,\n" +
                "(select id_target from sdg_target where id = :id_target ) as kode_t,\n" +
                "(select nm_target from sdg_target where id = :id_target ) as nm_t,\n" +
                "(select nm_target_eng from sdg_target where id = :id_target ) as nm_t_e";
        }
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", idgoals);
        query.setParameter("id_target", idtarget);
//        query.setParameter("id_indicator", id_indicator);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getprogov")
    public @ResponseBody List<Object> getprogov(@RequestParam("id_program") int idgoals, @RequestParam("id_activity") int idtarget, 
    		@RequestParam("id_indicator") int idindikator) {
        System.out.println("program = "+idgoals+" activity = "+idtarget +" indicator = "+idindikator);
        
    	String sql = "SELECT a.id AS idindicator, a.nm_indicator, a.nm_indicator_eng, b.id AS idprog, b.nm_program, b.nm_program_eng, "
    			+ "c.id AS idact, c.nm_activity, c.nm_activity_eng, a.internal_code as ic1, b.internal_code as ic2, c.internal_code as ic3 FROM gov_indicator a LEFT JOIN "
    			+ "gov_program b ON b.id = a.id_program LEFT JOIN "
    			+ "gov_activity c ON c.id = a.id_activity "
    			+ "WHERE a.id = :id_indicator";
    	Query query = manager.createNativeQuery(sql);
//        query.setParameter("id_program", idgoals);
//        query.setParameter("id_activity", idtarget);
        query.setParameter("id_indicator", idindikator);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getprogov_kosong")
    public @ResponseBody List<Object> getprogov_kosong(@RequestParam("id_program") int idgoals, @RequestParam("id_activity") int idtarget, 
    		@RequestParam("id_indicator") int idindikator) {
        System.out.println("program = "+idgoals+" activity = "+idtarget +" indicator = "+idindikator);
        
    	String sql = "SELECT b.id AS idprog, b.nm_program, b.nm_program_eng, "
    			+ "a.id AS idact, a.nm_activity, a.nm_activity_eng, b.internal_code as ic2, a.internal_code as ic3 FROM gov_activity a LEFT JOIN "
    			+ "gov_program b ON b.id = a.id_program  "
    			+ "WHERE a.id = :idtarget";
    	Query query = manager.createNativeQuery(sql);
//        query.setParameter("id_program", idgoals);
//        query.setParameter("id_activity", idtarget);
        query.setParameter("idtarget", idtarget);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getpronsa")
    public @ResponseBody List<Object> getpronsa(@RequestParam("id_program") int idgoals, @RequestParam("id_activity") int idtarget, 
    		@RequestParam("id_indicator") int idindikator) {
    	String sql = "SELECT a.id AS idindicator, a.nm_indicator, a.nm_indicator_eng, b.id AS idprog, b.nm_program, b.nm_program_eng, "
    			+ "c.id AS idact, c.nm_activity, c.nm_activity_eng, a.internal_code as ic1, b.internal_code as ic2, c.internal_code as ic3 FROM nsa_indicator a LEFT JOIN "
    			+ "nsa_program b ON b.id = a.id_program LEFT JOIN "
    			+ "nsa_activity c ON c.id = a.id_activity  "
    			+ "WHERE a.id = :id_indicator";
    	Query query = manager.createNativeQuery(sql);
//        query.setParameter("id_program", idgoals);
//        query.setParameter("id_activity", idtarget);
        query.setParameter("id_indicator", idindikator);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getpronsa_kosong")
    public @ResponseBody List<Object> getpronsa_kosong(@RequestParam("id_program") int idgoals, @RequestParam("id_activity") int idtarget, 
    		@RequestParam("id_indicator") int idindikator) {
    	String sql = "SELECT b.id AS idprog, b.nm_program, b.nm_program_eng, "
    			+ "a.id AS idact, a.nm_activity, a.nm_activity_eng, b.internal_code as ic2, a.internal_code as ic3 FROM nsa_activity a LEFT JOIN "
    			+ "nsa_program b ON b.id = a.id_program "
    			+ "WHERE a.id = :idtarget";
    	Query query = manager.createNativeQuery(sql);
//        query.setParameter("id_program", idgoals);
//        query.setParameter("id_activity", idtarget);
        query.setParameter("idtarget", idtarget);
        List list = query.getResultList();
        return list;
    }

    //********************* Header Table *********************
    @GetMapping("admin/getheaderyear")
    public @ResponseBody List<Object> getheaderyear(@RequestParam("id_monper") int idmonper, @RequestParam("id_prov") int idprov) {
    	String sql = "SELECT start_year FROM ran_rad WHERE id_monper = :id_monper AND id_prov = :id_prov";
    	Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_prov", idprov);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/ceksdgmonper")
    public @ResponseBody List<Object> ceksdgmonper(@RequestParam("id_monper") int idmonper) {
        String sql = "SELECT sdg_indicator FROM ran_rad WHERE id_monper = :id_monper";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/ceksdgmonper_sdg")
    public @ResponseBody List<Object> ceksdgmonper_sdg(@RequestParam("id_monper") int idmonper, @RequestParam("id_indicator") int id_indicator, @RequestParam("id_prov") String id_prov) {
        String sql = "SELECT sdg_indicator FROM ran_rad WHERE id_monper = :id_monper union all SELECT count(*) FROM sdg_ranrad_disaggre WHERE id_indicator = :id_indicator union all SELECT start_year FROM ran_rad WHERE id_monper = :id_monper AND id_prov = :id_prov";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_indicator", id_indicator);
        query.setParameter("id_prov", id_prov);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/get_data_sdg_disaggre")
    public @ResponseBody Map<String, Object> get_data_sdg_disaggre(@RequestParam("id_monper") int idmonper, @RequestParam("id_indicator") int id_indicator, @RequestParam("id_prov") String id_prov, @RequestParam("tahun") int tahun, @RequestParam("role") String role) {
        Query query;
        Optional<RanRad> monper = radService.findOne(idmonper);
    	String status = monper.get().getStatus();
        
        if(role.equals("11111")){
            String sql = "";
            if(status.equals("completed")) {
                sql = "select a.id_role, nm_role, \n" +
                        "(SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM history_sdg_indicator WHERE id_old = :id_indicator)) as nama_unit,\n" +
                        "b1.value as target_1, b2.value as target_2, b3.value as target_3, b4.value as target_4, b5.value as target_5,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+tahun+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c1.realisasi_11 end) real_11, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c1.realisasi_12 end) real_12, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c1.realisasi_13 end) real_13, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c1.realisasi_14 end) real_14,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c2.realisasi_21 end) real_21, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c2.realisasi_22 end) real_22, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c2.realisasi_23 end) real_23, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c2.realisasi_24 end) real_24,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c3.realisasi_31 end) real_31, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c3.realisasi_32 end) real_32, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c3.realisasi_33 end) real_33, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c3.realisasi_34 end) real_34,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c4.realisasi_41 end) real_41, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c4.realisasi_42 end) real_42, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c4.realisasi_43 end) real_43, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c4.realisasi_44 end) real_44,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c5.realisasi_51 end) real_51, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c5.realisasi_52 end) real_52, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c5.realisasi_53 end) real_53, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c5.realisasi_54 end) real_54,\n" +
                        "(select baseline from sdg_funding where id_monper = :id_monper and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as ratb,\n" +
                        "(select funding_source from sdg_funding where id_sdg_indicator = :id_indicator and id_monper = :id_monper) as sumber,\n" +
                        "'JICA SDG' as pelaku\n" +
                        "from ref_role a \n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+0)+"') as b1 on a.id_role = b1.id_role\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+1)+"') as b2 on a.id_role = b2.id_role\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+2)+"') as b3 on a.id_role = b3.id_role\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+3)+"') as b4 on a.id_role = b4.id_role\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+4)+"') as b5 on a.id_role = b5.id_role\n" +
                        "\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_11, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_12, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_13, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_14, id_role FROM entry_sdg_detail where year_entry = '"+(tahun+0)+"' and id_monper = :id_monper and id_disaggre = (select id_old from history_sdg_ranrad_disaggre where id_indicator = :id_indicator) and id_disaggre_detail = (select b.id as id_disaggre_detail from history_sdg_ranrad_disaggre a left join history_sdg_ranrad_disaggre_detail b on a.id_old = b.id_disaggre where a.id_indicator = :id_indicator) ) as c1 on a.id_role = c1.id_role\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_21, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_22, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_23, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_24, id_role FROM entry_sdg_detail where year_entry = '"+(tahun+1)+"' and id_monper = :id_monper and id_disaggre = (select id_old from history_sdg_ranrad_disaggre where id_indicator = :id_indicator) and id_disaggre_detail = (select b.id as id_disaggre_detail from history_sdg_ranrad_disaggre a left join history_sdg_ranrad_disaggre_detail b on a.id_old = b.id_disaggre where a.id_indicator = :id_indicator) ) as c2 on a.id_role = c2.id_role\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_31, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_32, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_33, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_34, id_role FROM entry_sdg_detail where year_entry = '"+(tahun+2)+"' and id_monper = :id_monper and id_disaggre = (select id_old from history_sdg_ranrad_disaggre where id_indicator = :id_indicator) and id_disaggre_detail = (select b.id as id_disaggre_detail from history_sdg_ranrad_disaggre a left join history_sdg_ranrad_disaggre_detail b on a.id_old = b.id_disaggre where a.id_indicator = :id_indicator) ) as c3 on a.id_role = c3.id_role\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_41, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_42, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_43, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_44, id_role FROM entry_sdg_detail where year_entry = '"+(tahun+3)+"' and id_monper = :id_monper and id_disaggre = (select id_old from history_sdg_ranrad_disaggre where id_indicator = :id_indicator) and id_disaggre_detail = (select b.id as id_disaggre_detail from history_sdg_ranrad_disaggre a left join history_sdg_ranrad_disaggre_detail b on a.id_old = b.id_disaggre where a.id_indicator = :id_indicator) ) as c4 on a.id_role = c4.id_role\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_51, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_52, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_53, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_54, id_role FROM entry_sdg_detail where year_entry = '"+(tahun+4)+"' and id_monper = :id_monper and id_disaggre = (select id_old from history_sdg_ranrad_disaggre where id_indicator = :id_indicator) and id_disaggre_detail = (select b.id as id_disaggre_detail from history_sdg_ranrad_disaggre a left join history_sdg_ranrad_disaggre_detail b on a.id_old = b.id_disaggre where a.id_indicator = :id_indicator) ) as c5 on a.id_role = c5.id_role\n" +
                        "\n" +
                        "where a.id_prov = :id_prov ";
            }else{
                sql = "select a.id_role, nm_role, \n" +
                        "(SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM sdg_indicator WHERE id = :id_indicator)) as nama_unit,\n" +
                        "b1.value as target_1, b2.value as target_2, b3.value as target_3, b4.value as target_4, b5.value as target_5,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+tahun+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c1.realisasi_11 end) real_11, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c1.realisasi_12 end) real_12, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c1.realisasi_13 end) real_13, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c1.realisasi_14 end) real_14,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c2.realisasi_21 end) real_21, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c2.realisasi_22 end) real_22, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c2.realisasi_23 end) real_23, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c2.realisasi_24 end) real_24,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c3.realisasi_31 end) real_31, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c3.realisasi_32 end) real_32, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c3.realisasi_33 end) real_33, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c3.realisasi_34 end) real_34,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c4.realisasi_41 end) real_41, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c4.realisasi_42 end) real_42, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c4.realisasi_43 end) real_43, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c4.realisasi_44 end) real_44,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c5.realisasi_51 end) real_51, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c5.realisasi_52 end) real_52, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c5.realisasi_53 end) real_53, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c5.realisasi_54 end) real_54,\n" +
                        "(select baseline from sdg_funding where id_monper = :id_monper and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as ratb,\n" +
                        "(select funding_source from sdg_funding where id_monper = :id_monper and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as sumber,\n" +
                        "'JICA SDG' as pelaku\n" +
                        "from ref_role a \n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+0)+"') as b1 on a.id_role = b1.id_role\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+1)+"') as b2 on a.id_role = b2.id_role\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+2)+"') as b3 on a.id_role = b3.id_role\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+3)+"') as b4 on a.id_role = b4.id_role\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+4)+"') as b5 on a.id_role = b5.id_role\n" +
                        "\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_11, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_12, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_13, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_14, id_role FROM entry_sdg_detail where year_entry = '"+(tahun+0)+"' and id_monper = :id_monper and id_disaggre = (select id from sdg_ranrad_disaggre where id_indicator = :id_indicator) and id_disaggre_detail = (select b.id as id_disaggre_detail from sdg_ranrad_disaggre a left join sdg_ranrad_disaggre_detail b on a.id = b.id_disaggre where a.id_indicator = :id_indicator) ) as c1 on a.id_role = c1.id_role\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_21, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_22, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_23, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_24, id_role FROM entry_sdg_detail where year_entry = '"+(tahun+1)+"' and id_monper = :id_monper and id_disaggre = (select id from sdg_ranrad_disaggre where id_indicator = :id_indicator) and id_disaggre_detail = (select b.id as id_disaggre_detail from sdg_ranrad_disaggre a left join sdg_ranrad_disaggre_detail b on a.id = b.id_disaggre where a.id_indicator = :id_indicator) ) as c2 on a.id_role = c2.id_role\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_31, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_32, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_33, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_34, id_role FROM entry_sdg_detail where year_entry = '"+(tahun+2)+"' and id_monper = :id_monper and id_disaggre = (select id from sdg_ranrad_disaggre where id_indicator = :id_indicator) and id_disaggre_detail = (select b.id as id_disaggre_detail from sdg_ranrad_disaggre a left join sdg_ranrad_disaggre_detail b on a.id = b.id_disaggre where a.id_indicator = :id_indicator) ) as c3 on a.id_role = c3.id_role\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_41, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_42, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_43, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_44, id_role FROM entry_sdg_detail where year_entry = '"+(tahun+3)+"' and id_monper = :id_monper and id_disaggre = (select id from sdg_ranrad_disaggre where id_indicator = :id_indicator) and id_disaggre_detail = (select b.id as id_disaggre_detail from sdg_ranrad_disaggre a left join sdg_ranrad_disaggre_detail b on a.id = b.id_disaggre where a.id_indicator = :id_indicator) ) as c4 on a.id_role = c4.id_role\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_51, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_52, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_53, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_54, id_role FROM entry_sdg_detail where year_entry = '"+(tahun+4)+"' and id_monper = :id_monper and id_disaggre = (select id from sdg_ranrad_disaggre where id_indicator = :id_indicator) and id_disaggre_detail = (select b.id as id_disaggre_detail from sdg_ranrad_disaggre a left join sdg_ranrad_disaggre_detail b on a.id = b.id_disaggre where a.id_indicator = :id_indicator) ) as c5 on a.id_role = c5.id_role\n" +
                        "\n" +
                        "where a.id_prov = :id_prov ";
            }
            query = manager.createNativeQuery(sql);
            query.setParameter("id_monper", idmonper);
            query.setParameter("id_indicator", id_indicator);
            query.setParameter("id_prov", id_prov);
//            query.setParameter("tahun", tahun);
//            query.setParameter("role", role);
        }else{
            String sql = "";
            if(status.equals("completed")) {
                sql  = "select '' as id_role, (case when ((select id_role from entry_sdg where id_monper = :id_monper and id_sdg_indicator = :id_indicator limit 1) is null ) then 'Unassign' else (select nm_role from ref_role where id_role = :role) end) as nama_role, \n" +
                        "(SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM history_sdg_indicator WHERE id_old = :id_indicator)) as nama_unit,\n" +
                        "(select value from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+tahun+"' ) as target_1,\n" +
                        "(select value from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+1)+"' ) as target_2,\n" +
                        "(select value from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+2)+"' ) as target_3,\n" +
                        "(select value from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+3)+"' ) as target_4,\n" +
                        "(select value from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+4)+"' ) as target_5,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end) real_11, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end) real_12, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c1.realisasi_13 end) end) real_13, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c1.realisasi_14 end) end) real_14,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end) real_21, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end) real_22, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c2.realisasi_23 end) end) real_23, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c2.realisasi_24 end) end) real_24,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end) real_31, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end) real_32, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c3.realisasi_33 end) end) real_33, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c3.realisasi_34 end) end) real_34,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end) real_41, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end) real_42, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c4.realisasi_43 end) end) real_43, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c4.realisasi_44 end) end) real_44,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end) real_51, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end) real_52, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c5.realisasi_53 end) end) real_53, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c5.realisasi_54 end) end) real_54,\n" +
                        "(select baseline from sdg_funding where id_monper = :id_monper and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as ratb,\n" +
                        "(select funding_source from sdg_funding where id_monper = :id_monper and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as sumber,\n" +
                        "a.nm_disaggre, a.nm_disaggre_eng, a.desc_disaggre, a.desc_disaggre_eng ,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c1.realisasi_13 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c1.realisasi_14 end) end) )as total_quarter1,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c2.realisasi_23 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c2.realisasi_24 end) end) )as total_quarter2,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c3.realisasi_33 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c3.realisasi_34 end) end) )as total_quarter3,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c4.realisasi_43 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c4.realisasi_44 end) end) )as total_quarter4,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c5.realisasi_53 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c5.realisasi_54 end) end) )as total_quarter5,\n" +
                        
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end) )as total_smt1,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end) )as total_smt2,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end) )as total_smt3,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end) )as total_smt4,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end) )as total_smt5 \n" +
                        "from \n" +
                        "(select z.*, b.id_old as id_detail, b.id_disaggre as id_disaggre_dt, b.desc_disaggre, b.desc_disaggre_eng\n" +
                        "from history_sdg_ranrad_disaggre as z\n" +
                        "left join history_sdg_ranrad_disaggre_detail as b on z.id_old = b.id_disaggre\n" +
                        "where z.id_indicator = :id_indicator) as a\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_11, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_12, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_13, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_14, id_role, id_disaggre, id_disaggre_detail FROM entry_sdg_detail where year_entry = '"+(tahun+0)+"' and id_monper = :id_monper ) as c1 on a.id_disaggre_dt = c1.id_disaggre and a.id_detail = c1.id_disaggre_detail\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_21, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_22, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_23, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_24, id_role, id_disaggre, id_disaggre_detail FROM entry_sdg_detail where year_entry = '"+(tahun+1)+"' and id_monper = :id_monper ) as c2 on a.id_disaggre_dt = c2.id_disaggre and a.id_detail = c2.id_disaggre_detail\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_31, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_32, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_33, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_34, id_role, id_disaggre, id_disaggre_detail FROM entry_sdg_detail where year_entry = '"+(tahun+2)+"' and id_monper = :id_monper ) as c3 on a.id_disaggre_dt = c3.id_disaggre and a.id_detail = c3.id_disaggre_detail\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_41, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_42, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_43, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_44, id_role, id_disaggre, id_disaggre_detail FROM entry_sdg_detail where year_entry = '"+(tahun+3)+"' and id_monper = :id_monper ) as c4 on a.id_disaggre_dt = c4.id_disaggre and a.id_detail = c4.id_disaggre_detail\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_51, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_52, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_53, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_54, id_role, id_disaggre, id_disaggre_detail FROM entry_sdg_detail where year_entry = '"+(tahun+4)+"' and id_monper = :id_monper ) as c5 on a.id_disaggre_dt = c5.id_disaggre and a.id_detail = c5.id_disaggre_detail";
            }else{
                sql  = "select distinct '' as id_role,(case when ((select id_role from entry_sdg where id_monper = :id_monper and id_sdg_indicator = :id_indicator limit 1) is null ) then 'Unassign' else (select nm_role from ref_role where id_role = :role) end) as nama_role, \n" +
                        "(SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM sdg_indicator WHERE id = :id_indicator)) as nama_unit,\n" +
                        "(select value from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+tahun+"' ) as target_1,\n" +
                        "(select value from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+1)+"' ) as target_2,\n" +
                        "(select value from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+2)+"' ) as target_3,\n" +
                        "(select value from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+3)+"' ) as target_4,\n" +
                        "(select value from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+4)+"' ) as target_5,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end) real_11, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end) real_12, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c1.realisasi_13 end) end) real_13, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c1.realisasi_14 end) end) real_14,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end) real_21, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end) real_22, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c2.realisasi_23 end) end) real_23, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c2.realisasi_24 end) end) real_24,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end) real_31, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end) real_32, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c3.realisasi_33 end) end) real_33, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c3.realisasi_34 end) end) real_34,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end) real_41, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end) real_42, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c4.realisasi_43 end) end) real_43, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c4.realisasi_44 end) end) real_44,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end) real_51, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end) real_52, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c5.realisasi_53 end) end) real_53, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c5.realisasi_54 end) end) real_54,\n" +
                        "(select baseline from sdg_funding where id_monper = :id_monper and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as ratb,\n" +
                        "(select funding_source from sdg_funding where id_monper = :id_monper and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as sumber,\n" +
                        "a.nm_disaggre, a.nm_disaggre_eng, a.desc_disaggre, a.desc_disaggre_eng, \n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c1.realisasi_13 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c1.realisasi_14 end) end) ) as total_quarter1,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c2.realisasi_23 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c2.realisasi_24 end) end) ) as total_quarter2,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c3.realisasi_33 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c3.realisasi_34 end) end) ) as total_quarter3,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c4.realisasi_43 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c4.realisasi_44 end) end) ) as total_quarter4,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c5.realisasi_53 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c5.realisasi_54 end) end) ) as total_quarter5,\n" +
                        
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end) ) as total_smt1,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end) ) as total_smt2,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end) ) as total_smt3,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end) ) as total_smt4,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end) ) as total_smt5 \n" +
                        
                        "from \n" +
                        "(select z.*, b.id as id_detail, b.id_disaggre as id_disaggre_dt, b.desc_disaggre, b.desc_disaggre_eng\n" +
                        "from sdg_ranrad_disaggre as z\n" +
                        "left join sdg_ranrad_disaggre_detail as b on z.id = b.id_disaggre\n" +
                        "where z.id_indicator = :id_indicator) as a\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_11, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_12, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_13, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_14, id_role, id_disaggre, id_disaggre_detail FROM entry_sdg_detail where year_entry = '"+(tahun+0)+"' and id_monper = :id_monper ) as c1 on a.id_disaggre_dt = c1.id_disaggre and a.id_detail = c1.id_disaggre_detail\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_21, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_22, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_23, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_24, id_role, id_disaggre, id_disaggre_detail FROM entry_sdg_detail where year_entry = '"+(tahun+1)+"' and id_monper = :id_monper ) as c2 on a.id_disaggre_dt = c2.id_disaggre and a.id_detail = c2.id_disaggre_detail\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_31, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_32, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_33, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_34, id_role, id_disaggre, id_disaggre_detail FROM entry_sdg_detail where year_entry = '"+(tahun+2)+"' and id_monper = :id_monper ) as c3 on a.id_disaggre_dt = c3.id_disaggre and a.id_detail = c3.id_disaggre_detail\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_41, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_42, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_43, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_44, id_role, id_disaggre, id_disaggre_detail FROM entry_sdg_detail where year_entry = '"+(tahun+3)+"' and id_monper = :id_monper ) as c4 on a.id_disaggre_dt = c4.id_disaggre and a.id_detail = c4.id_disaggre_detail\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_51, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_52, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_53, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_54, id_role, id_disaggre, id_disaggre_detail FROM entry_sdg_detail where year_entry = '"+(tahun+4)+"' and id_monper = :id_monper ) as c5 on a.id_disaggre_dt = c5.id_disaggre and a.id_detail = c5.id_disaggre_detail";
            }
            query = manager.createNativeQuery(sql);
            query.setParameter("id_monper", idmonper);
            query.setParameter("id_indicator", id_indicator);
//            query.setParameter("id_prov", id_prov);
//            query.setParameter("tahun", tahun);
            query.setParameter("role", role);
        }
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/getentryshowreport_problemiden/{id_monper}/{year}")
    public @ResponseBody Map<String, Object> getentryshowreport_problemiden(@PathVariable("id_monper") String id_monper, @PathVariable("year") String year) {
    	   System.out.println("masuk = "+id_monper);
        Query query;
//        999999###111111
//        if(id_monper.equals("999999")){
//            String sql = "select * from entry_show_report where type = 'entry_problem_identify' ";
//            query = manager.createNativeQuery(sql);
//            query.setParameter("id_monper", id_monper);
//            query.setParameter("year", year);
//        }else{
            String sql = "select count(*) as total from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_problem_identify' ";
            query = manager.createNativeQuery(sql);
            query.setParameter("id_monper", id_monper);
            query.setParameter("year", year);
//        }
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/getentryshowreport_bestpract/{id_monper}/{year}")
    public @ResponseBody Map<String, Object> getentryshowreport_bestpract(@PathVariable("id_monper") String id_monper, @PathVariable("year") String year) {
    	   System.out.println("masuk = "+id_monper);
        Query query;
//        999999###111111
//        if(id_monper.equals("999999")){
//            String sql = "select * from entry_show_report where type = 'entry_problem_identify' ";
//            query = manager.createNativeQuery(sql);
//            query.setParameter("id_monper", id_monper);
//            query.setParameter("year", year);
//        }else{
            String sql = "select count(*) as total from entry_show_report where id_monper = :id_monper and year = :year and type = 'entry_best_practice' ";
            query = manager.createNativeQuery(sql);
            query.setParameter("id_monper", id_monper);
            query.setParameter("year", year);
//        }
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    
    @GetMapping("admin/get_data_sdg")
    public @ResponseBody Map<String, Object> get_data_sdg(@RequestParam("id_monper") int idmonper, @RequestParam("id_indicator") int id_indicator, @RequestParam("id_prov") String id_prov, @RequestParam("tahun") int tahun, @RequestParam("role") String role) {
        Query query;
        Optional<RanRad> monper = radService.findOne(idmonper);
    	String status = monper.get().getStatus();
        
        if(role.equals("11111")){
            String sql = "";
            if(status.equals("completed")) {
                //gak pengaruh
                sql = "select a.id_role, nm_role, \n" +
                        "(SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM sdg_indicator WHERE id = :id_indicator)) as nama_unit,\n" +
                        "b1.value as target_1, "
                        + "b2.value as target_2, "
                        + "b3.value as target_3, "
                        + "b4.value as target_4, "
                        + "b5.value as target_5,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+tahun+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c1.realisasi_11 end) real_11, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c1.realisasi_12 end) real_12, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c1.realisasi_13 end) real_13, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c1.realisasi_14 end) real_14,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c2.realisasi_21 end) real_21, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c2.realisasi_22 end) real_22, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c2.realisasi_23 end) real_23, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c2.realisasi_24 end) real_24,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c3.realisasi_31 end) real_31, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c3.realisasi_32 end) real_32, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c3.realisasi_33 end) real_33, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c3.realisasi_34 end) real_34,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c4.realisasi_41 end) real_41, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c4.realisasi_42 end) real_42, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c4.realisasi_43 end) real_43, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c4.realisasi_44 end) real_44,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c5.realisasi_51 end) real_51, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c5.realisasi_52 end) real_52, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c5.realisasi_53 end) real_53, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c5.realisasi_54 end) real_54,\n" +
                        "(select baseline from sdg_funding where id_monper = :id_monper and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as ratb,\n" +
                        "(select funding_source from sdg_funding where id_monper = :id_monper and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as sumber,\n" +
                        "'JICA SDG' as pelaku\n" +
                        "from ref_role a \n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+0)+"') as b1 on a.id_role = b1.id_role\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+1)+"') as b2 on a.id_role = b2.id_role\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+2)+"') as b3 on a.id_role = b3.id_role\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+3)+"') as b4 on a.id_role = b4.id_role\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+4)+"') as b5 on a.id_role = b5.id_role\n" +
                        "\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_11, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_12, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_13, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_14, id_role FROM entry_sdg where year_entry = '"+(tahun+0)+"' and id_monper = :id_monper and id_sdg_indicator = :id_indicator ) as c1 on a.id_role = c1.id_role\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_21, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_22, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_23, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_24, id_role FROM entry_sdg where year_entry = '"+(tahun+1)+"' and id_monper = :id_monper and id_sdg_indicator = :id_indicator ) as c2 on a.id_role = c2.id_role\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_31, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_32, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_33, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_34, id_role FROM entry_sdg where year_entry = '"+(tahun+2)+"' and id_monper = :id_monper and id_sdg_indicator = :id_indicator ) as c3 on a.id_role = c3.id_role\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_41, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_42, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_43, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_44, id_role FROM entry_sdg where year_entry = '"+(tahun+3)+"' and id_monper = :id_monper and id_sdg_indicator = :id_indicator ) as c4 on a.id_role = c4.id_role\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_51, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_52, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_53, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_54, id_role FROM entry_sdg where year_entry = '"+(tahun+4)+"' and id_monper = :id_monper and id_sdg_indicator = :id_indicator ) as c5 on a.id_role = c5.id_role\n" +
                        "\n" +
                        "where a.id_prov = :id_prov ";
            }else{
                sql = "select a.id_role, nm_role, \n" +
                        "(SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM sdg_indicator WHERE id = :id_indicator)) as nama_unit,\n" +
                        "b1.value as target_1, b2.value as target_2, b3.value as target_3, b4.value as target_4, b5.value as target_5,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+tahun+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c1.realisasi_11 end) real_11, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c1.realisasi_12 end) real_12, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c1.realisasi_13 end) real_13, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c1.realisasi_14 end) real_14,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c2.realisasi_21 end) real_21, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c2.realisasi_22 end) real_22, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c2.realisasi_23 end) real_23, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c2.realisasi_24 end) real_24,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c3.realisasi_31 end) real_31, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c3.realisasi_32 end) real_32, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c3.realisasi_33 end) real_33, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c3.realisasi_34 end) real_34,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c4.realisasi_41 end) real_41, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c4.realisasi_42 end) real_42, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c4.realisasi_43 end) real_43, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c4.realisasi_44 end) real_44,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else c5.realisasi_51 end) real_51, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else c5.realisasi_52 end) real_52, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else c5.realisasi_53 end) real_53, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else c5.realisasi_54 end) real_54,\n" +
                        "(select baseline from sdg_funding where id_monper = :id_monper and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as ratb,\n" +
                        "(select funding_source from sdg_funding where id_monper = :id_monper and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as sumber,\n" +
                        "'JICA SDG' as pelaku\n" +
                        "from ref_role a \n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+0)+"') as b1 on a.id_role = b1.id_role\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+1)+"') as b2 on a.id_role = b2.id_role\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+2)+"') as b3 on a.id_role = b3.id_role\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+3)+"') as b4 on a.id_role = b4.id_role\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+4)+"') as b5 on a.id_role = b5.id_role\n" +
                        "\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_11, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_12, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_13, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_14, id_role FROM entry_sdg where year_entry = '"+(tahun+0)+"' and id_monper = :id_monper and id_sdg_indicator = :id_indicator ) as c1 on a.id_role = c1.id_role\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_21, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_22, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_23, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_24, id_role FROM entry_sdg where year_entry = '"+(tahun+1)+"' and id_monper = :id_monper and id_sdg_indicator = :id_indicator ) as c2 on a.id_role = c2.id_role\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_31, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_32, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_33, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_34, id_role FROM entry_sdg where year_entry = '"+(tahun+2)+"' and id_monper = :id_monper and id_sdg_indicator = :id_indicator ) as c3 on a.id_role = c3.id_role\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_41, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_42, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_43, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_44, id_role FROM entry_sdg where year_entry = '"+(tahun+3)+"' and id_monper = :id_monper and id_sdg_indicator = :id_indicator ) as c4 on a.id_role = c4.id_role\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_51, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_52, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_53, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_54, id_role FROM entry_sdg where year_entry = '"+(tahun+4)+"' and id_monper = :id_monper and id_sdg_indicator = :id_indicator ) as c5 on a.id_role = c5.id_role\n" +
                        "\n" +
                        "where a.id_prov = :id_prov ";
            }
            query = manager.createNativeQuery(sql);
            query.setParameter("id_monper", idmonper);
            query.setParameter("id_indicator", id_indicator);
            query.setParameter("id_prov", id_prov);
//            query.setParameter("tahun", tahun);
//            query.setParameter("role", role);
        }else{
            String sql = "select a.id_role, (case when ((select id_role from entry_sdg where id_monper = :id_monper and id_sdg_indicator = :id_indicator limit 1) is null ) then 'Unassign' else nm_role end) as nama_role, \n" +
                        "(SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM sdg_indicator WHERE id = :id_indicator)) as nama_unit,\n" +
                        "(select value from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+0)+"') as target_1, "
                    + "(select value from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+1)+"') as target_2, "
                    + "(select value from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+2)+"') as target_3, "
                    + "(select value from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+3)+"') as target_4, "
                    + "(select value from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+4)+"') as target_5,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end) real_11, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end) real_12, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c1.realisasi_13 end) end) real_13, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c1.realisasi_14 end) end) real_14,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end) real_21, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end) real_22, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c2.realisasi_23 end) end) real_23, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c2.realisasi_24 end) end) real_24,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end) real_31, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end) real_32, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c3.realisasi_33 end) end) real_33, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c3.realisasi_34 end) end) real_34,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end) real_41, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end) real_42, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c4.realisasi_43 end) end) real_43, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c4.realisasi_44 end) end) real_44,\n" +
                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end) real_51, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end) real_52, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c5.realisasi_53 end) end) real_53, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c5.realisasi_54 end) end) real_54,\n" +
                        "(select baseline from sdg_funding where id_monper = :id_monper and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as ratb,\n" +
                        "(select funding_source from sdg_funding where id_monper = :id_monper and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as sumber,\n" +
                        "'JICA SDG' as pelaku, \n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c1.id_role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c1.id_role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c1.id_role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c1.realisasi_13 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c1.id_role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c1.realisasi_14 end) end) ) as total_quarter1,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c2.id_role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c2.id_role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c2.id_role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c2.realisasi_23 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c2.id_role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c2.realisasi_24 end) end) ) as total_quarter2,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c3.id_role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c3.id_role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c3.id_role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c3.realisasi_33 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c3.id_role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c3.realisasi_34 end) end) ) as total_quarter3,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c4.id_role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c4.id_role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c4.id_role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c4.realisasi_43 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c4.id_role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c4.realisasi_44 end) end) ) as total_quarter4,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c5.id_role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c5.id_role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c5.id_role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '3' and approval <> '3') = 0 ) then '' else c5.realisasi_53 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c5.id_role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '4' and approval <> '3') = 0 ) then '' else c5.realisasi_54 end) end) ) as total_quarter5,\n" +
                    
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c1.id_role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c1.id_role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end) ) as total_smt1,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c2.id_role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c2.id_role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end) ) as total_smt2,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c3.id_role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c3.id_role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end) ) as total_smt3,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c4.id_role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c4.id_role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end) ) as total_smt4,\n" +
                        "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c5.id_role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = c5.id_role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end) ) as total_smt5 \n" +
                    
//                        "(select id_role from entry_sdg where id_monper = :id_monper and id_sdg_indicator = :id_indicator limit 1) as cek_role_unassign \n" +
                        "from ref_role a \n" +
                        "left join (select * from ran_rad where id_monper = :id_monper  ) as rr on a.id_prov = rr.id_prov\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+0)+"') as b1 on rr.id_monper = b1.id_monper\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+1)+"') as b2 on rr.id_monper = b2.id_monper\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+2)+"') as b3 on rr.id_monper = b3.id_monper\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+3)+"') as b4 on rr.id_monper = b4.id_monper\n" +
                        "left join (select * from sdg_indicator_target where id_monper = :id_monper and id_sdg_indicator = :id_indicator and year = '"+(tahun+4)+"') as b5 on rr.id_monper = b5.id_monper\n" +
                        "\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_11, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_12, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_13, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_14, id_role, id_monper FROM entry_sdg where year_entry = '"+(tahun+0)+"' and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as c1 on rr.id_monper = c1.id_monper\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_21, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_22, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_23, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_24, id_role, id_monper FROM entry_sdg where year_entry = '"+(tahun+1)+"' and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as c2 on rr.id_monper = c2.id_monper\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_31, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_32, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_33, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_34, id_role, id_monper FROM entry_sdg where year_entry = '"+(tahun+2)+"' and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as c3 on rr.id_monper = c3.id_monper\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_41, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_42, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_43, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_44, id_role, id_monper FROM entry_sdg where year_entry = '"+(tahun+3)+"' and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as c4 on rr.id_monper = c4.id_monper\n" +
                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_51, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_52, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_53, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_54, id_role, id_monper FROM entry_sdg where year_entry = '"+(tahun+4)+"' and id_sdg_indicator = :id_indicator and id_monper = :id_monper) as c5 on rr.id_monper = c5.id_monper\n" +
                        "\n" +
                        "where a.id_prov = :id_prov \n" +
                        "and a.id_role = :role";
//            String sql = "select a.id_role, nm_role, \n" +
//                        "(SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM sdg_indicator WHERE id = :id_indicator)) as nama_unit,\n" +
//                        "b1.value as target_1, b2.value as target_2, b3.value as target_3, b4.value as target_4, b5.value as target_5,\n" +
//                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+tahun+"' and type = 'entry_sdg' and period = '1') = 0 ) then 0 else c1.realisasi_11 end) real_11, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '2') = 0 ) then 0 else c1.realisasi_12 end) real_12, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '3') = 0 ) then 0 else c1.realisasi_13 end) real_13, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_sdg' and period = '4') = 0 ) then 0 else c1.realisasi_14 end) real_14,\n" +
//                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '1') = 0 ) then 0 else c2.realisasi_21 end) real_21, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '2') = 0 ) then 0 else c2.realisasi_22 end) real_22, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '3') = 0 ) then 0 else c2.realisasi_23 end) real_23, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_sdg' and period = '4') = 0 ) then 0 else c2.realisasi_24 end) real_24,\n" +
//                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '1') = 0 ) then 0 else c3.realisasi_31 end) real_31, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '2') = 0 ) then 0 else c3.realisasi_32 end) real_32, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '3') = 0 ) then 0 else c3.realisasi_33 end) real_33, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_sdg' and period = '4') = 0 ) then 0 else c3.realisasi_34 end) real_34,\n" +
//                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '1') = 0 ) then 0 else c4.realisasi_41 end) real_41, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '2') = 0 ) then 0 else c4.realisasi_42 end) real_42, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '3') = 0 ) then 0 else c4.realisasi_43 end) real_43, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_sdg' and period = '4') = 0 ) then 0 else c4.realisasi_44 end) real_44,\n" +
//                        "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '1') = 0 ) then 0 else c5.realisasi_51 end) real_51, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '2') = 0 ) then 0 else c5.realisasi_52 end) real_52, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '3') = 0 ) then 0 else c5.realisasi_53 end) real_53, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_sdg' and period = '4') = 0 ) then 0 else c5.realisasi_54 end) real_54,\n" +
//                        "(select baseline from sdg_funding where id_sdg_indicator = :id_indicator and id_monper = :id_monper) as ratb,\n" +
//                        "(select funding_source from sdg_funding where id_sdg_indicator = :id_indicator and id_monper = :id_monper) as sumber,\n" +
//                        "'JICA SDG' as pelaku\n" +
//                        "from ref_role a \n" +
//                        "left join (select * from sdg_indicator_target where id_sdg_indicator = :id_indicator and year = '"+(tahun+0)+"') as b1 on a.id_role = b1.id_role\n" +
//                        "left join (select * from sdg_indicator_target where id_sdg_indicator = :id_indicator and year = '"+(tahun+1)+"') as b2 on a.id_role = b2.id_role\n" +
//                        "left join (select * from sdg_indicator_target where id_sdg_indicator = :id_indicator and year = '"+(tahun+2)+"') as b3 on a.id_role = b3.id_role\n" +
//                        "left join (select * from sdg_indicator_target where id_sdg_indicator = :id_indicator and year = '"+(tahun+3)+"') as b4 on a.id_role = b4.id_role\n" +
//                        "left join (select * from sdg_indicator_target where id_sdg_indicator = :id_indicator and year = '"+(tahun+4)+"') as b5 on a.id_role = b5.id_role\n" +
//                        "\n" +
//                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_11, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_12, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_13, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_14, id_role FROM entry_sdg where year_entry = '"+(tahun+0)+"' and id_monper = :id_monper and id_sdg_indicator = :id_indicator ) as c1 on a.id_role = c1.id_role\n" +
//                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_21, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_22, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_23, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_24, id_role FROM entry_sdg where year_entry = '"+(tahun+1)+"' and id_monper = :id_monper and id_sdg_indicator = :id_indicator ) as c2 on a.id_role = c2.id_role\n" +
//                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_31, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_32, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_33, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_34, id_role FROM entry_sdg where year_entry = '"+(tahun+2)+"' and id_monper = :id_monper and id_sdg_indicator = :id_indicator ) as c3 on a.id_role = c3.id_role\n" +
//                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_41, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_42, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_43, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_44, id_role FROM entry_sdg where year_entry = '"+(tahun+3)+"' and id_monper = :id_monper and id_sdg_indicator = :id_indicator ) as c4 on a.id_role = c4.id_role\n" +
//                        "left join (SELECT COALESCE(NULLIF(new_value1,''),achievement1) as realisasi_51, COALESCE(NULLIF(new_value2,''),achievement2) as realisasi_52, COALESCE(NULLIF(new_value3,''),achievement3) as realisasi_53, COALESCE(NULLIF(new_value4,''),achievement4) as realisasi_54, id_role FROM entry_sdg where year_entry = '"+(tahun+4)+"' and id_monper = :id_monper and id_sdg_indicator = :id_indicator ) as c5 on a.id_role = c5.id_role\n" +
//                        "\n" +
//                        "where a.id_prov = :id_prov \n" +
//                        "and a.id_role = :role";
            query = manager.createNativeQuery(sql);
            query.setParameter("id_monper", idmonper);
            query.setParameter("id_indicator", id_indicator);
            query.setParameter("id_prov", id_prov);
//            query.setParameter("tahun", tahun);
            query.setParameter("role", role);
        }
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/cek_total_unit")
    public @ResponseBody Map<String, Object> cek_total_unit(@RequestParam("id_indicator") String id_indicator) {
        Query query;
        String sql = "SELECT calculation FROM ref_unit WHERE id_unit = (SELECT unit FROM sdg_indicator WHERE id = :id_indicator)";
        query = manager.createNativeQuery(sql);
        query.setParameter("id_indicator", id_indicator);
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/cek_total_unit_gov")
    public @ResponseBody Map<String, Object> cek_total_unit_gov(@RequestParam("id_indicator") String id_indicator) {
        Query query;
        String sql = "SELECT calculation FROM ref_unit WHERE id_unit = (SELECT unit FROM gov_indicator WHERE id = :id_indicator)";
        query = manager.createNativeQuery(sql);
        query.setParameter("id_indicator", id_indicator);
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/cek_total_unit_non_gov")
    public @ResponseBody Map<String, Object> cek_total_unit_non_gov(@RequestParam("id_indicator") String id_indicator) {
        Query query;
        String sql = "SELECT calculation FROM ref_unit WHERE id_unit = (SELECT unit FROM nsa_indicator WHERE id = :id_indicator)";
        query = manager.createNativeQuery(sql);
        query.setParameter("id_indicator", id_indicator);
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    //gov indicator
    @GetMapping("admin/get_data_show_gov")
    public @ResponseBody Map<String, Object> get_data_show_gov(@RequestParam("id_monper") int idmonper, @RequestParam("id_indicator") String id_indicator, @RequestParam("id_prov") String id_prov, @RequestParam("tahun") int tahun, @RequestParam("role") String role, @RequestParam("kode_bud") String kode_bud, @RequestParam("idacty") String id_acty) {
        Query query;
        if(kode_bud.equals("1")){
            if(role.equals("11111")){
                String sql = "select z.id_role, z.nm_role, \n" +
                            " '' as nama_unit,\n" +
                            " (select budget_allocation from gov_activity where id = :id_acty) as target_1, \n" +
                            " (select budget_allocation from gov_activity where id = :id_acty) as target_2, \n" +
                            " (select budget_allocation from gov_activity where id = :id_acty) as target_3, \n" +
                            " (select budget_allocation from gov_activity where id = :id_acty) as target_4, \n" +
                            " (select budget_allocation from gov_activity where id = :id_acty) as target_5,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else c1.realisasi_11 end) real_11, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else c1.realisasi_12 end) real_12, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and period = '3') = 0 ) then '' else c1.realisasi_13 end) real_13, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and period = '4') = 0 ) then '' else c1.realisasi_14 end) real_14,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else c2.realisasi_21 end) real_21, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else c2.realisasi_22 end) real_22, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and period = '3') = 0 ) then '' else c2.realisasi_23 end) real_23, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and period = '4') = 0 ) then '' else c2.realisasi_24 end) real_24,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else c3.realisasi_31 end) real_31, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else c3.realisasi_32 end) real_32, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and period = '3') = 0 ) then '' else c3.realisasi_33 end) real_33, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and period = '4') = 0 ) then '' else c3.realisasi_34 end) real_34,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else c4.realisasi_41 end) real_41, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else c4.realisasi_42 end) real_42, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and period = '3') = 0 ) then '' else c4.realisasi_43 end) real_43, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and period = '4') = 0 ) then '' else c4.realisasi_44 end) real_44,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else c5.realisasi_51 end) real_51, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else c5.realisasi_52 end) real_52, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and period = '3') = 0 ) then '' else c5.realisasi_53 end) real_53, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and period = '4') = 0 ) then '' else c5.realisasi_54 end) real_54,\n" +
                            " '' as ratb,\n" +
                            " '' as sumber,\n" +
                            "'JICA SDG' as pelaku\n" +
                            "from ref_role z \n" +
                            "\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_11, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_12, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_13, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_14, (select id_role from gov_activity where id = :id_acty ) as id_role FROM entry_gov_budget a where a.year_entry = '"+(tahun+0)+"' and a.id_monper = :id_monper and a.id_gov_activity = :id_acty ) as c1 on z.id_role = c1.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_21, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_22, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_23, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_24, (select id_role from gov_activity where id = :id_acty ) as id_role FROM entry_gov_budget a where a.year_entry = '"+(tahun+1)+"' and a.id_monper = :id_monper and a.id_gov_activity = :id_acty ) as c2 on z.id_role = c2.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_31, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_32, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_33, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_34, (select id_role from gov_activity where id = :id_acty ) as id_role FROM entry_gov_budget a where a.year_entry = '"+(tahun+2)+"' and a.id_monper = :id_monper and a.id_gov_activity = :id_acty ) as c3 on z.id_role = c3.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_41, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_42, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_43, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_44, (select id_role from gov_activity where id = :id_acty ) as id_role FROM entry_gov_budget a where a.year_entry = '"+(tahun+3)+"' and a.id_monper = :id_monper and a.id_gov_activity = :id_acty ) as c4 on z.id_role = c4.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_51, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_52, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_53, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_54, (select id_role from gov_activity where id = :id_acty ) as id_role FROM entry_gov_budget a where a.year_entry = '"+(tahun+4)+"' and a.id_monper = :id_monper and a.id_gov_activity = :id_acty ) as c5 on z.id_role = c5.id_role\n" +
                            "\n" +
                            "where z.id_prov = :id_prov ";
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", idmonper);
                query.setParameter("id_acty", id_acty);
                query.setParameter("id_prov", id_prov);
    //            query.setParameter("tahun", tahun);
    //            query.setParameter("role", role);
            }else{
                String sql  = "select z.id_role, z.nm_role, \n" +
                            " '' as nama_unit,\n" +
                            " (select budget_allocation from gov_activity where id = :id_acty) as target_1, \n" +
                            " (select budget_allocation from gov_activity where id = :id_acty) as target_2, \n" +
                            " (select budget_allocation from gov_activity where id = :id_acty) as target_3, \n" +
                            " (select budget_allocation from gov_activity where id = :id_acty) as target_4, \n" +
                            " (select budget_allocation from gov_activity where id = :id_acty) as target_5,\n" +
                        
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end) real_11, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end) real_12, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and periode = '3' and approval <> '3') = 0 ) then '' else c1.realisasi_13 end) end) real_13, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and periode = '4' and approval <> '3') = 0 ) then '' else c1.realisasi_14 end) end) real_14,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end) real_21, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end) real_22, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and periode = '3' and approval <> '3') = 0 ) then '' else c2.realisasi_23 end) end) real_23, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and periode = '4' and approval <> '3') = 0 ) then '' else c2.realisasi_24 end) end) real_24,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end) real_31, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end) real_32, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and periode = '3' and approval <> '3') = 0 ) then '' else c3.realisasi_33 end) end) real_33, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and periode = '4' and approval <> '3') = 0 ) then '' else c3.realisasi_34 end) end) real_34,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end) real_41, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end) real_42, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and periode = '3' and approval <> '3') = 0 ) then '' else c4.realisasi_43 end) end) real_43, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and periode = '4' and approval <> '3') = 0 ) then '' else c4.realisasi_44 end) end) real_44,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end) real_51, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end) real_52, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and periode = '3' and approval <> '3') = 0 ) then '' else c5.realisasi_53 end) end) real_53, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and periode = '4' and approval <> '3') = 0 ) then '' else c5.realisasi_54 end) end) real_54,\n" +
                            " '' as ratb,\n" +
                            " '' as sumber,\n" +
                            "'JICA SDG' as pelaku,  \n" +
                        
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and periode = '3' and approval <> '3') = 0 ) then '' else c1.realisasi_13 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and periode = '4' and approval <> '3') = 0 ) then '' else c1.realisasi_14 end) end) ) as total_quarter1,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and periode = '3' and approval <> '3') = 0 ) then '' else c2.realisasi_23 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and periode = '4' and approval <> '3') = 0 ) then '' else c2.realisasi_24 end) end) ) as total_quarter2,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and periode = '3' and approval <> '3') = 0 ) then '' else c3.realisasi_33 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and periode = '4' and approval <> '3') = 0 ) then '' else c3.realisasi_34 end) end) ) as total_quarter3,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and periode = '3' and approval <> '3') = 0 ) then '' else c4.realisasi_43 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and periode = '4' and approval <> '3') = 0 ) then '' else c4.realisasi_44 end) end) ) as total_quarter4,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and periode = '3' and approval <> '3') = 0 ) then '' else c5.realisasi_53 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and periode = '4' and approval <> '3') = 0 ) then '' else c5.realisasi_54 end) end) ) as total_quarter5,\n" +
                        
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_budget' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end) ) as total_smt1,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_budget' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end) ) as total_smt2,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_budget' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end) ) as total_smt3,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_budget' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end) ) as total_smt4,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_budget' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end) ) as total_smt5 \n" +
                        
                            "from ref_role z \n" +
                            "\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_11, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_12, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_13, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_14, (select id_role from gov_activity where id = :id_acty ) as id_role FROM entry_gov_budget a where a.year_entry = '"+(tahun+0)+"' and a.id_monper = :id_monper and a.id_gov_activity = :id_acty ) as c1 on z.id_role = c1.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_21, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_22, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_23, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_24, (select id_role from gov_activity where id = :id_acty ) as id_role FROM entry_gov_budget a where a.year_entry = '"+(tahun+1)+"' and a.id_monper = :id_monper and a.id_gov_activity = :id_acty ) as c2 on z.id_role = c2.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_31, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_32, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_33, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_34, (select id_role from gov_activity where id = :id_acty ) as id_role FROM entry_gov_budget a where a.year_entry = '"+(tahun+2)+"' and a.id_monper = :id_monper and a.id_gov_activity = :id_acty ) as c3 on z.id_role = c3.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_41, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_42, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_43, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_44, (select id_role from gov_activity where id = :id_acty ) as id_role FROM entry_gov_budget a where a.year_entry = '"+(tahun+3)+"' and a.id_monper = :id_monper and a.id_gov_activity = :id_acty ) as c4 on z.id_role = c4.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_51, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_52, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_53, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_54, (select id_role from gov_activity where id = :id_acty ) as id_role FROM entry_gov_budget a where a.year_entry = '"+(tahun+4)+"' and a.id_monper = :id_monper and a.id_gov_activity = :id_acty ) as c5 on z.id_role = c5.id_role\n" +
                            "\n" +
                            "where z.id_prov = :id_prov \n" +
                            "and z.id_role = :role ";
                System.out.println("query = "+sql);
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", idmonper);
                query.setParameter("id_acty", id_acty);
                query.setParameter("id_prov", id_prov);
    //            query.setParameter("tahun", tahun);
                query.setParameter("role", role);
            }
        }else{
            if(role.equals("11111")){
                String sql = "select z.id_role, z.nm_role, \n" +
                            "(SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM gov_indicator WHERE id = :id_indicator)) as nama_unit,\n" +
                            "(select value from gov_target where id_gov_indicator = :id_indicator and year = '"+(tahun+0)+"') as target_1, \n" +
                            "(select value from gov_target where id_gov_indicator = :id_indicator and year = '"+(tahun+1)+"') as target_2, \n" +
                            "(select value from gov_target where id_gov_indicator = :id_indicator and year = '"+(tahun+2)+"') as target_3, \n" +
                            "(select value from gov_target where id_gov_indicator = :id_indicator and year = '"+(tahun+3)+"') as target_4, \n" +
                            "(select value from gov_target where id_gov_indicator = :id_indicator and year = '"+(tahun+4)+"') as target_5,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else c1.realisasi_11 end) real_11, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else c1.realisasi_12 end) real_12, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and period = '3') = 0 ) then '' else c1.realisasi_13 end) real_13, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and period = '4') = 0 ) then '' else c1.realisasi_14 end) real_14,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else c2.realisasi_21 end) real_21, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else c2.realisasi_22 end) real_22, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and period = '3') = 0 ) then '' else c2.realisasi_23 end) real_23, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and period = '4') = 0 ) then '' else c2.realisasi_24 end) real_24,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else c3.realisasi_31 end) real_31, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else c3.realisasi_32 end) real_32, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and period = '3') = 0 ) then '' else c3.realisasi_33 end) real_33, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and period = '4') = 0 ) then '' else c3.realisasi_34 end) real_34,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else c4.realisasi_41 end) real_41, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else c4.realisasi_42 end) real_42, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and period = '3') = 0 ) then '' else c4.realisasi_43 end) real_43, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and period = '4') = 0 ) then '' else c4.realisasi_44 end) real_44,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else c5.realisasi_51 end) real_51, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else c5.realisasi_52 end) real_52, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and period = '3') = 0 ) then '' else c5.realisasi_53 end) real_53, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and period = '4') = 0 ) then '' else c5.realisasi_54 end) real_54,\n" +
                            "(select baseline from gov_funding where id_gov_indicator = :id_indicator and id_monper = :id_monper) as ratb,\n" +
                            "(select funding_source from gov_funding where id_gov_indicator = :id_indicator and id_monper = :id_monper) as sumber,\n" +
                            "'JICA SDG' as pelaku\n" +
                            "from ref_role z \n" +
                            "\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_11, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_12, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_13, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_14, (select id_role from gov_activity where id = (select id_activity from gov_indicator where id = :id_indicator ) ) as id_role FROM entry_gov_indicator a where a.year_entry = '"+(tahun+0)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c1 on z.id_role = c1.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_21, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_22, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_23, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_24, (select id_role from gov_activity where id = (select id_activity from gov_indicator where id = :id_indicator ) ) as id_role FROM entry_gov_indicator a where a.year_entry = '"+(tahun+1)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c2 on z.id_role = c2.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_31, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_32, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_33, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_34, (select id_role from gov_activity where id = (select id_activity from gov_indicator where id = :id_indicator ) ) as id_role FROM entry_gov_indicator a where a.year_entry = '"+(tahun+2)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c3 on z.id_role = c3.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_41, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_42, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_43, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_44, (select id_role from gov_activity where id = (select id_activity from gov_indicator where id = :id_indicator ) ) as id_role FROM entry_gov_indicator a where a.year_entry = '"+(tahun+3)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c4 on z.id_role = c4.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_51, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_52, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_53, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_54, (select id_role from gov_activity where id = (select id_activity from gov_indicator where id = :id_indicator ) ) as id_role FROM entry_gov_indicator a where a.year_entry = '"+(tahun+4)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c5 on z.id_role = c5.id_role\n" +
                            "\n" +
                            "where z.id_prov = :id_prov ";
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", idmonper);
                query.setParameter("id_indicator", id_indicator);
                query.setParameter("id_prov", id_prov);
    //            query.setParameter("tahun", tahun);
    //            query.setParameter("role", role);
            }else{
                String sql  = "select z.id_role, z.nm_role, \n" +
                            "(SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM gov_indicator WHERE id = :id_indicator)) as nama_unit,\n" +
                            "(select value from gov_target where id_gov_indicator = :id_indicator and year = '"+(tahun+0)+"') as target_1, \n" +
                            "(select value from gov_target where id_gov_indicator = :id_indicator and year = '"+(tahun+1)+"') as target_2, \n" +
                            "(select value from gov_target where id_gov_indicator = :id_indicator and year = '"+(tahun+2)+"') as target_3, \n" +
                            "(select value from gov_target where id_gov_indicator = :id_indicator and year = '"+(tahun+3)+"') as target_4, \n" +
                            "(select value from gov_target where id_gov_indicator = :id_indicator and year = '"+(tahun+4)+"') as target_5,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end) real_11, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end) real_12, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c1.realisasi_13 end) end) real_13, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c1.realisasi_14 end) end) real_14,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end) real_21, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end) real_22, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c2.realisasi_23 end) end) real_23, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c2.realisasi_24 end) end) real_24,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end) real_31, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end) real_32, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c3.realisasi_33 end) end) real_33, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c3.realisasi_34 end) end) real_34,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end) real_41, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end) real_42, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c4.realisasi_43 end) end) real_43, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c4.realisasi_44 end) end) real_44,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end) real_51, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end) real_52, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c5.realisasi_53 end) end) real_53, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c5.realisasi_54 end) end) real_54,\n" +
                            "(select baseline from gov_funding where id_gov_indicator = :id_indicator and id_monper = :id_monper) as ratb,\n" +
                            "(select funding_source from gov_funding where id_gov_indicator = :id_indicator and id_monper = :id_monper) as sumber,\n" +
                            "'JICA SDG' as pelaku, "+ 
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c1.realisasi_13 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c1.realisasi_14 end) end)) as total_quarter1, \n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c2.realisasi_23 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c2.realisasi_24 end) end) ) as total_quarter2,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c3.realisasi_33 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c3.realisasi_34 end) end) ) as total_quarter3,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c4.realisasi_43 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c4.realisasi_44 end) end) ) as total_quarter4,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c5.realisasi_53 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c5.realisasi_54 end) end) ) as total_quarter5,\n" +
                        
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_gov_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end)) as total_semester1, \n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_gov_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end) ) as total_semester2,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_gov_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end) ) as total_semester3,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_gov_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end) ) as total_semester4,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_gov_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end) ) as total_semester5\n" +
                            "from ref_role z \n" +
                            "\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_11, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_12, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_13, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_14, (select id_role from gov_activity where id = (select id_activity from gov_indicator where id = :id_indicator ) ) as id_role FROM entry_gov_indicator a where a.year_entry = '"+(tahun+0)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c1 on z.id_role = c1.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_21, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_22, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_23, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_24, (select id_role from gov_activity where id = (select id_activity from gov_indicator where id = :id_indicator ) ) as id_role FROM entry_gov_indicator a where a.year_entry = '"+(tahun+1)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c2 on z.id_role = c2.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_31, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_32, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_33, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_34, (select id_role from gov_activity where id = (select id_activity from gov_indicator where id = :id_indicator ) ) as id_role FROM entry_gov_indicator a where a.year_entry = '"+(tahun+2)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c3 on z.id_role = c3.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_41, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_42, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_43, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_44, (select id_role from gov_activity where id = (select id_activity from gov_indicator where id = :id_indicator ) ) as id_role FROM entry_gov_indicator a where a.year_entry = '"+(tahun+3)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c4 on z.id_role = c4.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_51, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_52, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_53, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_54, (select id_role from gov_activity where id = (select id_activity from gov_indicator where id = :id_indicator ) ) as id_role FROM entry_gov_indicator a where a.year_entry = '"+(tahun+4)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c5 on z.id_role = c5.id_role\n" +
                            "\n" +
                            "where z.id_prov = :id_prov \n" +
                            "and z.id_role = :role ";
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", idmonper);
                query.setParameter("id_indicator", id_indicator);
                query.setParameter("id_prov", id_prov);
    //            query.setParameter("tahun", tahun);
                query.setParameter("role", role);
            }
        }
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    //nsa indicator
    @GetMapping("admin/get_data_show_non_gov")
    public @ResponseBody Map<String, Object> get_data_show_non_gov(@RequestParam("id_monper") int idmonper, @RequestParam("id_indicator") String id_indicator, @RequestParam("id_prov") String id_prov, @RequestParam("tahun") int tahun, @RequestParam("role") String role, @RequestParam("kode_bud") String kode_bud, @RequestParam("idacty") String id_acty) {
        Query query;
        if(kode_bud.equals("1")){
            if(role.equals("11111")){
                String sql = "select z.id_role, z.nm_role, \n" +
                            " '' as nama_unit,\n" +
                            " (select budget_allocation from nsa_activity where id = :id_acty) as target_1, \n" +
                            " (select budget_allocation from nsa_activity where id = :id_acty) as target_2, \n" +
                            " (select budget_allocation from nsa_activity where id = :id_acty) as target_3, \n" +
                            " (select budget_allocation from nsa_activity where id = :id_acty) as target_4, \n" +
                            " (select budget_allocation from nsa_activity where id = :id_acty) as target_5,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end) real_11, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c1.realisasi_12 end) real_12, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and period = '3') = 0 ) then '' else c1.realisasi_13 end) real_13, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and period = '4') = 0 ) then '' else c1.realisasi_14 end) real_14,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end) real_21, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c2.realisasi_22 end) real_22, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and period = '3') = 0 ) then '' else c2.realisasi_23 end) real_23, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and period = '4') = 0 ) then '' else c2.realisasi_24 end) real_24,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end) real_31, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c3.realisasi_32 end) real_32, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and period = '3') = 0 ) then '' else c3.realisasi_33 end) real_33, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and period = '4') = 0 ) then '' else c3.realisasi_34 end) real_34,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end) real_41, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c4.realisasi_42 end) real_42, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and period = '3') = 0 ) then '' else c4.realisasi_43 end) real_43, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and period = '4') = 0 ) then '' else c4.realisasi_44 end) real_44,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end) real_51, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c5.realisasi_52 end) real_52, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and period = '3') = 0 ) then '' else c5.realisasi_53 end) real_53, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and period = '4') = 0 ) then '' else c5.realisasi_54 end) real_54,\n" +
                            " '' as ratb,\n" +
                            " '' as sumber,\n" +
                            "'JICA SDG' as pelaku\n" +
                            "from ref_role z \n" +
                            "\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_11, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_12, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_13, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_14, (select id_role from nsa_activity where id = :id_acty ) as id_role FROM entry_nsa_budget a where a.year_entry = '"+(tahun+0)+"' and a.id_monper = :id_monper and a.id_nsa_activity = :id_acty ) as c1 on z.id_role = c1.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_21, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_22, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_23, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_24, (select id_role from nsa_activity where id = :id_acty ) as id_role FROM entry_nsa_budget a where a.year_entry = '"+(tahun+1)+"' and a.id_monper = :id_monper and a.id_nsa_activity = :id_acty ) as c2 on z.id_role = c2.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_31, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_32, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_33, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_34, (select id_role from nsa_activity where id = :id_acty ) as id_role FROM entry_nsa_budget a where a.year_entry = '"+(tahun+2)+"' and a.id_monper = :id_monper and a.id_nsa_activity = :id_acty ) as c3 on z.id_role = c3.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_41, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_42, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_43, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_44, (select id_role from nsa_activity where id = :id_acty ) as id_role FROM entry_nsa_budget a where a.year_entry = '"+(tahun+3)+"' and a.id_monper = :id_monper and a.id_nsa_activity = :id_acty ) as c4 on z.id_role = c4.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_51, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_52, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_53, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_54, (select id_role from nsa_activity where id = :id_acty ) as id_role FROM entry_nsa_budget a where a.year_entry = '"+(tahun+4)+"' and a.id_monper = :id_monper and a.id_nsa_activity = :id_acty ) as c5 on z.id_role = c5.id_role\n" +
                            "\n" +
                            "where z.id_prov = :id_prov ";
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", idmonper);
                query.setParameter("id_acty", id_acty);
                query.setParameter("id_prov", id_prov);
    //            query.setParameter("tahun", tahun);
    //            query.setParameter("role", role);
            }else{
                String sql  = "select z.id_role, z.nm_role, \n" +
                            " '' as nama_unit,\n" +
                            " (select budget_allocation from nsa_activity where id = :id_acty) as target_1, \n" +
                            " (select budget_allocation from nsa_activity where id = :id_acty) as target_2, \n" +
                            " (select budget_allocation from nsa_activity where id = :id_acty) as target_3, \n" +
                            " (select budget_allocation from nsa_activity where id = :id_acty) as target_4, \n" +
                            " (select budget_allocation from nsa_activity where id = :id_acty) as target_5,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end) real_11, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c1.realisasi_12 end) real_12, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and period = '3') = 0 ) then '' else c1.realisasi_13 end) real_13, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and period = '4') = 0 ) then '' else c1.realisasi_14 end) real_14,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end) real_21, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c2.realisasi_22 end) real_22, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and period = '3') = 0 ) then '' else c2.realisasi_23 end) real_23, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and period = '4') = 0 ) then '' else c2.realisasi_24 end) real_24,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end) real_31, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c3.realisasi_32 end) real_32, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and period = '3') = 0 ) then '' else c3.realisasi_33 end) real_33, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and period = '4') = 0 ) then '' else c3.realisasi_34 end) real_34,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end) real_41, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c4.realisasi_42 end) real_42, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and period = '3') = 0 ) then '' else c4.realisasi_43 end) real_43, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and period = '4') = 0 ) then '' else c4.realisasi_44 end) real_44,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end) real_51, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c5.realisasi_52 end) real_52, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and period = '3') = 0 ) then '' else c5.realisasi_53 end) real_53, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and period = '4') = 0 ) then '' else c5.realisasi_54 end) real_54,\n" +
                            " '' as ratb,\n" +
                            " '' as sumber,\n" +
                            "'JICA SDG' as pelaku , \n" +
                        
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c1.realisasi_12 end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and period = '3') = 0 ) then '' else c1.realisasi_13 end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and period = '4') = 0 ) then '' else c1.realisasi_14 end) ) as total_quarter1,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c2.realisasi_22 end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and period = '3') = 0 ) then '' else c2.realisasi_23 end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and period = '4') = 0 ) then '' else c2.realisasi_24 end) ) as total_quarter2,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c3.realisasi_32 end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and period = '3') = 0 ) then '' else c3.realisasi_33 end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and period = '4') = 0 ) then '' else c3.realisasi_34 end) ) as total_quarter3,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c4.realisasi_42 end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and period = '3') = 0 ) then '' else c4.realisasi_43 end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and period = '4') = 0 ) then '' else c4.realisasi_44 end) ) as total_quarter4,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c5.realisasi_52 end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and period = '3') = 0 ) then '' else c5.realisasi_53 end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and period = '4') = 0 ) then '' else c5.realisasi_54 end) ) as total_quarter5,\n" +
                        
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c1.realisasi_12 end) ) as total_smt1,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c2.realisasi_22 end) ) as total_smt2,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c3.realisasi_32 end) ) as total_smt3,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c4.realisasi_42 end) ) as total_smt4,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_budget' and period = '2') = 0 ) then '' else c5.realisasi_52 end) ) as total_smt5 \n" +
                            "from ref_role z \n" +
                            "\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_11, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_12, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_13, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_14, (select id_role from nsa_activity where id = :id_acty ) as id_role FROM entry_nsa_budget a where a.year_entry = '"+(tahun+0)+"' and a.id_monper = :id_monper and a.id_nsa_activity = :id_acty ) as c1 on z.id_role = c1.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_21, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_22, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_23, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_24, (select id_role from nsa_activity where id = :id_acty ) as id_role FROM entry_nsa_budget a where a.year_entry = '"+(tahun+1)+"' and a.id_monper = :id_monper and a.id_nsa_activity = :id_acty ) as c2 on z.id_role = c2.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_31, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_32, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_33, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_34, (select id_role from nsa_activity where id = :id_acty ) as id_role FROM entry_nsa_budget a where a.year_entry = '"+(tahun+2)+"' and a.id_monper = :id_monper and a.id_nsa_activity = :id_acty ) as c3 on z.id_role = c3.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_41, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_42, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_43, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_44, (select id_role from nsa_activity where id = :id_acty ) as id_role FROM entry_nsa_budget a where a.year_entry = '"+(tahun+3)+"' and a.id_monper = :id_monper and a.id_nsa_activity = :id_acty ) as c4 on z.id_role = c4.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_51, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_52, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_53, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_54, (select id_role from nsa_activity where id = :id_acty ) as id_role FROM entry_nsa_budget a where a.year_entry = '"+(tahun+4)+"' and a.id_monper = :id_monper and a.id_nsa_activity = :id_acty ) as c5 on z.id_role = c5.id_role\n" +
                            "\n" +
                            "where z.id_prov = :id_prov \n" +
                            "and z.id_role = :role ";
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", idmonper);
                query.setParameter("id_acty", id_acty);
                query.setParameter("id_prov", id_prov);
    //            query.setParameter("tahun", tahun);
                query.setParameter("role", role);
            }
        }else{
            if(role.equals("11111")){
                String sql = "select z.id_role, z.nm_role, \n" +
                            "(SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM nsa_indicator WHERE id = :id_indicator)) as nama_unit,\n" +
                            "(select value from nsa_target where id_nsa_indicator = :id_indicator and year = '"+(tahun+0)+"') as target_1, \n" +
                            "(select value from nsa_target where id_nsa_indicator = :id_indicator and year = '"+(tahun+1)+"') as target_2, \n" +
                            "(select value from nsa_target where id_nsa_indicator = :id_indicator and year = '"+(tahun+2)+"') as target_3, \n" +
                            "(select value from nsa_target where id_nsa_indicator = :id_indicator and year = '"+(tahun+3)+"') as target_4, \n" +
                            "(select value from nsa_target where id_nsa_indicator = :id_indicator and year = '"+(tahun+4)+"') as target_5,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end) real_11, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else c1.realisasi_12 end) real_12, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and period = '3') = 0 ) then '' else c1.realisasi_13 end) real_13, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and period = '4') = 0 ) then '' else c1.realisasi_14 end) real_14,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end) real_21, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else c2.realisasi_22 end) real_22, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and period = '3') = 0 ) then '' else c2.realisasi_23 end) real_23, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and period = '4') = 0 ) then '' else c2.realisasi_24 end) real_24,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end) real_31, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else c3.realisasi_32 end) real_32, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and period = '3') = 0 ) then '' else c3.realisasi_33 end) real_33, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and period = '4') = 0 ) then '' else c3.realisasi_34 end) real_34,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end) real_41, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else c4.realisasi_42 end) real_42, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and period = '3') = 0 ) then '' else c4.realisasi_43 end) real_43, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and period = '4') = 0 ) then '' else c4.realisasi_44 end) real_44,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end) real_51, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else c5.realisasi_52 end) real_52, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and period = '3') = 0 ) then '' else c5.realisasi_53 end) real_53, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and period = '4') = 0 ) then '' else c5.realisasi_54 end) real_54,\n" +
                            "(select baseline from nsa_funding where id_nsa_indicator = :id_indicator and id_monper = :id_monper) as ratb,\n" +
                            "(select funding_source from nsa_funding where id_nsa_indicator = :id_indicator and id_monper = :id_monper) as sumber,\n" +
                            "'JICA SDG' as pelaku\n" +
                            "from ref_role z \n" +
                            "\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_11, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_12, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_13, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_14, (select id_role from nsa_activity where id = (select id_activity from nsa_indicator where id = :id_indicator ) ) as id_role FROM entry_nsa_indicator a where a.year_entry = '"+(tahun+0)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c1 on z.id_role = c1.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_21, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_22, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_23, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_24, (select id_role from nsa_activity where id = (select id_activity from nsa_indicator where id = :id_indicator ) ) as id_role FROM entry_nsa_indicator a where a.year_entry = '"+(tahun+1)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c2 on z.id_role = c2.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_31, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_32, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_33, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_34, (select id_role from nsa_activity where id = (select id_activity from nsa_indicator where id = :id_indicator ) ) as id_role FROM entry_nsa_indicator a where a.year_entry = '"+(tahun+2)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c3 on z.id_role = c3.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_41, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_42, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_43, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_44, (select id_role from nsa_activity where id = (select id_activity from nsa_indicator where id = :id_indicator ) ) as id_role FROM entry_nsa_indicator a where a.year_entry = '"+(tahun+3)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c4 on z.id_role = c4.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_51, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_52, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_53, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_54, (select id_role from nsa_activity where id = (select id_activity from nsa_indicator where id = :id_indicator ) ) as id_role FROM entry_nsa_indicator a where a.year_entry = '"+(tahun+4)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c5 on z.id_role = c5.id_role\n" +
                            "\n" +
                            "where z.id_prov = :id_prov ";
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", idmonper);
                query.setParameter("id_indicator", id_indicator);
                query.setParameter("id_prov", id_prov);
    //            query.setParameter("tahun", tahun);
    //            query.setParameter("role", role);
            }else{
                String sql  = "select z.id_role, z.nm_role, \n" +
                            "(SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM nsa_indicator WHERE id = :id_indicator)) as nama_unit,\n" +
                            "(select value from nsa_target where id_nsa_indicator = :id_indicator and year = '"+(tahun+0)+"') as target_1, \n" +
                            "(select value from nsa_target where id_nsa_indicator = :id_indicator and year = '"+(tahun+1)+"') as target_2, \n" +
                            "(select value from nsa_target where id_nsa_indicator = :id_indicator and year = '"+(tahun+2)+"') as target_3, \n" +
                            "(select value from nsa_target where id_nsa_indicator = :id_indicator and year = '"+(tahun+3)+"') as target_4, \n" +
                            "(select value from nsa_target where id_nsa_indicator = :id_indicator and year = '"+(tahun+4)+"') as target_5,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end) real_11, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end) real_12, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c1.realisasi_13 end) end) real_13, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c1.realisasi_14 end) end) real_14,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end) real_21, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end) real_22, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c2.realisasi_23 end) end) real_23, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c2.realisasi_24 end) end) real_24,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end) real_31, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end) real_32, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c3.realisasi_33 end) end) real_33, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c3.realisasi_34 end) end) real_34,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end) real_41, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end) real_42, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c4.realisasi_43 end) end) real_43, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c4.realisasi_44 end) end) real_44,\n" +
                            "(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end) real_51, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end) real_52, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c5.realisasi_53 end) end) real_53, (case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c5.realisasi_54 end) end) real_54,\n" +
                            "(select baseline from nsa_funding where id_nsa_indicator = :id_indicator and id_monper = :id_monper) as ratb,\n" +
                            "(select funding_source from nsa_funding where id_nsa_indicator = :id_indicator and id_monper = :id_monper) as sumber,\n" +
                            "'JICA SDG' as pelaku ,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c1.realisasi_13 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c1.realisasi_14 end) end) ) as total_quarter1,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c2.realisasi_23 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c2.realisasi_24 end) end) ) as total_quarter2,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c3.realisasi_33 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c3.realisasi_34 end) end) ) as total_quarter3,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c4.realisasi_43 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c4.realisasi_44 end) end) ) as total_quarter4,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and period = '3') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and periode = '3' and approval <> '3') = 0 ) then '' else c5.realisasi_53 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and period = '4') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and periode = '4' and approval <> '3') = 0 ) then '' else c5.realisasi_54 end) end) ) as total_quarter5,\n" +
                        
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c1.realisasi_11 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+0)+"' and type = 'entry_nsa_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c1.realisasi_12 end) end) ) as total_smt1,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c2.realisasi_21 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+1)+"' and type = 'entry_nsa_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c2.realisasi_22 end) end) ) as total_smt2,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c3.realisasi_31 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+2)+"' and type = 'entry_nsa_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c3.realisasi_32 end) end) ) as total_smt3,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c4.realisasi_41 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+3)+"' and type = 'entry_nsa_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c4.realisasi_42 end) end) ) as total_smt4,\n" +
                            "((case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and period = '1') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and periode = '1' and approval <> '3') = 0 ) then '' else c5.realisasi_51 end) end)+(case when ((select count(*) from entry_show_report where id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and period = '2') = 0 ) then '' else (case when ((select count(*) from entry_approval where id_role = :role and id_monper = :id_monper and year = '"+(tahun+4)+"' and type = 'entry_nsa_indicator' and periode = '2' and approval <> '3') = 0 ) then '' else c5.realisasi_52 end) end) ) as total_smt5 \n" +
                            "from ref_role z \n" +
                            "\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_11, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_12, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_13, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_14, (select id_role from nsa_activity where id = (select id_activity from nsa_indicator where id = :id_indicator ) ) as id_role FROM entry_nsa_indicator a where a.year_entry = '"+(tahun+0)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c1 on z.id_role = c1.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_21, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_22, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_23, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_24, (select id_role from nsa_activity where id = (select id_activity from nsa_indicator where id = :id_indicator ) ) as id_role FROM entry_nsa_indicator a where a.year_entry = '"+(tahun+1)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c2 on z.id_role = c2.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_31, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_32, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_33, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_34, (select id_role from nsa_activity where id = (select id_activity from nsa_indicator where id = :id_indicator ) ) as id_role FROM entry_nsa_indicator a where a.year_entry = '"+(tahun+2)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c3 on z.id_role = c3.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_41, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_42, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_43, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_44, (select id_role from nsa_activity where id = (select id_activity from nsa_indicator where id = :id_indicator ) ) as id_role FROM entry_nsa_indicator a where a.year_entry = '"+(tahun+3)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c4 on z.id_role = c4.id_role\n" +
                            "left join (SELECT COALESCE(NULLIF(a.new_value1,''),a.achievement1) as realisasi_51, COALESCE(NULLIF(a.new_value2,''),a.achievement2) as realisasi_52, COALESCE(NULLIF(a.new_value3,''),a.achievement3) as realisasi_53, COALESCE(NULLIF(a.new_value4,''),a.achievement4) as realisasi_54, (select id_role from nsa_activity where id = (select id_activity from nsa_indicator where id = :id_indicator ) ) as id_role FROM entry_nsa_indicator a where a.year_entry = '"+(tahun+4)+"' and a.id_monper = :id_monper and a.id_assign = :id_indicator ) as c5 on z.id_role = c5.id_role\n" +
                            "\n" +
                            "where z.id_prov = :id_prov \n" +
                            "and z.id_role = :role ";
                query = manager.createNativeQuery(sql);
                query.setParameter("id_monper", idmonper);
                query.setParameter("id_indicator", id_indicator);
                query.setParameter("id_prov", id_prov);
    //            query.setParameter("tahun", tahun);
                query.setParameter("role", role);
            }
        }
        
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/cekgmonper_bud")
    public @ResponseBody List<Object> cekgmonper_bud(@RequestParam("id_monper") int idmonper, @RequestParam("id_prov") int id_prov) {
        String sql = "SELECT gov_prog_bud FROM ran_rad WHERE id_monper = :id_monper union all SELECT start_year FROM ran_rad WHERE id_monper = :id_monper AND id_prov = :id_prov";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_prov", id_prov);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/cekgmonper")
    public @ResponseBody List<Object> cekgmonper(@RequestParam("id_monper") int idmonper, @RequestParam("id_prov") int id_prov) {
        String sql = "SELECT gov_prog FROM ran_rad WHERE id_monper = :id_monper union all SELECT start_year FROM ran_rad WHERE id_monper = :id_monper AND id_prov = :id_prov";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_prov", id_prov);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/ceknongmonper_bud")
    public @ResponseBody List<Object> ceknongmonper_bud(@RequestParam("id_monper") int idmonper, @RequestParam("id_prov") int id_prov) {
        String sql = "SELECT nsa_prog_bud FROM ran_rad WHERE id_monper = :id_monper union all SELECT start_year FROM ran_rad WHERE id_monper = :id_monper AND id_prov = :id_prov";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_prov", id_prov);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/ceknongmonper")
    public @ResponseBody List<Object> ceknongmonper(@RequestParam("id_monper") int idmonper, @RequestParam("id_prov") int id_prov) {
        String sql = "SELECT nsa_prog FROM ran_rad WHERE id_monper = :id_monper union all SELECT start_year FROM ran_rad WHERE id_monper = :id_monper AND id_prov = :id_prov";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        query.setParameter("id_prov", id_prov);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/ceknmonper")
    public @ResponseBody List<Object> ceknmonper(@RequestParam("id_monper") int idmonper) {
        String sql = "SELECT gov_prog FROM ran_rad WHERE id_monper = :id_monper";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/cekgovmonper")
    public @ResponseBody List<Object> cekgovmonper(@RequestParam("id_goals") int idsdg, @RequestParam("id_target") int idtarget,
    		@RequestParam("id_indicator") int idsdgindi, @RequestParam("id_gov_indicator") int id) {
        String sql = "SELECT gov_prog FROM ran_rad WHERE id_monper IN (SELECT id_monper FROM gov_map "
        		+ "WHERE id_goals = :id_goals AND id_target = :id_target AND "
        		+ "id_indicator = :id_indicator AND id_gov_indicator = :id_gov_indicator)";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", idsdg);
        query.setParameter("id_target", idtarget);
        query.setParameter("id_indicator", idsdgindi);
        query.setParameter("id_gov_indicator", id);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/ceknsamonper")
    public @ResponseBody List<Object> ceknsamonper(@RequestParam("id_sdg_goals") int idsdg, @RequestParam("id_sdg_target") int idtarget,
    		@RequestParam("id_sdg_indicator") int idsdgindi, @RequestParam("id_nsa_indicator") int id) {
    	String sql = "SELECT nsa_prog FROM ran_rad WHERE id_monper = (SELECT id_monper FROM nsa_map "
        		+ "WHERE id_goals = :id_goals AND id_target = :id_target AND "
        		+ "id_indicator = :id_indicator AND id_nsa_indicator = :id_nsa_indicator)";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", idsdg);
        query.setParameter("id_target", idtarget);
        query.setParameter("id_indicator", idsdgindi);
        query.setParameter("id_nsa_indicator", id);
        List list = query.getResultList();
        return list;
    }

    @GetMapping("admin/getperiodeheader")
    public @ResponseBody List<Object> getperiodeheader(@RequestParam("id_gov_indicator") int id) {
        String sql = "SELECT a.id_monper, b.start_year, b.end_year FROM gov_map a LEFT JOIN " +
                "ran_rad b ON b.id_monper = a.id_monper WHERE a.id_gov_indicator = :id_gov_indicator";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_gov_indicator", id);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getnsaperiodeheader")
    public @ResponseBody List<Object> getnsaperiodeheader(@RequestParam("id_nsa_indicator") int id) {
        String sql = "SELECT a.id_monper, b.start_year, b.end_year FROM nsa_map a LEFT JOIN " +
                "ran_rad b ON b.id_monper = a.id_monper WHERE a.id_nsa_indicator = :id_nsa_indicator";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_nsa_indicator", id);
        List list = query.getResultList();
        return list;
    }
    
  //****************************** Gov Grap *******************************
    
    @GetMapping("admin/reportgovgrap")
    public @ResponseBody List<Object> reportgovgrap(@RequestParam("id_sdg_goals") int idsdg, @RequestParam("id_sdg_target") int idtarget,
    		@RequestParam("id_sdg_indicator") int idsdgindi, @RequestParam("id_gov_indicator") int id) {
        String sql = "SELECT a.*, b.nm_unit FROM gov_map a LEFT JOIN "
                +"ref_unit b ON b.id_unit = (SELECT unit FROM gov_indicator WHERE id = a.id_gov_indicator) "
                + "WHERE id_goals = :id_goals AND id_target = :id_target AND id_indicator = :id_indicator AND id_gov_indicator = :id_gov_indicator";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", idsdg);
        query.setParameter("id_target", idtarget);
        query.setParameter("id_indicator", idsdgindi);
        query.setParameter("id_gov_indicator", id);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/targetgov")
    public @ResponseBody List<Object> targetgov(@RequestParam("id_gov_indicator") int idindi, @RequestParam("year") String tahun) {
    	String sql = "SELECT value FROM gov_target WHERE id_gov_indicator = :id_gov_indicator AND year = :year ORDER BY YEAR ASC";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_gov_indicator", idindi);
    	query.setParameter("year", tahun);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realgovyear")
    public @ResponseBody List<Object> realgov(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun, 
    		@RequestParam("id_gov_indicator") int idgovindi) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1) FROM entry_gov_indicator WHERE id_assign = :id_assign AND year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
    	query.setParameter("id_assign", idgovindi);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realgovsemester")
    public @ResponseBody List<Object> realgovsemester(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun, 
    		@RequestParam("id_gov_indicator") int idgovindi) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1), COALESCE(NULLIF(new_value2,''),achievement2) "
    			+ "FROM entry_gov_indicator WHERE id_assign = :id_assign AND "
    			+ "year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
    	query.setParameter("id_assign", idgovindi);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realgovquarter")
    public @ResponseBody List<Object> realgovquarter(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun, 
    		@RequestParam("id_gov_indicator") int idgovindi) {
    	String sql = "SELECT SELECT COALESCE(NULLIF(new_value1,''),achievement1), COALESCE(NULLIF(new_value2,''),achievement2), "
    			+ "COALESCE(NULLIF(new_value3,''),achievement3), COALESCE(NULLIF(new_value4,''),achievement4) "
    			+ "FROM entry_gov_indicator WHERE "
    			+ "id_assign = :id_assign AND year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
    	query.setParameter("id_assign", idgovindi);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getgovfundsource")
    public @ResponseBody List<Object> getgovfundsource(@RequestParam("id_monper") int idmonper, @RequestParam("id_gov_indicator") int idindi){
    	String sql = "SELECT * FROM gov_funding WHERE id_gov_indicator = :id_gov_indicator AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_gov_indicator", idindi);
    	query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
    //****************************** NSA Grap *******************************
    
    @GetMapping("admin/reportnsagrap")
    public @ResponseBody List<Object> reportnsagrap(@RequestParam("id_sdg_goals") int idsdg, @RequestParam("id_sdg_target") int idtarget,
    		@RequestParam("id_sdg_indicator") int idsdgindi, @RequestParam("id_nsa_indicator") int id) {
        String sql = "SELECT a.*, b.nm_unit FROM nsa_map a LEFT JOIN "
                +"ref_unit b ON b.id_unit = (SELECT unit FROM nsa_indicator WHERE id = a.id_nsa_indicator) "
                + "WHERE id_goals = :id_goals AND id_target = :id_target AND id_indicator = :id_indicator AND id_nsa_indicator = :id_nsa_indicator";
        Query query = manager.createNativeQuery(sql);
        query.setParameter("id_goals", idsdg);
        query.setParameter("id_target", idtarget);
        query.setParameter("id_indicator", idsdgindi);
        query.setParameter("id_nsa_indicator", id);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/targetnsa")
    public @ResponseBody List<Object> targetnsa(@RequestParam("id_nsa_indicator") int idindi, @RequestParam("year") String tahun) {
    	String sql = "SELECT value FROM nsa_target WHERE id_nsa_indicator = :id_nsa_indicator AND year = :year ORDER BY YEAR ASC";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_nsa_indicator", idindi);
    	query.setParameter("year", tahun);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realnsayear")
    public @ResponseBody List<Object> realnsayear(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun, 
    		@RequestParam("id_nsa_indicator") int idindikator) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1) FROM entry_nsa_indicator "
    			+ "WHERE id_assign = :id_assign AND year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
    	query.setParameter("id_assign", idindikator);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realnsasemester")
    public @ResponseBody List<Object> realnsasemester(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun, 
    		@RequestParam("id_nsa_indicator") int idindikator) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1), COALESCE(NULLIF(new_value2,''),achievement2) "
    			+ "FROM entry_nsa_indicator WHERE id_assign = :id_assign AND year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
    	query.setParameter("id_assign", idindikator);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/realnsaquarter")
    public @ResponseBody List<Object> realnsaquarter(@RequestParam("id_monper") int idmonper, @RequestParam("year") String tahun, 
    		@RequestParam("id_nsa_indicator") int idindikator) {
    	String sql = "SELECT COALESCE(NULLIF(new_value1,''),achievement1), COALESCE(NULLIF(new_value2,''),achievement2), "
    			+ "COALESCE(NULLIF(new_value3,''),achievement3), COALESCE(NULLIF(new_value4,''),achievement4) "
    			+ "FROM entry_nsa_indicator WHERE id_assign = :id_assign AND year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", tahun);
    	query.setParameter("id_assign", idindikator);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getnsafundsource")
    public @ResponseBody List<Object> getnsafundsource(@RequestParam("id_monper") int idmonper, @RequestParam("id_nsa_indicator") int idindi){
    	String sql = "SELECT * FROM nsa_funding WHERE id_nsa_indicator = :id_nsa_indicator AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_nsa_indicator", idindi);
    	query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
  //****************************** Isi Table ****************************** 
    @GetMapping("admin/getentrysdg")
    public @ResponseBody List<Object> getentrysdg(@RequestParam("id_sdg_indicator") int idsdgindikator, @RequestParam("id_role") int idrole, @RequestParam("year") int year) {
    	String sql = "SELECT value FROM sdg_indicator_target WHERE id_sdg_indicator = :id_sdg_indicator AND id_role = :id_role "
    			+ "AND year = :year";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_sdg_indicator", idsdgindikator);
    	query.setParameter("id_role", idrole);
    	query.setParameter("year", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getunit")
    public @ResponseBody List<Object> getunit(@RequestParam("id_sdg_indicator") int idsdgindikator) {
    	String sql = "SELECT nm_unit FROM ref_unit WHERE id_unit = (SELECT unit FROM sdg_indicator WHERE id = :id_sdg_indicator)";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_sdg_indicator", idsdgindikator);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getsdgfunding")
    public @ResponseBody List<Object> getsdgfunding(@RequestParam("id_sdg_indicator") int idsdgindikator, @RequestParam("id_monper") int idmonper) {
    	String sql = "SELECT funding_source FROM sdg_funding WHERE id_sdg_indicator = :id_sdg_indicator AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_sdg_indicator", idsdgindikator);
    	query.setParameter("id_monper", idmonper);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getshowrep")
    public @ResponseBody List<Object> getrealisasi(@RequestParam("id_monper") int idmonper, @RequestParam("year") int year) {
    	String sql = "SELECT EXISTS(SELECT * FROM entry_show_report WHERE id_monper = :id_monper AND year = :year LIMIT 1)";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", year);
        List list = query.getResultList();
        return list;
    }
    
    @GetMapping("admin/getrealyear")
    public @ResponseBody List<Object> getreal(@RequestParam("id_monper") int idmonper, @RequestParam("year") int year) {
    	String sql = "SELECT id_disaggre, id_disaggre_detail, COALESCE(NULLIF(new_value1,''),achievement1) FROM entry_sdg_detail "
    			+ "WHERE year_entry = :year AND id_monper = :id_monper";
    	Query query = manager.createNativeQuery(sql);
    	query.setParameter("id_monper", idmonper);
    	query.setParameter("year", year);
        List list = query.getResultList();
        return list;
    }
    
   
   //*********************************************************************************************** 
    @GetMapping("admin/report-problem-identification")
    public String report_problem_identify(Model model,  HttpSession session) {    	
        model.addAttribute("title", "Define RAN/RAD/SDGs Indicator");        
        model.addAttribute("refcategory",modelCrud.getRefCategory());
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        return "admin/report/problemidentify";
    }
    
    @GetMapping("admin/list-report-problem")
     public @ResponseBody Map<String, Object> ProblemList() {
        String sql = "SELECT a.id,a.id_cat,b.nm_cat, a.problem,a.follow_up FROM entry_problem_identify a "
                     + " LEFT JOIN ref_category b ON  a.id_cat = b.id_cat ";        
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<Problemlist> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(new Problemlist((Integer)row[0],(String)row[1],(String)row[2], (String)row[3],(String)row[4]));
        }
        hasil.put("content",result);
        return hasil;
    }
     
    @GetMapping("admin/list-report-problem/{id_cat}")
     public @ResponseBody Map<String, Object> ProblemList(@PathVariable("id_cat") String id_cat) {
        String sql = "SELECT a.id,a.id_cat,b.nm_cat, a.problem,a.follow_up FROM entry_problem_identify a "
                     + " LEFT JOIN ref_category b ON  a.id_cat = b.id_cat where a.id_cat = '"+id_cat+"'";        
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<Problemlist> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(new Problemlist((Integer)row[0],(String)row[1],(String)row[2], (String)row[3],(String)row[4]));
        }
        hasil.put("content",result);
        return hasil;
    }
     
    @GetMapping("admin/list-report-problem-goals/{id_goals}")
     public @ResponseBody Map<String, Object> ProblemListGoals(@PathVariable("id_goals") String id_goals) {
        String sql = "SELECT a.id,a.id_cat,b.nm_cat, a.problem,a.follow_up FROM entry_problem_identify a "
                     + " LEFT JOIN ref_category b ON  a.id_cat = b.id_cat where a.id_goals = '"+id_goals+"'";        
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<Problemlist> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(new Problemlist((Integer)row[0],(String)row[1],(String)row[2], (String)row[3],(String)row[4]));
        }
        hasil.put("content",result);
        return hasil;
    }
    @GetMapping("admin/list-report-problem-role/{id_role}")
     public @ResponseBody Map<String, Object> ProblemListRole(@PathVariable("id_role") String id_role) {
        String sql = "SELECT a.id,a.id_cat,b.nm_cat, a.problem,a.follow_up FROM entry_problem_identify a "
                     + " LEFT JOIN ref_category b ON  a.id_cat = b.id_cat where a.id_role = '"+id_role+"'";        
        Query list = em.createNativeQuery(sql);
        List<Object[]> rows = list.getResultList();
        List<Problemlist> result = new ArrayList<>(rows.size());
        Map<String, Object> hasil = new HashMap<>();
        for (Object[] row : rows) {
            result.add(new Problemlist((Integer)row[0],(String)row[1],(String)row[2], (String)row[3],(String)row[4]));
        }
        hasil.put("content",result);
        return hasil;
    }
     
    @GetMapping("admin/report-problem/get-category")
     public @ResponseBody Map<String, Object> ProblemGetCategory() {
        String sql = "SELECT * FROM ref_category WHERE id_cat IN(SELECT DISTINCT id_cat FROM entry_problem_identify)";        
        Query query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
    
    @GetMapping("admin/report-problem/get-goals")
     public @ResponseBody Map<String, Object> ProblemGetGoals() {
        String sql = "SELECT id,nm_goals FROM sdg_goals WHERE id IN(SELECT DISTINCT id_goals FROM entry_problem_identify)";        
        Query query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }
     
    @GetMapping("admin/report-problem/get-role")
     public @ResponseBody Map<String, Object> ProblemGetRole() {
        String sql = "SELECT * FROM ref_role WHERE id_role IN(SELECT DISTINCT id_role FROM entry_problem_identify)";        
        Query query = em.createNativeQuery(sql);
        List list   = query.getResultList();
        Map<String, Object> hasil = new HashMap<>();
        hasil.put("content",list);
        return hasil;
    }

     @GetMapping("admin/home-report/gri-ojk")
        public String listgriojk(Model model, HttpSession session) {
            Integer id_role = (Integer) session.getAttribute("id_role");
            Optional<Role> list = roleService.findOne(id_role);
            String id_prov = list.get().getId_prov();
            String privilege = list.get().getPrivilege();
            if(privilege.equals("SUPER")) {
                    model.addAttribute("listprov", provinsiService.findAllProvinsi());
            }else {
                    Optional<Provinsi> list1 = provinsiService.findOne(id_prov);
                    list1.ifPresent(foundUpdateObject1 -> model.addAttribute("listprov", foundUpdateObject1));
            }
            model.addAttribute("lang", session.getAttribute("bahasa"));
                    model.addAttribute("name", session.getAttribute("name"));
                    model.addAttribute("id_prov", id_prov);
                    model.addAttribute("privilege", privilege);
                    model.addAttribute("id_role", id_role);
                    
            
            String sql = "SELECT DISTINCT company_name FROM entry_gri_ojk where approval = 2 ";             
            Query list2 = em.createNativeQuery(sql);
            
            String sql2 = "SELECT DISTINCT year FROM entry_gri_ojk where approval = 2  ";             
            Query list3 = em.createNativeQuery(sql2);
            Map<String, Object> hasil = new HashMap<>();
            
            hasil.put("company",list2.getResultList());  
            hasil.put("tahun",list3.getResultList());  
            
            model.addAttribute("data", hasil);
            return "admin/report/gri_ojk";

        }
        
        @GetMapping("admin/get-report/gri-ojk/{year}/{company}")
        public @ResponseBody Map<String, Object> getReport(@PathVariable("year") Integer year,@PathVariable("company") String  company) {
            String sql = "SELECT a.kode,a.pjok,a.sdgs,a.sdgs_desc,a.gri,a.gri_desc,b.year,b.company_name,b.value,a.unit FROM excell a JOIN trx_excell b ON a.kode = b.kode \n" +
                          "where b.year = '"+year+"' and company_name ='"+company+"'";
            Query list = em.createNativeQuery(sql);
            Map<String, Object> hasil = new HashMap<>();
            hasil.put("content",list.getResultList());
            return hasil;
        }
        @GetMapping("admin/get-report/gri-ojk")
        public @ResponseBody Map<String, Object> getReport() {
            String sql = "SELECT a.kode,a.pjok,a.sdgs,a.sdgs_desc,a.gri,a.gri_desc,b.year,b.company_name,b.value,a.unit FROM excell a JOIN trx_excell b ON a.kode = b.kode ";
            Query list = em.createNativeQuery(sql);
            Map<String, Object> hasil = new HashMap<>();
            hasil.put("content",list.getResultList());
            return hasil;
        }
        
       @GetMapping("admin/get-last-year-company/gri-ojk/{company}")
        public @ResponseBody Map<String, Object> getLastYear(@PathVariable("company") String  company) {
            String sql = "SELECT DISTINCT YEAR FROM entry_gri_ojk WHERE company_name = '"+company+"' AND approval = '2'  ORDER BY YEAR ";
            Query list = em.createNativeQuery(sql);
            Map<String, Object> hasil = new HashMap<>();
            hasil.put("content",list.getResultList());
            return hasil;
        }
        
        
        @GetMapping("admin/get-all-company/gri-ojk/{year}")
        public @ResponseBody Map<String, Object> getAllCompany(@PathVariable("year") String  year) {
            String sql = "SELECT DISTINCT company_name FROM entry_gri_ojk WHERE YEAR  = '"+year+"' AND approval = '2'  ORDER BY company_name ";
            Query list = em.createNativeQuery(sql);
            Map<String, Object> hasil = new HashMap<>();
            hasil.put("content",list.getResultList());
            return hasil;
        }
        
         @GetMapping("admin/get-all-row-company/gri-ojk/{query}")
        public @ResponseBody Map<String, Object> getAllRow(@PathVariable("query") String  query) {
            String sql = query;
            Query list = em.createNativeQuery(sql);
            Map<String, Object> hasil = new HashMap<>();
            hasil.put("content",list.getResultList());
            return hasil;
        }
        
         @GetMapping("admin/exportgraph")
         public void exportgraph(HttpServletResponse response) {
//        	 response.setContentType("application/octet-stream");
//           response.setHeader("Content-Disposition", "attachment; filename=NSA_Profile-"+idrole+".xlsx");
//           ByteArrayInputStream stream = exprofil(idprov, idrole);
//           IOUtils.copy(stream, response.getOutputStream());
         }
         
         @GetMapping("admin/report-evaluation")
         public String evaluation(Model model, HttpSession session) {
             model.addAttribute("listprov", provinsiService.findAllProvinsi());
             model.addAttribute("listrole", roleService.findAll());
             model.addAttribute("listranrad", radService.findAll());
             model.addAttribute("listgoals", goalsService.findAll());

             model.addAttribute("title", "SDG Problem Identification & Follow Up");
             model.addAttribute("lang", session.getAttribute("bahasa"));
             model.addAttribute("name", session.getAttribute("name"));
             return "admin/report/evaluation";
         }
         
         @GetMapping("admin/data-report/problem-identify")
         public String govprogram(Model model, HttpSession session) {
        model.addAttribute("title", "SDG Indicators Monitoring");
//        model.addAttribute("listprov", provinsiService.findAllProvinsi());
//        model.addAttribute("lang", session.getAttribute("bahasa"));
//        model.addAttribute("name", session.getAttribute("name"));
        
        Integer id_role = (Integer) session.getAttribute("id_role");
        model.addAttribute("lang", session.getAttribute("bahasa"));
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("id_role", session.getAttribute("id_role"));
        model.addAttribute("listNsaProfile", nsaProfilrService.findRoleGov());
    	Optional<Role> list = roleService.findOne(id_role);
    	String id_prov      = list.get().getId_prov();
    	String privilege    = list.get().getPrivilege();
    	 model.addAttribute("listprov", provinsiService.findAllProvinsi());
        
         Query query3 = em.createNativeQuery("SELECT DISTINCT a.id,a.nm_goals AS nm,LPAD(a.id,3,'0') AS id_parent,'1' AS LEVEL ,a.id_goals AS id_text ,'#' AS id_parent2 FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                                                "	UNION \n" +
                                                "	SELECT DISTINCT  CONCAT(a.id,'.',b.id) AS id,b.nm_target AS nm,CONCAT(LPAD(a.id,3,'0'),'.',LPAD(b.id,3,'0')) AS id_parent,'2' AS LEVEL ,CONCAT(a.id_goals,'-',b.id_target) AS id_text ,a.id AS id_parent2 FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                                                "	UNION \n" +
                                                "	SELECT DISTINCT  CONCAT(a.id,'.',b.id,'.',c.id) AS id,c.nm_indicator AS nm,CONCAT(LPAD(a.id,3,'0') ,'.',LPAD(b.id,3,'0'),'.',LPAD(c.id,3,'0')) AS id_parent,'3' AS LEVEL ,CONCAT(a.id_goals,'-',b.id_target,'-',c.id_indicator) AS id_text ,CONCAT(a.id,'.',b.id) AS id_parent2  FROM sdg_goals a JOIN sdg_target b ON a.id = b.id_goals JOIN sdg_indicator c ON b.id = c.id_target\n" +
                                                "	ORDER BY id_parent");
        
        List list3 =  query3.getResultList();
        Map<String, Object> filtersdg = new HashMap<>();
        filtersdg.put("data",list3);
        model.addAttribute("filtersdg",filtersdg);
        model.addAttribute("id_prov", id_prov);
        model.addAttribute("privilege", privilege);        
        model.addAttribute("refcategory",modelCrud.getRefCategory());
        return "admin/report/problemgoals";
    }
         
         
    
    @GetMapping("admin/generate-report/problem-identify/{id_prov}/{id_role}/{id_monper}/{id_category}/{id_goals}/{id_target}/{id_indicator}/{group}/{year}")
    public @ResponseBody Map<String, Object> getOptionIndicatorList(@PathVariable("id_prov") String id_prov,@PathVariable("id_role") String id_role,@PathVariable("id_monper") String id_monper,@PathVariable("id_category") String id_category,@PathVariable("id_goals") String id_goals,@PathVariable("id_target") String id_target,@PathVariable("id_indicator") String id_indicator,@PathVariable("group") String group,@PathVariable("year") String year) {
        String whereidrole ="";
        String wheremonper = "";
        String whereidcategory ="";
        String whereidgoals ="";
        String whereidtarget ="";
        String whereidindicator ="";
        /*WHERE a.id_prov = '000' AND d.id_role = '5' AND a.id_monper = '1' AND c.id_cat = '01' AND a.id_goals = '1'  AND a.id_target = '2' AND a.id_indicator = '1'*/
        if(!id_monper.equals("*")&&!id_monper.equals("0")){
          wheremonper = " AND  a.id_monper = '"+id_monper+"' AND b.year = '"+year+"'";  
        }

        if(!id_role.equals("*")&&!id_role.equals("0")){
          whereidrole = " AND d.id_role = '"+id_role+"'";  
        }

        if(!id_category.equals("*")&&!id_category.equals("0")){
          whereidcategory = " AND c.id_cat =  '"+id_category+"'";  
        }        
        if(!id_goals.equals("*")&&!id_goals.equals("0")){
          whereidgoals = "  and a.id_goals in ('"+id_goals+"')";  
        }

        if(!id_target.equals("*")&&!id_target.equals("0")){
          whereidtarget = "and a.id_target in ('"+id_target+"')";  
        }
        
        if(!id_indicator.equals("*")&&!id_indicator.equals("0")){
          whereidindicator = "and a.id_indicator in ('"+id_indicator+"')";  
        }
        
        Optional<RanRad> monper = ranRadService.findOne(Integer.parseInt(id_monper));
     	String status = (monper.isPresent())?monper.get().getStatus():"";
     	
     	String sql;
     	
     	if(status.equals("completed")) {
     		sql  =   "       SELECT  DISTINCT c.id_cat,a.id_goals,d.id_role,f.nm_goals,c.nm_cat,d.nm_role,b.problem,b.follow_up,f.id_goals as kode_id, concat(f.id_goals,'.',j.id_target), j.nm_target, j.nm_target_eng, concat(f.id_goals,'.',j.id_target,'.',k.id_indicator), k.nm_indicator, k.nm_indicator_eng,f.nm_goals_eng  FROM entry_problem_identify_map a\n" +
                    "	LEFT JOIN entry_problem_identify b ON a.id_relation_entry_problem_identify = b.id_relation\n" +
                    "	LEFT JOIN ref_category c ON b.id_cat = c.id_cat \n" +
                    "	LEFT JOIN ref_role d ON b.id_role = d.id_role\n" +
                    "	LEFT JOIN ref_province e ON b.id_prov = e.id_prov \n" +
                    "	LEFT JOIN history_sdg_goals f ON  a.id_goals = f.id_old and f.id_monper = a.id_monper "+
                    "	LEFT JOIN history_sdg_target j ON  a.id_target = j.id_old and a.id_monper = j.id_monper  "+
                    "	LEFT JOIN history_sdg_indicator k ON  a.id_indicator = k.id_old and a.id_monper = k.id_monper  "
            +       "       JOIN entry_show_report g on a.id_monper = g.id_monper and g.year = b.year and g.type = 'entry_problem_identify' "
            		+ "		JOIN entry_approval h on b.id_role = h.id_role and b.id_monper = h.id_monper and b.year = h.year and h.type = 'entry_problem_identify' and h.approval != '3' "
            + " where a.id_prov = :id_prov  "+whereidrole+wheremonper+whereidcategory+whereidgoals+whereidtarget+whereidindicator;
    
     	}else {
     		sql  =   "       SELECT  DISTINCT c.id_cat,a.id_goals,d.id_role,f.nm_goals,c.nm_cat,d.nm_role,b.problem,b.follow_up,f.id_goals as kode_id, concat(f.id_goals,'.',j.id_target), j.nm_target, j.nm_target_eng, concat(f.id_goals,'.',j.id_target,'.',k.id_indicator), k.nm_indicator, k.nm_indicator_eng,f.nm_goals_eng  FROM entry_problem_identify_map a\n" +
                    "	LEFT JOIN entry_problem_identify b ON a.id_relation_entry_problem_identify = b.id_relation\n" +
                    "	LEFT JOIN ref_category c ON b.id_cat = c.id_cat \n" +
                    "	LEFT JOIN ref_role d ON b.id_role = d.id_role\n" +
                    "	LEFT JOIN ref_province e ON b.id_prov = e.id_prov \n" +
                    "	LEFT JOIN sdg_goals f ON  a.id_goals = f.id "+
                    "	LEFT JOIN sdg_target j ON  a.id_target = j.id "+
                    "	LEFT JOIN sdg_indicator k ON  a.id_indicator = k.id "
            +       "       JOIN entry_show_report g on a.id_monper = g.id_monper and g.year = b.year and g.type = 'entry_problem_identify' "
            		+ "		JOIN entry_approval h on b.id_role = h.id_role and b.id_monper = h.id_monper and b.year = h.year and h.type = 'entry_problem_identify' and h.approval != '3' "
            + " where a.id_prov = :id_prov  "+whereidrole+wheremonper+whereidcategory+whereidgoals+whereidtarget+whereidindicator;
    
     	}

        
        Query query = em.createNativeQuery(sql);
        query.setParameter("id_prov", id_prov);
        List list   = query.getResultList();
        
        String wheregroup = "";
        if(group.equals("1")){
            wheregroup = " t.id_cat,t.nm_cat ";
        }
        
        if(group.equals("2")){
            wheregroup = " t.id_goals,t.nm_goals,t.kode_id ";
        }
        
        if(group.equals("3")){
            wheregroup = " t.id_role,t.nm_role ";
        }
        String sql2;
        if(status.equals("completed")) {
        	sql2  =   "SELECT DISTINCT "+wheregroup+" FROM (\n" +
                    " SELECT  DISTINCT c.id_cat,a.id_goals,d.id_role,f.nm_goals,c.nm_cat,d.nm_role,b.problem,b.follow_up,f.id_goals as kode_id, j.id_target, j.nm_target, j.nm_target_eng, k.id_indicator, k.nm_indicator, k.nm_indicator_eng,f.nm_goals_eng  FROM entry_problem_identify_map a\n" +
                    "	LEFT JOIN entry_problem_identify b ON a.id_relation_entry_problem_identify = b.id_relation\n" +
                    "	LEFT JOIN ref_category c ON b.id_cat = c.id_cat \n" +
                    "	LEFT JOIN ref_role d ON b.id_role = d.id_role\n" +
                    "	LEFT JOIN ref_province e ON b.id_prov = e.id_prov \n" +
                    "	LEFT JOIN history_sdg_goals f ON  a.id_goals = f.id_old and a.id_monper = f.id_monper "+
                    "	LEFT JOIN history_sdg_target j ON  a.id_target = j.id_old and a.id_monper = j.id_monper  "+
                    "	LEFT JOIN history_sdg_indicator k ON  a.id_indicator = k.id_old and a.id_monper = k.id_monper  "
           +        "      JOIN entry_show_report g on a.id_monper = g.id_monper and g.year = b.year and g.type = 'entry_problem_identify' "
           		+ "		JOIN entry_approval h on b.id_role = h.id_role and b.id_monper = h.id_monper and b.year = h.year and h.type = 'entry_problem_identify' and h.approval != '3' "
           + "where a.id_prov = :id_prov "+whereidrole+wheremonper+whereidcategory+whereidgoals+whereidtarget+whereidindicator+" ) t	ORDER BY "+wheregroup+" ASC ";
   
        }else {
        	sql2  =   "SELECT DISTINCT "+wheregroup+" FROM (\n" +
                    " SELECT  DISTINCT c.id_cat,a.id_goals,d.id_role,f.nm_goals,c.nm_cat,d.nm_role,b.problem,b.follow_up,f.id_goals as kode_id, j.id_target, j.nm_target, j.nm_target_eng, k.id_indicator, k.nm_indicator, k.nm_indicator_eng,f.nm_goals_eng   FROM entry_problem_identify_map a\n" +
                    "	LEFT JOIN entry_problem_identify b ON a.id_relation_entry_problem_identify = b.id_relation\n" +
                    "	LEFT JOIN ref_category c ON b.id_cat = c.id_cat \n" +
                    "	LEFT JOIN ref_role d ON b.id_role = d.id_role\n" +
                    "	LEFT JOIN ref_province e ON b.id_prov = e.id_prov \n" +
                    "	LEFT JOIN sdg_goals f ON  a.id_goals = f.id "+
                    "	LEFT JOIN sdg_target j ON  a.id_target = j.id "+
                    "	LEFT JOIN sdg_indicator k ON  a.id_indicator = k.id "
           +        "      JOIN entry_show_report g on a.id_monper = g.id_monper and g.year = b.year and g.type = 'entry_problem_identify' "
           		+ "		JOIN entry_approval h on b.id_role = h.id_role and b.id_monper = h.id_monper and b.year = h.year and h.type = 'entry_problem_identify' and h.approval != '3' "
           + "where a.id_prov = :id_prov "+whereidrole+wheremonper+whereidcategory+whereidgoals+whereidtarget+whereidindicator+" ) t	ORDER BY "+wheregroup+" ASC ";
   
        }
        
        Query query2 = em.createNativeQuery(sql2);
              query2.setParameter("id_prov", id_prov);
        List list2   = query2.getResultList();
        
        
        
        Map<String, Object> hasil = new HashMap<>();        
        hasil.put("content",list);
        hasil.put("group",list2);
        return hasil;
    }
         
         
}
