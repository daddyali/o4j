package com.organist4j.view.service.plan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.organist4j.dao.ServiceDAO;
import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.O4J;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.action.service.plan.ApplyMusicItemAction;
import com.organist4j.view.action.service.plan.ClearMusicItemAction;
import com.organist4j.view.action.service.plan.ClearWarningsAction;
import com.organist4j.view.action.service.plan.RemoveServiceElementAction;
import com.organist4j.view.action.service.plan.ResetSetTuneAction;
import com.organist4j.view.action.service.plan.ShowAlternateTunesAction;
import com.organist4j.view.action.service.plan.ShowMusicItemsForServiceItemAction;
import com.organist4j.view.action.service.plan.ToggleServiceElementCheckUsageAction;
import com.organist4j.view.action.service.plan.ToggleServiceElementMandatoryAction;
import com.organist4j.view.music.editor.MusicItemEditor;

public class ServicePlan implements DisposeListener {
	Logger logger = Logger.getLogger(ServicePlan.class);

	ApplicationWindow aw = null;
	ServiceVO service = null;
	CTabFolder ct = null;	
	CTabItem ct1 = null;
	Font font;
	ProgressBar bar = null;
	TableViewer tv = null;
	Label l = null;
	public TableViewer getTv() {
		return tv;
	}

	public ServicePlan(CTabFolder ct,ApplicationWindow aw,ServiceVO service) {
		this.aw = aw;
		this.ct = ct;
		this.service = service;
	}
	
	public void showService() {

		//Check it's not already an open tab
		CTabItem[] items = ct.getItems();
		for (int i=0;i<items.length;i++) {
			if (items[i].getData().equals(service)) {
				//already open
				ct.setSelection(items[i]);
				return;
			}

		}



		ct1 = new CTabItem(ct,SWT.NONE);
		
		ct1.setShowClose(true);
		if (service.getDateTime() != null) {
			ct1.setToolTipText(new SimpleDateFormat("EEE dd-MMM-yyyy HHmm").format(service.getDateTime()));
		}
		ct1.setData(service);
		ct1.addDisposeListener(this);

		ct.setSelection(ct1);
		
		ct.setSelectionBackground(service.getLectionary().getSeasonBackgroundColor(service));
		ct.setSelectionForeground(service.getLectionary().getSeasonForegroundColor(service));
		
//		ct.setSelectionBackground(new Color[]{service.getColour(),
//				service.getColour(),
//				Display.getCurrent().getSystemColor(SWT.COLOR_WHITE),
//				Display.getCurrent().getSystemColor(SWT.COLOR_WHITE)},
//				new int[] {25, 50, 100});
		ct.setBorderVisible(true);
		

		//Right hand side
		Composite cc = new Composite(ct,SWT.NONE);
		cc.setLayout(new GridLayout(1,false));
		cc.setLayoutData(new GridData(GridData.FILL_BOTH));
		cc.setBackground(service.getLectionary().getSeasonBackgroundColor(service));

		Composite hc = new Composite(cc,SWT.NONE);
		hc.setBackground(service.getLectionary().getSeasonBackgroundColor(service));

		GridLayout hcg = new GridLayout(3,false);
		hcg.marginHeight = 0;
		hcg.marginWidth = 0;
		hc.setLayout(hcg);
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		
		
		
		hc.setLayoutData(gd);
		font = new Font(Display.getCurrent(), "Calibri", 16, SWT.BOLD);
		
		Canvas img = new Canvas(hc,SWT.NONE);
		img.setBackground(service.getLectionary().getSeasonBackgroundColor(service));
		final Image image = service.getLectionary().getImage(service);				
		img.setLayoutData(new GridData(100,65));
		//img.setSize(60, 60);
		img.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				if (image == null) return;
				Rectangle rect = image.getBounds ();
				double width = rect.width;
				double height = rect.height;
				
				GC gc = e.gc;
				int x = 0, y = 0;
				double targetHeight = 65;
				double scalingFactor = targetHeight / height;
				double targetWidth = width * scalingFactor;
				
				gc.drawImage (image, 0, 0, (int)width, (int)height, x, y, (int)Math.round(targetWidth), (int)Math.round(targetHeight));
				
			}

		
		
		});
		
		l = new Label(hc,SWT.NONE);		
		refreshTitle();
		l.setFont(font);
		l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Text t = new Text(hc,SWT.MULTI | SWT.BORDER);
		t.setMessage("Comments");
		if (service.getDesc() != null) {
			t.setText(service.getDesc());
		}
		t.setData(service);
		t.setData("dirty",false);
		t.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				e.widget.setData("dirty",true);
				ServiceVO s = (ServiceVO)e.widget.getData();
				if (s != null) {
					
					s.setDesc(  ((Text)e.widget).getText() );					
				}
			}
			
		});
		
		t.addDisposeListener(new DisposeListener() {

		

			@Override
			public void widgetDisposed(DisposeEvent e) {
				if ((Boolean)e.widget.getData("dirty")) {
					ServiceVO s = (ServiceVO)e.widget.getData();
					if (s != null) {
						//logger.debug("Saved comments for " + s.toString());
						s.setDesc(  ((Text)e.widget).getText() );
						ServiceLocator.getInstance().getServiceDAO().updateService(s);
					}
				}
			}
			
		});
		gd = new GridData();
		gd.heightHint = 50;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		t.setLayoutData(gd);
		
		bar = new ProgressBar(cc,SWT.HORIZONTAL );
		bar.setMaximum(100);
		bar.setSelection((int)service.getPlanningPct());
