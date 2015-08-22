package com.organist4j.view.music.item;

import java.util.Collection;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.organist4j.model.music.MusicItemVO;

public class MusicItemBrowserFilter extends ViewerFilter {
	
	private String search = "";
	private Collection<MusicItemVO> searchSet = null;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search.toUpperCase();
	}
	

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (search.trim().length() <= 0 && searchSet == null) return true;
		
		MusicItemVO m = (MusicItemVO)element;
		//Break the search into space separated tokens
		StringTokenizer st = new StringTokenizer(search," ");
		String ss = null;
		
		if (searchSet != null && !searchSet.contains(m)) {
			//System.out.println("false for " + m.toString());
			return false;
		}

		while (st.hasMoreTokens()) {
			ss = st.nextToken();
			
			
			
			if (m.getName() != null && m.getName().toUpperCase().indexOf(ss) >= 0) {
				continue;
			}
			if (m.getBookAcronym().toUpperCase().startsWith(ss)) {
				continue;
			}
			if (m.getNumber() != null && m.getNumber().startsWith(ss)) {
				continue;
			}
			if (m.getBookName().toUpperCase().indexOf(ss) >= 0) {
				continue;
			}
			if (m.getTune() != null && m.getTune().toUpperCase().indexOf(ss) >= 0) {
				continue;
			}
			if (m.getMeter() != null && m.getMeter().toUpperCase().indexOf(ss) >= 0) {
				continue;
			}
			if (m.getAuthor() != null && m.getAuthor().toUpperCase().indexOf(ss) >= 0) {
				continue;
			}
			if (m.getComposer() != null && m.getComposer().toUpperCase().indexOf(ss) >= 0) {
				continue;
			}
			if (m.getPsalm() != null && m.getPsalm().toUpperCase().indexOf(ss) >= 0) {
				continue;
			}
			if (m.getNotes() != null && m.getNotes().toUpperCase().indexOf(ss) >= 0) {
				continue;
			}
			return false;
		}
		return true;
		
		
	}

	public void setSearchSet(Collection<MusicItemVO> searchSet) {
		this.searchSet = searchSet;
	}

}
