package com.organist4j.view.service.explorer;

import java.util.Date;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.Node;

public class ServiceExplorerSorter extends ViewerSorter {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		Node n1 = (Node)e1;
		Node n2 = (Node)e2;
		Date d1 = n1.getDate();
		Date d2 = n2.getDate();
		
		if (n1.getData() instanceof ServiceVO) {
			d1 = ((ServiceVO)n1.getData()).getDateTime();
		}
		if (n2.getData() instanceof ServiceVO) {
			d2 = ((ServiceVO)n2.getData()).getDateTime();
		}

		if (d1 == null || d2 == null) {
			return super.compare(viewer, e1, e2);
		}
		
		return d1.compareTo(d2);
	}

	
}
