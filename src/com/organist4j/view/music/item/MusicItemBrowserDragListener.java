package com.organist4j.view.music.item;

import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.widgets.Table;

import com.organist4j.util.O4J;

public class MusicItemBrowserDragListener implements DragSourceListener {

	@Override
	public void dragFinished(DragSourceEvent event) {
		 
		
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		DragSource ds = (DragSource)event.widget;
		Table table = (Table)ds.getControl();
		event.data = O4J.DND_MUSIC_ITEM_BROWSER  + table.getSelectionIndex();
		
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		 
		
	}

}
