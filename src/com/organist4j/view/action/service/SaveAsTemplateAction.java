package com.organist4j.view.action.service;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.window.Window;

import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.Node;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.service.explorer.ServiceExplorer;

public class SaveAsTemplateAction extends Action implements ISelectionChangedListener {
	ApplicationWindow window;
	ServiceVO service = null;

	public SaveAsTemplateAction(ApplicationWindow w) {
		window = w;
	

		setText("Save As New &Template...");
	}
	public void run() {
		
		//Dialog here 
		if (service == null) {
			//Pick the service from the selected item in the tree if any
		}
		
		//Now we should have a service object
		
		//Get hold of the ServicePlan
		
		
		
		InputDialog dlg = new InputDialog(window.getShell(),
	            "Save As Template...", "Enter Template Name", service.getName() + " Template", new IInputValidator() {

					@Override
					public String isValid(String name) {
						if (ServiceLocator.getInstance().getServiceDAO().isServiceNameExists(name)) {
							return "Name already exists";
						}
						return null;
					}});
		ServiceVO template = new ServiceVO();
		template.setDateTime(null);
		template.setTemplate(true);
		
		if (dlg.open() == Window.OK) {
	          // User clicked OK; update the label with the input
			template.setName(dlg.getValue());    
			
	     } else {
	    	 return;
	     }
		
		//Build the ServiceElements
		Iterator<ServiceElementVO> it = service.getServiceElements().iterator();
		ServiceElementVO tse = null;
		while (it.hasNext()) {
			ServiceElementVO se = it.next();
			if (se.getServiceItem() != null) {
				tse = new ServiceElementVO(template,se.getServiceItem());
			} else {
				tse = new ServiceElementVO(template,null);
				tse.setName(se.getName());
			}			
			//tse.setCheckForItemWarnings(se.getCheckForItemWarnings());
			tse.setMandatory(se.getMandatory());
			tse.setComments(se.getComments());
			tse.setDesc(se.getDesc());
			tse.setItem(se.getItem());
			tse.setTuneItem(se.getTuneItem());			
			template.getServiceElements().add(tse);
			template.setType(service.getType());
		}
		
		//Now create a new service
		ServiceLocator.getInstance().getServiceDAO().updateService(template);
		
		//Refresh the ServiceExplorer
		ServiceExplorer se = ServiceLocator.getInstance().getServiceExplorer();
		se.reload();
	}
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		IStructuredSelection s = (IStructuredSelection)arg0.getSelection();
		Node n = (Node)s.getFirstElement();
		if (n == null) return;
		if (n.getData() instanceof ServiceVO) {					
			this.setEnabled(true);	
			service = (ServiceVO)n.getData();
		} else {
			this.setEnabled(false);
		}
		
	}
}
