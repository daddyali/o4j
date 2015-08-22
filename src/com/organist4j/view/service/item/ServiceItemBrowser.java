package com.organist4j.view.service.item;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.organist4j.dao.ServiceDAO;
import com.organist4j.model.service.ServiceItemVO;
import com.organist4j.util.ContentCreator;
import com.organist4j.util.O4J;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.action.service.item.AddServiceItemAction;
import com.organist4j.view.action.service.item.DeleteServiceItemAction;
import com.organist4j.view.action.service.item.RenameServiceItemAction;

public class ServiceItemBrowser implements ContentCreator {
	 TableViewer tbvItemBrowser;
	 ApplicationWindow aw;
	 
	 
	 public ServiceItemBrowser(ApplicationWindow aw) {
		 this.aw = aw;
	 }
	 
	public TableViewer getTbvItemBrowser() {
		return tbvItemBrowser;
	}

	@Override
	public Control createContent(Composite parent) {
		
//		ct.setSelection(0);

		Composite s = new Composite(parent, SWT.NONE);
		s.setLayout(new GridLayout(1,true));
		s.setLayoutData(new GridData(GridData.FILL_BOTH));

		Text t = new Text(s, SWT.BORDER | SWT.SEARCH);

		t.setTextLimit(100);
		t.setToolTipText("Search all Service Sections");
		t.setMessage("Search");
		GridData gd = new GridData(SWT.LEFT,SWT.TOP,false,false);
		gd.widthHint = 200;
		t.setLayoutData(gd);


		tbvItemBrowser = new TableViewer(s, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI );
		tbvItemBrowser.getTable().setLayout(new GridLayout(1,true));
		tbvItemBrowser.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		tbvItemBrowser.setContentProvider(new ServiceItemBrowserContentProvider());
		tbvItemBrowser.setLabelProvider(new ServiceItemBrowserLabelProvider());
		final ServiceItemBrowserFilter f = new ServiceItemBrowserFilter();
		tbvItemBrowser.setFilters(new ViewerFilter[] {f});
		final ServiceItemBrowserSorter sorter = new ServiceItemBrowserSorter(tbvItemBrowser);
		tbvItemBrowser.setSorter(sorter);

		t.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				Text t = (Text)e.widget;
				f.setSearch(t.getText());
				tbvItemBrowser.refresh();

			}

		});


		String[] colProps = new String[ServiceItemVO.ORDER.length];	
		CellEditor[] editors = new CellEditor[colProps.length];
		TableColumn column;
		for (int i = 0;i<ServiceItemVO.ORDER.length;i++) {
			colProps[i] = "" + ServiceItemVO.ORDER[i][0];
			column = new TableColumn(tbvItemBrowser.getTable(), ServiceItemVO.ORDER[i][3]);
			column.setText(ServiceItemVO.LABELS[ServiceItemVO.ORDER[i][0]]);
			column.setWidth(ServiceItemVO.ORDER[i][1]);
			column.setData(ServiceItemVO.ORDER[i][0]);
			column.setToolTipText(ServiceItemVO.TOOLTIPS[ServiceItemVO.ORDER[i][0]]);
			
			column.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}
				@Override
				public void widgetSelected(SelectionEvent e) {
					TableColumn tc = (TableColumn)e.widget;
					if (tc == tbvItemBrowser.getTable().getSortColumn()) {
						if (tbvItemBrowser.getTable().getSortDirection() == SWT.UP) {
							tbvItemBrowser.getTable().setSortDirection(SWT.DOWN);
						} else {
							tbvItemBrowser.getTable().setSortDirection(SWT.UP);
						}
					} else {
						tbvItemBrowser.getTable().setSortColumn(tc);
						tbvItemBrowser.getTable().setSortDirection(SWT.DOWN);
					}
					tbvItemBrowser.refresh();
				}			
			});
			if ( ServiceItemVO.ORDER[i][2] == O4J.COLEDIT_TEXT ) {
				editors[i] = new TextCellEditor(tbvItemBrowser.getTable());
			}
			if ( ServiceItemVO.ORDER[i][2] == O4J.COLEDIT_BOOL ) {
				editors[i] = new CheckboxCellEditor(tbvItemBrowser.getTable());
			}

		}
		tbvItemBrowser.getTable().setSortColumn(tbvItemBrowser.getTable().getColumn(0));
		tbvItemBrowser.getTable().setSortDirection(SWT.DOWN);
		
		tbvItemBrowser.setColumnProperties(colProps);
		tbvItemBrowser.setCellEditors(editors);
		tbvItemBrowser.setCellModifier(new ServiceItemCellModifier(tbvItemBrowser));
		

		tbvItemBrowser.getTable().setHeaderVisible(true);
		tbvItemBrowser.getTable().setLinesVisible(true);

		ServiceDAO serviceDAO = ServiceLocator.getInstance().getServiceDAO();
		
		tbvItemBrowser.setInput(serviceDAO.getServiceItems());



		DragSource ds = new DragSource(tbvItemBrowser.getTable(),DND.DROP_COPY | DND.DROP_MOVE);
		ds.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		ds.addDragListener(new ServiceItemBrowserDragListener());

	
		MenuManager menu_manager = new MenuManager();
		tbvItemBrowser.getTable().setMenu(menu_manager.createContextMenu(tbvItemBrowser.getTable()));

	    AddServiceItemAction add = new AddServiceItemAction(aw);
	    RenameServiceItemAction ren = new RenameServiceItemAction(aw);
		DeleteServiceItemAction del = new DeleteServiceItemAction(tbvItemBrowser);
	    
	    menu_manager.add(add);
	    //tbvItemBrowser.addSelectionChangedListener(add);
	    menu_manager.add(ren);
	    tbvItemBrowser.addSelectionChangedListener(ren);
	    menu_manager.add(del);
	    tbvItemBrowser.addSelectionChangedListener(del);
		
		return s;
	}

	public void refresh() {
		refresh(false);
		
	}
	
	public void refresh(boolean reload) {
		
		if (reload) {
			ServiceDAO sd = ServiceLocator.getInstance().getServiceDAO();
			tbvItemBrowser.setInput(sd.getServiceItems());
		}
		tbvItemBrowser.refresh();
		
	}

}
