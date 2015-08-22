package com.organist4j.view.service.plan;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.util.ServiceLocator;


public class ServicePlanCellModifier implements ICellModifier {
	TableViewer tbvService;
	public ServicePlanCellModifier(TableViewer tbvService) {
		this.tbvService = tbvService;
	}

	@Override
	public boolean canModify(Object element, String property) {
		if (property.equals(""+ServiceElementVO.COMMENTS_POS)) {
			return true;
		}
		return false;
	}

	@Override
	public Object getValue(Object element, String property) {
		ServiceElementVO se = (ServiceElementVO)element;
		if (property.equals(""+ServiceElementVO.COMMENTS_POS)) {
			if (se.getComments() != null) {
				return se.getComments();
			} else {
				return "";
			}
		}
		

		
		
		return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		TableItem ti =(TableItem)element;
		ServiceElementVO se = (ServiceElementVO)ti.getData();
		if (property.equals(""+ServiceElementVO.COMMENTS_POS)) {
			se.setComments((String)value);
			ServiceLocator.getInstance().getServiceDAO().updateServiceElement(se);
			this.tbvService.refresh();
		}
		
		
		
	}


	
}
