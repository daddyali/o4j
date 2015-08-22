package com.organist4j.view.service.item;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.organist4j.model.service.ServiceItemVO;
import com.organist4j.util.O4J;
import com.organist4j.util.ServiceLocator;

public class ServiceItemBrowserLabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		ServiceItemVO mbe = (ServiceItemVO)arg0;
		Image img = null;
		switch (arg1) {
		case ServiceItemVO.NAME_POS:
		break;
		case ServiceItemVO.DESC_POS: 
		break;
		case ServiceItemVO.MANDATORY_POS:
			if (mbe.getMandatory()) {
				img = ServiceLocator.getInstance().getImgRef().get(O4J.IMG_REDFLAG);
			} else {
				img = ServiceLocator.getInstance().getImgRef().get(O4J.IMG_WHITEFLAG);
					
			}
		break;
		case ServiceItemVO.SHORTNAME_POS: 
		break;
		case ServiceItemVO.CHECKPROXIMITY_POS: 
			if (mbe.getCheckProximity()) {
				img = ServiceLocator.getInstance().getImgRef().get(O4J.IMG_WARN);
			} else {
				img = null;
			}
			break;
		default:

		}
		return img;
	}

	@Override
	public String getColumnText(Object arg0, int colIndex) {
		ServiceItemVO mbe = (ServiceItemVO)arg0;
		String txt = "";
		switch (colIndex) {
		case ServiceItemVO.NAME_POS: txt = mbe.getName();
		break;
		case ServiceItemVO.DESC_POS: txt = mbe.getDesc();
		break;
		case ServiceItemVO.MANDATORY_POS: txt = null;
		break;
		case ServiceItemVO.SHORTNAME_POS: txt = mbe.getShortName();
		break;
		case ServiceItemVO.CHECKPROXIMITY_POS: txt = null;
		break;
		default:

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
}
