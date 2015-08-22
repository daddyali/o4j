package com.organist4j.view.service.explorer;

import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.Node;

public class ServiceExplorerDecoratingLabelProvider extends DecoratingLabelProvider {

	public ServiceExplorerDecoratingLabelProvider(ILabelProvider provider,
			ILabelDecorator decorator) {
		super(provider, decorator);
		
	}

	@Override
	public Color getForeground(Object element) {
		Node n = (Node)element;
		if (n.getData() != null && n.getData() instanceof ServiceVO) {
			ServiceVO s = (ServiceVO)n.getData();
			if (!s.isTemplate() && s.getPlanningPct() < 100) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
			}
		} 
		return super.getForeground(element);
		
		
	}
	
	
	
	

}
