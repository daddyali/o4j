package com.organist4j.view.action.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.organist4j.dao.ServiceDAO;
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.Node;
import com.organist4j.util.ServiceLocator;
import com.organist4j.util.ToolTipHelper;
import com.organist4j.view.service.explorer.ServiceExplorer;
import com.organist4j.view.service.plan.ServicePlanWorkbench;

public class CopyServiceAction extends Action implements ISelectionChangedListener {
	ApplicationWindow window;
	String name;
	String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	String templateName;
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


	public CopyServiceAction(ApplicationWindow w) {
		window = w;
		setText("&Copy Service...@Ctrl+C");
	}
	public void run() {

		dateTime = new Date();
		if (selectedService != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(selectedService.getDateTime());
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			cal.add(Calendar.YEAR, 1);
			cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
			
			dateTime = cal.getTime();
//			if (selectedService.getTemplateName() != null) {
//				templateName = selectedService.getTemplateName();
//			}
		}
		
		//Dialog here to capture the name and date of the service
		//and an (optional) template
		CopyServiceDialog dlg = new CopyServiceDialog(window.getShell(),this);
		
		int rc = dlg.open();
		if (rc == IDialogConstants.CANCEL_ID) {
			return;
		}
		
		//Now we can create and store the basic service object
		ServiceVO service = new ServiceVO();
		service.setName(name);
		service.setType(type);
		service.setDateTime(dateTime);
		if (selectedService.getTemplateName() != null) {
			service.setTemplateName(selectedService.getTemplateName());
		}

		service.setTemplate(new Boolean(false));
		
		//STORE THE SERVICE
		ServiceDAO serviceDAO = ServiceLocator.getInstance().getServiceDAO();		
		
		//ServiceVO existing = serviceDAO.getService(name);
		ServiceVO existing = serviceDAO.getService(service);
		if (existing != null) {
			return;
		}
		
		//Copy details from source service
		Iterator<ServiceElementVO> it = selectedService.getServiceElements().iterator();
		ServiceElementVO se = null;
		ServiceElementVO tse = null;
		service.setType(selectedService.getType());
		while (it.hasNext()) {
			tse = it.next();
			if (tse.getServiceItem() != null) {
				se = new ServiceElementVO(service,tse.getServiceItem());
			} else {
				se = new ServiceElementVO(service,null);
				se.setName(tse.getName());
			}
			se.setComments(tse.getComments());
			se.setDesc(tse.getDesc());
			se.setMandatory(tse.getMandatory());
			se.setItem(tse.getItem());
			se.setTuneItem(tse.getTuneItem());
			service.addServiceElement(se);				
		}
	
		serviceDAO.updateService(service);
		
		//If we have a template we need to APPLY it
		if (templateName != null) {
			ServiceVO templateNameService = new ServiceVO();
			templateNameService.setName(templateName);
			ServiceVO template = serviceDAO.getService(templateNameService);
			it = template.getServiceElements().iterator();
			se = null;
			tse = null;
			//service.setType(template.getType());
			while (it.hasNext()) {
				tse = it.next();
				
				//Look for the matching ServiceElement in the existing
				//service
				
				Iterator<ServiceElementVO> existingSes = service.getServiceElements().iterator();
				while (existingSes.hasNext()) {
					se = existingSes.next();
					if (se.getName().equalsIgnoreCase(tse.getName())) {
						break;
					} else {
						se = null;
					}
				}
				if (se == null) {
					if (tse.getServiceItem() != null) {
						se = new ServiceElementVO(service,tse.getServiceItem());
					} else {
						se = new ServiceElementVO(service,null);
						se.setName(tse.getName());
					}
					service.addServiceElement(se);
				}
				se.setComments(tse.getComments());
				se.setDesc(tse.getDesc());
				se.setMandatory(tse.getMandatory());
				se.setItem(tse.getItem());
				se.setTuneItem(tse.getTuneItem());
								
			}
		}
		serviceDAO.updateService(service);
		
		//Refresh the ServiceBrowser
		ServiceExplorer sex = ServiceLocator.getInstance().getServiceExplorer();
		sex.reload(service);
		
		//Get hold of the ServicePlanWorkbench
		ServicePlanWorkbench sp = (ServicePlanWorkbench)window.getShell().getDisplay().getData("ServicePlan");
		sp.showService(service);
	}


	public class CopyServiceDialog extends TitleAreaDialog implements ModifyListener, SelectionListener {

		CopyServiceAction action;
		public CopyServiceDialog(Shell shell,CopyServiceAction action) {
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
			setTitle("Copy Service");

			// Set the message
			setMessage("Check and amend name and a date for the service", IMessageProvider.INFORMATION);

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
			nameT.setToolTipText(ToolTipHelper.getServiceName());
			nameT.setData("name");			
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
			
			Label tempL = new Label(d, SWT.NONE);
			tempL.setText("Apply Template");
			Combo combo1 = new Combo(d, SWT.VERTICAL |
				       SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
			Iterator<ServiceVO> it = ServiceLocator.getInstance().getServiceDAO().getServiceTemplates().iterator();
		    combo1.add("No template");
		    while (it.hasNext()) {
		    	combo1.add(it.next().getName());
		    }
		    combo1.addSelectionListener(this);
			
		    if (action.getSelectedService() != null) {
				nameT.setText(action.getSelectedService().getName());
				if (action.getSelectedService().getType() != null) typeT.setText(action.getSelectedService().getType());
				if (action.getSelectedService().getTemplateName() != null) {
					combo1.select( combo1.indexOf("No template") );
					
				}
				if (action.getSelectedService().getDateTime() != null) {
					Calendar cal = Calendar.getInstance();
//					cal.setTime(action.getSelectedService().getDateTime());
//					cal.add(Calendar.DATE, 7);
					cal.setTime(action.getDateTime());
		
					timeD.setTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), 0);
					dateD.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
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
			
			if (e.widget instanceof Combo) {
			   Combo c = (Combo)e.widget;
			   if (c.getText() != null && !c.getText().equalsIgnoreCase("No Template")) {
				   templateName = c.getText();
			   } else {
				   templateName = null;
			   }
			}
		}
	}


	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		IStructuredSelection s = (IStructuredSelection)arg0.getSelection();
		Node n = (Node)s.getFirstElement();
		if (n == null) return;
		if (n.getData() instanceof ServiceVO) {					
			this.setEnabled(true);	
			selectedService = (ServiceVO)n.getData();
			
		} else {
			this.setEnabled(false);
		}
		
	}

}
