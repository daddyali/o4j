package com.organist4j.view.action.service.plan;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.service.plan.ServicePlan;

public class ShowMusicItemsForServiceItemAction extends Action implements ISelectionChangedListener {
	ServicePlan plan = null;
	ServiceElementVO se = null;

	public ShowMusicItemsForServiceItemAction(ServicePlan plan) {
		//window = w;
		this.plan = plan;
		setText("Filter by Service &Item");
		this.setChecked(false);
	}
	public void run() {
		if (se != null) {

			
			ServiceLocator.getInstance().getMusicItemBrowser().setFilterByServiceItem(!ServiceLocator.getInstance().getMusicItemBrowser().isFilterByServiceItem());
			this.setChecked(ServiceLocator.getInstance().getMusicItemBrowser().isFilterByServiceItem());
			if (ServiceLocator.getInstance().getMusicItemBrowser().isFilterByServiceItem() && se.getServiceItem() != null) {
				ServiceLocator.getInstance().getMusicItemBrowser().setSearchFilterByServiceItem(se.getServiceItem());
			} else {
				ServiceLocator.getInstance().getMusicItemBrowser().setSearchFilterByServiceItem(null);
			}
			ServiceLocator.getInstance().getMusicItemBrowser().refresh();
			
			//ServiceLocator.getInstance().getMusicItemBrowser().setSearchFilter(null);

		}


	}
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		IStructuredSelection s = (IStructuredSelection)arg0.getSelection();
		ServiceElementVO se = (ServiceElementVO)s.getFirstElement();
		this.se = se;
		if (se != null) {

			this.setEnabled(true);
		} else {
			this.setEnabled(false);
		}
	}

}
