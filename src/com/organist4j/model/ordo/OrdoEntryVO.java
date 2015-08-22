package com.organist4j.model.ordo;

public class OrdoEntryVO {
	
	String name;
	String desc;
	OrdoSeasonVO season;
	public OrdoEntryVO(OrdoSeasonVO season,String name) {
		super();
		this.name = name;
		this.season = season;
	}
	public OrdoEntryVO(OrdoSeasonVO season) {
		this(season,null);
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
	
	
	public String toString() {
		return name;
	}
	
	

}
