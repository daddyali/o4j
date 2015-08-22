package com.organist4j.view.action.service.plan;



import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;

import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.service.plan.ServicePlan;

public class ResetSetTuneAction extends Action implements ISelectionChangedListener {
	ServicePlan plan = null;
	ServiceElementVO se = null;

	public ResetSetTuneAction(ServicePlan plan) {
		//window = w;
		this.plan = plan;
		setText("&Reset Set Tune");
	}
	public void run() {
		if (se != null) {


			MusicItemVO item = se.getItem();
			MusicItemVO tuneItem = se.getTuneItem();
			if (item == null || tuneItem == null) {
				return;
			}

			se.setTuneItem(se.getItem());
			ServiceLocator.getInstance().getServiceDAO().updateService(se.getService());			

			ServiceLocator.getInstance().getMusicDAO().updateMusicItem(item,true);
			ServiceLocator.getInstance().getMusicDAO().updateMusicItem(tuneItem,true);
			ServiceLocator.getInstance().getMusicItemBrowser().refresh();



			plan.refresh();

		}


	}
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		IStructuredSelection s = (IStructuredSelection)arg0.getSelection();
		ServiceElementVO se = (ServiceElementVO)s.getFirstElement();
		this.se = se;
		if (se != null) {
			if (se.getItem() != null &&
					se.getTuneItem() != null &&
					se.getItem() != se.getTuneItem()) {
				this.setEnabled(true);
			} else {
				this.setEnabled(false);
			}
		}
	}
}

