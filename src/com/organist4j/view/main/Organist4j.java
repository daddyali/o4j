package com.organist4j.view.main;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.organist4j.util.Node;
import com.organist4j.util.O4J;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.action.music.DeleteMusicItemsAction;
import com.organist4j.view.action.music.DeleteRelatedMusicItemsAction;
import com.organist4j.view.action.music.ExportMusicItemsAction;
import com.organist4j.view.action.music.ExportServicesAction;
import com.organist4j.view.action.music.ImportMusicItemsAction;
import com.organist4j.view.action.music.NewMusicItemAction;
import com.organist4j.view.action.music.OpenMusicItemAction;
import com.organist4j.view.action.music.RefreshMusicItemAction;
import com.organist4j.view.action.music.RelateMusicItemsAction;
import com.organist4j.view.action.music.ShowRelatedMusicItemsAction;
import com.organist4j.view.action.report.MonthlyServiceReport2;
import com.organist4j.view.action.report.MusicItemReport;
import com.organist4j.view.action.report.RehearsalReport2;

import com.organist4j.view.action.report.ResourceReport;
import com.organist4j.view.action.report.ServicePlanReport;
import com.organist4j.view.action.service.NewServiceAction;
import com.organist4j.view.action.service.item.AddServiceItemAction;

import com.organist4j.view.action.util.AboutAction;
import com.organist4j.view.action.util.ExitAction;
import com.organist4j.view.action.util.PreferencesAction;
import com.organist4j.view.service.plan.ServicePlanWorkbench;


