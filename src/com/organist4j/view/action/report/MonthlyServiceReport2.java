package com.organist4j.view.action.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.organist4j.dao.ServiceDAO;
import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.model.service.ServiceItemVO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.report.util.ReportWriter;
import com.organist4j.util.Node;
import com.organist4j.util.O4J;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.service.explorer.ServiceExplorer;

public class MonthlyServiceReport2 extends AbstractReportAction {

	public MonthlyServiceReport2(ApplicationWindow w) {
		super(w);

	}

	@Override
	protected String getActionText() {
		 
		return "&Monthly Service Report...";
	}

	@Override
	protected String getReportName() {
		return "Monthly Service Report";
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
		Font font = new Font(device, fontName, 12, SWT.NORMAL);
		Font fontb = new Font(device, fontName, 12, SWT.BOLD);
		Font fontf = new Font(device, fontName, 8, SWT.NORMAL);
		gc.setFont(font);
		gc.setBackground(white);
		gc.setForeground(black);

		ServiceExplorer se = ServiceLocator.getInstance().getServiceExplorer();
		IStructuredSelection sel  = (IStructuredSelection)se.getTreeViewer().getSelection();
		if (sel.isEmpty()) {
			return;
		} 

		String monthStr = "";
		Iterator<Node> it = sel.iterator();
		while (it.hasNext()) {
			Node n = it.next();
			if (n.getData() != null && n.getData() instanceof ServiceVO) {
				ServiceVO s = (ServiceVO)n.getData();
				if (s.getDateTime() != null) {
					monthStr = new SimpleDateFormat("MMM yyyy").format(s.getDateTime());
					break;
				}
			}
		}


		ReportWriter rp = new ReportWriter(gc,trim,dpi);
		gc.setFont(fontb);
		rp.println(ServiceLocator.getInstance().getPreferenceStore().getString("church.name"));
		gc.setFont(font);
		rp.crlf();
		//Always force the title
		
		if (title != null && title.getText().length() > 0) {
			rp.println(title.getText(),true);
		} else {
			rp.println("Music for " + monthStr,true);
			title.setText("Music for " + monthStr);
		}
		rp.crlf();
		
		rp.println(notes.getText());
		
		//rp.printcols(new String[] {"NEH = New English Hymnal","LHON = Liturgical Hymns Old & New",""});
		rp.crlf();




		it = sel.iterator();
		while (it.hasNext()) {
			Node n = it.next();
			if (n.getData() != null && n.getData() instanceof ServiceVO) {
				printService(rp,(ServiceVO)n.getData());
				rp.crlf();
				rp.crlf();
			}
		}




		gc.setFont(fontf);
		rp.printfooter("Produced by " + O4J.APPLICATION_NAME + " v" + O4J.APPLICATION_VERSION + " - " + O4J.APPLICATION_WEBSITE_URL);




		//gc.dispose();
		font.dispose();
		fontb.dispose();
		fontf.dispose();
	}


