package com.organist4j.view.service.item;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.organist4j.model.service.ServiceItemVO;

public class ServiceItemBrowserFilter extends ViewerFilter {

	private String search = "";

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search.toUpperCase();
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (search.trim().length() <= 0) return true;
		
		ServiceItemVO m = (ServiceItemVO)element;
		if (m.getName() != null && m.getName().toUpperCase().indexOf(search) >= 0) {
			return true;
		}
		if (m.getDesc() != null && m.getDesc().toUpperCase().indexOf(search) >= 0) {
			return true;
		}
		
		return false;
		
		
	}

}
