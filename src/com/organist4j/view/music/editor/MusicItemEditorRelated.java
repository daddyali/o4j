package com.organist4j.view.music.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;

import com.organist4j.model.music.MusicItemVO;
import com.organist4j.util.ContentCreator;
import com.organist4j.view.action.music.editor.DeleteItemRelatedAction;
import com.organist4j.view.music.item.MusicItemBrowserSorter;

public class MusicItemEditorRelated implements ContentCreator {
	TableViewer tv;
	MusicItemVO item = null;
	MusicItemEditorRelated(MusicItemVO item) {
		this.item = item;
	}
	@Override
	public Control createContent(Composite parent) {
		Composite compPage = new Composite(parent,SWT.NONE);
		compPage.setLayout(new GridLayout(1,true));

		tv = new TableViewer(compPage, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tv.getTable().setLayout(new GridLayout(1,true));
		tv.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));

		tv.setContentProvider(new MusicItemEditorRelatedContentProvider());
		tv.setLabelProvider(new MusicItemEditorRelatedLabelProvider());
		tv.setSorter(new MusicItemBrowserSorter(tv));


		String[] colProps = new String[MusicItemVO.REFERENCESORDER.length];
		TableColumn tc;

		for (int i = 0;i<MusicItemVO.REFERENCESORDER.length;i++) {
			colProps[i] = "" + MusicItemVO.REFERENCESORDER[i][0];
			tc = new TableColumn(tv.getTable(), SWT.LEFT);
			tc.setText(MusicItemVO.LABELS[MusicItemVO.REFERENCESORDER[i][0]]);
			tc.setWidth(MusicItemVO.REFERENCESORDER[i][1]);
			tc.setData(MusicItemVO.REFERENCESORDER[i][0]);
			tc.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}
				@Override
				public void widgetSelected(SelectionEvent e) {
					TableColumn tc = (TableColumn)e.widget;
					if (tc == tv.getTable().getSortColumn()) {
						if (tv.getTable().getSortDirection() == SWT.UP) {
							tv.getTable().setSortDirection(SWT.DOWN);
						} else {
							tv.getTable().setSortDirection(SWT.UP);
						}
					} else {
						tv.getTable().setSortColumn(tc);
						tv.getTable().setSortDirection(SWT.DOWN);
					}
					tv.refresh();
				}			
			});

		}
		tv.getTable().setSortColumn(tv.getTable().getColumn(1));
		tv.getTable().setSortDirection(SWT.DOWN);
		
		

		tv.setColumnProperties(colProps);

		tv.getTable().setHeaderVisible(true);
		tv.getTable().setLinesVisible(true);

		if (item != null) {
			tv.setInput(item);
		} 
		
		
		MenuManager menu_manager = new MenuManager();
		tv.getTable().setMenu(menu_manager.createContextMenu(tv.getTable()));

	 
		DeleteItemRelatedAction dmia = new DeleteItemRelatedAction(this);
	    
	    menu_manager.add(dmia);
	    tv.addSelectionChangedListener(dmia);

		

		DropTarget dt = new DropTarget(tv.getTable(),DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_DEFAULT);
		dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dt.addDropListener(new MusicItemEditorRelatedDropListener(tv,this));

		return compPage;
	}
	public TableViewer getTv() {
		return tv;
	}
	public void setTv(TableViewer tv) {
		this.tv = tv;
	}
	public MusicItemVO getItem() {
		return item;
	}
	public void setItem(MusicItemVO item) {
		this.item = item;
	}


	private class MusicItemEditorRelatedLabelProvider extends ColumnLabelProvider implements ITableLabelProvider,ITableColorProvider {



		@Override
		public Image getColumnImage(Object arg0, int arg1) {
			 
			return null;
		}

		@Override
		public String getColumnText(Object arg0, int colIndex) {
			MusicItemVO mbe = (MusicItemVO)arg0;
			String txt = "";
			switch (colIndex) {
			case MusicItemVO.BOOK_POS: txt = mbe.getBookName();
			if (mbe.getBookAcronym() != null && mbe.getBookAcronym().length() > 0) {
				txt = mbe.getBookAcronym() + " " + txt;
			}
			break;
			case MusicItemVO.NUMBER_POS: txt = mbe.getNumber();
			break;
			case 2: txt = mbe.getName();
			break;
			//		case MusicItemVO.TUNE_POS: txt = mbe.getTune();
			//		break;
			//		case MusicItemVO.LASTUSED_POS: 
			//			if (mbe.getLastDateTimeUsed() != null) {
			//				txt = new SimpleDateFormat("dd/MM/yyyy").format(mbe.getLastDateTimeUsed());
			//			} else {
			//				txt = "";
			//			}
			//			break;
			//		case MusicItemVO.TUNELASTUSED_POS: 
			//			if (mbe.getTuneLastDateTimeUsed() != null) {
			//				txt = new SimpleDateFormat("dd/MM/yyyy").format(mbe.getTuneLastDateTimeUsed());
			//			} else {
			//				txt = "";
			//			}
			//			break;
			//		case MusicItemVO.PSALM_POS: txt = mbe.getPsalm() != null ? mbe.getPsalm() : "";
			//		break;
			//		case MusicItemVO.METER_POS: txt = mbe.getMeter() != null ? mbe.getMeter() : "";
			//		break;
			//		case MusicItemVO.COMMENTS_POS: txt = mbe.getNotes() != null ? mbe.getNotes() : "";
			//		break;
			//		case MusicItemVO.FAVOURITE_POS: txt = mbe.isFavourite() + "";
			//		break;
			default:

			}
			return txt;
		}

		@Override
		public void addListener(ILabelProviderListener arg0) {
			 

		}

		@Override
		public void dispose() {
			 

		}

		@Override
		public boolean isLabelProperty(Object arg0, String arg1) {
			 
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener arg0) {
			 

		}

		@Override
		public Color getBackground(Object arg0, int arg1) {
			MusicItemVO tableItem = (MusicItemVO)arg0;
			if (item == tableItem) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			} else {
				return super.getBackground(arg0);
			}
		}

		@Override
		public Color getForeground(Object arg0, int arg1) {
			 
			return null;
		}
	}


	public class MusicItemEditorRelatedContentProvider implements IStructuredContentProvider {

		
		@Override
		public Object[] getElements(Object arg0) {
			MusicItemVO item = (MusicItemVO)arg0;
			List<MusicItemVO> list = new ArrayList<MusicItemVO>();
			list.add(item);
			if (item.getRelatedItems() != null) {
				list.addAll(item.getRelatedItems());
			}
			return list.toArray();
		}

		@Override
		public void dispose() {
			 

		}

		@Override
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
			 

		}


	}

}