public class Organist4j extends ApplicationWindow {
	static Logger logger = Logger.getLogger(Organist4j.class);
	static boolean readyToDisplay = false;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		new Organist4j().run();

	}
	public Organist4j() throws Exception {
		super(null);
	}

	public void run() {

		try {
	
			//ServiceLocator.getInstance().init();
			logger.info("Initialise");
			
			
			
			setBlockOnOpen(true);

			this.addMenuBar();
			
			

			//this.addCoolBar(SWT.NONE);

//			this.addStatusLine();



			open();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			

			//Clean up any resources and the database
			try { ServiceLocator.getInstance().dispose(); } catch (Exception ig) {}
			
			//Get rid of the display resources
			try { Display.getCurrent().dispose(); } catch (Exception ig) {}
		}

		
	}

	protected Control createContents(Composite parent) {
		

		//doSplashScreen();
		new AboutAction(this).run();
		try {
			ServiceLocator.getInstance().init();
		} catch (InstantiationException e) {
			 
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			 
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			 
			e.printStackTrace();
		} catch (SQLException e) {
			 
			e.printStackTrace();
		}
		
		ServiceLocator.getInstance().loadImages();
		
		if (ServiceLocator.getInstance().getPreferenceStore().contains(O4J.WINDOW_WIDTH)) {
			getShell().setSize(ServiceLocator.getInstance().getPreferenceStore().getInt(O4J.WINDOW_WIDTH),
						ServiceLocator.getInstance().getPreferenceStore().getInt(O4J.WINDOW_HEIGHT));
		}
		if (ServiceLocator.getInstance().getPreferenceStore().contains(O4J.WINDOW_LOCX)) {
			getShell().setLocation(ServiceLocator.getInstance().getPreferenceStore().getInt(O4J.WINDOW_LOCX),
						ServiceLocator.getInstance().getPreferenceStore().getInt(O4J.WINDOW_LOCY));
		}
		
		setDefaultImage(ServiceLocator.getInstance().getImgRef().get(O4J.IMG_ICON));

		getShell().setImage(ServiceLocator.getInstance().getImgRef().get(O4J.IMG_ICON));
		getShell().setText(O4J.APPLICATION_NAME + " " + O4J.APPLICATION_VERSION);	
		//getShell().setMaximized(true);
		
		parent.getDisplay().setData("AW",this);
		


		//Create a three pane SWT.SMOOTH
		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL | SWT.SMOOTH);
		ExplorerWorkbench se = new ExplorerWorkbench(this);
		
		se.createContent(sashForm);

		SashForm sashForm2 = new SashForm(sashForm, SWT.VERTICAL | SWT.SMOOTH);	
		sashForm.setWeights(new int[]{25,75});
		sashForm.SASH_WIDTH = 3;
		ServicePlanWorkbench sp = new ServicePlanWorkbench(this);
		parent.getDisplay().setData("ServicePlan", sp);
		sp.createContent(sashForm2);
		ItemWorkbench iwb = new ItemWorkbench(this);
		iwb.createContent(sashForm2);
		parent.getDisplay().setData("ItemWorkbench", iwb);
		sashForm2.setWeights(new int[]{60,40});
		sashForm2.SASH_WIDTH = 3;


		//		
		//		Canvas rhs1 = new Canvas(rhs, SWT.NONE);
		//		Canvas rhs2 = new Canvas(rhs, SWT.NONE);

		
		
		return sashForm;
		//return ContentFactory.getInstance().getDefaultContentCreator().createContent(parent);	
		
		
		
		

	}
	
	
	
	private void doSplashScreen() {
		final Display display = this.getShell().getDisplay();
		
		//Things to do
		
		
		final Image image = ImageDescriptor.createFromFile(null, "images/splash.gif").createImage();
//		GC gc = new GC(image);
//		gc.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
//		gc.fillRectangle(image.getBounds());
//		gc.drawText("organist4j", 10, 10);
//		gc.dispose();
		final Shell splash = new Shell(SWT.ON_TOP);
//		final ProgressBar bar = new ProgressBar(splash, SWT.NONE);
//		bar.setMaximum(count[0]);
//		Label label = new Label(splash, SWT.NONE);
//		label.setImage(image);
		GridLayout layout = new  GridLayout(2,false);
		splash.setLayout(layout);
		Label l = new Label(splash,SWT.NONE);
		l.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,false,false));
		l.setImage(image);
		
			
		
		Composite cc = new Composite(splash,SWT.NONE);
		cc.setLayout(new GridLayout(1,false));
		
		final Font font1 = new Font(Display.getCurrent(), "Calibri", 40, SWT.BOLD);
		l = new Label(cc,SWT.NONE);	
		l.setFont(font1);
		l.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,false,false));
		l.setText(O4J.APPLICATION_NAME);
		
		
		final Font font2 = new Font(Display.getCurrent(), "Calibri", 12, SWT.BOLD);
		
		
		l = new Label(cc,SWT.NONE);	
		l.setFont(font2);
		l.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,false,false));
		l.setText("Alistair McCormick");
		
		l = new Label(cc,SWT.NONE);	
		l.setFont(font2);
		l.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,false,false));
		l.setText("Version " + O4J.APPLICATION_VERSION);
