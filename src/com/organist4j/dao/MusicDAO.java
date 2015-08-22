package com.organist4j.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.db4o.ObjectContainer;
import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.model.service.ServiceItemVO;

public class MusicDAO extends DAO {
	
	private Map<String,String> bookNameToAcronym = new TreeMap<String,String>();

	public MusicDAO(ObjectContainer db) {
		super(db);
		init();
		
		
	}
	
	private void init() {
		//Load all bookNames
		Iterator<MusicItemVO> items = getMusicItems().iterator();
		MusicItemVO item = null;
		while (items.hasNext()) {
			item = items.next();
			bookNameToAcronym.put(item.getBookName(),item.getBookAcronym());
		}
	}



	public List<MusicItemVO> getMusicItems() {
		List <MusicItemVO> cpl = db.queryByExample(MusicItemVO.class);
		return cpl;
	}

	public List<MusicItemVO> getMusicItemsForBook(String bookName) {
		MusicItemVO item = new MusicItemVO();
		item.setBookName(bookName);
		List <MusicItemVO> cpl = db.queryByExample(item);
		return cpl;
	}

	public void deleteMusicItem(MusicItemVO item) {
		db.delete(item);
	}

//	public MusicItemVO getMusicItemByPrimaryKey(String key) {
//		MusicItemVO mbe = new MusicItemVO();
//		mbe.applyKey(key);
//		List <MusicItemVO> cpl = db.queryByExample(mbe);
//		if (cpl != null && cpl.size() > 0) {
//			return cpl.iterator().next();
//		} else {
//			return null;
//		}
//	}

	public void importMusicItems(String bookName,List<MusicItemVO> items) {
		Iterator<MusicItemVO> it = getMusicItemsForBook(bookName).iterator();
		while (it.hasNext()) {
			db.delete(it.next());
		}
		it = items.iterator();
		while (it.hasNext()) {
			db.store(it.next());
		}

	}

	public void addMusicItem(MusicItemVO item) {
		updateMusicItem(item,false);    	
	}


	public void updateMusicItem(MusicItemVO item) {
		updateMusicItem(item,false);		
	}

	public void updateMusicItem(MusicItemVO item,boolean calculateLastUsed) {
		//Check for existing item
		if (item == null) return;
		
		Date latestDateUsed = null;
		Date d = null;
		List<MusicItemVO> items = new ArrayList<MusicItemVO>();
		items.add(item);
		if (calculateLastUsed) {			
			
			
			if (item.getRelatedItems() != null) {
				items.addAll(item.getRelatedItems());
			}			
				
			Iterator<MusicItemVO> it = items.iterator();
			while (it.hasNext()) {
				d = getLastDateTimeUsed(it.next());
				if (latestDateUsed == null) {
					latestDateUsed = d;
				} else {
					if (d != null && d.after(latestDateUsed)) {
						latestDateUsed = d;
					}
				}
			}			
			
				
			
			item.setTuneLastDateTimeUsed(getTuneLastDateTimeUsed(item));
		}
		
		//Now apply
		Iterator<MusicItemVO> it = items.iterator();
		MusicItemVO m;
		while (it.hasNext()) {
			m = it.next();
			if (calculateLastUsed) {
				m.setLastDateTimeUsed(latestDateUsed);
			}
			db.store(item);
		}	
		
		
	}
	




//	private void updateRelatedItems(MusicItemVO item) {
//		if (true) {
//			return;
//		}
//		if (item.getRelatedItems() == null) {
//			return;
//		}
//		Date latestLastUsed = item.getLastDateTimeUsed();
//		MusicItemVO i;
//		Iterator<MusicItemVO> it = item.getRelatedItems().iterator();
//		while (it.hasNext()) {
//			i = it.next();
//			setLastDateTimeUsed(i);
//			if (latestLastUsed == null) {
//				latestLastUsed = i.getLastDateTimeUsed();
//			} else {
//				if (i.getLastDateTimeUsed() != null && i.getLastDateTimeUsed().after(latestLastUsed)) {
//					latestLastUsed = i.getLastDateTimeUsed();
//				}
//			}
//		}
//		//if (latestLastUsed != null) {
//			item.setLastDateTimeUsed(latestLastUsed);
//			it = item.getRelatedItems().iterator();
//			while (it.hasNext()) {
//				i = it.next();
//				i.setLastDateTimeUsed(latestLastUsed);
//			}
//		//}
//		
//	}
	
