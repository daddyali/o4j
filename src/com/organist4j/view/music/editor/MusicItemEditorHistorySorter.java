package com.organist4j.view.music.editor;

import java.util.Date;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceElementVO;


public class MusicItemEditorHistorySorter extends ViewerSorter {
	
	TableViewer tv;
	public MusicItemEditorHistorySorter(TableViewer tv) {
		this.tv = tv;
	}
	public int compare(Viewer viewer, Object e1, Object e2) {
		ServiceElementVO se1 = (ServiceElementVO)e1;
		ServiceElementVO se2 = (ServiceElementVO)e2;
		
		Object s1 = "";
		Object s2 = "";
		int rc = 0;
		int col = 0;
		try {
			col = Integer.parseInt( tv.getTable().getSortColumn().getData().toString() );
		} catch (Exception ig) {}
		switch (col) {
		case MusicItemVO.SERVICE_POS:
			s1 = se1.getService().getName() == null ? "" : se1.getService().getName();
			s2 = se2.getService().getName() == null ? "" : se2.getService().getName();
			
			rc = s1.toString().toUpperCase().compareTo(s2.toString().toUpperCase());
			
			break;

		case MusicItemVO.USAGE_POS:
			s1 = se1.getName() == null ? "" : se1.getName();
			s2 = se2.getName() == null ? "" : se2.getName();
			
			rc = s1.toString().toUpperCase().compareTo(s2.toString().toUpperCase());
			
			break;
		case MusicItemVO.SERVICE_DATE_POS:
			s1 = se1.getService().getDateTime() == null ? new Date(0) : se1.getService().getDateTime();
			s2 = se2.getService().getDateTime() == null ? new Date(0) : se2.getService().getDateTime();
			rc = ((Date)s1).compareTo((Date)s2);
			break;
		}
		
		//If down, switch
		if (tv.getTable().getSortDirection() == SWT.UP) {
			rc = rc * -1;
		}
		
		return rc;
		
	}
	
	

}
