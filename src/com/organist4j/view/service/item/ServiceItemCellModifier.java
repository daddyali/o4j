package com.organist4j.view.service.item;


import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.model.service.ServiceItemVO;
import com.organist4j.util.ServiceLocator;


public class ServiceItemCellModifier implements ICellModifier {
	TableViewer tbvService;
	public ServiceItemCellModifier(TableViewer tbvService) {
		this.tbvService = tbvService;
	}

	@Override
	public boolean canModify(Object element, String property) {
		if (property.equals(""+ServiceItemVO.NAME_POS)) {
			return false;
		} else {
		
		return true;
		}
	}

	@Override
	public Object getValue(Object element, String property) {
		ServiceItemVO se = (ServiceItemVO)element;
		if (property.equals(""+ServiceItemVO.NAME_POS)) {
			if (se.getName() != null) {
				return se.getName();
			} else {
				return "";
			}
		}
		
		if (property.equals(""+ServiceItemVO.DESC_POS)) {
			if (se.getDesc() != null) {
				return se.getDesc();
			} else {
				return "";
			}
		}
		
		if (property.equals(""+ServiceItemVO.SHORTNAME_POS)) {
			if (se.getShortName() != null) {
				return se.getShortName();
			} else {
				return "";
			}
		}
		

		if (property.equals(""+ServiceItemVO.MANDATORY_POS)) {
			return se.getMandatory();
		}
		
		if (property.equals(""+ServiceItemVO.CHECKPROXIMITY_POS)) {
			return se.getCheckProximity();
		}
		
		return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		TableItem ti =(TableItem)element;
		ServiceItemVO se = (ServiceItemVO)ti.getData();
		if (property.equals(""+ServiceItemVO.NAME_POS)) {
			se.setName((String)value);
			ServiceLocator.getInstance().getServiceDAO().updateServiceItem(se);
			this.tbvService.refresh();
		}
		
		if (property.equals(""+ServiceItemVO.SHORTNAME_POS)) {
			se.setShortName((String)value);
			ServiceLocator.getInstance().getServiceDAO().updateServiceItem(se);
			this.tbvService.refresh();
		}
		
		if (property.equals(""+ServiceItemVO.DESC_POS)) {
			se.setDesc((String)value);
			ServiceLocator.getInstance().getServiceDAO().updateServiceItem(se);
			this.tbvService.refresh();
		}
		
		if (property.equals(""+ServiceItemVO.MANDATORY_POS)) {
			se.setMandatory(((Boolean)value).booleanValue());
			ServiceLocator.getInstance().getServiceDAO().updateServiceItem(se);
			this.tbvService.refresh();
		}
		
		if (property.equals(""+ServiceItemVO.CHECKPROXIMITY_POS)) {
			se.setCheckProximity(((Boolean)value).booleanValue());
			ServiceLocator.getInstance().getServiceDAO().updateServiceItem(se);
			this.tbvService.refresh();
		}
		
	}


	
}

