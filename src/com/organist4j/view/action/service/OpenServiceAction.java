package com.organist4j.view.action.service;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.ApplicationWindow;

import com.organist4j.model.service.ServiceVO;
import com.organist4j.view.service.plan.ServicePlanWorkbench;

public class OpenServiceAction extends Action {
	ApplicationWindow window;
	ServiceVO service = null;

	public OpenServiceAction(ApplicationWindow w,ServiceVO service) {
		window = w;
		this.service = service;

		setText("&Open Service...");
	}
	public void run() {
		
		//Dialog here 
		if (service == null) {
			//Need a dialog here to pick the service
		}
		
		//Now we should have a service object
		
		//Get hold of the ServicePlan
		ServicePlanWorkbench sp = (ServicePlanWorkbench)window.getShell().getDisplay().getData("ServicePlan");
		
		sp.showService(service);
	}
}