//		bar.setState(SWT.ERROR);
		bar.setState(SWT.PAUSED);
		if (bar.getMaximum() == bar.getSelection()) {
			bar.setState(SWT.NORMAL);
		}
		gd = new GridData();
		
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		bar.setLayoutData(gd);
		
		
		//font = new Font(Display.getCurrent(), "Calibri", 16, SWT.BOLD);
		//name.setFont(font);
		//name.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_CYAN));




		//TableViewer tbvService = new TableViewer(ct, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		//ct1.setControl(tbvService.getControl());
		 tv = new TableViewer(cc, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		
		tv.getTable().setLayout(new GridLayout(1,true));
		tv.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		tv.getTable().setHeaderVisible(true);
		tv.getTable().setLinesVisible(true);
		
		//tv.getTable().setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));

		ct1.setControl(cc);

		tv.setContentProvider(new ServicePlanContentProvider());
		tv.setLabelProvider(new ServicePlanLabelProvider());
		
		
		//ColumnViewerToolTipSupport.enableFor(tbvService,ToolTip.NO_RECREATE);
		
		String[] colProps = new String[ServiceElementVO.ORDER.length];				
		CellEditor[] editors = new CellEditor[colProps.length];
		TableColumn column;
		for (int i = 0;i<ServiceElementVO.ORDER.length;i++) {
			colProps[i] = "" + ServiceElementVO.ORDER[i][0];
			column = new TableColumn(tv.getTable(), SWT.LEFT);
			
			
			column.setText(ServiceElementVO.LABELS[ServiceElementVO.ORDER[i][0]]);
			column.setWidth(ServiceElementVO.ORDER[i][1]);
			column.setData(ServiceElementVO.ORDER[i][0]);
			column.setMoveable(true);
			column.setResizable(true);
			
			
			
			if ( ServiceElementVO.ORDER[i][2] == O4J.COLEDIT_TEXT ) {
				editors[i] = new TextCellEditor(tv.getTable());
			}
			if ( ServiceElementVO.ORDER[i][2] == O4J.COLEDIT_BOOL ) {
				editors[i] = new CheckboxCellEditor(tv.getTable());
			}
		}


		tv.setCellEditors(editors);
		tv.setCellModifier(new ServicePlanCellModifier(tv));

		

//		TableColumn[] tcs = tbvService.getTable().getColumns();
//		String[] colProps = new String[tcs.length];
//		for (int i=0;i<tcs.length;i++) {
//			colProps[i] = tcs[i].getText();
//		}
		tv.setColumnProperties(colProps);

		tv.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				IStructuredSelection sesel = (IStructuredSelection)arg0.getSelection();
				ServiceElementVO se = (ServiceElementVO)sesel.getFirstElement();
				if (se == null) return;
				if (se.getServiceItem() == null) return;
				if (ServiceLocator.getInstance().getMusicItemBrowser().isFilterByServiceItem()) {
					ServiceLocator.getInstance().getMusicItemBrowser().setSearchFilterByServiceItem(se.getServiceItem());
					ServiceLocator.getInstance().getMusicItemBrowser().refresh();
				}
			}
			
		});
		
		tv.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent arg0) {
				
				
				IStructuredSelection sesel = (IStructuredSelection)arg0.getSelection();
				ServiceElementVO se = (ServiceElementVO)sesel.getFirstElement();
				
				if (se.getItem() == null) return;
				
				List<MusicItemVO> l = new ArrayList<MusicItemVO>();
				l.add(se.getItem());
								
				MusicItemEditor editor = new MusicItemEditor(tv.getTable().getShell(),l,se);
				editor.run();
				
			}
			
		});
		
		

		ServiceDAO serviceDAO = ServiceLocator.getInstance().getServiceDAO();		

		tv.setInput(serviceDAO.getService(service));

		

		MenuManager menu_manager = new MenuManager();
		tv.getTable().setMenu(menu_manager.createContextMenu(tv.getTable()));

		ClearWarningsAction cwa = new ClearWarningsAction(this);
		
		ToggleServiceElementMandatoryAction tsem = new ToggleServiceElementMandatoryAction(this);
		ResetSetTuneAction rst = new ResetSetTuneAction(this);
		ClearMusicItemAction cmi = new ClearMusicItemAction(this);
		ApplyMusicItemAction ami = new ApplyMusicItemAction(this);
		RemoveServiceElementAction rse = new RemoveServiceElementAction(this);
		//ToggleServiceElementCheckUsageAction cu = new ToggleServiceElementCheckUsageAction(this);
		ShowAlternateTunesAction sat = new ShowAlternateTunesAction(this);
		ShowMusicItemsForServiceItemAction smisi = new ShowMusicItemsForServiceItemAction(this);
		
		
		menu_manager.add(cwa);
		tv.addSelectionChangedListener(cwa);
		menu_manager.add(tsem);
		tv.addSelectionChangedListener(tsem);
