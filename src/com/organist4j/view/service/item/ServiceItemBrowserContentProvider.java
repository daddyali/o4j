package com.organist4j.view.service.item;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ServiceItemBrowserContentProvider implements IStructuredContentProvider {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object[] getElements(Object arg0) {
		return ((List)arg0).toArray();
	}

	@Override
	public void dispose() {
		 
		
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		 
		
	}


}
