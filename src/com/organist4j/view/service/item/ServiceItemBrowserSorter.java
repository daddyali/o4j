package com.organist4j.view.service.item;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

import com.organist4j.model.service.ServiceItemVO;

public class ServiceItemBrowserSorter extends ViewerSorter {
	TableViewer tv;
	public ServiceItemBrowserSorter(TableViewer tv) {
		this.tv = tv;
	}
	public int compare(Viewer viewer, Object e1, Object e2) {
		ServiceItemVO se1 = (ServiceItemVO)e1;
		ServiceItemVO se2 = (ServiceItemVO)e2;
		
		Object s1 = "";
		Object s2 = "";
		
		int col = 0;
		try {
			col = Integer.parseInt( tv.getTable().getSortColumn().getData().toString() );
		} catch (Exception ig) {}
		switch (col) {
		case ServiceItemVO.NAME_POS:
			s1 = se1.getName() == null ? "" : se1.getName();
			s2 = se2.getName() == null ? "" : se2.getName();
			break;
		case ServiceItemVO.DESC_POS:
			s1 = se1.getDesc() == null ? "" : se1.getDesc();
			s2 = se2.getDesc() == null ? "" : se2.getDesc();
			break;
		case ServiceItemVO.MANDATORY_POS:
			s1 = se1.getMandatory() == null ? "" : se1.getMandatory();
			s2 = se2.getMandatory() == null ? "" : se2.getMandatory();
			break;
		case ServiceItemVO.SHORTNAME_POS:
			s1 = se1.getShortName() == null ? "" : se1.getShortName();
			s2 = se2.getShortName() == null ? "" : se2.getShortName();
			break;
		}
		int rc = s1.toString().toUpperCase().compareTo(s2.toString().toUpperCase());
		
		//If down, switch
		if (tv.getTable().getSortDirection() == SWT.UP) {
			rc = rc * -1;
		}
		
		return rc;
		
	}
	
	

}
