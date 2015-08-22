package com.organist4j.view.action.music;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Shell;

import com.organist4j.util.ServiceLocator;
import com.organist4j.view.music.editor.MusicItemEditor;
import com.organist4j.view.music.item.MusicItemBrowser;

public class OpenMusicItemAction extends Action implements ISelectionChangedListener {
	//ApplicationWindow window;
	Shell s;
	public OpenMusicItemAction(ApplicationWindow w) {
		super();
		s = w.getShell();
		setText("&Open Music Item...");
	}
	public OpenMusicItemAction(TableViewer tbvItemBrowser) {
		super();
		s = tbvItemBrowser.getTable().getShell();
		setText("&Open Music Item...");
	}
	public void run() {
		//Need to get a music item
		//Pick the one that's selected in the browser
		
		MusicItemBrowser tv = ServiceLocator.getInstance().getMusicItemBrowser();
		IStructuredSelection sel = (IStructuredSelection) tv.getTbvItemBrowser().getSelection();
		if (sel.size() <= 0) {
			return;
		}
		
		MusicItemEditor editor = new MusicItemEditor(s,sel.toList(),null);
		editor.run();

	}
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		
		
	}
}
