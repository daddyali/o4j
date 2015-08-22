package com.organist4j.view.action.report;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.report.util.ReportWriter;
import com.organist4j.util.Node;
import com.organist4j.util.O4J;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.service.explorer.ServiceExplorer;

public class ResourceReport extends AbstractReportAction {

	Font font = null;
	Font fontb = null;
	Font fontf = null;
	Font fonti = null;

	public ResourceReport(ApplicationWindow w) {
		super(w);

	}

	@Override
	protected String getActionText() {
		 
		return "&Resource Report...";
	}

	@Override
	protected String getReportName() {
		return "Resource Report";
	}
	@Override
	protected void printReport(Device device,GC gc,Rectangle trim) {
		Color black = device.getSystemColor(SWT.COLOR_BLACK);
		Color white = device.getSystemColor(SWT.COLOR_WHITE);

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

		ServiceExplorer se = ServiceLocator.getInstance().getServiceExplorer();
		IStructuredSelection sel  = (IStructuredSelection)se.getTreeViewer().getSelection();
		if (sel.isEmpty()) {
			return;
		} 
		
		Set<String> allBooks = new TreeSet<String>();

		@SuppressWarnings("unchecked")
		Iterator<Node> it = sel.iterator();
		ServiceVO service = null;
		Node n = null;
		while (it.hasNext()) {
			n = it.next();
			if (n.getData() != null && n.getData() instanceof ServiceVO) {
				service = (ServiceVO)n.getData();
			}
			Iterator<ServiceElementVO> sit = service.getServiceElements().iterator();
			while (sit.hasNext()) {
				ServiceElementVO s = sit.next();
				if (s.getItem() != null) {
					allBooks.add(s.getItem().getBookName());
				}
			}
		}





		ReportWriter rp = new ReportWriter(gc,trim,dpi);
		gc.setFont(fontb);
		rp.println(ServiceLocator.getInstance().getPreferenceStore().getString("church.name") + " - SERVICE PLAN",true);
		rp.println("Resource Report");
		//		if (service.getType() != null)
		//			rp.println(service.getType());
		//		if (service.getDateTime() != null)
		//			rp.println(new SimpleDateFormat("EEEE dd MMMM yyyy, HHmm").format(service.getDateTime()));

		rp.crlf();
		//rp.printcols(new String[] {"Section","Item/Source","Title/Tune"});

		Iterator<String> ab = allBooks.iterator();
		while (ab.hasNext()) {
			rp.println(ab.next().toString());
		}
		
		gc.setFont(font);
		//rp.crlf();


		//printService(rp,service);

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


	@SuppressWarnings("unchecked")
	private void printService(ReportWriter rp,ServiceVO service) {

		Iterator<ServiceElementVO> it = service.getServiceElements().iterator();
		while (it.hasNext()) {
			ServiceElementVO se = it.next();
			if (se.getItem() != null) {

				rp.println("");
				rp.hr();
				//if (se.getItem() != null) {


				rp.printcols(new String[] {se.getName(),se.getItem().getDisplayName(),se.getItem().getName()},false);
				if ((se.getItem().getBookAcronym() != null && se.getItem().getBookAcronym().length() > 0) || (se.getItem().getTune() != null && se.getItem().getTune().length() > 0)) {
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
					rp.printcols(new String[] {"",se.getItem().getBookName(),tune },false);
					rp.setFont(font);
				}
			} else {
				if (se.getMandatory()) {
					rp.println("");
					rp.hr();
					rp.printcols(new String[] {se.getName(),"TBC",""});
				} else {
					if (se.getComments() != null && se.getComments().length() > 0) {
						rp.println("");
						rp.hr();
						rp.printcols(new String[] {se.getName(),"",""});
					} else {
						continue;
					}
				}
			}
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

		}





	}

	@Override
	protected void loadData() {
		 
		
	}
	
	@Override
	protected String getTitleLabel() {
		 
		return "Title: ";
		//return null;
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
