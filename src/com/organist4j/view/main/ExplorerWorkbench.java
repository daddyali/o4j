package com.organist4j.view.main;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.organist4j.util.ContentCreator;
import com.organist4j.view.service.explorer.ServiceExplorer;

public class ExplorerWorkbench implements ContentCreator {
	ServiceExplorer se = null;
	CTabFolder ct = null;
	//CTabItem ct1 = null;
	
	ApplicationWindow aw = null;
	public ExplorerWorkbench(ApplicationWindow aw) {
		this.aw = aw;
	}

	@Override
	public Control createContent(Composite parent) {
		ct = new CTabFolder(parent, SWT.TOP);
		ct.setSimple(false);
		ct.setBorderVisible(true);
		
		ct.setLayout(new GridLayout(1,true));
		ct.setLayoutData(new GridData(GridData.FILL_BOTH));
		ct.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_CYAN));
		createServiceExplorer();
		
		return ct;
		
	}
	
	public void createServiceExplorer() {
		
		CTabItem ct1 = new CTabItem(ct,SWT.NONE);
		ct1.setText("Service Explorer");
		//Select this tab
		ct.setSelection(0);
		
		se = new ServiceExplorer(aw);

		ct1.setControl(se.createTabContent(ct));
		
	}

}
