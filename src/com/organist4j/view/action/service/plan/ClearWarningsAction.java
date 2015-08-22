package com.organist4j.view.action.service.plan;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.service.plan.ServicePlan;

public class ClearWarningsAction extends Action implements ISelectionChangedListener {
	ServicePlan plan = null;
	ServiceVO service = null;

	public ClearWarningsAction(ServicePlan plan) {
		//window = w;
		this.plan = plan;
		
		setText("Clear &Warnings");
	}
	public void run() {
		IStructuredSelection s = (IStructuredSelection)plan.getTv().getSelection();
		if (s != null) {
			
			Iterator<ServiceElementVO> it = s.iterator();
			while (it.hasNext()) {
				ServiceElementVO se = it.next();
				se.clearWarnings();
				ServiceLocator.getInstance().getServiceDAO().updateServiceElement(se);			
				
			}
	
			//ServiceLocator.getInstance().getServiceDAO().updateService(service);			
			
		
			plan.refresh();
			
		}
	
		
	}
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		IStructuredSelection s = (IStructuredSelection)arg0.getSelection();
		ServiceElementVO se = (ServiceElementVO)s.getFirstElement();
//		this.se = se;
//		if (se != null) {
//			if (se.getItem() == null) {
//				this.setEnabled(false);
//			} else {
//				this.setEnabled(true);
//			}
//		}
	}
}
