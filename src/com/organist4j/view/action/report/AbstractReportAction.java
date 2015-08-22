package com.organist4j.view.action.report;


import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.organist4j.util.ServiceLocator;

public abstract class AbstractReportAction extends Action {
	Logger logger = Logger.getLogger(AbstractReportAction.class);
	ApplicationWindow window;
	Text title = null;
	Text notes = null;
	Label titleLabel = null;
	Label notesLabel = null;
	Canvas c = null;
	
	
	public AbstractReportAction(ApplicationWindow w) {
		window = w;

		setText(getActionText());
	}
	
	protected abstract String getActionText();
	protected abstract String getReportName();
	protected abstract void printReport(Device device,GC gc,Rectangle trim);
	protected abstract String getTitleLabel();
	protected abstract String getNotesLabel();
//	protected abstract String getTitleDefaultText();
//	protected abstract String getNotesDefaultText();
	
	protected abstract boolean isDefaultTitle();
	
	
	
	protected abstract void loadData();
	
	public void run() {
		loadData();
		Shell shell = new Shell(window.getShell().getDisplay(),SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout());
		
		shell.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				//save any settings
				if (title.getText() != null) ServiceLocator.getInstance().getPreferenceStore().putValue(getReportName() + ".title", title.getText());
				if (notes.getText() != null) ServiceLocator.getInstance().getPreferenceStore().putValue(getReportName() + ".notes", notes.getText());
			}
			
		});
		
		ScrolledComposite sc = new ScrolledComposite(shell,SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
		
		Composite comp = new Composite(sc,SWT.NONE);
		comp.setLayout(new GridLayout(1,true));
		
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setContent(comp);
		sc.setMinSize(comp.computeSize(500, 500));
		//sc.setAlwaysShowScrollBars(true);
		
		Composite header = new Composite(comp,SWT.NONE);
		header.setLayout(new GridLayout(5,false));
		
		
		Button b = new Button(header,SWT.NONE);
		b.setText("Print...");
		b.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				 
				
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				//DO the print
				PrintDialog pd = new PrintDialog(e.widget.getDisplay().getActiveShell());

				PrinterData data = pd.open();
				if (data == null) {
					logger.warn("Warning: No default printer.");
					return;
				}
				Printer printer = new Printer(data);
				if (printer.startJob(getReportName())) {
					
					Rectangle trim = printer.computeTrim(0, 0, 0, 0);
					GC gc = new GC(printer);

					if (printer.startPage()) {
						
						printReport(printer,gc,null);
						printer.endPage();
					}
					gc.dispose();
					printer.endJob();
				}
				printer.dispose();
				
			}
			
		});
		
		titleLabel = new Label(header,SWT.NONE);
		titleLabel.setText(getTitleLabel());
		
		title = new Text(header,SWT.BORDER);
		
		GridData gd = new GridData(SWT.LEFT,SWT.CENTER,false,false);
		gd.widthHint = 200;
		title.setLayoutData(gd);
		if (isDefaultTitle()) {
			title.setText(ServiceLocator.getInstance().getPreferenceStore().getString(getReportName() + ".title"));
		}
		title.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				c.redraw();
				
			}
			
		});
		
		
		notesLabel = new Label(header,SWT.NONE);
		notesLabel.setText(getNotesLabel());
		
		notes = new Text(header,SWT.BORDER);
		notes.setText(ServiceLocator.getInstance().getPreferenceStore().getString(getReportName() + ".notes"));
		gd = new GridData(SWT.LEFT,SWT.CENTER,true,false);
		gd.widthHint = 400;
		notes.setLayoutData(gd);
		notes.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				c.redraw();
				
			}
			
		});
		
		
		c = new Canvas(comp,SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		c.setLayoutData(gd);
		
		Printer p = new Printer();
		logger.debug(p.getPrinterData().name);
		logger.debug(p.getBounds() + " " + p.getDPI());
		logger.debug(p.getClientArea());
		Point screenDpi = comp.getDisplay().getDPI();
		Point printerDpi = p.getDPI();
		//Page width on screen
		int width = (p.getBounds().width / printerDpi.x) * screenDpi.x;
		int height = (p.getBounds().height / printerDpi.y) * screenDpi.y;
		logger.debug(width + " " + height);
		
		
		//c.setBounds(new Printer().getBounds());
		//c.setSize(width, height);
		c.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		c.addPaintListener(new ExampleReportPaintListener());
		
		
		
		Rectangle r = c.computeTrim(c.getBounds().x, c.getBounds().y, width, height);
		//c.setBounds(r);
		
		gd.minimumHeight = r.height;
		gd.minimumWidth = r.width;
		c.pack();
		shell.pack();
		shell.open();
		logger.debug(c.getClientArea());
		logger.debug(c.getSize());
		logger.debug(c.getBounds());
	}
	
	protected class ExampleReportPaintListener implements PaintListener {
		public void paintControl(PaintEvent e) {
			Canvas canvas = (Canvas)e.widget;
			Device device = e.display;
			Rectangle trim = canvas.getClientArea();
			GC gc = e.gc;
			printReport(device,gc,trim);

		}

	}
	
	
}