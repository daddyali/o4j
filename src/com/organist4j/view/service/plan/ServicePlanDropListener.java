package com.organist4j.view.service.plan;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.TableItem;

import com.organist4j.dao.MusicDAO;
import com.organist4j.dao.ServiceDAO;
import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.model.service.ServiceItemVO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.O4J;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.music.item.MusicItemBrowser;

public class ServicePlanDropListener implements DropTargetListener {

	private TableViewer tbvService = null;
	private ServicePlan plan = null;

	public ServicePlanDropListener(TableViewer tbvService,ServicePlan plan) {
		this.tbvService  = tbvService;
		this.plan = plan;
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		 
		
	}

	@Override
	public void dragLeave(DropTargetEvent event) {
		 
		
	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {
		 
		
	}

	@Override
	public void dragOver(DropTargetEvent event) {
		
		//For MusicItems
		
			event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
//			event.feedback = DND.FEEDBACK_INSERT_BEFORE | DND.FEEDBACK_SCROLL;
				
		
		
	}

	@Override
	public void drop(DropTargetEvent event) {
		
		if (TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
			
			String data = (String)event.data;
			
			if (data.startsWith(O4J.DND_MUSIC_ITEM_BROWSER)) {
				
				data = data.substring(1);
			
				//Use this key to refine the MusicBookEntry
				MusicDAO musicDAO = ServiceLocator.getInstance().getMusicDAO();
				MusicItemVO mbe = (MusicItemVO)ServiceLocator.getInstance().getMusicItemBrowser().getTbvItemBrowser().getElementAt(Integer.parseInt(data));

				TableItem item = (TableItem)event.item;
				ServiceElementVO se = (ServiceElementVO)item.getData();
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
				
				

				tbvService.setInput(serviceDAO.getService(((ServiceVO)tbvService.getInput())));
				plan.refreshOpenService();
				ServiceLocator.getInstance().getMusicItemBrowser().refresh();
				return;
			} 
			
			if (data.startsWith(O4J.DND_SERVICE_ITEM_BROWSER)) {
				data = data.substring(1);
				//Use this key direct
				
				ServiceDAO serviceDAO = ServiceLocator.getInstance().getServiceDAO();		
				ServiceItemVO si = serviceDAO.getServiceItem(data);
				
				TableItem item = (TableItem)event.item;
				ServiceElementVO se = null;
				if (event.item == null) {
					ServiceVO sv = (ServiceVO)tbvService.getInput();
					
					
					se = new ServiceElementVO(sv,si);
					se.setMandatory(si.getMandatory());
					//se.setCheckForItemWarnings(si.getCheckProximity());
					
					sv.addServiceElement(se);
					
					serviceDAO.updateService(sv);
					
					

				} else {
					se = (ServiceElementVO)item.getData();
					if (!MessageDialog.openConfirm(tbvService.getTable().getShell(), "Service Element Replace", "Are you sure you want to replace the current service element (" + se.getName() + ") with " + data +  "?")) {
						return;
					}
					
					se.setName(data);
					serviceDAO.updateServiceElement(se);
				}
				
				
				
				tbvService.refresh();
				return;
			}
			
			if (data.startsWith(O4J.DND_SERVICE_PLAN)) {
				data = data.substring(1);
				
				TableItem item = (TableItem)event.item;
				ServiceElementVO dropse = (ServiceElementVO)item.getData();	
				
				ServiceVO service = dropse.getService();
				int dropIndex = service.getServiceElements().indexOf(dropse);
				
				ServiceElementVO dragse = service.getServiceElements().get(Integer.parseInt(data));
				
				service.getServiceElements().remove(dragse);
				service.getServiceElements().add(dropIndex, dragse);
				
				ServiceDAO serviceDAO = ServiceLocator.getInstance().getServiceDAO();		

				serviceDAO.updateService(service);
				
				tbvService.setInput(serviceDAO.getService(((ServiceVO)tbvService.getInput())));

				tbvService.refresh();
				return;
			}
		}
		
	}

	@Override
	public void dropAccept(DropTargetEvent event) {
		 
		
	}

}
