package com.organist4j.model.service;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;

import com.organist4j.util.O4J;

public class ServiceItemVO {
	
	public static final int NAME_POS = 0;
	public static final int DESC_POS = 1;
	public static final int MANDATORY_POS = 2;
	public static final int SHORTNAME_POS = 3;	
	public static final int CHECKPROXIMITY_POS = 4;
	
	public static final String[] LABELS = {
		"Section Name",
		"Description",
		"M",
		"Short Name",
		"C"
	};
	
	public static final String[] TOOLTIPS = {
		"Section Name",
		"Description",
		"Mandatory Section",
		"Short Name",
		"Check Last Usage"
	};
	
	public static final int[][] ORDER = {
		{ NAME_POS,200,O4J.COLEDIT_TEXT,SWT.LEFT },
		{ DESC_POS,200,O4J.COLEDIT_TEXT,SWT.LEFT  },
		{ MANDATORY_POS,25,O4J.COLEDIT_BOOL,SWT.CENTER  },
		{ SHORTNAME_POS,80,O4J.COLEDIT_TEXT,SWT.LEFT  },
		{ CHECKPROXIMITY_POS,25,O4J.COLEDIT_BOOL,SWT.CENTER  }
	};
	
	
	
	

	
	String name;
	String desc;
	String shortName;
	Boolean mandatory;
	Boolean checkProximity;
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
	
	
	public Boolean getCheckProximity() {
		if (checkProximity == null) checkProximity = false;
		return checkProximity;
	}
	public void setCheckProximity(Boolean checkProximity) {
		this.checkProximity = checkProximity;
	}
	public Boolean getMandatory() {
		if (mandatory == null) mandatory = false;
		return mandatory;
	}
	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}
//	public ServiceItemVO(String name, String shortName,Boolean mandatory) {
//		super();
//		this.name = name;
//		this.shortName = shortName;
//		this.mandatory = mandatory;
//	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public ServiceItemVO(String name,Boolean mandatory,Boolean checkProximity) {
		super();
		this.name = name;
		this.mandatory = mandatory;
		this.checkProximity = checkProximity;
		
	}

	

}