//		menu_manager.add(cu);
//		tv.addSelectionChangedListener(cu);
		menu_manager.add(sat);
		tv.addSelectionChangedListener(sat);
		menu_manager.add(smisi);
		tv.addSelectionChangedListener(smisi);
		
		menu_manager.add(new Separator());
		
		menu_manager.add(ami);
		tv.addSelectionChangedListener(ami);
		menu_manager.add(rst);
		tv.addSelectionChangedListener(rst);
		menu_manager.add(cmi);
		tv.addSelectionChangedListener(cmi);
		
		
		menu_manager.add(new Separator());
		
		menu_manager.add(rse);
		tv.addSelectionChangedListener(rse);
		
		
		


		DropTarget dt = new DropTarget(tv.getTable(),DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_DEFAULT);
		dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dt.addDropListener(new ServicePlanDropListener(tv,this));

		DragSource ds = new DragSource(tv.getTable(),DND.DROP_COPY | DND.DROP_MOVE);
		ds.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		ds.addDragListener(new ServicePlanDragListener());



	}
	
	private void refreshTitle() {
		String title = service.getName();
		if (service.getType() != null) {
			title += " (" + service.getType() + ") ";
		}
		ct1.setText(title);
		title += "\n";
		if (service.isTemplate()) {
			title += "Template";
		} else {
			title += new SimpleDateFormat("EEEE d MMMM yyyy, HHmm").format(service.getDateTime());
		}
		l.setBackground(service.getLectionary().getSeasonBackgroundColor(service));
		l.setForeground(service.getLectionary().getSeasonForegroundColor(service));
		l.setText(title);
		
	}

	@Override
	public void widgetDisposed(DisposeEvent e) {
		//Look for tab closures
		CTabItem i = (CTabItem)e.widget;

		ServiceVO s = (ServiceVO)i.getData();
		
		ServiceLocator.getInstance().getServiceDAO().updateService(s);
		
		i.getControl().dispose();
		logger.debug("Closed Service " + s.toString());

		try { font.dispose(); } catch (Exception ig) {}
	}

	public void closeService() {
		
		
		
		CTabItem[] items = ct.getItems();

		for (int i=0;i<items.length;i++) {
			if (items[i].getData().equals(service)) {
				//already open
				items[i].dispose();
				ct.setSelection(0);
				return;
			}

		}


	}
	
	public void refreshOpenService() {
		CTabItem ct1 = null;
		CTabItem[] items = ct.getItems();
		for (int i=0;i<items.length;i++) {
			if (items[i].getData().equals(service)) {
				//already open
				ct1 = items[i];
				break;
			}

		}
		if (ct1 != null) {
			//bar.setSelection((int)service.getPlanningPct());
			refresh();
			if (service.getDateTime() != null) {
				ct1.setToolTipText(new SimpleDateFormat("EEE dd-MMM-yyyy HHmm").format(service.getDateTime()));
			}
			refreshTitle();
		}
	}

	public void refresh() {
		bar.setSelection((int)service.getPlanningPct());		
		if (bar.getMaximum() == bar.getSelection()) {			
			bar.setState(SWT.NORMAL);
		} else {
			bar.setState(SWT.PAUSED);
		}
		ServiceLocator.getInstance().getServiceExplorer().refresh();
		tv.refresh();
	}
	
	
	
	
}
