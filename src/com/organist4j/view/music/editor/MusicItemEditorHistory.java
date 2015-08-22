package com.organist4j.view.music.editor;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
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
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.util.ContentCreator;
import com.organist4j.util.O4J;
import com.organist4j.util.ServiceLocator;

public class MusicItemEditorHistory implements ContentCreator {
	Date referenceDate = new Date();
	TableViewer tv;
	MusicItemVO item = null;
	MusicItemEditorHistory(MusicItemVO item,Date referenceDate) {
		this.item = item;
		this.referenceDate = referenceDate;
	}
	@Override
	public Control createContent(Composite parent) {
		Composite compPage = new Composite(parent,SWT.NONE);
		compPage.setLayout(new GridLayout(1,true));

		tv = new TableViewer(compPage, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tv.getTable().setLayout(new GridLayout(1,true));
		tv.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		

		tv.setContentProvider(new MusicItemHistoryContentProvider());
		tv.setLabelProvider(new MusicItemHistoryLabelProvider());
		tv.setSorter(new MusicItemEditorHistorySorter(tv));


		String[] colProps = new String[MusicItemVO.HISTORYORDER.length];
		TableColumn tc;
		for (int i = 0;i<MusicItemVO.HISTORYORDER.length;i++) {
			colProps[i] = "" + MusicItemVO.HISTORYORDER[i][0];
			tc = new TableColumn(tv.getTable(), SWT.LEFT);
			tc.setText(MusicItemVO.LABELSHISTORY[MusicItemVO.HISTORYORDER[i][0]]);
			tc.setWidth(MusicItemVO.HISTORYORDER[i][1]);
			tc.setData(MusicItemVO.HISTORYORDER[i][0]);
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
		tv.getTable().setSortDirection(SWT.UP);

		tv.setColumnProperties(colProps);

		tv.getTable().setHeaderVisible(true);
		tv.getTable().setLinesVisible(true);

		if (item != null) {
			tv.setInput(ServiceLocator.getInstance().getMusicDAO().getMusicItemUsage(item));
		}


		return compPage;
	}

	private class MusicItemHistoryContentProvider implements IStructuredContentProvider {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public Object[] getElements(Object arg0) {
			return ((Collection)arg0).toArray();
		}

		@Override
		public void dispose() {
			 

		}

		@Override
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
			 

		}


	}

	private class MusicItemHistoryLabelProvider extends ColumnLabelProvider implements ITableLabelProvider,ITableColorProvider {



		@Override
		public Image getColumnImage(Object arg0, int arg1) {
			 
			return null;
		}

		@Override
		public String getColumnText(Object arg0, int colIndex) {
			ServiceElementVO s = (ServiceElementVO)arg0;
			String txt = "";
			switch (colIndex) {
			case MusicItemVO.SERVICE_POS: txt = s.getService().getName();
			if (s.getService().getType() != null && s.getService().getType().length() > 0) {
				txt += " (" + s.getService().getType() + ")";
			}
			break;

			case MusicItemVO.SERVICE_DATE_POS: 
				if (s.getService().getDateTime() != null) {
					txt = new SimpleDateFormat("dd/MM/yyyy").format(s.getService().getDateTime());
				} else {
					if (s.getService().isTemplate()) {
						txt = "Template";
					}
				}
				break;
			case MusicItemVO.USAGE_POS: 

				String c = "";
				if (s.getItem() != null && s.getItem() == item) {
					c += "Words";
				}
				if (s.getTuneItem() != null && s.getTuneItem() == item) {
					if (c.length() > 0) {
						c += " & Tune";
					} else {
						c += "Tune";
					}
				}
				if (item.getRelatedItems() != null && item.getRelatedItems().contains(s.getItem())) {
					c += s.getItem().getDisplayName() + " Words";
				}
				if (item.getRelatedItems() != null && s.getTuneItem() != null && item.getRelatedItems().contains(s.getTuneItem())) {
					if (c.length() > 0) {
						c += " & Tune";
					} else {
						c += s.getTuneItem().getDisplayName() + " Tune";
					}
				}
				txt += s.getName() + " (" + c + ") ";



				break;

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
//			MusicItemVO tableItem = (MusicItemVO)arg0;
//			if (item == tableItem) {
//				return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
//			} else {
//				return super.getBackground(arg0);
//			}
			return null;
		}

		@Override
		public Color getForeground(Object arg0, int arg1) {
			
			ServiceElementVO se = (ServiceElementVO)arg0;
			if (se.getService().isTemplate()) return null; //for templates
			SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
			if (df.format(referenceDate).equals(df.format(se.getService().getDateTime()))) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
			}
			
			long diffDays = (referenceDate.getTime() - se.getService().getDateTime().getTime() ) / (1000 * 60 * 60 * 24);
			
			long wks = ServiceLocator.getInstance().getPreferenceStore().getLong(O4J.PREF_ITEM_USE_WARN_WKS);
			if (diffDays < (7 * wks)) {
				
				return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
			} else {				
				return null;
			}
		}
	}

}