	/**
	 * Set the last date time used for the words
	 * @param item
	 */
	private Date getLastDateTimeUsed(MusicItemVO item) {
		
		
		Date d = null;
    	//Query all service elements with this music item
    	ServiceElementVO se = new ServiceElementVO(null,null);
    	se.setItem(item);
    	List <ServiceElementVO> cpl = db.queryByExample(se);
    	Iterator<ServiceElementVO> it = cpl.iterator();
    	while (it.hasNext()) {
    		se = it.next();
    		if (se.getService().isTemplate()) {
    			continue;
    		}
    
    		if (d == null) {
    			d = se.getService().getDateTime();
    		}
    		if (d != null && se.getService().getDateTime().after(d)) {
    			d = se.getService().getDateTime();
    		}
    	}
    	return d;
    }
	
	/**
	 * Set the last date time used for the tune
	 * @param item
	 */
	private Date getTuneLastDateTimeUsed(MusicItemVO item) {
		
		
		Date d = null;
    	//Query all service elements with this music item
    	ServiceElementVO se = new ServiceElementVO(null,null);
    	se.setTuneItem(item);
    	List <ServiceElementVO> cpl = db.queryByExample(se);
    	Iterator<ServiceElementVO> it = cpl.iterator();
    	while (it.hasNext()) {
    		se = it.next();
    		if (se.getService().isTemplate()) {
    			continue;
    		}
    
    		if (d == null) {
    			d = se.getService().getDateTime();
    		}
    		if (d != null && se.getService().getDateTime().after(d)) {
    			d = se.getService().getDateTime();
    		}
    	}
    	return d;
    }



	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection<ServiceElementVO> getMusicItemUsage(MusicItemVO item) {
		Set<ServiceElementVO> l = new HashSet<ServiceElementVO>();
		ServiceElementVO se = new ServiceElementVO(null,null);
    	se.setItem(item);    	
    	l.addAll( (Collection)db.queryByExample(se) );
    	
    	se = new ServiceElementVO(null,null);
    	se.setTuneItem(item);
    	l.addAll( (Collection)db.queryByExample(se) );
    	
    	//Now we also need to add ServiceELements that have any of the
    	//*related* items referenced
    	if (item.getRelatedItems() != null) {
    		Iterator<MusicItemVO> it = item.getRelatedItems().iterator();
    		MusicItemVO m = null;
    		while (it.hasNext()) {
    			m = it.next();
    			se = new ServiceElementVO(null,null);
    	    	se.setItem(m);    	    	
    	    	l.addAll( (Collection)db.queryByExample(se) );
    	    	
    	    	se = new ServiceElementVO(null,null);
    	    	se.setTuneItem(m);
    	    	l.addAll( (Collection)db.queryByExample(se) );
    		}
    	}
    	
    	return l;
	}



	public Collection<MusicItemVO> getMusicItemsForServiceItem(ServiceItemVO serviceItem) {
		Collection<MusicItemVO> items = new ArrayList<MusicItemVO>();
		ServiceElementVO se = new ServiceElementVO(null,null);
    	se.setServiceItem(serviceItem);    	
    	
		Collection<ServiceElementVO> coll = (Collection)db.queryByExample(se) ;
    	Iterator<ServiceElementVO> it = coll.iterator();
    	while (it.hasNext()) {
    		se = it.next();
    		if (se.getItem() != null) {
    			items.add(se.getItem());
    		}
    	}
    	return items;
	}



	public ServiceElementVO getLastMusicItemUsage(
			MusicItemVO item) {
		 
		Collection<ServiceElementVO> coll = this.getMusicItemUsage(item);
		if (coll == null) return null;
		Iterator<ServiceElementVO> it = coll.iterator();
		ServiceElementVO se = null;
		ServiceElementVO latest = null;
		while (it.hasNext()) {
			se = it.next();
			if (se.getServiceItem() != null && se.getService() != null && se.getService().getDateTime() != null) {
				if (latest == null) latest = se;
				if (se.getService().getDateTime().after(latest.getService().getDateTime())) {
					latest = se;
				}
			}
			
			
			
		}
		return latest;
	}



	public String findSuggestedBookName(String text) {
		
		if (text == null || text.trim().length() <= 0) return null;
		String book = null;
		Iterator<String> it = bookNameToAcronym.keySet().iterator();
		while (it.hasNext()) {
			book = it.next();
			if (book.toUpperCase().startsWith(text.toUpperCase())) {
				return book;
			}
		}
		return null;
		
	}



	public String findAcronymForBookName(String text) {
		// 
		return bookNameToAcronym.get(text);
	}





}
