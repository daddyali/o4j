package com.organist4j.view.action.service.plan;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;

import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.service.plan.ServicePlan;

public class ToggleServiceElementMandatoryAction extends Action implements ISelectionChangedListener {
	ServicePlan plan = null;
	IStructuredSelection sel = null;

	public ToggleServiceElementMandatoryAction(ServicePlan plan) {
		this.plan = plan;
		//window = w;
		setText("&Toggle Mandatory");
	}
	@SuppressWarnings("unchecked")
	public void run() {
		if (sel == null) return;
				
		ServiceVO service = null;
		Iterator<ServiceElementVO> it = sel.iterator();
		while (it.hasNext()) {
			ServiceElementVO se = (ServiceElementVO)it.next();
			if (service == null) service = se.getService();
			if (se.getMandatory() != null) {
				se.setMandatory(!se.getMandatory().booleanValue());
			} else {
				se.setMandatory(true);
			}
			
		}
		ServiceLocator.getInstance().getServiceDAO().updateService(service);
		
		plan.refresh();
		
		
	}
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		sel = (IStructuredSelection)arg0.getSelection();

	}
}
