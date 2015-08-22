package com.organist4j.view.action.service.plan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;

import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.music.item.MusicItemBrowser;
import com.organist4j.view.service.plan.ServicePlan;

public class ClearMusicItemAction extends Action implements ISelectionChangedListener {
	ServicePlan plan = null;
	List<ServiceElementVO> seList = null;

	public ClearMusicItemAction(ServicePlan plan) {
		//window = w;
		this.plan = plan;
		setText("&Clear Music Item");
	}
	public void run() {
		Iterator<ServiceElementVO> it = seList.iterator();
		while (it.hasNext()) {
			
			ServiceElementVO se = it.next();
			MusicItemVO item = se.getItem();
			MusicItemVO tuneItem = se.getTuneItem();
			se.setItem(null);	
			se.setTuneItem(null);
			se.clearWarnings();
			se.setComments(null);
			ServiceLocator.getInstance().getServiceDAO().updateService(se.getService());			
			
			if (item != null || tuneItem != null) {
				if (item != null) ServiceLocator.getInstance().getMusicDAO().updateMusicItem(item,true);
				if (tuneItem != null) ServiceLocator.getInstance().getMusicDAO().updateMusicItem(tuneItem,true);
				ServiceLocator.getInstance().getMusicItemBrowser().refresh();
				
			}
			
			
			
		}
		
		plan.refresh();
	
		
	}
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		IStructuredSelection s = (IStructuredSelection)arg0.getSelection();
		seList = new ArrayList<ServiceElementVO>();
		
		ServiceElementVO se = null;
		Iterator<ServiceElementVO> it = s.iterator();
		while (it.hasNext()) {
			se = it.next();
			if (se.getItem() != null) {
				seList.add(se);
			}
		}
		
		if (seList != null && seList.size() > 0) {
			if (seList.size() > 1) {
				this.setEnabled(true);
				setText("&Clear Music Items");
			} else {
				this.setEnabled(true);
				setText("&Clear Music Item");
			}
		} else {
			this.setEnabled(false);
		}
	}
}
