package com.organist4j.model.ordo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class OrdoVO {
	static Logger logger = Logger.getLogger(OrdoVO.class);
	
	String name;
	Map<String,OrdoDateVO> ordoEntries = new TreeMap<String,OrdoDateVO>();
	
	Map<String,OrdoEntryVO> relative = new HashMap<String,OrdoEntryVO>();
	Map<String,OrdoEntryVO> fixed = new HashMap<String,OrdoEntryVO>();
	

	
	List<OrdoSeasonVO> seasons = new ArrayList<OrdoSeasonVO>();
	
	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	
	public OrdoVO() {
		create();
	}
	
	private void create() {
		
		OrdoSeasonVO season = new OrdoSeasonVO("Advent");
		addRelativeDate(1,Calendar.SUNDAY,new OrdoEntryVO(season,"Advent 1"));
		addRelativeDate(2,Calendar.SUNDAY,new OrdoEntryVO(season,"Advent 2"));
		addRelativeDate(3,Calendar.SUNDAY,new OrdoEntryVO(season,"Advent 3"));
		addRelativeDate(4,Calendar.SUNDAY,new OrdoEntryVO(season,"Advent 4"));
		season = new OrdoSeasonVO("Christmastide");
		addFixedDate(24,Calendar.DECEMBER,new OrdoEntryVO(season,"Vigil of Christmas"));
		addFixedDate(25,Calendar.DECEMBER,new OrdoEntryVO(season,"The Nativity of the Lord"));
		addFixedDate(26,Calendar.DECEMBER,new OrdoEntryVO(season,"St Stephen"));
		addFixedDate(27,Calendar.DECEMBER,new OrdoEntryVO(season,"St John"));
		addFixedDate(28,Calendar.DECEMBER,new OrdoEntryVO(season,"Holy Innocents"));
				
		
	}
	
	public void addRelativeDate(int week,int dayOfWeek,OrdoEntryVO entry) {
		relative.put(week + ":" + dayOfWeek, entry);		
	}
	public OrdoEntryVO getRelativeDate(int week,int dayOfWeek) {
		return relative.get(week + ":" + dayOfWeek);
	}

	public void addFixedDate(int day, int month, OrdoEntryVO entry) {
		fixed.put(day + ":" + month, entry);
		
	}
	public OrdoEntryVO getFixedDate(int day,int month) {
		return fixed.get(day + ":" + month);
	}
	
	
	public void generate(Date adventSunday) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(adventSunday);
		boolean moreToDo = true;
		int week = 1;
		int day = 1;
		while(moreToDo) {
			if (day > 7) {
				day = 1;
				week++;
			}
			
			//Check for any fixed
			OrdoEntryVO fixed = getFixedDate(cal.get(Calendar.DATE),cal.get(Calendar.MONTH));
			
			
			OrdoEntryVO rel = getRelativeDate(week,cal.get(Calendar.DAY_OF_WEEK));
			String dateKey = df.format(cal.getTime());
			OrdoDateVO d = ordoEntries.get(dateKey);
			if (d == null) {
				d = new OrdoDateVO();
				ordoEntries.put(dateKey, d);
			}
			if (fixed != null) {
				d.getEntries().add(fixed);
			} else {
				if (rel != null) {
					d.getEntries().add(rel);
				}
			}
			
			cal.add(Calendar.DATE, 1);
			day++;
			
			if (week > 5) moreToDo = false; //temp stop
		}
		
		
	}
	
	public void debug() {
		Iterator<Map.Entry<String, OrdoDateVO>> it = ordoEntries.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, OrdoDateVO> entry = it.next();
			logger.debug(entry.getKey() + ": " + entry.getValue().toString());
		}
				
		
	}
}
