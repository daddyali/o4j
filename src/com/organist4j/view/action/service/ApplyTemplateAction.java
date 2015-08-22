package com.organist4j.view.action.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.organist4j.dao.ServiceDAO;
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.Node;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.service.explorer.ServiceExplorer;
import com.organist4j.view.service.plan.ServicePlanWorkbench;

public class ApplyTemplateAction extends Action implements ISelectionChangedListener {
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


	public ApplyTemplateAction(ApplicationWindow w) {
		window = w;
		setText("&Apply Template...@Ctrl+V");
	}
	public void run() {

		
		
		//Dialog here to capture the name and date of the service
		//and an (optional) template
		ApplyTemplateActionDialog dlg = new ApplyTemplateActionDialog(window.getShell(),this);
		
		int rc = dlg.open();
		if (rc == IDialogConstants.CANCEL_ID) {
			return;
		}
		
	
		
		//STORE THE SERVICE
		ServiceDAO serviceDAO = ServiceLocator.getInstance().getServiceDAO();		
		
	
		
		//If we have a template we need to APPLY it
		if (templateName != null) {
			ServiceVO templateNameService = new ServiceVO();
			templateNameService.setName(templateName);
			ServiceVO template = serviceDAO.getService(templateNameService);
			Iterator<ServiceElementVO> it = template.getServiceElements().iterator();
			ServiceElementVO se = null;
			ServiceElementVO tse = null;
			List<ServiceElementVO> seAlreadyLookedAt = new ArrayList<ServiceElementVO>();
			//service.setType(template.getType());
			while (it.hasNext()) {
				tse = it.next();
				
				//Look for the matching ServiceElement in the existing
				//service
				
				Iterator<ServiceElementVO> existingSes = selectedService.getServiceElements().iterator();
				while (existingSes.hasNext()) {
					se = existingSes.next();
					if (se.getName().equalsIgnoreCase(tse.getName()) && !seAlreadyLookedAt.contains(se)) {
						break;
					} else {
						se = null;
					}
				}
				if (se == null) {
					if (tse.getServiceItem() != null) {
						se = new ServiceElementVO(selectedService,tse.getServiceItem());
					} else {
						se = new ServiceElementVO(selectedService,null);
						se.setName(tse.getName());
					}
					selectedService.addServiceElement(se);
				}
				se.setComments(tse.getComments());
				se.setDesc(tse.getDesc());
				se.setMandatory(tse.getMandatory());
				se.setItem(tse.getItem());
				se.setTuneItem(tse.getTuneItem());
				seAlreadyLookedAt.add(se);			
			}
		}
		serviceDAO.updateService(selectedService);
		
		//Refresh the ServiceBrowser
		ServiceExplorer sex = ServiceLocator.getInstance().getServiceExplorer();
		sex.reload(selectedService);
		
		//Get hold of the ServicePlanWorkbench
		ServicePlanWorkbench sp = (ServicePlanWorkbench)window.getShell().getDisplay().getData("ServicePlan");
		
		sp.closeService(selectedService);
		sp.showService(selectedService);
		
	}


	public class ApplyTemplateActionDialog extends TitleAreaDialog implements ModifyListener, SelectionListener {

		ApplyTemplateAction action;
		public ApplyTemplateActionDialog(Shell shell,ApplyTemplateAction action) {
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
			setTitle("Apply Template");

			// Set the message
			setMessage("Choose Template Name", IMessageProvider.INFORMATION);

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
				
				if (action.getSelectedService().getTemplateName() != null) {
					combo1.select( combo1.indexOf("No template") );
					
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
			

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			 
			
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			
		
			
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
