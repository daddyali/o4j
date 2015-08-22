package com.organist4j.view.action.util;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.window.ApplicationWindow;

import com.organist4j.util.ServiceLocator;

public class PreferencesAction extends Action {
	ApplicationWindow window;
	public PreferencesAction(ApplicationWindow w) {
		window = w;
		setText("&Preferences");
	}
	public void run() {
		PreferenceDialog dlg = new PreferenceDialog(null,ServiceLocator.getInstance().getPreferenceManager());
		dlg.setPreferenceStore(ServiceLocator.getInstance().getPreferenceStore());
		dlg.open();
	
	}
}
