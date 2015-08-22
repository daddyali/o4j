package com.organist4j.view.music.item;

import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.organist4j.model.music.MusicItemVO;



public class MusicItemBrowserLabelProvider implements ITableLabelProvider,ITableColorProvider {



	@Override
	public Image getColumnImage(Object arg0, int arg1) {

		return null;
	}

	@Override
	public String getColumnText(Object arg0, int colIndex) {
		MusicItemVO mbe = (MusicItemVO)arg0;
		String txt = "";
		switch (colIndex) {
		case MusicItemVO.BOOK_POS: txt = mbe.getBookName();
		if (mbe.getBookAcronym() != null && mbe.getBookAcronym().length() > 0) {
			txt = mbe.getBookAcronym() + " " + txt;
		}
		break;
		case MusicItemVO.NUMBER_POS: txt = mbe.getNumber();
		break;
		case MusicItemVO.NAME_POS: 
			txt = mbe.getName();
			if (mbe.getRelatedItems() != null && mbe.getRelatedItems().size() > 0) {
				txt = "*" + txt;
			}
			break;
		case MusicItemVO.TUNE_POS: txt = mbe.getTune();
		break;
		case MusicItemVO.LASTUSED_POS: 
			if (mbe.getLastDateTimeUsed() != null) {
				txt = new SimpleDateFormat("dd/MM/yyyy").format(mbe.getLastDateTimeUsed());
			} else {
				txt = "";
			}
			break;
		case MusicItemVO.TUNELASTUSED_POS: 
			if (mbe.getTuneLastDateTimeUsed() != null) {
				txt = new SimpleDateFormat("dd/MM/yyyy").format(mbe.getTuneLastDateTimeUsed());
			} else {
				txt = "";
			}
			break;
		case MusicItemVO.PSALM_POS: txt = mbe.getPsalm() != null ? mbe.getPsalm() : "";
		break;
		case MusicItemVO.METER_POS: txt = mbe.getMeter() != null ? mbe.getMeter() : "";
		break;
		case MusicItemVO.COMMENTS_POS: txt = mbe.getNotes() != null ? mbe.getNotes() : "";
		break;
		case MusicItemVO.BACKINGEXISTS_POS: txt = mbe.isBackingExists() + "";
		break;
		case MusicItemVO.AUTHOR_POS: txt = mbe.getAuthor() != null ? mbe.getAuthor() : "";
		break;
		case MusicItemVO.COMPOSER_POS: txt = mbe.getComposer() != null ? mbe.getComposer() : "";
		break;
		case MusicItemVO.RESPONSE_POS: txt = mbe.getResponse() != null ? mbe.getResponse() : "";
		break;
		case MusicItemVO.REHEARSALS_POS: txt = mbe.getRehearsalsNeeded() != null ? mbe.getRehearsalsNeeded() : "";
		break;
		default:

		}
		return txt;
	}

	@Override
	public void addListener(ILabelProviderListener arg0) {


	}

	@Override
	public void dispose() {


	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {

		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {


	}

	@Override
	public Color getBackground(Object arg0, int arg1) {
		MusicItemVO mbe = (MusicItemVO)arg0;
		if (mbe.getRelatedItems() != null && mbe.getRelatedItems().size() > 0) {
			return Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW); 
		}
		return null;
	}

	@Override
	public Color getForeground(Object arg0, int arg1) {
		MusicItemVO mbe = (MusicItemVO)arg0;
		return null;
	}
}