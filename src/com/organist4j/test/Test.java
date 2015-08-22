package com.organist4j.test;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.model.service.ServiceItemVO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.O4J;

public class Test {
	static Logger logger = Logger.getLogger(Test.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 
		Test t = new Test();
		try {
			//t.doTestCheck();

			t.doClearAllObjects();
			//t.doTestWriteItems();


			//t.doTestPrint2();
		} catch (Exception e) {
			 
			e.printStackTrace();
		}
	}

	private void doTestCheck() throws Exception {
		ObjectContainer db = null;
		try {
			db=Db4o.openFile("myTestDB");

			ServiceElementVO se = new ServiceElementVO(null,null);
			//se.setItem(item);
			List <ServiceElementVO> cpl = db.queryByExample(se);
			Iterator<ServiceElementVO> it = cpl.iterator();
			while (it.hasNext()) {
				ServiceElementVO sel = it.next();
				if (sel != null) {
					Object obj = (Object)sel.getService();
					if (obj != null)
						logger.debug(obj.getClass().getName() + '@' + Integer.toHexString(hashCode())
								+ " " + sel.getService() + " " + sel.toString());
				}
			}


			ServiceVO sv = new ServiceVO();
			sv.setTemplate(false);
			sv.setName("Epiphany");
			List <ServiceVO> cpl2 = db.queryByExample(sv);
			Iterator<ServiceVO> it2 = cpl2.iterator();
			while (it2.hasNext()) {
				ServiceVO s = it2.next();
				logger.debug(s.getName());
				logger.debug(s.getServiceElements());
			}


		} finally {
			db.close();
		}
	}

