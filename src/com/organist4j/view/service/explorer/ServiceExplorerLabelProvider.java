package com.organist4j.view.service.explorer;

import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.Node;

public class ServiceExplorerLabelProvider implements ILabelProvider {

	@Override
	public Image getImage(Object arg0) {
		
		return null;
	}

	@Override
	public String getText(Object arg0) {
		 
		Node obj = (Node)arg0;
		if (obj.getData() != null && obj.getData() instanceof ServiceVO) {
			ServiceVO s = (ServiceVO)obj.getData();
			if (s.getDateTime() != null) {
				if (s.getType() != null) {
					return new SimpleDateFormat("EEE dd, HHmm").format(s.getDateTime()) + " - " + s.getName() + " (" + s.getType() + ")";
				} else {
				return new SimpleDateFormat("EEE dd, HHmm").format(s.getDateTime()) + " - " + s.getName();
				}
			} else {
				if (s.getType() != null) {
				return s.getName() + " (" + s.getType() + ")";
				} else {
					return s.getName();
				}
					
			}
		} else {
			if (obj.getDate() != null) {
				if (obj.getLevel() == 1) {
					return new SimpleDateFormat("yyyy").format(obj.getDate());
				}
				if (obj.getLevel() == 2) {
					return new SimpleDateFormat("MMMM").format(obj.getDate());
				}
				return obj.getDate().toString();
			} else {
				return obj.getData() != null ? obj.getData().toString() : "Not Set";
			}
		}
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
