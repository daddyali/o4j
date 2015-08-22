package com.organist4j.view.action.service.item;



import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.window.Window;

import com.organist4j.model.service.ServiceItemVO;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.service.item.ServiceItemBrowser;

public class RenameServiceItemAction extends  Action implements ISelectionChangedListener {
	ApplicationWindow window;
	
	String newName = "";

	public RenameServiceItemAction(ApplicationWindow w) {
		window = w;


		setText("&Rename Service Item...");
	}
	public void run() {

	
		ServiceItemBrowser tv = ServiceLocator.getInstance().getServiceItemBrowser();
		IStructuredSelection sel = (IStructuredSelection) tv.getTbvItemBrowser().getSelection();
		if (sel.size() <= 0) {
			return;
		}
		ServiceItemVO serviceItem = (ServiceItemVO)sel.getFirstElement();
		String titleBar = "Rename Service Item...";
		String instruction = "Enter new name";
		
		InputDialog dlg = new InputDialog(window.getShell(),
				titleBar, instruction, serviceItem.getName(), null);


		if (dlg.open() == Window.OK) {
			// User clicked OK; update the label with the input
			serviceItem.setName(dlg.getValue());   
		} else {
			return;
		}

		ServiceLocator.getInstance().getServiceDAO().updateServiceItem(serviceItem);
	
		//Refresh the ItemExplorer
		ServiceItemBrowser sib = ServiceLocator.getInstance().getServiceItemBrowser();
		sib.refresh(true);


	}
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		

	}
}

