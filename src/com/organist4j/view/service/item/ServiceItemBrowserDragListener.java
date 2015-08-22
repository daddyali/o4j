package com.organist4j.view.service.item;

import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.organist4j.model.service.ServiceItemVO;
import com.organist4j.util.O4J;

public class ServiceItemBrowserDragListener implements DragSourceListener {

	@Override
	public void dragFinished(DragSourceEvent event) {
		 

	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		DragSource ds = (DragSource)event.widget;
		Table table = (Table)ds.getControl();
		TableItem[] selection = table.getSelection();
		ServiceItemVO mbe = (ServiceItemVO)selection[0].getData();
		event.data = O4J.DND_SERVICE_ITEM_BROWSER + mbe.getName();
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		 

	}

}
