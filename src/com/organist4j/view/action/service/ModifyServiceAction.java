package com.organist4j.view.action.service;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.organist4j.dao.ServiceDAO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.Node;
import com.organist4j.util.ServiceLocator;
import com.organist4j.util.ToolTipHelper;
import com.organist4j.view.service.explorer.ServiceExplorer;
import com.organist4j.view.service.plan.ServicePlanWorkbench;

public class ModifyServiceAction extends  Action implements ISelectionChangedListener {
	ApplicationWindow window;
	
	String name;
	String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	
	ServiceVO selectedService = null;
	
	public ServiceVO getSelectedService() {
		return selectedService;
	}
	public void setSelectedService(ServiceVO selectedService) {
		this.selectedService = selectedService;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	Date dateTime;

	public ModifyServiceAction(ApplicationWindow w) {
		window = w;
	
		setText("&Modify Service...@Ctrl+M");
	}
	public void run() {
		
		dateTime = null;
		if (selectedService != null) {
			dateTime = selectedService.getDateTime();
	
		}
		
		//Dialog here to capture the name and date of the service
		//and an (optional) template
		ModifyServiceDialog dlg = new ModifyServiceDialog(window.getShell(),this);
		
		int rc = dlg.open();
		if (rc == IDialogConstants.CANCEL_ID) {
			return;
		}
		
		//Now we can modify and store the basic service object		
		selectedService.setName(name);
		selectedService.setType(type);
		selectedService.setDateTime(dateTime);
		
		
		
		//update THE SERVICE
		ServiceDAO serviceDAO = ServiceLocator.getInstance().getServiceDAO();				
		serviceDAO.updateService(selectedService);
		
		//Refresh the ServiceBrowser
		ServiceExplorer se = ServiceLocator.getInstance().getServiceExplorer();
		se.reload(selectedService);
		
		//Get hold of the ServicePlanWorkbench
		ServicePlanWorkbench sp = (ServicePlanWorkbench)window.getShell().getDisplay().getData("ServicePlan");
		sp.refreshOpenService(selectedService);
		
		
	
	}
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		IStructuredSelection s = (IStructuredSelection)arg0.getSelection();
		Node n = (Node)s.getFirstElement();
		if (n == null) return;
		if (n.getData() instanceof ServiceVO) {					
			this.setEnabled(true);	
			selectedService = (ServiceVO)n.getData();
			if (selectedService.isTemplate()) {
				setText("&Modify Template...");
			} else {
				setText("&Modify Service...");
			}
		} else {
			this.setEnabled(false);
		}
		
	}
	
	
	
	public class ModifyServiceDialog extends TitleAreaDialog implements ModifyListener, SelectionListener {

		ModifyServiceAction action;
		public ModifyServiceDialog(Shell shell,ModifyServiceAction action) {
			super(shell);
			this.action = action;
		}

		/**
		 * Closes the dialog box Override so we can dispose the image we created
		 */
		public boolean close() {
			return super.close();
		}

		/**
		 * Creates the dialog's contents
		 * 
		 * @param parent the parent composite
		 * @return Control
		 */
		protected Control createContents(Composite parent) {
			Control contents = super.createContents(parent);

			// Set the title
			setTitle("Modify Service");
			// Set the message
			setMessage("You can modify the name, type or date/time for the service", IMessageProvider.INFORMATION);

			if (selectedService.isTemplate()) {
				setMessage("You can modify the name and type for the template", IMessageProvider.INFORMATION);

			}

			
			return contents;
		}

		/**
		 * Creates the gray area
		 * 
		 * @param parent the parent composite
		 * @return Control
		 */
		protected Control createDialogArea(Composite parent) {
			Composite composite = (Composite) super.createDialogArea(parent);

			Composite d = new Composite(composite, SWT.NONE);
			d.setLayout(new GridLayout(2,false));
			
			Label nameL = new Label(d, SWT.LEFT);
			nameL.setText("Name");
			Text nameT = new Text(d,SWT.BORDER);
			nameT.setData("name");	
			nameT.setToolTipText(ToolTipHelper.getServiceName());
			GridData gd = new GridData();
			gd.grabExcessHorizontalSpace = true;
			gd.horizontalAlignment = SWT.FILL;
			nameT.setLayoutData(gd);
			nameT.addModifyListener(this);
			
			Label typeL = new Label(d, SWT.LEFT);
			typeL.setText("Type");
			Text typeT = new Text(d,SWT.BORDER);
			typeT.setData("type");
			typeT.setToolTipText(ToolTipHelper.getServiceType());
			gd = new GridData();
			gd.grabExcessHorizontalSpace = true;
			gd.horizontalAlignment = SWT.FILL;
			typeT.setLayoutData(gd);
			typeT.addModifyListener(this);
			
			if (!selectedService.isTemplate()) {
			
				Label dateL = new Label(d, SWT.NONE);
				dateL.setText("Date");
				DateTime dateD = new DateTime(d, SWT.CALENDAR); 
				dateD.setData("date");
				dateD.addSelectionListener(this);

				Label timeL = new Label(d, SWT.NONE);
				timeL.setText("Time");
				DateTime timeD = new DateTime(d, SWT.TIME); 
				timeD.setData("time");
				timeD.setSeconds(0);
				timeD.addSelectionListener(this);
				
				if (action.getSelectedService().getDateTime() != null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(action.getSelectedService().getDateTime());
		
					timeD.setTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), 0);
					dateD.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
				}
			
			}
			
			
			
		    if (action.getSelectedService() != null) {
		    	if (action.getSelectedService().getName() != null) {
		    		nameT.setText(action.getSelectedService().getName());
		    	}
				if (action.getSelectedService().getType() != null) {
					typeT.setText(action.getSelectedService().getType());
				}
				
			}


			d.pack();


			return composite;
		}

		/**
		 * Creates the buttons for the button bar
		 * 
		 * @param parent the parent composite
		 */
		protected void createButtonsForButtonBar(Composite parent) {
			createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
			createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
			
		}

		@Override
		public void modifyText(ModifyEvent e) {
			if (e.widget.getData().toString().equals("name")) {
				action.setName(((Text)e.widget).getText());
			} 
			
			if (e.widget.getData().toString().equals("type")) {
				action.setType(((Text)e.widget).getText());
			} 

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			 
			
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			
			if (e.widget instanceof DateTime) {

				Calendar cal = Calendar.getInstance();
				if (dateTime != null) {
					cal.setTime(dateTime);
				}
				if (e.widget.getData().toString().equals("date")) {
					DateTime d = (DateTime)e.widget;
					cal.set(Calendar.DATE, d.getDay());
					cal.set(Calendar.MONTH, d.getMonth());
					cal.set(Calendar.YEAR, d.getYear());
				} 
				if (e.widget.getData().toString().equals("time")) {
					DateTime d = (DateTime)e.widget;
					cal.set(Calendar.HOUR_OF_DAY, d.getHours());
					cal.set(Calendar.MINUTE, d.getMinutes());

				}
				dateTime = cal.getTime();
			}
			
			
		}
	}
}
