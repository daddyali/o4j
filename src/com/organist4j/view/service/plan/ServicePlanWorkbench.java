package com.organist4j.view.service.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.ContentCreator;
import com.organist4j.view.action.service.plan.CloseAllServicesAction;
import com.organist4j.view.action.service.plan.CloseOtherServicesAction;

public class ServicePlanWorkbench implements ContentCreator {

	CTabFolder ct = null;	
	Map<ServiceVO,ServicePlan> servicePlans = new HashMap<ServiceVO,ServicePlan>();
	ApplicationWindow aw;
	
	public ServicePlanWorkbench(ApplicationWindow aw) {
		this.aw = aw;
	}

	public Control createContent(Composite parent) {




		ct = new CTabFolder(parent, SWT.TOP);
		ct.setSimple(false);
		ct.setBorderVisible(true);

		ct.setLayout(new GridLayout(1,true));
		ct.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		ct.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
				
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				CTabItem ti = ct.getSelection();
				ServiceVO service = (ServiceVO)ti.getData();
				ct.setSelectionBackground(service.getLectionary().getSeasonBackgroundColor(service));				
				ct.setSelectionForeground(service.getLectionary().getSeasonForegroundColor(service));
				
			}
			
		});
		
		
		MenuManager menu_manager = new MenuManager();
		ct.setMenu(menu_manager.createContextMenu(ct));

		CloseAllServicesAction cas = new CloseAllServicesAction(this);
		CloseOtherServicesAction cos = new CloseOtherServicesAction(this);
		
		menu_manager.add(cas);
		menu_manager.add(cos);

		return ct;
	}

	public void closeService(ServiceVO service) {
		ServicePlan plan = servicePlans.get(service);
		if (plan != null) {
			plan.closeService();
			servicePlans.remove(service);
		}
	}

	public void showService(ServiceVO service) {
		ServicePlan plan = servicePlans.get(service);
		if (plan == null) {
			plan = new ServicePlan(ct,aw,service);
			servicePlans.put(service, plan);
		}
		plan.showService();
		
	}

	public void refreshOpenService(ServiceVO service) {
		ServicePlan plan = servicePlans.get(service);
		if (plan != null) {
			plan.refreshOpenService();			
		}
		
	}

	public void closeAllServices(boolean keepCurrentOpen) {
		//Current tab
		CTabItem current = ct.getSelection();
		ServiceVO currentService = (ServiceVO)current.getData();
		List<ServiceVO> l = new ArrayList<ServiceVO>();
		l.addAll(servicePlans.keySet());
		Iterator<ServiceVO> it = l.iterator();
		while (it.hasNext()) {			
			ServiceVO s = it.next();
			
			if (keepCurrentOpen && s == currentService) {
				// do nothing
			} else {
				closeService(s);
			}
			
		}
		
	}









}
