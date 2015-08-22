package com.organist4j.view.service.plan;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.util.O4J;
import com.organist4j.util.ServiceLocator;

public class ServicePlanLabelProvider extends ColumnLabelProvider implements ITableLabelProvider,ITableColorProvider {

	public ServicePlanLabelProvider() {

	}
	
	@Override
	public Image getColumnImage(Object arg0, int colIndex) {
		ServiceElementVO se = (ServiceElementVO)arg0;
		switch (colIndex) {
		case ServiceElementVO.STATUS_POS: 
			if (se.hasWarnings()) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_WARN);
			}
			if (se.getItem() == null && se.getMandatory()) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_REDFLAG);
			}
			if (se.getItem() == null && !se.getMandatory()) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_WHITEFLAG);
			}
			if (se.getItem() != null) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_TICK);
			}
			
			
			break;
		default:
			return null;
			
				
		}
		return null;
	}

	@Override
	public String getColumnText(Object arg0, int colIndex) {
		ServiceElementVO se = (ServiceElementVO)arg0;
		String txt = "";
		
		switch (colIndex) {
		case ServiceElementVO.SECTION_POS: txt = se.getName();
		if (se.getCheckForItemWarnings() != null && se.getCheckForItemWarnings()) {
			txt += "*";
		}
		break;
		case ServiceElementVO.ITEM_POS: 
			if (se.getItem() != null) txt = se.getItem().getDisplayName();
			
		break;
		
		case ServiceElementVO.COMMENTS_POS: txt = se.getComments();
		break;
		case ServiceElementVO.ITEMNAME_POS: 
			if (se.getItem() != null) txt = se.getItem().getName();
			break;
		case ServiceElementVO.STATUS_POS: 
			if (se.getWarnings() != null) txt = se.getWarnings();			
		break;
		case ServiceElementVO.TUNE_POS: 
			if (se.getTuneItem() != null) {
				if (se.getItem() == se.getTuneItem()) {
					return se.getTuneItem().getTune();
				} else {
					txt = se.getTuneItem().getDisplayName();
				}
				
			} else {
				return "";
			}
		break;
		default:
				
		}
		if (txt == null) {
			txt = "";
		}
		
		return txt;
	}

	@Override
	public void addListener(ILabelProviderListener arg0) {
		 
		
	}

	@Override
	public void dispose() {
		 
		
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		 
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		 
		
	}
	
	public String getToolTipText(Object element) {
		return "Tooltip (" + element + ")";
	}
	
	public void update(ViewerCell cell) {
		 cell.setText(this.getColumnText(cell.getElement(),cell.getColumnIndex()));
		
	}

	@Override
	public Color getBackground(Object element, int colIndex) {
		ServiceElementVO se = (ServiceElementVO)element;
		Color c = null;
		if (se == null) return c;
		
		switch (colIndex) {
		case ServiceElementVO.SECTION_POS: 
		break;
		case ServiceElementVO.ITEM_POS: 
			
			
		break;
		case ServiceElementVO.COMMENTS_POS: 
		break;
		case ServiceElementVO.ITEMNAME_POS: 
			
		break;
		case ServiceElementVO.TUNE_POS: 
			break;
		case ServiceElementVO.STATUS_POS:
//			if (se.getItem() != null) {
//				c = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
//			}
//			if (se.getItem() == null && se.getMandatory()) {
//				c = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
//			}
//			if (se.hasWarnings()) {
//				c = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
//			}
		break;
		default:
				
		}
	
	return c;

	}

	@Override
	public Color getForeground(Object element, int colIdx) {
		ServiceElementVO se = (ServiceElementVO)element;
		if (se != null) {
			if (se != null && se.getMandatory()) {
				return super.getForeground(element);
			} else {
				if (se != null && se.getItem() != null) {
					return super.getForeground(element);
				} else {
				return Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
				}
			}
		} else {
			return super.getForeground(element);
		}
	}

	
	
	


	

}
