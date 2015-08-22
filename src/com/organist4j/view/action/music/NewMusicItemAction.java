package com.organist4j.view.action.music;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Shell;

import com.organist4j.model.music.MusicItemVO;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.music.editor.MusicItemEditor;
import com.organist4j.view.music.item.MusicItemBrowser;

public class NewMusicItemAction extends Action implements ISelectionChangedListener {
	//ApplicationWindow window;
	Shell s;
	public NewMusicItemAction(ApplicationWindow w) {
		super();
		s = w.getShell();
		setText("&New Music Item...@Ctrl+M");
	}
	public NewMusicItemAction(TableViewer tbvItemBrowser) {
		super();
		s = tbvItemBrowser.getTable().getShell();
		setText("&New Music Item...");
	}
	public void run() {
		//Need to get a music item
		//Pick the one that's selected in the browser
		
		MusicItemBrowser tv = ServiceLocator.getInstance().getMusicItemBrowser();
		IStructuredSelection sel = (IStructuredSelection) tv.getTbvItemBrowser().getSelection();
//		if (sel.size() <= 0) {
//			return;
//		}
		List<MusicItemVO> selItems = new ArrayList<MusicItemVO>();
		if (sel.size() > 0) {
			selItems = sel.toList();
		} else {
			//set default
			selItems.add(new MusicItemVO());
		}
	
		MusicItemEditor editor = new MusicItemEditor(s,selItems,null);
		editor.setCreateNewMode(true);
		
		editor.run();

	}
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		
		
	}
}