	@SuppressWarnings("unchecked")
	private void printService(ReportWriter rp,ServiceVO service) {
		if (service.getDateTime() == null) {
			logger.debug("No date on " + service.getName());
		}

		SimpleDateFormat df = new SimpleDateFormat("EEEE dd");

		List<ServiceElementVO> hymns = new ArrayList<ServiceElementVO>();
		List<ServiceElementVO> nothymns = new ArrayList<ServiceElementVO>();

		//We have a forced format of certain items


		Map<String, Integer> hymnBookFreq = new HashMap<String, Integer>();
		@SuppressWarnings("unused")
		String gloria = "-";
		String psalm = "";
		String kyries = "-";
		String anthem = "-";
		String gospel = "-";
		String memacc = "";
		String gtamen ="";
		String sanctus = "";
		String agnusdei ="";
		String marian = "";

		String memaccNoNum = "";
		String gtamenNoNum ="";
		String sanctusNoNum = "";
		String memaccNo = "";
		String gtamenNo ="";
		String sanctusNo = "";

		//Pre process the items
		ServiceElementVO se = null;
		Iterator<ServiceElementVO> it = service.getServiceElements().iterator();
		Integer bookCount = new Integer(0);
		while (it.hasNext()) {
			se = it.next();

			if (se.getItem() != null && se.getName().toUpperCase().indexOf("HYMN") >= 0) {
				hymns.add(se);
				if (hymnBookFreq.containsKey(se.getItem().getBookAcronym())) {
					bookCount = hymnBookFreq.get(se.getItem().getBookAcronym());
					bookCount = new Integer(bookCount.intValue() + 1);
				} else {
					bookCount = new Integer(1);
				}
				hymnBookFreq.put(se.getItem().getBookAcronym(),bookCount);
			} else {
				if (se.getItem() != null && se.getName().indexOf("Organ") < 0) {
					nothymns.add(se);
				}
				if (se.getItem() != null && se.getName().indexOf("Psalm") >= 0) {

					psalm = se.getItem().getDisplayName();
					if (se.getTuneItem() != null && se.getTuneItem() != se.getItem()) {
						psalm += " (T " + se.getTuneItem().getDisplayName() + ")";
					}

				}
				if (se.getName().indexOf("Gloria") >= 0) {
					if (se.getItem() != null) { 
						gloria = se.getItem().getDisplayName();
					} else {
						gloria = se.getComments() == null ? "" : se.getComments();
					}
				}

				if (se.getName().indexOf("Kyrie") >= 0) {
					if (se.getItem() != null) { 
						kyries = se.getItem().getDisplayName();
					} else {
						kyries = se.getComments() == null ? "" : se.getComments();

					}
				}

				if (se.getItem() != null && se.getName().indexOf("Anthem") >= 0) {
					anthem = se.getItem().getDisplayName();

				}

				if (se.getName().indexOf("Gospel") >= 0) {
					if (se.getItem() != null) {
						gospel = se.getItem().getDisplayName();
						if (se.getTuneItem() != null && se.getTuneItem() != se.getItem()) {
							gospel += " (T " + se.getTuneItem().getDisplayName() + ")";
						}
					} else {
						gospel = se.getComments() == null ? "" : se.getComments();
					}

				}

				if (se.getName().indexOf("Agnus") >= 0) {
					if (se.getItem() != null) { 
						agnusdei = se.getItem().getDisplayName();
					} else {
						agnusdei = se.getComments() == null ? "" : se.getComments();
					}
				}

				if (se.getItem() != null && se.getName().indexOf("Sanctus") >= 0) {
					sanctus = se.getItem().getDisplayName();
					sanctusNoNum = se.getItem().getDisplayName(true);
				}

				if (se.getItem() != null && se.getName().indexOf("Mem Acc") >= 0) {
					memacc = se.getItem().getDisplayName();
					memaccNoNum = se.getItem().getDisplayName(true);
					memaccNo = se.getItem().getNumber();
				}

				if (se.getItem() != null && se.getName().indexOf("Great Amen") >= 0) {
					gtamen = se.getItem().getDisplayName();
					gtamenNoNum = se.getItem().getDisplayName(true);
					gtamenNo = se.getItem().getNumber();
				}

				if (se.getName().indexOf("Marian") >= 0) {
					if (se.getItem() != null) { 
						marian = se.getItem().getDisplayName();
					}	else { 
						marian = se.getComments() == null ? "" : se.getComments();
					}
				}
			}
		}

			//Process setting
			Map<String, String> settingMap = new HashMap<String, String>();
			settingMap.put(sanctusNoNum, sanctus);
			if (!settingMap.containsKey(memaccNoNum)) {
				settingMap.put(memaccNoNum, memacc);
			} else {
				if (memaccNo != null && memaccNo.length() > 0) {
					settingMap.put(memaccNoNum, settingMap.get(memaccNoNum) + "/" + memaccNo);
				}
			}
			if (!settingMap.containsKey(gtamenNoNum)) {
				settingMap.put(gtamenNoNum, gtamen);
			} else {
				if (gtamenNo != null && gtamenNo.length() > 0) {
					settingMap.put(gtamenNoNum, settingMap.get(gtamenNoNum) + "/" + gtamenNo);
				}
			}

			String setting = "";
			Iterator<String> i2 = settingMap.values().iterator();
			while (i2.hasNext()) {
				if (setting.length() > 0) setting += "/";
				setting+= i2.next();
			}




			String bookAcronym = "";
			int highest = 0;
			Iterator<Map.Entry<String,Integer>> bit = hymnBookFreq.entrySet().iterator();
			while (bit.hasNext()) {
				Map.Entry<String,Integer> entry = bit.next();
				Integer ii = (Integer)entry.getValue();
				if (ii.intValue() > highest) {
					highest = ii.intValue();
					bookAcronym = entry.getKey().toString();
				}

			}
		
			rp.printcols(new String[] {df.format(service.getDateTime()),service.getName(),""},true);
			rp.printcols(new String[] {"\t" + bookAcronym,"",""});

			ServiceDAO sd = ServiceLocator.getInstance().getServiceDAO();


			boolean morerows = true;
			int rowIndex = 0;
			String slot1 = "";
			String slot1b = "";
			String slot2 = "";
			String slot2b = "";
			String slot3 = "";
			String slot3b = "";
			ServiceItemVO si = null;
			while (morerows) {
				slot1 = "";
				slot1b = "";
				slot2 = "";
				slot2b = "";
				slot3 = "";
				slot3b = "";
				if (rowIndex < hymns.size()) {
					si = sd.getServiceItem(hymns.get(rowIndex).getName());

					if (si != null) {
						slot1 = si.getShortName() + "\t";
					} else {
						slot1 = " \t"; 
					}


					MusicItemVO  hymn = hymns.get(rowIndex).getItem();
					if (hymn.getBookAcronym().equals(bookAcronym)) {
						slot1b = hymn.getNumber();
					} else {
						slot1b = hymn.getBookAcronym() + " " + hymn.getNumber();
					}
					if (hymns.get(rowIndex).getItem() == hymns.get(rowIndex).getTuneItem()) {
						//set tune
					} else {
						slot1b += " (T " + hymns.get(rowIndex).getTuneItem().getBookAcronym() + hymns.get(rowIndex).getTuneItem().getNumber() + ")";
					}

				}

				int idx = rowIndex;
				MusicItemVO  nothymn = null;
				if (idx < (Math.round(nothymns.size() / 2.0)) ) {
					si = sd.getServiceItem(nothymns.get(idx).getName());
					if (si != null) {
						if (si.getShortName() != null && si.getShortName().length() > 0) {
							slot2 = si.getShortName() + "\t";
						} else {
							slot2 = si.getName() + "\t";
						}
					} else {
						slot2 = " \t"; 
					}
					nothymn = nothymns.get(idx).getItem();
					if (nothymn != null)
						slot2b = nothymn.getDisplayName();
				}

				idx = (int)(rowIndex + (Math.round(nothymns.size() / 2.0)));
				if (idx < nothymns.size()) {

					si = sd.getServiceItem(nothymns.get(idx).getName());
					if (si != null) {
						if (si.getShortName() != null && si.getShortName().length() > 0) {
							slot3 = si.getShortName() + "\t";
						} else {
							slot3 = si.getName() + "\t";
						}} else {
							slot3 = " \t"; 
						}
					nothymn = nothymns.get(idx).getItem();

					if (nothymn != null)
						slot3b = nothymn.getDisplayName();
				}
				//
				//			switch(rowIndex) {
				//			case 0:
				//				slot2 = "Kyries:\t" + kyries;
				//				slot3 = "Setting:\t" + setting;
				//				break;
				//			case 1:
				//				slot2 = "Gloria:\t" + gloria;
				//				slot3 = "Agn Dei:\t" + agnusdei;
				//				break;
				//			case 2:
				//				slot2 = "Psalm:\t" + psalm;
				//				slot3 = "Anthem:\t" + anthem;
				//				break;
				//			case 3:
				//				slot2 = "Accl:\t" + gospel;
				//				slot3 = "Marian:\t" + marian;
				//				break;				
				//			default:
				//
				//			}

				if (slot1.length() > 0 || slot2.length() > 0 || slot3.length() > 0)
					rp.printcols(new String[] {slot1 + slot1b,slot2 + slot2b,slot3 + slot3b});
				rowIndex ++;
				if (rowIndex >= hymns.size() && rowIndex > (Math.round(nothymns.size() / 2))) {
					morerows = false;
				}
			}


		}

	@Override
	protected void loadData() {
		 
		
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
		 
		return false;
	}



	
}
