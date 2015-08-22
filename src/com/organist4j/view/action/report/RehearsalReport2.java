package com.organist4j.view.action.report;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.organist4j.dao.ServiceDAO;
import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.report.util.ReportWriter;
import com.organist4j.util.O4J;
import com.organist4j.util.ServiceLocator;

public class RehearsalReport2 extends AbstractReportAction {


	Font font = null;
	Font fontb = null;
	Font fontf = null;
	Font fonti = null;
	Set<RehearsalItem> rhItems = null;

	public RehearsalReport2(ApplicationWindow w) {
		super(w);

	}

	@Override
	protected String getActionText() {
		 
		return "Rehearsal Report...";
	}

	@Override
	protected String getReportName() {
		return "Rehearsal Report";
	}
	@Override
	protected void printReport(Device device,GC gc,Rectangle trim) {
		
		Color black = device.getSystemColor(SWT.COLOR_BLACK);
		Color white = device.getSystemColor(SWT.COLOR_WHITE);
		Color blue = device.getSystemColor(SWT.COLOR_BLUE);
		Color red = device.getSystemColor(SWT.COLOR_RED);
		Color yellow = device.getSystemColor(SWT.COLOR_YELLOW);
		Color cyan = device.getSystemColor(SWT.COLOR_CYAN);
		
		Image image = ServiceLocator.getInstance().getImgRef().get(O4J.IMG_TICK);


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


		

	



		SimpleDateFormat df = new SimpleDateFormat("EEE dd MMMM yyyy");

		ReportWriter rp = new ReportWriter(gc,trim,dpi);
		gc.setFont(fontb);
		rp.println(ServiceLocator.getInstance().getPreferenceStore().getString("church.name") + " - REHEARSAL PLAN",true);
		gc.setFont(font);	
		rp.println("Effective " + new SimpleDateFormat("EEEE dd MMMM yyyy, HHmm").format(new Date()) );
		rp.drawImage(image);
		//		if (service.getType() != null)
		//			rp.println(service.getType());
		//		if (service.getDateTime() != null)
		//			rp.println(new SimpleDateFormat("EEEE dd MMMM yyyy, HHmm").format(service.getDateTime()));

		rp.crlf();
		//rp.printcols(new String[] {"Section","Item/Source","Title/Tune"});

		/**
		 * Music Items
		 */
		gc.setFont(fontb);	
		gc.setForeground(red);
		rp.printcols224(new String[] {"Due/Position","Item/Source","Item Details"},false);
		gc.setFont(font);	
		gc.setForeground(blue);
		rp.println("SPECIFIC ITEMS");
		
		gc.setForeground(black);
		
				
		Iterator<RehearsalItem> ab = rhItems.iterator();
		while (ab.hasNext()) {
			RehearsalItem rh = ab.next();	
			if (rh.instances > 1) continue;
			if (rh.rehearsalCategory != RehearsalItem.GENERAL) continue;

				
			printRehearsalItem(device,gc,rp,rh);
		}			
		
		rp.hr();

		/**
		 * Hymns
		 */
		gc.setForeground(blue);
		rp.println("HYMNS");
		
		gc.setForeground(black);
		gc.setFont(font);	
		
		ab = rhItems.iterator();
		while (ab.hasNext()) {
			RehearsalItem rh = ab.next();	
			if (rh.instances > 1) continue;
			if (rh.rehearsalCategory != RehearsalItem.HYMN) continue;
			
			
						
			printRehearsalItem(device,gc,rp,rh);
		}			
		
		rp.hr();

		/**
		 * Regular Items
		 */
		gc.setForeground(blue);
		rp.println("RECURRENT ITEMS");
		gc.setForeground(black);
		gc.setFont(font);
		
		ab = rhItems.iterator();
		while (ab.hasNext()) {
			RehearsalItem rh = ab.next();	
			if (rh.instances <= 1) continue;
			
			printRehearsalItem(device,gc,rp,rh);
		}			
		
		rp.hr();




		gc.setFont(fontf);
		rp.printfooter("Produced by " + O4J.APPLICATION_NAME + " v" + O4J.APPLICATION_VERSION + " - " + O4J.APPLICATION_WEBSITE_URL);




		//gc.dispose();
		font.dispose();
		fontb.dispose();
		fontf.dispose();
		fonti.dispose();
	}

	@SuppressWarnings("rawtypes")
	private class RehearsalItem implements Comparable {

		public static final int GENERAL = 0;
		public static final int HYMN = 1;

		public ServiceElementVO se;
		public int rehearsalCategory = GENERAL;
		public Date startDate;
		public Date firstDueDate;
		public int instances = 0;
		
		@Override
		public int compareTo(Object that) {
			 
			return this.toString().compareTo(that.toString());
		}
		public String toString() {
			return se.getItem().toString();
		}
	}


	private void printRehearsalItem(Device device,GC gc,ReportWriter rp,RehearsalItem ri) {
		
		Color black = device.getSystemColor(SWT.COLOR_BLACK);
		Color white = device.getSystemColor(SWT.COLOR_WHITE);
		Color blue = device.getSystemColor(SWT.COLOR_BLUE);
		Color red = device.getSystemColor(SWT.COLOR_RED);
		Color yellow = device.getSystemColor(SWT.COLOR_YELLOW);
		Color cyan = device.getSystemColor(SWT.COLOR_CYAN);
		Color grey = device.getSystemColor(SWT.COLOR_DARK_GRAY);
		
//		int lookAheadWeeksForBigItemsOnly = 1;
//		int maxRehLookAheadWeeks = 12;
//		int largeRehearsalsTrigger = 4;
		
		int lookAheadWeeksForBigItemsOnly = ServiceLocator.getInstance().getPreferenceStore().getInt(O4J.PREF_LOOK_AHEAD_WEEKS_BIG_ITEMS);
		int maxRehLookAheadWeeks = ServiceLocator.getInstance().getPreferenceStore().getInt(O4J.MAX_REH_LOOK_AHEAD_WEEKS);
		int largeRehearsalsTrigger = ServiceLocator.getInstance().getPreferenceStore().getInt(O4J.PREF_LARGE_REH_TRIGGER);
		
		
		//Ignore if rehearsals needed is zero
		if (ri.se.getItem().getRehearsalsNeededAsInt() <= 0) {
			return;
		}
		

		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_YEAR, maxRehLookAheadWeeks);
		
