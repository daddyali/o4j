package com.organist4j.view.action.service.item;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.organist4j.model.service.ServiceItemVO;
import com.organist4j.util.ServiceLocator;

public class AddServiceItemAction extends Action  {
	ApplicationWindow window;
	String name;
	String shortName;



	String desc;
	boolean mandatory = false;
	boolean checkProximity = false;

	public boolean isCheckProximity() {
		return checkProximity;
	}
	public void setCheckProximity(boolean checkProximity) {
		this.checkProximity = checkProximity;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}



	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public boolean getMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public AddServiceItemAction(ApplicationWindow w) {
		window = w;


		setText("&New Service Item...@Ctrl+I");
	}
	public void run() {



		//Dialog here to capture the name and date of the service
		//and an (optional) template
		NewServiceItemDialog dlg = new NewServiceItemDialog(window.getShell(),this);
		int rc = dlg.open();
		if (rc == IDialogConstants.CANCEL_ID) {
			return;
		}
		
		if (name != null && name.trim().length() > 0) {
			//continue
		} else {
			return;
		}

		//Now we can create and store the basic service object
		ServiceItemVO si = new ServiceItemVO(name,mandatory,checkProximity);

		si.setShortName(shortName);
		si.setDesc(desc);
		




		//STORE THE SERVICE ITEM
		ServiceLocator.getInstance().getServiceDAO().updateServiceItem(si);		

		ServiceLocator.getInstance().getServiceItemBrowser().refresh(true);

	}


	public class NewServiceItemDialog extends TitleAreaDialog implements ModifyListener, SelectionListener {

		AddServiceItemAction action;
		public NewServiceItemDialog(Shell shell,AddServiceItemAction action) {
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
			setTitle("Create New Service Item");

			// Set the message
			setMessage("Choose a name and details for the service item", IMessageProvider.INFORMATION);

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
			nameL.setText(ServiceItemVO.LABELS[ServiceItemVO.NAME_POS]);
			Text nameT = new Text(d,SWT.BORDER);
			nameT.setData(""+ServiceItemVO.NAME_POS);			
			GridData gd = new GridData();
			gd.widthHint = 200;
			nameT.setLayoutData(gd);
			nameT.addModifyListener(this);

			Label descL = new Label(d, SWT.LEFT);
			descL.setText(ServiceItemVO.LABELS[ServiceItemVO.DESC_POS]);
			Text descT = new Text(d,SWT.BORDER);
			descT.setData(""+ServiceItemVO.DESC_POS);	
			gd = new GridData();
			gd.grabExcessHorizontalSpace = true;
			gd.horizontalAlignment = SWT.FILL;
			descT.setLayoutData(gd);
			descT.addModifyListener(this);

			Label shortL = new Label(d, SWT.LEFT);
			shortL.setText(ServiceItemVO.LABELS[ServiceItemVO.SHORTNAME_POS]);
			Text shortT = new Text(d,SWT.BORDER);
			shortT.setData(""+ServiceItemVO.SHORTNAME_POS);			
			gd = new GridData();
			gd.widthHint = 100;
			shortT.setLayoutData(gd);
			shortT.addModifyListener(this);
			
			Label mandL = new Label(d, SWT.LEFT);
			mandL.setText(ServiceItemVO.LABELS[ServiceItemVO.MANDATORY_POS]);
			Button mandC = new Button(d,SWT.CHECK);
			
			mandC.setData(""+ServiceItemVO.MANDATORY_POS);			
			//gd = new GridData();
			//gd.widthHint = 100;
			//mandC.setLayoutData(gd);
			mandC.addSelectionListener(this);
			
			
			Label cpL = new Label(d, SWT.LEFT);
			cpL.setText(ServiceItemVO.LABELS[ServiceItemVO.CHECKPROXIMITY_POS]);
			Button cpC = new Button(d,SWT.CHECK);
			
			cpC.setData(""+ServiceItemVO.CHECKPROXIMITY_POS);			
			//gd = new GridData();
			//gd.widthHint = 100;
			//mandC.setLayoutData(gd);
			cpC.addSelectionListener(this);
			



			return composite;
		}

		/**
		 * Creates the buttons for the button bar
		 * 
		 * @param parent the parent composite
		 */
		protected void createButtonsForButtonBar(Composite parent) {
			createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
			createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);

		}

		@Override
		public void modifyText(ModifyEvent e) {
			if (e.widget.getData().toString().equals(""+ServiceItemVO.NAME_POS)) {
				action.setName(((Text)e.widget).getText());
			} 
			
			if (e.widget.getData().toString().equals(""+ServiceItemVO.DESC_POS)) {
				action.setDesc(((Text)e.widget).getText());
			} 
			
			if (e.widget.getData().toString().equals(""+ServiceItemVO.SHORTNAME_POS)) {
				action.setShortName(((Text)e.widget).getText());
			}
			
			
			
			

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			 
			
		}

		@Override
		public void widgetSelected(SelectionEvent e) {

			
			if (e.widget.getData().toString().equals(""+ServiceItemVO.MANDATORY_POS)) {
				action.setMandatory(((Button)e.widget).getSelection());
			}
			if (e.widget.getData().toString().equals(""+ServiceItemVO.CHECKPROXIMITY_POS)) {
				action.setCheckProximity(((Button)e.widget).getSelection());
			}
			
		}
	}




}

