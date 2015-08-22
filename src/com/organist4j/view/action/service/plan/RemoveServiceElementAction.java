package com.organist4j.view.action.service.plan;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;

import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.music.item.MusicItemBrowser;
import com.organist4j.view.service.plan.ServicePlan;

public class RemoveServiceElementAction extends Action implements ISelectionChangedListener {
	ServicePlan plan = null;
	IStructuredSelection sel = null;

	public RemoveServiceElementAction(ServicePlan plan) {
		this.plan = plan;
		//window = w;
		setText("&Remove Service Element");
	}
	@SuppressWarnings("unchecked")
	public void run() {
		if (sel == null) return;
		
		
		
		String title = "";
		String msg = "";
		if (sel.size() > 1) {
			title = "Delete Service Elements";
			msg = "Are you sure you want to delete the selected service elements? ";
		} else {
			ServiceElementVO se = (ServiceElementVO)sel.getFirstElement();
			title = "Delete Service Element";
			msg = "Are you sure you want to delete the service element " + se.getName() + "? ";
			if (se.getItem() != null) {
				msg += "This is currently assigned to Music Item " + se.getItem().getDisplayName() + ".";
			}
		}
		
		if (!MessageDialog.openConfirm(plan.getTv().getTable().getShell(), title, msg)) {
			return;
		}
		
		Iterator<ServiceElementVO> it = sel.iterator();
		while (it.hasNext()) {
			ServiceElementVO se = (ServiceElementVO)it.next();
			MusicItemVO item = se.getItem();
			MusicItemVO tuneItem = se.getTuneItem();
			se.getService().getServiceElements().remove(se);
			ServiceLocator.getInstance().getServiceDAO().updateService(se.getService());
			ServiceLocator.getInstance().getServiceDAO().deleteServiceElement(se);
			if (item != null || tuneItem != null) {
				ServiceLocator.getInstance().getMusicDAO().updateMusicItem(item,true);
				ServiceLocator.getInstance().getMusicItemBrowser().refresh();
				
			}
			
		}
		plan.refresh();
		
		
	}
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		sel = (IStructuredSelection)arg0.getSelection();
		
//		this.se = se;
	}
}