	public void doTestPrint() throws Exception {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.open();
		PrintDialog pd = new PrintDialog(shell);

		PrinterData data = pd.open();
		if (data == null) {
			logger.debug("Warning: No default printer.");
			return;
		}
		Printer printer = new Printer(data);
		if (printer.startJob("SWT Printing Snippet")) {
			
			Rectangle trim = printer.computeTrim(0, 0, 0, 0);
			GC gc = new GC(printer);

			if (printer.startPage()) {
				
				printReport(printer,gc,null);
				printer.endPage();
			}
			gc.dispose();
			printer.endJob();
		}
		printer.dispose();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public void doTestPrint2() throws Exception {
		Display display = new Display();
		Shell shell = new Shell(display);
		createContents(shell);
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private void createContents(Shell shell) {
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		Canvas c = new Canvas(shell,SWT.NONE);
		PrintDialog pd = new PrintDialog(shell);

		PrinterData data = pd.open();
		Printer p = new Printer(data);
		c.setBounds( p.getClientArea() );
//		c.pack();
		c.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		c.addPaintListener(new ExampleReportPaintListener());
	}
	
	private void printReport(Device device,GC gc,Rectangle trim) {
		Color black = device.getSystemColor(SWT.COLOR_BLACK);
		Color white = device.getSystemColor(SWT.COLOR_WHITE);
		
		Point dpi = device.getDPI();	
		//This is actually printable area, not trim.
		if (trim == null) {
		 trim = device.getClientArea();
		}
		String fontName = "Calibri";
//		String fontName = "Courier New";
		Font font = new Font(device, fontName, 12, SWT.NORMAL);
		Font fontb = new Font(device, fontName, 12, SWT.BOLD);
		Font fontf = new Font(device, fontName, 8, SWT.NORMAL);
		gc.setFont(font);
		gc.setBackground(white);
		gc.setForeground(black);
		
		
	
		ReportWriter rp = new ReportWriter(gc,trim,dpi);
		gc.setFont(fontb);
		rp.println("SS JULIUS & AARON");
		gc.setFont(font);
		rp.crlf();
		rp.println("Music for January 2010",true);
		rp.crlf();
		rp.printcols(new String[] {"NEH = New English Hymnal","LHON = Liturgical Hymns Old & New",""});
		rp.crlf();
		rp.printcols(new String[] {"Sunday 4th","Epiphany",""},true);
		rp.printcols(new String[] {"\tNEH","",""});
		rp.printcols(new String[] {"E\t47","Kyries:\tChristmas M Prosser","Setting:\t\tSt Thomas"});
		rp.printcols(new String[] {"O\t55","Gloria:\tSt Thomas","Agnus Dei:\tSt Thomas"});
		rp.printcols(new String[] {"PC\t49","Psalm:\tVE p96","Anthem:\t\tLead me Lord"});
		rp.printcols(new String[] {"","Accl:\tOverleaf","Marian:\t\tAngelus"});
		
		rp.crlf();
		rp.crlf();
		rp.printcols(new String[] {"Sunday 11th","Epiphany 1",""},true);
		rp.printcols(new String[] {"\tNEH","",""});
		rp.printcols(new String[] {"E\t47","Kyries:\tChristmas M Prosser","Setting:\t\tSt Thomas"});
		rp.printcols(new String[] {"O\t55","Gloria:\tSt Thomas","Agnus Dei:\tSt Thomas"});
		rp.printcols(new String[] {"PC\t49","Psalm:\tVE p96","Anthem:\t\tLead me Lord"});
		rp.printcols(new String[] {"","Accl:\tOverleaf","Marian:\t\tAngelus"});
		
		rp.crlf();
		rp.crlf();
		rp.printcols(new String[] {"Sunday 18th","Epiphany 2",""},true);
		rp.printcols(new String[] {"\tNEH","",""});
		rp.printcols(new String[] {"E\t47","Kyries:\tChristmas M Prosser","Setting:\t\tSt Thomas"});
		rp.printcols(new String[] {"O\t55","Gloria:\tSt Thomas","Agnus Dei:\tSt Thomas"});
		rp.printcols(new String[] {"PC\t49","Psalm:\tVE p96","Anthem:\t\tLead me Lord"});
		rp.printcols(new String[] {"","Accl:\tOverleaf","Marian:\t\tAngelus"});
		
		rp.crlf();
		rp.crlf();
		rp.printcols(new String[] {"Sunday 25th","Epiphany 3",""},true);
		rp.printcols(new String[] {"\tNEH","",""});
		rp.printcols(new String[] {"E\t47","Kyries:\tChristmas M Prosser","Setting:\t\tSt Thomas"});
		rp.printcols(new String[] {"O\t55","Gloria:\tSt Thomas","Agnus Dei:\tSt Thomas"});
		rp.printcols(new String[] {"PC\t49","Psalm:\tVE p96","Anthem:\t\tLead me Lord"});
		rp.printcols(new String[] {"","Accl:\tOverleaf","Marian:\t\tAngelus"});
		
		
		gc.setFont(fontf);
		rp.printfooter("Produced by " + O4J.APPLICATION_NAME + " v" + O4J.APPLICATION_VERSION + " - " + O4J.APPLICATION_WEBSITE_URL);

		
		
		
		//gc.dispose();
		font.dispose();
		fontb.dispose();
		fontf.dispose();
	}

	private class ExampleReportPaintListener implements PaintListener {
		public void paintControl(PaintEvent e) {
			Canvas canvas = (Canvas)e.widget;
			Device device = e.display;
			//Rectangle trim = device.computeTrim(0, 0, 0, 0);
			Rectangle trim = canvas.getClientArea();
			GC gc = e.gc;
			printReport(device,gc,trim);

			//printer.endJob();
		}

	}
	
	public class ReportWriter {
		int row = 0;
		GC gc = null;
		Point dpi = null;
		Rectangle trim = null;
		int leftMargin = 0;
		int topMargin = 0;
		
		public ReportWriter(GC gc,Rectangle trim,Point dpi) {
			this.gc = gc;
			this.trim = trim;
			this.dpi = dpi;
			leftMargin = dpi.x + trim.x; // one inch from left side of
			// paper
			topMargin = dpi.y / 2 + trim.y; // one-half inch from top edge
			// of paper
		}
		public void printfooter(String text) {
			//Need to find the bottom, less the font
			//And centre it
			int x = (trim.width - gc.textExtent(text).x) / 2;
			gc.drawText(text, x, trim.height - gc.getFontMetrics().getHeight());
			
		}
		public void println(String text) {
			println(text,false);
		}
		public void println(String text,boolean underline) {
			gc.drawText(text, leftMargin, topMargin + row);			
			if (underline) {
				Point p = gc.textExtent(text);
				gc.drawLine(leftMargin, topMargin + row + gc.getFontMetrics().getHeight() - 1, leftMargin + p.x, topMargin + row +  gc.getFontMetrics().getHeight() - 1);
			}
			row += gc.getFontMetrics().getHeight();
		}
		public void printcols(String[] texts) {
		 printcols(texts,false);
		}
		public void printcols(String[] texts,boolean underline) {
			//Available line width is
			int totalLineWidth = trim.width - (leftMargin * 2);
			int columnWidth = totalLineWidth / texts.length;
			int x = leftMargin;
			for (int i=0;i<texts.length;i++) {
				gc.drawText(texts[i], x, topMargin + row);
				if (underline) {
					Point p = gc.textExtent(texts[i]);
					gc.drawLine(x, topMargin + row + gc.getFontMetrics().getHeight() - 1, x + p.x, topMargin + row +  gc.getFontMetrics().getHeight() - 1);

				}
				x += columnWidth;
			}
			row += gc.getFontMetrics().getHeight();
		}
		
		public void crlf() {
			row += gc.getFontMetrics().getHeight();
		}
	}

	//	public void doTest1() throws Exception {
	//		// accessDb4o
	//		ObjectContainer db=Db4o.openFile("myTestDB");
	//		try {
	//		    // do something with db4o
	//			ServiceTemplate st = new ServiceTemplate("Sunday Mass");
	//						
	//			ServiceElementVO seHymn = new ServiceElementVO("Hymn");
	//			ServiceElementVO seOrgan = new ServiceElementVO("Organ Piece");
	//			
	//			st.getTemplateElements().add(new TemplateElement("Pre-Service Music",true,seOrgan));
	//			st.getTemplateElements().add(new TemplateElement("Hymn 1",true,seHymn));
	//			st.getTemplateElements().add(new TemplateElement("Hymn 2",true,seHymn));
	//			st.getTemplateElements().add(new TemplateElement("Hymn 2.2",false,seHymn));
	//			st.getTemplateElements().add(new TemplateElement("Communion",false,seOrgan));
	//			st.getTemplateElements().add(new TemplateElement("Hymn 3",true,seHymn));
	//			st.getTemplateElements().add(new TemplateElement("Outgoing Voluntary",true,seOrgan));
	//			
	//			db.store(st);
	//			
	//			
	//		}
	//		finally {
	//		    db.close();
	//		}
	//	}

	public void doClearAllObjects() throws Exception {
		// accessDb4o
		ObjectContainer db=Db4o.openFile("myTestDB");
		try {
			// do something with db4o
			List <Object> obj = db.queryByExample(new Object());
			Iterator<Object> it = obj.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o != null) {
					logger.debug(o.getClass().getSimpleName());
					if ((o instanceof ServiceVO)) {
						ServiceVO s = (ServiceVO)o;
						if (s.getName().equalsIgnoreCase("test")) {
							db.delete(o);
						}
					}
				}
			}

		}
		finally {
			db.close();
		}
	}

//	public void doTestWriteItems() throws Exception {
//		// accessDb4o
//		ObjectContainer db=Db4o.openFile("myTestDB");
//		try {
//			// do something with db4o
//
//
//			db.store(new MusicItemVO("New English Hymnal","105","O come O come emmanuel"));
//			db.store(new MusicItemVO("New English Hymnal","15","On Jordan's bank"));
//			db.store(new MusicItemVO("New English Hymnal","16","Hills of the North rejoice"));
//
//
//			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HHmm");
//
//			ServiceVO sv = null;
//
//
//
//			sv = new ServiceVO();
//			sv.setName("Advent 1");
//			sv.setDateTime(df.parse("08-12-2009 1000"));
//
//			sv.addServiceElement(new ServiceElementVO(sv,"1Opening Hymn"));
//			sv.addServiceElement(new ServiceElementVO(sv,"Kyries"));
//			sv.addServiceElement(new ServiceElementVO(sv,"Gloria"));
//			db.store(sv);
//
//			sv = new ServiceVO();
//			sv.setName("Advent 2");
//			sv.setDateTime(df.parse("11-12-2009 1000"));
//
//			sv.addServiceElement(new ServiceElementVO(sv,"2Opening Hymn"));
//			sv.addServiceElement(new ServiceElementVO(sv,"Kyries"));
//			sv.addServiceElement(new ServiceElementVO(sv,"Gloria"));
//			db.store(sv);
//
//
//
//			sv = new ServiceVO();
//			sv.setName("Advent 3");
//			sv.setDateTime(df.parse("18-12-2009 1000"));
//
//			sv.addServiceElement(new ServiceElementVO(sv,"3Opening Hymn"));
//			sv.addServiceElement(new ServiceElementVO(sv,"Kyries"));
//			sv.addServiceElement(new ServiceElementVO(sv,"Gloria"));
//			db.store(sv);
//
//			sv = new ServiceVO();
//			sv.setName("Epiphany");
//			sv.setDateTime(df.parse("02-01-2010 1000"));
//
//			sv.addServiceElement(new ServiceElementVO(sv,"eOpening Hymn"));
//			sv.addServiceElement(new ServiceElementVO(sv,"Kyries"));
//			sv.addServiceElement(new ServiceElementVO(sv,"Gloria"));
//			db.store(sv);
//
//		}
//		finally {
//			db.close();
//		}
//	}


	private void log(Object o) {
		logger.debug(o);
	}

}
