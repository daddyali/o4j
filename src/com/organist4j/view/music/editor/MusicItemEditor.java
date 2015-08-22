package com.organist4j.view.music.editor;

import java.util.Date;
import java.util.List;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceElementVO;

public class MusicItemEditor extends ApplicationWindow {
	List<MusicItemVO> musicItems;
	ServiceElementVO clickedServiceElement;
	boolean createNewMode = false;
	
	public boolean isCreateNewMode() {
		return createNewMode;
	}

	public void setCreateNewMode(boolean createNewMode) {
		this.createNewMode = createNewMode;
	}

	public MusicItemEditor(Shell parentShell,List<MusicItemVO> musicItems,ServiceElementVO clickedServiceElement) {
		super(parentShell);
		this.musicItems = musicItems;
		this.clickedServiceElement = clickedServiceElement;
	}
	
	public void run() {

		try {

			this.setShellStyle(SWT.CLOSE);
			setBlockOnOpen(true);


			//getShell().setSize(point);
			//this.addCoolBar(SWT.NONE);

			//this.addStatusLine();

			


			open();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {



		}
	}
	
	
	


	@Override
	protected void configureShell(Shell shell) {
		
		super.configureShell(shell);
	}

	protected Control createContents(Composite parent) {

		
		boolean bulkMode = false;
		if (createNewMode) {
			getShell().setText("Create New Music Item");
		} else {
			if (musicItems.size() > 1) {
				bulkMode = true;
				getShell().setText("Music Item Bulk Change");
			} else {
				getShell().setText("Music Item Editor");
			}
		}
		
		
		
		CTabFolder ct = new CTabFolder(parent, SWT.TOP);
		ct.setSimple(true);
		ct.setBorderVisible(true);

		ct.setLayout(new GridLayout(1,true));
		ct.setLayoutData(new GridData(GridData.FILL_BOTH));

		
		
		CTabItem ct1 = new CTabItem(ct,SWT.NONE);
		ct1.setText("Summary");
		ct.setSelection(0);
		MusicItemEditorSummary mib = new MusicItemEditorSummary(musicItems);
		mib.setCreateNewItem(createNewMode);
		
		
		
		ct1.setControl(mib.createContent(ct));
		Point point = parent.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		this.getShell().setSize(point);
		
		parent.getDisplay().setData("MusicItemEditorSummary",mib);
		
		if (!createNewMode && !bulkMode) {
			ct1 = new CTabItem(ct,SWT.NONE);
			ct1.setText("History");
			Date referenceDate = new Date();
			if (clickedServiceElement != null) {
				referenceDate = clickedServiceElement.getService().getDateTime();
			}
			MusicItemEditorHistory sib = new MusicItemEditorHistory(musicItems.get(0),referenceDate);		
			ct1.setControl(sib.createContent(ct));
			parent.getDisplay().setData("MusicItemEditorHistory",sib);

			ct1 = new CTabItem(ct,SWT.NONE);
			ct1.setText("Related");
			MusicItemEditorRelated rel = new MusicItemEditorRelated(musicItems.get(0));		
			ct1.setControl(rel.createContent(ct));
			parent.getDisplay().setData("MusicItemEditorRelated",rel);
		}
		
		
		

		return ct;
	
	}

}
