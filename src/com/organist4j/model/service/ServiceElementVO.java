package com.organist4j.model.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.organist4j.model.music.MusicItemVO;
import com.organist4j.util.O4J;
import com.organist4j.util.ServiceLocator;



public class ServiceElementVO {
	

	
	public static final int COMMENTS_POS = 5;
	public static final int ITEM_POS = 2;
	public static final int ITEMNAME_POS = 3;
	public static final int SECTION_POS = 1;
	public static final int STATUS_POS = 0;
	public static final int TUNE_POS = 4;
	
	public static final String[] LABELS = {
		"",
		"Section",
		"Item",
		"Title",
		"Tune / Setting",
		"Comments",
	};
	
	public static final int[][] ORDER = {
		{STATUS_POS,25,O4J.COLEDIT_NONE},
		{SECTION_POS,180,O4J.COLEDIT_NONE},
		{ITEM_POS,110,O4J.COLEDIT_NONE},
		{ITEMNAME_POS,250,O4J.COLEDIT_NONE},
		{TUNE_POS,125,O4J.COLEDIT_NONE},
		{COMMENTS_POS,250,O4J.COLEDIT_TEXT},		
	};
	
	public static final String WARNING_TUNE = "WARNING_TUNE";
	public static final String WARNING_ITEM = "WARNING_ITEM";
	
	String name;
	String desc;
	Boolean mandatory;
//	Boolean checkForItemWarnings;
	public Boolean getCheckForItemWarnings() {
		//return checkForItemWarnings;
		return serviceItem != null ? serviceItem.getCheckProximity() : false;
	}
//	public void setCheckForItemWarnings(Boolean checkForItemWarnings) {
//		this.checkForItemWarnings = checkForItemWarnings;
//	}

	String comments;
	MusicItemVO item;
	MusicItemVO tuneItem;
	ServiceItemVO serviceItem;
	Map<String, String> warnings = null;
	Set<String> tags = null;
	
	public void addTag(String tag) {
		if (tags == null) {
			tags = new HashSet<String>();
		}
		tags.add(tag);
	}
	public boolean hasTag(String tag) {
		if (tags != null) {
			return tags.contains(tag);
		} else {
			return false;
		}
	}
	public void removeTag(String tag) {
		if (tags != null) {
			tags.remove(tag);
		}
	}
	
	public void addWarning(String key,String msg) {
		if (warnings == null) {
			warnings = new HashMap<String, String>();
		}
		warnings.put(key, msg);
	}
	public boolean hasWarnings() {
		return warnings != null && warnings.size() > 0;
	}
	public void clearWarnings() {
		if (warnings != null) warnings.clear();
	}
	public void clearWarning(String key) {
		if (warnings != null) warnings.remove(key);
	}
	public String getWarnings() {
		if (warnings == null || warnings.size() <= 0) return "";
		String wc = "     ";		
		Iterator<String> it = warnings.values().iterator();
		while (it.hasNext()) {
			wc += it.next() + " ";
		}
		return wc;
		
	}
	public MusicItemVO getTuneItem() {
		return tuneItem;
	}
	public void setTuneItem(MusicItemVO tuneItem) {
		this.tuneItem = tuneItem;
		checkForTuneItemWarnings();
	}
	
	ServiceVO service;
	
	
	
	public Boolean getMandatory() {
		if (mandatory == null) return false;
		return mandatory;
	}
	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public MusicItemVO getItem() {
		return item;
	}
	public void setItem(MusicItemVO item) {
		this.item = item;
		checkForItemWarnings();
	}
	public String getName() {
		if (serviceItem != null) {
			return serviceItem.getName();
		} else {
			return name;
		}
	}
	public void setName(String name) {
		this.name = name;
		serviceItem = null; //force this to null as we now have an overriden name
	}
	public ServiceItemVO getServiceItem() {
		return serviceItem;
	}
	public void setServiceItem(ServiceItemVO serviceItem) {
		this.serviceItem = serviceItem;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public ServiceElementVO(ServiceVO service,ServiceItemVO serviceItem) {
		super();
		this.service = service;
		if (serviceItem != null) {
			this.name = serviceItem.getName();
		}
		this.serviceItem = serviceItem;
	}
	

	public ServiceVO getService() {
		return service;
	}
	public void setService(ServiceVO service) {
		this.service = service;
	}
	public String toString() {
		return name;
	}
	
	private void checkForTuneItemWarnings() {
		if (!getCheckForItemWarnings()) {
			return;
		} 
		if (tuneItem != null 
				&& tuneItem.getLastDateTimeUsed() != null 
				&& service != null
				&& service.getDateTime() != null) {
			long diff = Math.abs(item.getLastDateTimeUsed().getTime() - service.getDateTime().getTime());
			
			long wks = ServiceLocator.getInstance().getPreferenceStore().getLong(O4J.PREF_TUNE_USE_WARN_WKS);
			if (diff < (1000 * 60 * 60 * 24 * 7 * wks)) {
				addWarning(ServiceElementVO.WARNING_TUNE,"Tune Item has been used within " + wks + " weeks of this service date. ");
			} else {
				clearWarning(ServiceElementVO.WARNING_TUNE);
			}
		}
	}

	private void checkForItemWarnings() {
		if (!getCheckForItemWarnings()) {
			return;
		} 
		if (item != null 
				&& item.getLastDateTimeUsed() != null 
				&& service != null
				&& service.getDateTime() != null) {
			long diff = Math.abs(item.getLastDateTimeUsed().getTime() - service.getDateTime().getTime());
			
			long wks = ServiceLocator.getInstance().getPreferenceStore().getLong(O4J.PREF_ITEM_USE_WARN_WKS);
			if (diff < (1000 * 60 * 60 * 24 * 7 * wks)) {
				addWarning(ServiceElementVO.WARNING_ITEM,"Item has been used within " + wks + " weeks of this service date.");
			} else {
				clearWarning(ServiceElementVO.WARNING_ITEM);
				
			}
		}
	}
	
	public ServiceElementVO() {
	}
	
}
