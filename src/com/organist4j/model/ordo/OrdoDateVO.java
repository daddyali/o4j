package com.organist4j.model.ordo;

import java.util.ArrayList;
import java.util.List;

public class OrdoDateVO {
	List<OrdoEntryVO> entries = new ArrayList<OrdoEntryVO>();

	public List<OrdoEntryVO> getEntries() {
		return entries;
	}

	public void setEntries(List<OrdoEntryVO> entries) {
		this.entries = entries;
	}
	
	public String toString() {
		return entries.toString();
	}
}
