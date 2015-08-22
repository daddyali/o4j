package com.organist4j.view.action.service.plan;

import org.eclipse.jface.action.Action;

import com.organist4j.view.service.plan.ServicePlanWorkbench;

public class CloseOtherServicesAction extends Action  {
	ServicePlanWorkbench spw;
	public CloseOtherServicesAction(ServicePlanWorkbench spw) {
		this.spw = spw;
		setText("Close &Others");
	}
	@Override
	public void run() {
		spw.closeAllServices(true);
	}
	
	

}