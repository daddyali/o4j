package com.organist4j.view.action.music;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import com.organist4j.dao.MusicDAO;
import com.organist4j.model.music.MusicItemVO;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.music.item.MusicItemBrowser;

public class ImportMusicItemsAction extends Action {
	static Logger logger = Logger.getLogger(ImportMusicItemsAction.class);
	ApplicationWindow window;
	public ImportMusicItemsAction(ApplicationWindow w) {
		window = w;
		setText("&Import Music Items...");
	}
	public void run() {
		
		//Show file browser
		FileDialog fd = new FileDialog(window.getShell(),SWT.OPEN);
		fd.setFilterNames(new String[] {"Organist4j Item Libraries (*.txt)"});
		fd.setFilterExtensions(new String[] {"*.txt"});
		
		String file = fd.open();
		if (file == null) {
			return;
		}
		
		//Get the Music Items
		MusicDAO musicDAO = ServiceLocator.getInstance().getMusicDAO();	
		
		
		//Read the csv
		try
	      {
	         BufferedReader reader = new BufferedReader(new FileReader(file));
	       
	         List<MusicItemVO> items = new ArrayList<MusicItemVO>();
	         String bookName = null;
	         String l;
	         boolean firstLine = true;
	         while ((l = reader.readLine()) != null) {
	        	 //ignore the header line
	        	 if (firstLine) {
	        		 firstLine = false;
	        		 continue;
	        	 }
	        	 //parse the line
	        	 StringTokenizer st = new StringTokenizer(l,"\t");
	        	 
	        	 MusicItemVO item = new MusicItemVO();
	        	 
	        	 //book name
	        	 if (st.hasMoreTokens()) {
	        		 item.setBookName(hn(st.nextToken()));
	        		 if (bookName == null) {
	        			 bookName = item.getBookName();
	        		 }
	        	 }
	        	 
	        	 //book acronym
	        	 if (st.hasMoreTokens()) {
	        		 item.setBookAcronym(hn(st.nextToken()));
	        	 }
	        	 
	        	 //item name
	        	 if (st.hasMoreTokens()) {
	        		 item.setName(hn(st.nextToken()));
	        		 if (item.getName() != null) {
	        			 item.setName(item.getName().replace('"', ' ').trim());
	        		 }
	        	 }
	        	 
	        	 //item number
	        	 if (st.hasMoreTokens()) {
	        		 item.setNumber(hn(st.nextToken()));
	        	 }
	        	 
	        	//item meter
	        	 if (st.hasMoreTokens()) {
	        		 item.setMeter(hn(st.nextToken()));
	        	 }
	        	 
	        	//item tune
	        	 if (st.hasMoreTokens()) {
	        		 item.setTune(hn(st.nextToken()));
	        	 }
	        	 

	        	 
	        	//item notes
	        	 if (st.hasMoreTokens()) {
	        		 item.setNotes(hn(st.nextToken()));
	        	 }
	        	 
	        	//item favourite
	        	 if (st.hasMoreTokens()) {
	        		 item.setBackingExists(hb(st.nextToken()));
	        	 }
	        	 
	        	//item author
	        	 if (st.hasMoreTokens()) {
	        		 item.setAuthor(hn(st.nextToken()));
	        	 }
	        	 
	        	//item composer
	        	 if (st.hasMoreTokens()) {
	        		 item.setComposer(hn(st.nextToken()));
	        	 }
	        	 
	        	//item lastDateTimeUsed
	        	 if (st.hasMoreTokens()) {
	        		 item.setLastDateTimeUsed(hd(st.nextToken()));
	        	 }
	        	 
	        	//item tuneLastUsed
	        	 if (st.hasMoreTokens()) {
	        		 item.setTuneLastDateTimeUsed(hd(st.nextToken()));
	        	 }
	        	 
	        	//item psalm
	        	 if (st.hasMoreTokens()) {
	        		 item.setPsalm(hn(st.nextToken()));
	        	 }
	        	 
	        	//item response
	        	 if (st.hasMoreTokens()) {
	        		 item.setResponse(hn(st.nextToken()));
	        	 }
	        	 
	        	//item rehearsals needed
	        	 if (st.hasMoreTokens()) {
	        		 item.setRehearsalsNeeded(hn(st.nextToken()));
	        	 }
	        	 
	        	 items.add(item);
	        	 
	         }

	         musicDAO.importMusicItems(bookName,items);
	      
	         reader.close();
	         
	         //Now refresh the viewer
	         ServiceLocator.getInstance().getMusicItemBrowser().refresh(true);
	         
	      }
	      catch(IOException e)
	      {
	         logger.fatal("Problem creating file",e);
	      }

	}
	
	//Handle null for string
	private String hn(String token) {
		if (token == null) return "";
		if (token.equalsIgnoreCase("null")) return "";
		return token.trim();
	}
	
	//Handle null for bool
	private boolean hb(String token) {
		if (token == null) return false;
		if (token.equalsIgnoreCase("false")) return false;
		if (token.equalsIgnoreCase("true")) return true;
		if (token.equalsIgnoreCase("n")) return false;
		if (token.equalsIgnoreCase("y")) return true;
		return false;
	}
	
	//Handle for Date
	private Date hd(String token)  {
		if (token == null) return null;
		if (token.trim().length() <= 0) return null;
		
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(token);
		} catch (ParseException e) {
			 
			return null;
		}
	}

}
