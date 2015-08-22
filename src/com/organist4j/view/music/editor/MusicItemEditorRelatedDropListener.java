package com.organist4j.view.music.editor;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;

import com.organist4j.dao.MusicDAO;
import com.organist4j.model.music.MusicItemVO;
import com.organist4j.util.O4J;
import com.organist4j.util.ServiceLocator;

public class MusicItemEditorRelatedDropListener implements DropTargetListener {

	MusicItemEditorRelated rel;
	TableViewer tv;
	public MusicItemEditorRelatedDropListener(TableViewer tv,MusicItemEditorRelated rel) {
		this.rel = rel;
		this.tv = tv;
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
		 
		event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
	}

	@Override
	public void drop(DropTargetEvent event) {
		if (TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
			//DropTarget target = (DropTarget)event.widget;
			//Table table = (Table)target.getControl();
			String data = (String)event.data;
			
			if (data.startsWith(O4J.DND_MUSIC_ITEM_BROWSER)) {
				
				data = data.substring(1);
			
				//Use this key to refine the MusicBookEntry
				//The data passed in is the index of the MusicBrowser
				
				
				MusicDAO musicDAO = ServiceLocator.getInstance().getMusicDAO();
//				MusicItemVO newItem = musicDAO.getMusicItemByPrimaryKey(data);
				MusicItemVO newItem = (MusicItemVO)ServiceLocator.getInstance().getMusicItemBrowser().getTbvItemBrowser().getElementAt(Integer.parseInt(data));
				MusicItemVO item = rel.getItem();
				
				//Add the new item to the main item and vice versa
				item.addRelatedItem(newItem);
				
			
				musicDAO.updateMusicItem(rel.getItem(),true);

				tv.refresh(true);
				
				ServiceLocator.getInstance().getMusicItemBrowser().refresh(true);
				
				return;
			} 
		}
	}

	@Override
	public void dropAccept(DropTargetEvent event) {
		 
		
	}

}
