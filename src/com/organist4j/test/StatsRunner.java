package com.organist4j.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.organist4j.dao.MusicDAO;
import com.organist4j.dao.ServiceDAO;
import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.ServiceLocator;

public class StatsRunner {
	static Logger logger = Logger.getLogger(StatsRunner.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new StatsRunner().run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void run() throws Exception {
		MusicItemVO mi;
		ServiceElementVO se;
		ServiceLocator.getInstance().init();
		ServiceVO service;
		Map<String,Set<String>> map = new HashMap<String,Set<String>>();
		Set<String> set = null;
		String name = "";
		
		
		
		MusicDAO md = ServiceLocator.getInstance().getMusicDAO();
		ServiceDAO sd = ServiceLocator.getInstance().getServiceDAO();
		
		int totalItems = 0;
		
		Iterator<ServiceVO> sit = sd.getServices(false).iterator();
		while (sit.hasNext()) {
			service = sit.next() ;
			
			if (service.getType() == null) continue;
			
			if (service.getType().indexOf("Mass") >= 0 ||
					service.getType().indexOf("AAM") >= 0 ||
					service.getType().indexOf("AAW") >= 0) {
				
			} else {
				continue;
			}
			
			Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse("20/06/2012");
			if (service.getDateTime() != null && service.getDateTime().before(startDate)) {
				continue;
			}

			List<ServiceElementVO> ses = service.getServiceElements();
			Iterator<ServiceElementVO> sei = ses.iterator();
			
			while (sei.hasNext()) {
				se = sei.next();
				if (se.getItem() != null 
						&& se.getServiceItem() != null
						&& !se.getServiceItem().getName().startsWith("Organ")
						&& !se.getServiceItem().getName().startsWith("WEDDING")) {
					//	logger.debug(se.getServiceItem().getName() + "," + se.getItem().getBookName() + "," + se.getItem().getNumber() + "," + se.getItem().getName());

					String keyName = se.getServiceItem().getName();

					if (keyName.indexOf("HYMN") >= 0) {
						keyName = "HYMN";
					}
					if (keyName.indexOf("Sprinkling") >= 0) {
						keyName = "HYMN";
					}
					if (keyName.indexOf("Marian") >= 0) {
						keyName = "HYMN";
					}
					if (keyName.indexOf("Proclamation") >= 0) {
						keyName = "HYMN";
					}
					if (keyName.indexOf("Wreath") >= 0) {
						keyName = "HYMN";
					}
					if (keyName.indexOf("Introit") >= 0) {
						keyName = "Anthem";
					}
					if (keyName.indexOf("Lady Chapel") >= 0) {
						keyName = "Anthem";
					}
					if (keyName.indexOf("Watch") >= 0) {
						keyName = "Anthem";
					}
					if (keyName.indexOf("Meditation") >= 0) {
						keyName = "Anthem";
					}
					if (keyName.indexOf("Stripping") >= 0) {
						keyName = "Anthem";
					}
					if (keyName.indexOf("Healing") >= 0) {
						keyName = "Anthem";
					}
					if (keyName.indexOf("Moment") >= 0) {
						keyName = "Anthem";
					}
					if (keyName.indexOf("Litany") >= 0) {
						keyName = "Anthem";
					}
					if (keyName.indexOf("Veneration") >= 0) {
						keyName = "Anthem";
					}
					if (keyName.indexOf("Ashing") >= 0) {
						keyName = "Anthem";
					}
					if (keyName.indexOf("Foot Washing") >= 0) {
						keyName = "Anthem";
					}
					if (keyName.indexOf("Magnificat") >= 0) {
						keyName = "Psalm";
					}
					if (keyName.indexOf("Mem Acc") >= 0) {
						keyName = "MASS SETTING";
					}
					if (keyName.indexOf("Gloria") >= 0) {
						keyName = "MASS SETTING";
					}
					if (keyName.indexOf("Kyries") >= 0) {
						keyName = "MASS SETTING";
					}
					if (keyName.indexOf("Sanctus") >= 0) {
						keyName = "MASS SETTING";
					}
					if (keyName.indexOf("Great Amen") >= 0) {
						keyName = "MASS SETTING";
					}
					if (keyName.indexOf("Agnus Dei") >= 0) {
						keyName = "MASS SETTING";
					}

					
//					if (se.getItem().getNumber().equals("495")) {
//						logger.debug(se.getService().getDateTime() + ":" + se.getItem().getDisplayName());
//					}

					set = map.get(keyName);
					if (set == null) {
						set = new HashSet<String>();
						map.put(keyName, set);
					}

					//set.add(se.getItem().getName());
					name = "";
//					if (se.getItem().getBookAcronym() != null) {
//						name += se.getItem().getBookAcronym() + " ";
//					}
					name += se.getItem().getName();
					set.add(name);
					
					
					if (keyName == "HYMN") {
						System.out.println(name);
					}

					totalItems++;
				}
			}

		}
		
		Iterator<Entry<String, Set<String>>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Set<String>> entry = (Map.Entry<String, Set<String>>)it.next();
			logger.debug( entry.getKey() + " : " + entry.getValue().size() );
			logger.debug(entry.getValue());
		}
		
		
		logger.info("Total items: " + totalItems);
		
		ServiceLocator.getInstance().dispose();
	}

}
