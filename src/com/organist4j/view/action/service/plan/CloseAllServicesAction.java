package com.organist4j.view.action.service.plan;

import org.eclipse.jface.action.Action;

import com.organist4j.view.service.plan.ServicePlanWorkbench;

public class CloseAllServicesAction extends Action  {
	ServicePlanWorkbench spw;
	public CloseAllServicesAction(ServicePlanWorkbench spw) {
		this.spw = spw;
		setText("Close &All");
	}
	@Override
	public void run() {
		spw.closeAllServices(false);
	}
	
	

}
