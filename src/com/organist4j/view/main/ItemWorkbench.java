package com.organist4j.view.main;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.organist4j.util.ContentCreator;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.music.item.MusicItemBrowser;
import com.organist4j.view.service.item.ServiceItemBrowser;

public class ItemWorkbench implements ContentCreator {

	ApplicationWindow aw = null;
	
	public ItemWorkbench(ApplicationWindow aw) {
		this.aw = aw;
	}
	
	@Override
	public Control createContent(Composite parent) {
		CTabFolder ct = new CTabFolder(parent, SWT.TOP);
		ct.setSimple(false);
		ct.setBorderVisible(true);

		ct.setLayout(new GridLayout(1,true));
		ct.setLayoutData(new GridData(GridData.FILL_BOTH));

		CTabItem ct1 = new CTabItem(ct,SWT.NONE);
		ct1.setText("Music Item Browser");
		ct.setSelection(0);
		MusicItemBrowser mib = new MusicItemBrowser();
		ct1.setControl(mib.createContent(ct));
		ServiceLocator.getInstance().setMusicItemBrowser(mib);
		
		
		ct1 = new CTabItem(ct,SWT.NONE);
		ct1.setText("Service Section Browser");
		ServiceItemBrowser sib = new ServiceItemBrowser(aw);		
		ct1.setControl(sib.createContent(ct));
		ServiceLocator.getInstance().setServiceItemBrowser(sib);
		

		return ct;
	}

}
