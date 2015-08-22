package com.organist4j.view.action.service.item;


	import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Shell;

import com.organist4j.model.service.ServiceItemVO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.Node;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.service.item.ServiceItemBrowser;




	public class DeleteServiceItemAction extends Action implements ISelectionChangedListener {
		Shell s;
		ServiceVO service = null;
		Node n = null;

		public DeleteServiceItemAction(ApplicationWindow w) {
			super();
			s = w.getShell();


			setText("&Delete Service Item");
		}
		public DeleteServiceItemAction(TableViewer tbvItemBrowser) {
			super();
			
			s = tbvItemBrowser.getTable().getShell();
			setText("&Delete Service Item");
		}
		public void run() {

			//Dialog here 
			ServiceItemBrowser tv = ServiceLocator.getInstance().getServiceItemBrowser();
			IStructuredSelection sel = (IStructuredSelection) tv.getTbvItemBrowser().getSelection();
			if (sel.size() <= 0) {
				return;
			}
			ServiceItemVO item = (ServiceItemVO)sel.getFirstElement();
			String title = "Delete Service Item";
			String msg = "Are you sure you want to delete the service item " + item.getName() + "?";

			if (sel.size() > 1) {
				title = "Delete Service Items";
				msg = "Are you sure you want to delete all the selected service items?";
			}

			if (!MessageDialog.openConfirm(s, title, msg)) {
				return;
			}

			//Delete all the music items
			Iterator<ServiceItemVO> it = sel.iterator();
			while (it.hasNext()) {
				
				ServiceLocator.getInstance().getServiceDAO().deleteServiceItem(it.next());
			}
			
			//Refresh the Music Browser
			ServiceLocator.getInstance().getServiceItemBrowser().refresh(true);
			
			//Refresh any open services


		}
		@Override
		public void selectionChanged(SelectionChangedEvent arg0) {
			IStructuredSelection s = (IStructuredSelection)arg0.getSelection();
			if (s.size() <= 0) {
				this.setEnabled(false);
				return;
			} else {
				this.setEnabled(true);
			}
			if (s.size() > 1) {
				setText("&Delete Service Items");
			} else {
				setText("&Delete Service Item");
			}
		}
	}


