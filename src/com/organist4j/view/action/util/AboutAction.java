package com.organist4j.view.action.util;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.organist4j.util.O4J;

public class AboutAction extends Action {
	ApplicationWindow window;
	boolean dismiss = false;
	
	
	public AboutAction(ApplicationWindow w) {
		window = w;
		setText("&About");
	}

	
	public void run() {
		
		Display display  = window.getShell().getDisplay();
		
		Color cyan = Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
		Color black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		Color white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		Color blue = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		Color grey = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
	
		final Image image = ImageDescriptor.createFromFile(null, "images/splash.gif").createImage();

		final Shell splash = new Shell(SWT.ON_TOP);

		GridLayout layout = new  GridLayout(2,false);
		splash.setLayout(layout);
		splash.setBackground(cyan);
		Label l = new Label(splash,SWT.NONE);
		l.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,false,false));
		l.setImage(image);
		
			
		
		Composite cc = new Composite(splash,SWT.NONE);
		cc.setBackground(cyan);
		cc.setLayout(new GridLayout(1,false));
		
		final Font font1 = new Font(Display.getCurrent(), "Calibri", 40, SWT.BOLD);
		l = new Label(cc,SWT.NONE);	
		l.setFont(font1);
		l.setBackground(cyan);
		l.setForeground(blue);
		l.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,true,false));
		l.setText(O4J.APPLICATION_NAME);
		
		
		final Font font2 = new Font(Display.getCurrent(), "Calibri", 12, SWT.BOLD);
		
		
		l = new Label(cc,SWT.NONE);	
		l.setFont(font2);
		l.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,true,false));
		l.setBackground(cyan);
		l.setText("(C)2015 Alistair McCormick");
		
		final Font font3 = new Font(Display.getCurrent(), "Calibri", 15, SWT.ITALIC | SWT.BOLD);
		l = new Label(cc,SWT.NONE);	
		l.setFont(font3);
		l.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,true,false));
		l.setForeground(grey);
		l.setBackground(cyan);
		l.setText("Version " + O4J.APPLICATION_VERSION);
		
//		final Text status = new Text(splash,SWT.NONE);
//		status.setEditable(false);
//		status.setBackground(cyan);
//		status.setForeground(black);
//		status.setText("***");
//		status.setFont(font2);
//		status.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,true,false));
//		
//
//		
//		final ProgressBar bar = new ProgressBar(splash, SWT.NONE);
//		bar.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,true,true));
//		bar.setMaximum(10);
		
		
	
		

		splash.pack();
		Rectangle splashRect = splash.getBounds();
		Rectangle displayRect = display.getBounds();
		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		splash.setLocation(x, y);

	
		splash.open();
		display.asyncExec(new Runnable() {
			public void run() {
				
//				bar.setSelection(4);
				
				
				try {
					for (int i=1;i<=4;i++) {
		
						if (dismiss) break;						
						Thread.sleep(500);
						//bar.setSelection(i);
						
						
						
						
					}
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				
			
				splash.close();
				image.dispose();
				font1.dispose();
				font2.dispose();
				font3.dispose();
				
			}
		});
	}
	
	

}