//		FormData labelData = new FormData ();
//		labelData.right = new FormAttachment (100, 0);
//		labelData.bottom = new FormAttachment (100, 0);
//		label.setLayoutData(labelData);
//		FormData progressData = new FormData ();
//		progressData.left = new FormAttachment (0, 5);
//		progressData.right = new FormAttachment (100, -5);
//		progressData.bottom = new FormAttachment (100, -5);
//		bar.setLayoutData(progressData);
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
					Thread.currentThread().sleep(3000);
				} catch (InterruptedException e) {
					 
					e.printStackTrace();
				}
				
			
				splash.close();
				image.dispose();
				font1.dispose();
				font2.dispose();
				
			}
		});
		
		
		
	}
	@Override
	public boolean close() {
		
		
		//Save window geometry
		try {
			
			PreferenceStore ps = ServiceLocator.getInstance().getPreferenceStore();
			ps.putValue(O4J.WINDOW_WIDTH,this.getShell().getSize().x + "");
			ps.putValue(O4J.WINDOW_HEIGHT,this.getShell().getSize().y + "");
			ps.putValue(O4J.WINDOW_LOCX, this.getShell().getLocation().x + "");
			ps.putValue(O4J.WINDOW_LOCY, this.getShell().getLocation().y + "");
			
			
			ps.save();
		} catch (Exception ig) { ig.printStackTrace(); }
		return super.close();
		
	}
	
	
	
	@Override
	protected MenuManager createMenuManager() {
		MenuManager barMenu = new MenuManager("");

		MenuManager fileMenu = new MenuManager("&File");
		MenuManager editMenu = new MenuManager("&Edit");
		MenuManager viewMenu = new MenuManager("&View");
		
		MenuManager helpMenu = new MenuManager("&Help");

		barMenu.add(fileMenu);
		barMenu.add(editMenu);
		barMenu.add(viewMenu);

		barMenu.add(helpMenu);

		fileMenu.add(new NewServiceAction(this));
		fileMenu.add(new NewMusicItemAction(this));
		fileMenu.add(new AddServiceItemAction(this));
		fileMenu.add(new Separator());
		fileMenu.add(new ExitAction(this));

		
		editMenu.add(new ImportMusicItemsAction(this));
		editMenu.add(new Separator());
		editMenu.add(new ExportMusicItemsAction(this));
		editMenu.add(new ExportServicesAction(this));
		editMenu.add(new Separator());
		editMenu.add(new PreferencesAction(this));
//		editMenu.add(new OpenMusicItemAction(this));

		viewMenu.add(new MonthlyServiceReport2(this));
		
		viewMenu.add(new ServicePlanReport(this));
		viewMenu.add(new Separator());
		viewMenu.add(new ResourceReport(this));
		viewMenu.add(new MusicItemReport(this));
		viewMenu.add(new RehearsalReport2(this));
		
	
//		OpenMusicItemAction tsem = new OpenMusicItemAction(this);
//	    NewMusicItemAction nmi = new NewMusicItemAction(this);
//		DeleteMusicItemsAction dmia = new DeleteMusicItemsAction(this);
//		RefreshMusicItemAction rmia = new RefreshMusicItemAction(this);
//		RelateMusicItemsAction rel = new RelateMusicItemsAction(this);
//		DeleteRelatedMusicItemsAction delrel = new DeleteRelatedMusicItemsAction(this);
//		ShowRelatedMusicItemsAction showrel = new ShowRelatedMusicItemsAction(this);
//		
//		
//		ServiceLocator.getInstance().getMusicItemBrowser().getTbvItemBrowser().addSelectionChangedListener(tsem);
//		ServiceLocator.getInstance().getMusicItemBrowser().getTbvItemBrowser().addSelectionChangedListener(nmi);
//		ServiceLocator.getInstance().getMusicItemBrowser().getTbvItemBrowser().addSelectionChangedListener(dmia);
//		ServiceLocator.getInstance().getMusicItemBrowser().getTbvItemBrowser().addSelectionChangedListener(rmia);
//		ServiceLocator.getInstance().getMusicItemBrowser().getTbvItemBrowser().addSelectionChangedListener(rel);
//		ServiceLocator.getInstance().getMusicItemBrowser().getTbvItemBrowser().addSelectionChangedListener(delrel);	    
//		ServiceLocator.getInstance().getMusicItemBrowser().getTbvItemBrowser().addSelectionChangedListener(showrel);
//		
//		musicMenu.add(tsem);
//		musicMenu.add(nmi);
//		musicMenu.add(dmia);
//		musicMenu.add(new Separator());
//		musicMenu.add(rmia);
//		musicMenu.add(new Separator());
//		musicMenu.add(rel);
//		musicMenu.add(delrel);
//		musicMenu.add(showrel);

		helpMenu.add(new AboutAction(this));
		return barMenu;
	}









}
