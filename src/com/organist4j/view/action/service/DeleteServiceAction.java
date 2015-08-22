package com.organist4j.view.action.service;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.custom.BusyIndicator;

import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.Node;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.service.explorer.ServiceExplorer;
import com.organist4j.view.service.plan.ServicePlanWorkbench;

public class DeleteServiceAction extends Action implements ISelectionChangedListener {
	ApplicationWindow window;
	ServiceVO service = null;
	Node n = null;

	public DeleteServiceAction(ApplicationWindow w) {
		window = w;
	

		setText("&Delete Service");
	}
	public void run() {
		
		//Dialog here 
		if (service == null) {
			//Pick the service from the selected item in the tree if any
		}
		String title = "Delete Service";
		String msg = "Are you sure you want to delete the service " + service.getName() + "?";
	
		if (service.isTemplate()) {
			title = "Delete Template";
			msg = "Are you sure you want to delete the template " + service.getName() + "?";
		}
		
		if (!MessageDialog.openConfirm(window.getShell(), title, msg)) {
			return;
		}
		
		//Now we should have a service object
		
		//Get hold of the ServicePlan
		ServicePlanWorkbench sp = (ServicePlanWorkbench)window.getShell().getDisplay().getData("ServicePlan");
		
		sp.closeService(service);
		
		//Save any music items referenced by this service
		//TODO need to recalc music item last used affected by this delete
		
		//Now delete the service
		ServiceExplorer se = ServiceLocator.getInstance().getServiceExplorer();
		
		
		//removeServiceFromNodeChildren(null,service);
		BusyIndicator.showWhile(null, new Runnable() {

			@Override
			public void run() {
				 
				ServiceLocator.getInstance().getServiceDAO().deleteService(service);
				ServiceLocator.getInstance().getMusicItemBrowser().refresh();
				
			}
			
		});
		
		//reload the ServiceExplorer
		se.reload(service);
		
	}
	
//	private void removeServiceFromNodeChildren(Node nn,ServiceVO service) {
//		
//		Iterator<Node> it = nn.getChildren().iterator();
//		while (it.hasNext()) {
//			Node child = it.next();
//			ServiceVO s = (ServiceVO)child.getData();
//			if (s == service) {
//				nn.getChildren().remove(child);
//				return;
//			}
//		}
//		return;
//	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		IStructuredSelection s = (IStructuredSelection)arg0.getSelection();
		n = (Node)s.getFirstElement();
		if (n == null) return;
		if (n.getData() instanceof ServiceVO) {					
			this.setEnabled(true);	
			service = (ServiceVO)n.getData();
			if (service.isTemplate()) {
				setText("&Delete Template");
			} else {
				setText("&Delete Service");
			}
		} else {
			this.setEnabled(false);
		}
		
	}
}
