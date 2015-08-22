package com.organist4j.view.music.item;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.organist4j.dao.MusicDAO;
import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceItemVO;
import com.organist4j.util.ContentCreator;
import com.organist4j.util.O4J;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.action.music.DeleteMusicItemsAction;
import com.organist4j.view.action.music.DeleteRelatedMusicItemsAction;
import com.organist4j.view.action.music.NewMusicItemAction;
import com.organist4j.view.action.music.OpenMusicItemAction;
import com.organist4j.view.action.music.RefreshMusicItemAction;
import com.organist4j.view.action.music.RelateMusicItemsAction;
import com.organist4j.view.action.music.ShowRelatedMusicItemsAction;
import com.organist4j.view.music.editor.MusicItemEditor;

public class MusicItemBrowser implements ContentCreator {

	TableViewer tbvItemBrowser = null;
	MusicItemBrowserFilter musicItemBrowserFilter = null; 
	Text searchBox = null;
	Text msgLabel = null;
	boolean filterByServiceItem = false;
	
	public TableViewer getTbvItemBrowser() {
		return tbvItemBrowser;
	}

	public Control createContent(Composite parent) {


		

		Composite s = new Composite(parent, SWT.NONE);
		s.setLayout(new GridLayout(1,true));
		s.setLayoutData(new GridData(GridData.FILL_BOTH));
		
	
		Composite sb = new Composite(s,SWT.NONE);
		sb.setLayout(new GridLayout(3,true));
		//sb.setLayoutData(new GridData(GridData.FILL_BOTH));
		//sb.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		
		Text t = new Text(sb, SWT.BORDER | SWT.SEARCH | SWT.ICON_CANCEL);

		t.setTextLimit(100);
		t.setToolTipText("Search all Music Items");
		t.setMessage("Search");
		searchBox = t;
		GridData gd = new GridData(SWT.LEFT,SWT.CENTER,true,true);
		gd.widthHint = 300;
		t.setLayoutData(gd);
		
		Button b = new Button(sb,SWT.FLAT);
		b.setToolTipText("Clear Search");
		b.setImage(ServiceLocator.getInstance().getImgRef().get(O4J.IMG_CROSS));
		
		msgLabel = new Text(sb,SWT.NONE);
		msgLabel.setText("");	
		msgLabel.setEditable(false);
		
		gd = new GridData(SWT.LEFT,SWT.CENTER,true,true);
		gd.widthHint = 300;
		msgLabel.setLayoutData(gd);

		

		tbvItemBrowser = new TableViewer(s, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI );
		tbvItemBrowser.getTable().setLayout(new GridLayout(1,true));
		tbvItemBrowser.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		tbvItemBrowser.setContentProvider(new MusicItemBrowserContentProvider());
		tbvItemBrowser.setLabelProvider(new MusicItemBrowserLabelProvider());
		musicItemBrowserFilter = new MusicItemBrowserFilter();
		tbvItemBrowser.setFilters(new ViewerFilter[] {musicItemBrowserFilter});
		tbvItemBrowser.setSorter(new MusicItemBrowserSorter(tbvItemBrowser));

		
		t.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				Text t = (Text)e.widget;
				musicItemBrowserFilter.setSearch(t.getText());
				tbvItemBrowser.refresh();
				

			}

		});
		
		b.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				 
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				 
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				 
				musicItemBrowserFilter.setSearchSet(null);
				searchBox.setText("");
				
			}
			
		});
		
		t.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				 
				Text t = (Text)e.widget;
				
				t.setSelection(0, t.getText().length());
			}

			@Override
			public void mouseDown(MouseEvent e) {
				 
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				 
			
			}
			
		});
		
		String[] colProps = new String[MusicItemVO.ORDER.length];				
		CellEditor[] editors = new CellEditor[colProps.length];
		TableColumn column;
		for (int i = 0;i<MusicItemVO.ORDER.length;i++) {
			colProps[i] = "" + MusicItemVO.ORDER[i][0];
			column = new TableColumn(tbvItemBrowser.getTable(), SWT.LEFT);
			column.setText(MusicItemVO.LABELS[MusicItemVO.ORDER[i][0]]);
			column.setWidth(MusicItemVO.ORDER[i][1]);
			column.setData(MusicItemVO.ORDER[i][0]);
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
			if ( MusicItemVO.ORDER[i][2] == O4J.COLEDIT_TEXT ) {
				editors[i] = new TextCellEditor(tbvItemBrowser.getTable());
			}
			if ( MusicItemVO.ORDER[i][2] == O4J.COLEDIT_BOOL ) {
				editors[i] = new CheckboxCellEditor(tbvItemBrowser.getTable());
			}
		}
		tbvItemBrowser.getTable().setSortColumn(tbvItemBrowser.getTable().getColumn(0));
		tbvItemBrowser.getTable().setSortDirection(SWT.DOWN);
		
		tbvItemBrowser.setColumnProperties(colProps);
		tbvItemBrowser.setCellEditors(editors);
		tbvItemBrowser.setCellModifier(new MusicItemCellModifier(tbvItemBrowser));
		
		tbvItemBrowser.getTable().setHeaderVisible(true);
		tbvItemBrowser.getTable().setLinesVisible(true);

		MusicDAO musicDAO = ServiceLocator.getInstance().getMusicDAO();
		tbvItemBrowser.setInput(musicDAO.getMusicItems());

		tbvItemBrowser.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent arg0) {
				
				MusicItemBrowser tv = (MusicItemBrowser)ServiceLocator.getInstance().getMusicItemBrowser();
				IStructuredSelection sel = (IStructuredSelection) tv.getTbvItemBrowser().getSelection();
				if (sel.size() <= 0) {
					return;
				}
				
				
				MusicItemEditor editor = new MusicItemEditor(tbvItemBrowser.getTable().getShell(),sel.toList(),null);
				editor.run();
				
			}
			
		});

		DragSource ds = new DragSource(tbvItemBrowser.getTable(),DND.DROP_COPY | DND.DROP_MOVE);
		ds.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		ds.addDragListener(new MusicItemBrowserDragListener());

		
		MenuManager menu_manager = new MenuManager();
		tbvItemBrowser.getTable().setMenu(menu_manager.createContextMenu(tbvItemBrowser.getTable()));

	    OpenMusicItemAction tsem = new OpenMusicItemAction(tbvItemBrowser);
	    NewMusicItemAction nmi = new NewMusicItemAction(tbvItemBrowser);
		DeleteMusicItemsAction dmia = new DeleteMusicItemsAction(tbvItemBrowser);
		RefreshMusicItemAction rmia = new RefreshMusicItemAction(tbvItemBrowser);
		RelateMusicItemsAction rel = new RelateMusicItemsAction(tbvItemBrowser);
		DeleteRelatedMusicItemsAction delrel = new DeleteRelatedMusicItemsAction(tbvItemBrowser);
		ShowRelatedMusicItemsAction showrel = new ShowRelatedMusicItemsAction(tbvItemBrowser);
	    
	    menu_manager.add(tsem);
	    tbvItemBrowser.addSelectionChangedListener(tsem);
	    menu_manager.add(nmi);
	    tbvItemBrowser.addSelectionChangedListener(nmi);
	    
	    menu_manager.add(new Separator());
	    
	    menu_manager.add(dmia);
	    tbvItemBrowser.addSelectionChangedListener(dmia);
		
	    menu_manager.add(new Separator());
	    
	    menu_manager.add(rmia);
	    tbvItemBrowser.addSelectionChangedListener(rmia);
	    
	    menu_manager.add(new Separator());
	    
	    menu_manager.add(rel);
	    tbvItemBrowser.addSelectionChangedListener(rel);
	    menu_manager.add(delrel);
	    tbvItemBrowser.addSelectionChangedListener(delrel);
	    menu_manager.add(showrel);
	    tbvItemBrowser.addSelectionChangedListener(showrel);
		
		
		return s;
	}

	public void refresh() {
		refresh(false);
		
	}
	
	public void refresh(boolean reload) {
		
		if (reload) {
			MusicDAO musicDAO = ServiceLocator.getInstance().getMusicDAO();
			tbvItemBrowser.setInput(musicDAO.getMusicItems());
		}
		
		tbvItemBrowser.refresh();
		
	}
	
	public void setSearchFilter(String filter) {
		searchBox.setText(filter);

	}

	public void setSearchFilterByServiceItem(ServiceItemVO serviceItem) {
		if (serviceItem != null) {
			setMessage("   * Showing used for " + serviceItem.getName());
		} else {
			setMessage("");
		}
		
		Collection searchSet = ServiceLocator.getInstance().getMusicDAO().getMusicItemsForServiceItem(serviceItem);
		musicItemBrowserFilter.setSearchSet(searchSet);
	}

	public boolean isFilterByServiceItem() {
		return filterByServiceItem;
	}

	public void setFilterByServiceItem(boolean filterByServiceItem) {
		this.filterByServiceItem = filterByServiceItem;
	}
	public void setMessage(String msg) {
		System.out.println("Setting label to " + msg);
		msgLabel.setText(msg);
		
	}

	//Filter by music item to show related items
	public void setFilterByMusicItem(MusicItemVO item) {
		Collection searchSet = new HashSet();
		searchSet.add(item);
		if (item.getRelatedItems() != null) {
			searchSet.addAll(item.getRelatedItems());
		}
		musicItemBrowserFilter.setSearchSet(searchSet);
		
	}
	
	


	
}