		if (ri.startDate.after(cal.getTime())) {
			return;
		}	
		cal.setTime(new Date());
		cal.add(Calendar.WEEK_OF_YEAR, lookAheadWeeksForBigItemsOnly);
		if (ri.startDate.after(cal.getTime()) && ri.se.getItem().getRehearsalsNeededAsInt() < largeRehearsalsTrigger) {
			return;
		}
		
		rp.hr();
		
		if (ri.startDate.before(new Date())) {
			rp.setFont(fontb);
		} else {
			if (ri.startDate.after(cal.getTime()) && ri.se.getItem().getRehearsalsNeededAsInt() >= largeRehearsalsTrigger) {
				gc.setForeground(grey);
			} else {
				rp.setFont(font);
			}
		}
		ServiceElementVO se = ri.se;

		SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
		
		//if (se.getItem() != null) {

		String firstDueDate = df.format(ri.firstDueDate);
		if (ri.instances > 1) {
			firstDueDate += " (" + ri.instances + ")";
		}
		rp.printcols224(new String[] {firstDueDate,se.getItem().getDisplayName(),se.getItem().getName()},false);
		
		rp.setFont(fonti);
		String tune = "";
		if (se.getTuneItem() != null) {
			tune = se.getTuneItem().getDisplayName();
			//+ " " + se.getTuneItem().getTune() == null ? "" : se.getTuneItem().getTune();
		} else {
			tune = se.getItem().getTune() == null ? "" : se.getItem().getTune(); 
		}
		if (tune != null && tune.equalsIgnoreCase(se.getItem().getDisplayName())) {
			tune = "";
		}
		rp.printcols224(new String[] {se.getName() + " (" + se.getService().getName() + ")",se.getItem().getBookName(),tune },false);
		rp.setFont(font);

		
		if (se.getComments() != null && se.getComments().length() > 0) {
			rp.setFont(fonti);
			rp.println(se.getComments());
		}
		if (se.getItem() != null && se.getItem().getNotes() != null && se.getItem().getNotes().length() > 0) {
			rp.setFont(fonti);
			rp.println(se.getItem().getNotes());
		}
		if (se.getTuneItem() != null && se.getTuneItem().getNotes() != null && se.getTuneItem().getNotes().length() > 0) {				
			if (se.getItem() != null && se.getItem() == se.getTuneItem()) {
				//do nothing
			} else {
				rp.setFont(fonti);
				rp.println(se.getTuneItem().getNotes());
			}
		}

		rp.setFont(font);
		gc.setForeground(black);

	}

	@Override
	protected void loadData() {
		ServiceDAO sd = ServiceLocator.getInstance().getServiceDAO();
		List<ServiceVO> services = sd.getFutureServices();
		//Now build a list of Music Items and their service dates


		/**
		 * 			RehearsalItem
		 * 			public MusicItemVO musicItem;
			public List<ServiceItemVO> = new ArrayList<ServiceItemVO>();
			public Date startDate;
			public Date firstDueDate;
			public int instances = 0;
		 */



		//			ServiceExplorer se = ServiceLocator.getInstance().getServiceExplorer();
		//			IStructuredSelection sel  = (IStructuredSelection)se.getTreeViewer().getSelection();
		//			if (sel.isEmpty()) {
		//				return;
		//			} 

		Map<String,RehearsalItem> map = new HashMap<String,RehearsalItem>();

		rhItems = new TreeSet<RehearsalItem>();
		


		Iterator<ServiceVO> it = services.iterator();
		ServiceVO service = null;

		while (it.hasNext()) {
			service = it.next();
			Iterator<ServiceElementVO> sit = service.getServiceElements().iterator();
			while (sit.hasNext()) {
				ServiceElementVO s = (ServiceElementVO)sit.next();
				if (s.getItem() != null) {
					RehearsalItem rh = map.get(s.getItem().toString());
					if (rh == null) {
						rh = new RehearsalItem();
						if (s.getName().indexOf("HYMN") >= 0) rh.rehearsalCategory = RehearsalItem.HYMN;
						map.put(s.getItem().toString(), rh);
					}
					rh.se = s;
					rh.instances++;

					if (rh.firstDueDate == null  || s.getService().getDateTime().before(rh.firstDueDate)) {
						rh.firstDueDate = s.getService().getDateTime();
					}
					int rhNeeded = s.getItem().getRehearsalsNeededAsInt();
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(rh.firstDueDate);
					
					cal.add(Calendar.DATE, -1 * 7 * rhNeeded);
					
					rh.startDate = new Date(cal.getTimeInMillis());
					
					
					rhItems.add(rh);
				}
			}
		}
		
		System.out.println(rhItems.toString());
	}


	@Override
	protected String getTitleLabel() {
		 
		return "Title: ";
	}

	@Override
	protected String getNotesLabel() {
		 
		return "Freeform notes: ";
	}
	
	@Override
	protected boolean isDefaultTitle() {
		 
		return true;
	}


}

