package com.organist4j.view.music.item;

import java.util.Date;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

import com.organist4j.model.music.MusicItemVO;


public class MusicItemBrowserSorter extends ViewerSorter {

	TableViewer tv;
	public MusicItemBrowserSorter(TableViewer tv) {
		this.tv = tv;
	}
	public int compare(Viewer viewer, Object e1, Object e2) {
		MusicItemVO se1 = (MusicItemVO)e1;
		MusicItemVO se2 = (MusicItemVO)e2;

		Object s1 = "";
		Object s2 = "";
		int rc = 0;
		int col = 0;
		try {
			col = Integer.parseInt( tv.getTable().getSortColumn().getData().toString() );
		} catch (Exception ig) {}
		switch (col) {
		case MusicItemVO.NAME_POS:
			s1 = se1.getName() == null ? "" : se1.getName();
			s2 = se2.getName() == null ? "" : se2.getName();

			rc = s1.toString().toUpperCase().compareTo(s2.toString().toUpperCase());

			break;
		case MusicItemVO.NUMBER_POS:
			rc = compareNumbers(se1,se2);
			break;
		case MusicItemVO.TUNE_POS:
			s1 = se1.getTune() == null ? "" : se1.getTune();
			s2 = se2.getTune() == null ? "" : se2.getTune();
			rc = s1.toString().toUpperCase().compareTo(s2.toString().toUpperCase());
			break;
		case MusicItemVO.BOOK_POS:
			s1 = se1.getBookName() == null ? "" : se1.getBookName();
			s2 = se2.getBookName() == null ? "" : se2.getBookName();
			if (s1.toString().equals(s2.toString())) {
				rc = compareNumbers(se1,se2);				
			} else {
				rc = s1.toString().toUpperCase().compareTo(s2.toString().toUpperCase());
			}
			break;
		case MusicItemVO.LASTUSED_POS:
			s1 = se1.getLastDateTimeUsed() == null ? new Date(0) : se1.getLastDateTimeUsed();
			s2 = se2.getLastDateTimeUsed() == null ? new Date(0) : se2.getLastDateTimeUsed();
			rc = ((Date)s1).compareTo((Date)s2);
			break;
		case MusicItemVO.TUNELASTUSED_POS:
			s1 = se1.getTuneLastDateTimeUsed() == null ? new Date(0) : se1.getTuneLastDateTimeUsed();
			s2 = se2.getTuneLastDateTimeUsed() == null ? new Date(0) : se2.getTuneLastDateTimeUsed();
			rc = ((Date)s1).compareTo((Date)s2);
			break;
		case MusicItemVO.AUTHOR_POS:
			s1 = se1.getAuthor() == null ? "" : se1.getAuthor();
			s2 = se2.getAuthor() == null ? "" : se2.getAuthor();
			rc = s1.toString().toUpperCase().compareTo(s2.toString().toUpperCase());
			break;
		case MusicItemVO.COMPOSER_POS:
			s1 = se1.getComposer() == null ? "" : se1.getComposer();
			s2 = se2.getComposer() == null ? "" : se2.getComposer();
			rc = s1.toString().toUpperCase().compareTo(s2.toString().toUpperCase());
			break;
		case MusicItemVO.METER_POS:
			s1 = se1.getMeter() == null ? "" : se1.getMeter();
			s2 = se2.getMeter() == null ? "" : se2.getMeter();
			rc = s1.toString().toUpperCase().compareTo(s2.toString().toUpperCase());
			break;

		case MusicItemVO.PSALM_POS:
			rc = compareNumbers(se1,se2);
			break;
		}
		//If down, switch
		if (tv.getTable().getSortDirection() == SWT.UP) {
			rc = rc * -1;
		}

		return rc;

	}
	private int compareNumbers(MusicItemVO se1, MusicItemVO se2) {
		int rc = 0;
		try {
			Object s1 = se1.getNumberAsInteger() == null ? new Integer(0) : se1.getNumberAsInteger();
			Object s2 = se2.getNumberAsInteger() == null ? new Integer(0) : se2.getNumberAsInteger();
			rc = ((Integer)s1).compareTo((Integer)s2);
		} catch (Exception e) {
			rc = se1.getNumber().toUpperCase().compareTo(se2.getNumber().toUpperCase());
		}
		return rc;
	}




}
