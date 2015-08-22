package com.organist4j.view.action.service.plan;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import com.organist4j.dao.MusicDAO;
import com.organist4j.dao.ServiceDAO;
import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.service.plan.ServicePlan;

public class ApplyMusicItemAction extends Action implements ISelectionChangedListener {
	ServicePlan plan = null;
	IStructuredSelection sel = null;

	public ApplyMusicItemAction(ServicePlan plan) {
		this.plan = plan;
		//window = w;
		setText("&Set Selected Music Item");
	}
	@SuppressWarnings("unchecked")
	public void run() {
		if (sel == null) return;
		
		
		
		String title = "";
		String msg = "";
		if (sel.size() > 1) {
			title = "Set selected Music Item";
			msg = "Are you sure you want to delete the selected service elements? ";
		} else {
			ServiceElementVO se = (ServiceElementVO)sel.getFirstElement();
			title = "Set selected Music Item";
			msg = "Are you sure you want to delete the service element " + se.getName() + "? ";
			if (se.getItem() != null) {
				msg += "This is currently assigned to Music Item " + se.getItem().getDisplayName() + ".";
			}
		}
		
		//Just do it for now
//		if (!MessageDialog.openConfirm(plan.getTv().getTable().getShell(), title, msg)) {
//			return;
//		}
		
		Iterator<ServiceElementVO> it = sel.iterator();
		while (it.hasNext()) {
			ServiceElementVO se = (ServiceElementVO)it.next();
			//Use this key to refine the MusicBookEntry
			MusicDAO musicDAO = ServiceLocator.getInstance().getMusicDAO();
			int selectionIdx = ServiceLocator.getInstance().getMusicItemBrowser().getTbvItemBrowser().getTable().getSelectionIndex();
			MusicItemVO mbe = (MusicItemVO)ServiceLocator.getInstance().getMusicItemBrowser().getTbvItemBrowser().getElementAt(selectionIdx);

			
			
			ServiceDAO serviceDAO = ServiceLocator.getInstance().getServiceDAO();		

			MusicItemVO existingMusicItem = se.getItem();
			MusicItemVO existingTuneMusicItem = se.getTuneItem();
			if (existingMusicItem != null) {
				//If there is already a music item (i.e words), set this to be a tune item only
				se.setTuneItem(mbe);					
			} else {
				//If there is no music item set, this is both words and tune
				se.setItem(mbe);
				se.setTuneItem(mbe);	
			}
			
			
			
			
			serviceDAO.updateServiceElement(se);
			musicDAO.updateMusicItem(mbe,true);
			//Do an update of any existing music item too to ensure the last used is reset
			if (existingMusicItem != null) {
				musicDAO.updateMusicItem(existingMusicItem,true);
			}
			if (existingTuneMusicItem != null) {
				musicDAO.updateMusicItem(existingTuneMusicItem,true);
			}
			
			

			//tbvService.setInput(serviceDAO.getService(((ServiceVO)tbvService.getInput())));
			
			
		}
		plan.refresh();
		ServiceLocator.getInstance().getMusicItemBrowser().refresh();
		
		
	}
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		sel = (IStructuredSelection)arg0.getSelection();
		
//		this.se = se;
	}
}
