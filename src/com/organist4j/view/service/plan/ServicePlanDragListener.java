package com.organist4j.view.service.plan;

import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.util.O4J;

public class ServicePlanDragListener implements DragSourceListener {

	@Override
	public void dragFinished(DragSourceEvent event) {
		 

	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		DragSource ds = (DragSource)event.widget;
		Table table = (Table)ds.getControl();
		TableItem[] selection = table.getSelection();
		ServiceElementVO se = (ServiceElementVO)selection[0].getData();
		String key = "" + se.getService().getServiceElements().indexOf(se);
		event.data = O4J.DND_SERVICE_PLAN + key;

	}

	@Override
	public void dragStart(DragSourceEvent event) {
		 

	}

}
