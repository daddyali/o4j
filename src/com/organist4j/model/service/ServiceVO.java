package com.organist4j.model.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.organist4j.model.lectionary.Lectionary;

public class ServiceVO  {
	//transient Logger logger = Logger.getLogger(ServiceVO.class);
	Lectionary lectionary;
	public void setLectionary(Lectionary lectionary) {
		this.lectionary = lectionary;
	}

	private String name;
	String type;
	public String getType() {		
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	String desc;
	String templateName;
	
	
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public ServiceVO() {
		super();
	}

	private Date dateTime;
	private List<ServiceElementVO> serviceElements = new ArrayList<ServiceElementVO>();
	private Boolean template;

	public Boolean isTemplate() {
		return template;
	}
	public void setTemplate(Boolean template) {
		this.template = template;
	}
	public List<ServiceElementVO> getServiceElements() {
		return serviceElements;
	}
	public void addServiceElement(ServiceElementVO serviceElement) {
		serviceElements.add(serviceElement);
	}
	
	public String getName() {
		if (name == null) {
			return "";
		} else {
			return name;
		}
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	public String toString() {
		return name;
	}
	public Lectionary getLectionary() {
		if (lectionary == null) {
			return Lectionary.getDefault();
		} else {
			return lectionary;
		}
		
	}
	
	
	public long getPlanningPct() {
		
		Iterator<ServiceElementVO> it = serviceElements.iterator();
		double required = 0;
		double complete = 0;
		while (it.hasNext()) {
			ServiceElementVO se = it.next();
			if (se.getMandatory() || se.getItem() != null) {
				required++;
			} else {
				
			}
			if (se.getItem() != null) {
				complete++;
			}
		}
		//logger.debug(serviceElements.size() + ":" + complete + ":" + required + ":" +   Math.round(complete/required * 100) + "%");
		return Math.round(complete/required * 100);
	}
	
	
	

}
