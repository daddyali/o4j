package com.organist4j.view.action.service.plan;



import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.service.plan.ServicePlan;

public class ShowAlternateTunesAction extends Action implements ISelectionChangedListener {
	ServicePlan plan = null;
	ServiceElementVO se = null;

	public ShowAlternateTunesAction(ServicePlan plan) {
		//window = w;
		this.plan = plan;
		setText("&Show Alternate Tunes");
	}
	public void run() {
		if (se != null) {


			MusicItemVO item = se.getItem();
			MusicItemVO tuneItem = se.getTuneItem();
			
			String meter = "";
			if (item != null && item.getMeter() != null) {
				meter = item.getMeter();
			}
			if (tuneItem != null && tuneItem.getMeter() != null && meter.length() <= 0) {
				meter = tuneItem.getMeter();
			}
			if (meter == null || meter.length() <= 0) {
				return;
			}
			
			ServiceLocator.getInstance().getMusicItemBrowser().setSearchFilter(meter);
			
			//ServiceLocator.getInstance().getMusicItemBrowser().refresh();

		}


	}
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		IStructuredSelection s = (IStructuredSelection)arg0.getSelection();
		ServiceElementVO se = (ServiceElementVO)s.getFirstElement();
		this.se = se;
		if (se != null) {
			if (se.getItem() != null && se.getItem().getMeter() != null && se.getItem().getMeter().length() > 0) {				
				this.setEnabled(true);
			} else {
				if (se.getTuneItem() != null && se.getTuneItem().getMeter() != null && se.getTuneItem().getMeter().length() > 0) {
					this.setEnabled(true);	
				} else {
					this.setEnabled(false);
				}
			}
		}
	}
}

