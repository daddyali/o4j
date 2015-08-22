package com.organist4j.view.service.plan;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

public class ServicePlanCellLabelProvider extends CellLabelProvider {

	@Override
	public void update(ViewerCell cell) {
		 cell.setText(cell.getElement().toString());
		
	}
	
	public String getToolTipText(Object element) {
		return "Tooltip (" + element + ")";
	}


}
