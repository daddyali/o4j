package com.organist4j.view.music.item;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceItemVO;
import com.organist4j.util.ServiceLocator;

public class MusicItemCellModifier implements ICellModifier {
	TableViewer tbvService;
	public MusicItemCellModifier(TableViewer tbvService) {
		this.tbvService = tbvService;
	}
	
	@Override
	public boolean canModify(Object element, String property) {
		if (property.equals(""+MusicItemVO.TUNELASTUSED_POS) ||
				property.equals(""+MusicItemVO.LASTUSED_POS) ||
				property.equals(""+MusicItemVO.COMMENTS_POS) ||
				property.equals(""+MusicItemVO.BACKINGEXISTS_POS) ) {
			return true;
		} else {
		
		return false;
		}
	}

	@Override
	public Object getValue(Object element, String property) {
		MusicItemVO se = (MusicItemVO)element;
//		if (property.equals(""+MusicItemVO.NAME_POS)) {
//			if (se.getName() != null) {
//				return se.getName();
//			} else {
//				return "";
//			}
//		}
		
		if (property.equals(""+MusicItemVO.LASTUSED_POS)) {
			if (se.getLastDateTimeUsed() != null) {
				return new SimpleDateFormat("dd/MM/yyyy").format(se.getLastDateTimeUsed());
			} else {
				return "";
			}
		}
		
		if (property.equals(""+MusicItemVO.TUNELASTUSED_POS)) {
			if (se.getTuneLastDateTimeUsed() != null) {
				return new SimpleDateFormat("dd/MM/yyyy").format(se.getTuneLastDateTimeUsed());
			} else {
				return "";
			}
		}
		

		if (property.equals(""+MusicItemVO.BACKINGEXISTS_POS)) {
			return se.isBackingExists();
		}
		
		if (property.equals(""+MusicItemVO.COMMENTS_POS)) {
			if (se.getNotes() != null) {
				return se.getNotes();
			} else {
				return "";
			}
		}
		
		return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		TableItem ti =(TableItem)element;
		MusicItemVO se = (MusicItemVO)ti.getData();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		if (property.equals(""+MusicItemVO.TUNELASTUSED_POS)) {
			if (value != null && value.toString().trim().length() > 0) {
				try {
					se.setTuneLastDateTimeUsed(df.parse(value.toString()));
				} catch (ParseException e) {
					 
					se.setTuneLastDateTimeUsed(null);
				}
			} else {
				se.setTuneLastDateTimeUsed(null);
			}
			ServiceLocator.getInstance().getMusicDAO().updateMusicItem(se);
			this.tbvService.refresh();
		}
		
		if (property.equals(""+MusicItemVO.LASTUSED_POS)) {
			if (value != null && value.toString().trim().length() > 0) {
				try {
					se.setLastDateTimeUsed(df.parse(value.toString()));
				} catch (ParseException e) {
					se.setLastDateTimeUsed(null);
				}
			} else {
				se.setLastDateTimeUsed(null);
			}
			ServiceLocator.getInstance().getMusicDAO().updateMusicItem(se);
			this.tbvService.refresh();
		}
		
		if (property.equals(""+MusicItemVO.BACKINGEXISTS_POS)) {
			se.setBackingExists(((Boolean)value).booleanValue());
			ServiceLocator.getInstance().getMusicDAO().updateMusicItem(se);
			this.tbvService.refresh();
		}
		
		if (property.equals(""+MusicItemVO.COMMENTS_POS)) {
			if (value != null) {
				se.setNotes(value.toString());
			} else {
				se.setNotes("");
			}
			ServiceLocator.getInstance().getMusicDAO().updateMusicItem(se);
			this.tbvService.refresh();
		}
		
	}


	
}