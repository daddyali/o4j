package com.organist4j.view.action.report;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.TableItem;

import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.report.util.ReportWriter;
import com.organist4j.util.Node;
import com.organist4j.util.O4J;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.music.item.MusicItemBrowser;
import com.organist4j.view.service.explorer.ServiceExplorer;

public class MusicItemReport extends AbstractReportAction {
	

	Font font = null;
	Font fontb = null;
	Font fontf = null;
	Font fonti = null;
	
	public MusicItemReport(ApplicationWindow w) {
		super(w);

	}


	@Override
	protected String getActionText() {
		 
		return "Music &Item Report...";
	}

	@Override
	protected String getReportName() {
		 
		return "Music Item Report";
	}

	@Override
	protected void printReport(Device device, GC gc, Rectangle trim) {
		Color black = device.getSystemColor(SWT.COLOR_BLACK);
		Color white = device.getSystemColor(SWT.COLOR_WHITE);
		Color red = device.getSystemColor(SWT.COLOR_RED);
		
		Date now = new Date();

		Point dpi = device.getDPI();	
		//This is actually printable area, not trim.
		if (trim == null) {
			trim = device.getClientArea();
		}
		String fontName = "Calibri";
		font = new Font(device, fontName, 12, SWT.NORMAL);
		fontb = new Font(device, fontName, 12, SWT.BOLD);
		fontf = new Font(device, fontName, 8, SWT.NORMAL);
		fonti = new Font(device, fontName, 8, SWT.ITALIC);
		gc.setFont(font);
		gc.setBackground(white);
		gc.setForeground(black);

		
		
		SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");


		ReportWriter rp = new ReportWriter(gc,trim,dpi);
		gc.setFont(fontb);
		rp.println(ServiceLocator.getInstance().getPreferenceStore().getString("church.name") + " - Music Items (as of " + df.format(now) + ")",true);
	

		rp.crlf();
		rp.printcols(new String[] {"Book/Item","Composer","Last Used","Notes"});

		gc.setFont(fontf);
		//rp.crlf();

		String currentBookName = "";
		MusicItemBrowser mib = ServiceLocator.getInstance().getMusicItemBrowser();
		TableItem[] items = mib.getTbvItemBrowser().getTable().getItems();
		int maxItems = 100;
		
		for (int i=0;i< items.length;i++) {
			if (i > maxItems) break;
			MusicItemVO item = (MusicItemVO)items[i].getData();
			
			//Get last usage so we can see where it was used
			ServiceElementVO se = ServiceLocator.getInstance().getMusicDAO().getLastMusicItemUsage(item);
			if (!item.getBookName().equals(currentBookName)) {
				gc.setFont(font);
				rp.println(item.getBookName());
				
				gc.setFont(fontf);
				currentBookName = item.getBookName();
			}
			
			gc.setForeground(black);
			if (item.getLastDateTimeUsed() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(item.getLastDateTimeUsed());
				cal.add(Calendar.WEEK_OF_YEAR, (int)ServiceLocator.getInstance().getPreferenceStore().getLong(O4J.PREF_ITEM_USE_WARN_WKS));
				if (cal.getTime().after(now)) {
					//set red
					gc.setForeground(red);
				} else {
					gc.setForeground(black);
				}
			}
			
			rp.printcols(new String[] {
					item.getNumber() + "\t" + item.getName(),
					item.getComposer() != null ? item.getComposer() : "",
					(item.getLastDateTimeUsed() != null ? df.format(item.getLastDateTimeUsed()) : "") + " " + (se != null && item.getLastDateTimeUsed() != null ? "(" + se.getName() + ")" : ""),
					item.getNotes()});
			gc.setForeground(black);
			rp.hr();
			

			
		}
		
		rp.println("");
		rp.hr();
		
		
		gc.setFont(fontf);
		rp.printfooter("Produced by " + O4J.APPLICATION_NAME + " v" + O4J.APPLICATION_VERSION + " - " + O4J.APPLICATION_WEBSITE_URL);




		//gc.dispose();
		font.dispose();
		fontb.dispose();
		fontf.dispose();
		fonti.dispose();
		
	}

	@Override
	protected String getTitleLabel() {
		 
		return "Title";
	}

	@Override
	protected String getNotesLabel() {
		 
		return "Freeform Notes";
	}

	@Override
	protected boolean isDefaultTitle() {
		 
		return false;
	}

	@Override
	protected void loadData() {
		 
		
	}

}
