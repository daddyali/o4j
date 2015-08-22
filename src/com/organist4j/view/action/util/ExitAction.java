package com.organist4j.view.action.util;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.ApplicationWindow;

public class ExitAction extends Action {
	ApplicationWindow window;
	public ExitAction(ApplicationWindow w) {
		window = w;
		setText("E&xit");
	}
	public void run() {
		window.close();
	}
}
