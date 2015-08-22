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

import com.organist4j.dao.MusicDAO;
import com.organist4j.model.music.MusicItemVO;
import com.organist4j.util.ServiceLocator;

public class ExportMusicItemsAction extends Action {
	static Logger logger = Logger.getLogger(ExportMusicItemsAction.class);
	ApplicationWindow window;
	public ExportMusicItemsAction(ApplicationWindow w) {
		window = w;
		setText("&Export Music Items...");
	}
	public void run() {

		//Show file browser
		FileDialog fd = new FileDialog(window.getShell(),SWT.SAVE);
		fd.setFileName("Music Item Data.txt");
		String file = fd.open();
		if (file == null) {
			return;
		}

		//Get the Music Items
		MusicDAO musicDAO = ServiceLocator.getInstance().getMusicDAO();		
		List<MusicItemVO> list = musicDAO.getMusicItems();
		StringBuffer s=null;
		//Write the csv
		try
		{
			PrintWriter writer = new PrintWriter(new FileWriter(file));

			Iterator<MusicItemVO> it = list.iterator();
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			String sep = "\t";
			s = new StringBuffer();
			s.append("bookName").append(sep);
			s.append("bookAcronym").append(sep);
			s.append("name").append(sep);
			s.append("number").append(sep);
			s.append("meter").append(sep);
			s.append("tune").append(sep);
			s.append("notes").append(sep);
			s.append("backingExists").append(sep);
			s.append("author").append(sep);
			s.append("composer").append(sep);
			s.append("lastDateTimeUsed").append(sep);
			s.append("tuneLastDateTimeUsed").append(sep);
			s.append("psalm").append(sep);
			s.append("response").append(sep);
			s.append("rehearsalsNeeded").append(sep);
			
			
	
			
			writer.println(s.toString());

			while (it.hasNext()) {
				MusicItemVO item = it.next();
				s = new StringBuffer();
				//book name
				s.append(item.getBookName()).append(sep);
				//book acronym
				s.append(item.getBookAcronym()).append(sep);
				//name
				s.append(item.getName()).append(sep);
				//number
				s.append(item.getNumber()).append(sep);
				//meter
				s.append(item.getMeter()).append(sep);
				//tune
				s.append(item.getTune()).append(sep);
				//comments				
				s.append(item.getNotes()).append(sep);
				//backing exists
				s.append(item.isBackingExists()).append(sep);
				//author
				s.append(item.getAuthor()).append(sep);
				//composer
				s.append(item.getComposer()).append(sep);
				//last used
				if (item.getLastDateTimeUsed() != null) {
					s.append(df.format(item.getLastDateTimeUsed())).append(sep);
				} else {
					s.append(item.getLastDateTimeUsed()).append(sep);
				}
				//tune last used
				if (item.getTuneLastDateTimeUsed() != null) {
					s.append(df.format(item.getTuneLastDateTimeUsed())).append(sep);
				} else {
					s.append(item.getTuneLastDateTimeUsed()).append(sep);
				}
				//psalm
				s.append(item.getPsalm()).append(sep);
				//response
				s.append(item.getResponse()).append(sep);
				//rehearsals needed
				s.append(item.getRehearsalsNeeded()).append(sep);

				writer.println(s.toString());
			}
			writer.close();
		}
		catch(IOException e)
		{
			logger.fatal("Problem creating file",e);
		}

	}

}
