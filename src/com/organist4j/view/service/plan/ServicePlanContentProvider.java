package com.organist4j.view.service.plan;


import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.organist4j.model.service.ServiceVO;

public class ServicePlanContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(Object obj) {
		ServiceVO sv = (ServiceVO)obj;
	    return sv.getServiceElements().toArray();
	}

	@Override
	public void dispose() {
	
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {

	}


}
