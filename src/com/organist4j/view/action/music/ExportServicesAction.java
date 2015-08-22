package com.organist4j.view.action.music;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import com.organist4j.dao.ServiceDAO;
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.ServiceLocator;

public class ExportServicesAction extends Action {
	static Logger logger = Logger.getLogger(ExportServicesAction.class);
	ApplicationWindow window;
	public ExportServicesAction(ApplicationWindow w) {
		window = w;
		setText("Export &Services...");
	}
	public void run() {

		//Show file browser
		FileDialog fd = new FileDialog(window.getShell(),SWT.SAVE);
		fd.setFileName("Service Data.txt");
		String file = fd.open();
		if (file == null) {
			return;
		}

		//Get the Music Items
		ServiceDAO serviceDAO = ServiceLocator.getInstance().getServiceDAO();		
		List<ServiceVO> list = serviceDAO.getServices(true);
		StringBuffer s=null;
		String sep = "|";
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		ServiceVO service;
		ServiceElementVO se;
		//Write the csv
		try
		{
			PrintWriter writer = new PrintWriter(new FileWriter(file));
			//Write header line
			s = new StringBuffer();
			s.append("service.dateTime").append(sep);
			s.append("service.name").append(sep);
			s.append("service.desc").append(sep);
			s.append("service.templateName").append(sep);
			s.append("se.name").append(sep);
			s.append("se.desc").append(sep);
			s.append("se.mandatory").append(sep);
			s.append("se.comments").append(sep);
			s.append("se.item.bookName").append(sep);
			s.append("se.item.bookAcronym").append(sep);
			s.append("se.item.number").append(sep);
			s.append("se.item.name").append(sep);
			s.append("se.tuneItem.bookName").append(sep);
			s.append("se.tuneItem.bookAcronym").append(sep);
			s.append("se.tuneItem.number").append(sep);
			s.append("se.tuneItem.name").append(sep);
			writer.println(s.toString());
			
			
			Iterator<ServiceVO> services = list.iterator();
			while (services.hasNext()) {

				service = services.next();
				Iterator<ServiceElementVO> it = service.getServiceElements().iterator();
				while (it.hasNext()) {
					se = it.next();

					s = new StringBuffer();
					if (se.getService().getDateTime() != null) {
						s.append(df.format(se.getService().getDateTime())).append(sep);
					} else {
						s.append("null").append(sep);
					}
					s.append(se.getService().getName()).append(sep);
					s.append(se.getService().getDesc()).append(sep);
					s.append(se.getService().getTemplateName()).append(sep);
					s.append(se.getService().getType()).append(sep);


					s.append(se.getName()).append(sep);
					s.append(se.getDesc()).append(sep);
					s.append(se.getMandatory()).append(sep);
					s.append(se.getComments()).append(sep);


					if (se.getItem() != null) {
						//book name
						s.append(se.getItem().getBookName()).append(sep);
						//book acronym
						s.append(se.getItem().getBookAcronym()).append(sep);
						//book number
						s.append(se.getItem().getNumber()).append(sep);
						//title
						s.append(se.getItem().getName()).append(sep);
					} else {
						s.append("").append(sep);
						s.append("").append(sep);
						s.append("").append(sep);
						s.append("").append(sep);
					}

					if (se.getTuneItem() != null) {
						//book name
						s.append(se.getTuneItem().getBookName()).append(sep);
						//book acronym
						s.append(se.getTuneItem().getBookAcronym()).append(sep);
						//book number
						s.append(se.getTuneItem().getNumber()).append(sep);
						//title
						s.append(se.getTuneItem().getName()).append(sep);
					} else {
						s.append("").append(sep);
						s.append("").append(sep);
						s.append("").append(sep);
						s.append("").append(sep);
					}


					writer.println(s.toString());
				}
			}
			writer.close();
		}
		catch(IOException e)
		{
			logger.fatal("Problem creating file",e);
		}

	}

}
