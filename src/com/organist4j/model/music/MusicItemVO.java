package com.organist4j.model.music;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.organist4j.util.O4J;


@SuppressWarnings("rawtypes")
public class MusicItemVO implements Comparable {

	public static final int BOOK_POS = 0;
	public static final int NUMBER_POS = 1;
	public static final int PSALM_POS = 2;
	public static final int NAME_POS = 3;	
	public static final int TUNE_POS = 4;
	public static final int LASTUSED_POS = 5;
	public static final int TUNELASTUSED_POS = 6;
	public static final int METER_POS = 7;
	public static final int COMMENTS_POS = 8;
	public static final int BACKINGEXISTS_POS = 9;
	public static final int AUTHOR_POS = 10;
	public static final int COMPOSER_POS = 11;
	public static final int RESPONSE_POS = 12;
	public static final int REHEARSALS_POS = 13;

	public static final int SERVICE_POS = 0;
	public static final int SERVICE_DATE_POS = 1;
	public static final int USAGE_POS = 2;



	public static final String[] LABELS = {
		"Book / Collection",
		"No.",
		"Psalm",
		"Title / First Line",
		"Tune",
		"Last Used",
		"Tune Last Used",
		"Meter",
		"Notes",
		"Backing",
		"Author",
		"Composer",
		"Response",
		"Rehearsals"

	};
	public static final String[] LABELSHISTORY = {
		"Service",
		"Service Date",
		"Usage"
	};

	public static final int[][] ORDER = {
		{BOOK_POS,180,O4J.COLEDIT_NONE},
		{NUMBER_POS,50,O4J.COLEDIT_NONE},
		{PSALM_POS,50,O4J.COLEDIT_TEXT},
		{NAME_POS,200,O4J.COLEDIT_TEXT},
		{TUNE_POS,100,O4J.COLEDIT_TEXT},
		{LASTUSED_POS,100,O4J.COLEDIT_TEXT},
		{TUNELASTUSED_POS,100,O4J.COLEDIT_TEXT},
		{METER_POS,80,O4J.COLEDIT_TEXT},				
		{COMMENTS_POS,200,O4J.COLEDIT_TEXT},			
		{BACKINGEXISTS_POS,60,O4J.COLEDIT_BOOL},
		{AUTHOR_POS,100,O4J.COLEDIT_TEXT},
		{COMPOSER_POS,100,O4J.COLEDIT_TEXT},
		{RESPONSE_POS,200,O4J.COLEDIT_TEXT},
		{REHEARSALS_POS,60,O4J.COLEDIT_TEXT}

	};
	public static final int[][] REFERENCESORDER = {
		{BOOK_POS,180,O4J.COLEDIT_NONE},
		{NUMBER_POS,50,O4J.COLEDIT_NONE},
		{NAME_POS,300,O4J.COLEDIT_NONE}
	};

	public static final int[][] HISTORYORDER = {
		{SERVICE_POS,180,O4J.COLEDIT_NONE},
		{SERVICE_DATE_POS,90,O4J.COLEDIT_NONE},
		{USAGE_POS,300,O4J.COLEDIT_NONE}
	};


	private String name;

	private String number;

	private String psalm;
	private String tune;
	private String meter;

	private String bookName;

	private String bookAcronym;
	private String notes;
	//Actually redefined to be backingTrack exists
	private boolean backingExists;
	private String rehearsalsNeeded;
	String composer;
	String author;
	String response;
	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	private Set<MusicItemVO> relatedItems = new HashSet<MusicItemVO>();


	public Set<MusicItemVO> getRelatedItems() {
		if (relatedItems == null) {
			relatedItems = new HashSet<MusicItemVO>();
		}
		return relatedItems;
	}

	public void setRelatedItems(Set<MusicItemVO> relatedItems) {
		this.relatedItems = relatedItems;
	}



	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	private Date lastDateTimeUsed;

	private Date tuneLastDateTimeUsed;

	public Date getTuneLastDateTimeUsed() {
		return tuneLastDateTimeUsed;
	}

	public void setTuneLastDateTimeUsed(Date tuneLastDateTimeUsed) {
		this.tuneLastDateTimeUsed = tuneLastDateTimeUsed;		
	}

	public Date getLastDateTimeUsed() {
		return lastDateTimeUsed;
	}

	public void setLastDateTimeUsed(Date lastDateTimeUsed) {
		this.lastDateTimeUsed = lastDateTimeUsed;
	}

	public String getBookAcronym() {
		return bookAcronym;
	}

	public void setBookAcronym(String bookAcronym) {
		this.bookAcronym = bookAcronym;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MusicItemVO(String name) {
		super();
		this.name = name;
	}

	public MusicItemVO(String bookName,String number,String name) {
		super();
		this.bookName = bookName;
		this.number = number;
		this.name = name;
	}

	public MusicItemVO() {
		super();
	}

	public String getNumber() {
		return number;
	}

	public Integer getNumberAsInteger() {
		if (number != null && number.trim().length() > 0) {
			return new Integer(number);
		} else {
			return new Integer(0);
		}
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getTune() {
		return tune;
	}

	public void setTune(String tune) {
		this.tune = tune;
	}

	public String getMeter() {
		return meter;
	}

	public void setMeter(String meter) {
		this.meter = meter;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}




	public String getDisplayName() {
		return getDisplayName(false);
	}
	public String getDisplayName(boolean hideNumber) {
		StringBuffer sb = new StringBuffer();
		if (bookAcronym != null && bookAcronym.trim().length() > 0) {
			sb.append(bookAcronym);
		} else {
			if (bookName != null) sb.append(bookName);
		}
		if (number != null && number.trim().length() > 0) {
			if (!hideNumber) {
				sb.append(" ").append(number);
			}
		} else {
			//if no number always show name
			sb = new StringBuffer(name);


		}
		return sb.toString();
	}





	public String getPsalm() {
		return psalm;
	}

	public void setPsalm(String psalm) {
		this.psalm = psalm;
	}

	public void addRelatedItem(MusicItemVO newItem) {
		if (newItem.equals(this)) {
			return;
		}

		Iterator<MusicItemVO> it = getRelatedItems().iterator();
		MusicItemVO mi = null;
		while (it.hasNext()) {
			mi = it.next();
			if (newItem != mi)
				mi.getRelatedItems().add(newItem);
		}
		relatedItems.add(newItem);
		newItem.getRelatedItems().add(this);

	}

	public void removeRelatedItem(MusicItemVO oldItem) {

		oldItem.getRelatedItems().remove(this);
		relatedItems.remove(oldItem);

	}

	public String getRehearsalsNeeded() {
		return rehearsalsNeeded;
	}
	public int getRehearsalsNeededAsInt() {
		if (rehearsalsNeeded == null || rehearsalsNeeded.trim().length() <= 0) {
			return 1;
		} else {
			return Integer.parseInt( rehearsalsNeeded );
		}
	}

	public void setRehearsalsNeeded(String rehearsalsNeeded) {
		this.rehearsalsNeeded = rehearsalsNeeded;
	}


	@Override
	public int compareTo(Object that) {

		return this.toString().compareTo(that.toString()) ;
	}

	@Override
	public int hashCode() {
		 
		return toString().hashCode();
	}
	@Override
	public String toString() {
		 
		return bookAcronym + number + name;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MusicItemVO) {
			return this.toString().equals(((MusicItemVO)obj).toString());
		} else {
			return super.equals(obj);
		}
	}

	public boolean isBackingExists() {
		return backingExists;
	}

	public void setBackingExists(boolean backingExists) {
		this.backingExists = backingExists;
	}



}
