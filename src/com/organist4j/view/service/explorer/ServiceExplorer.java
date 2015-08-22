package com.organist4j.view.service.explorer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Control;

import com.organist4j.dao.ServiceDAO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.Node;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.action.service.ApplyTemplateAction;
import com.organist4j.view.action.service.CopyServiceAction;
import com.organist4j.view.action.service.DeleteServiceAction;
import com.organist4j.view.action.service.ModifyServiceAction;
import com.organist4j.view.action.service.NewServiceAction;
import com.organist4j.view.action.service.OpenServiceAction;
import com.organist4j.view.action.service.SaveAsTemplateAction;

public class ServiceExplorer  {

	TreeViewer tv = null;
	public TreeViewer getTreeViewer() {
		return tv;
	}

	DeleteServiceAction dse = null;
	SaveAsTemplateAction sata = null;
	ApplicationWindow aw = null;
	public ServiceExplorer(ApplicationWindow aw) {
		this.aw = aw;
		ServiceLocator.getInstance().setServiceExplorer(this);
	}

	public Control createTabContent(CTabFolder ct) {
		
		tv = new TreeViewer(ct);
		//tv.getTree().setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		
		tv.setContentProvider(new ServiceExplorerContentProvider());
		tv.setLabelProvider(new ServiceExplorerDecoratingLabelProvider(new ServiceExplorerLabelProvider(), null));
//
//		ServiceVO serviceKey = new ServiceVO();
//		try {
//			//serviceKey.setDateTime(new SimpleDateFormat("ddMMyyyy HHmm").parse("22012012 1000"));
//			serviceKey.setName("Epiphany 3");
//		} catch (Exception e) {
//			 
//			e.printStackTrace();
//		}
//		ServiceVO service = ServiceLocator.getInstance().getServiceDAO().getService(serviceKey);
//		if (service != null) System.out.println(service.toString());
		tv.setSorter(new ServiceExplorerSorter());
			
		
		reload();
		
		
		

		tv.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				IStructuredSelection s = (IStructuredSelection)arg0.getSelection();
				Node n = (Node)s.getFirstElement();
				if (n == null) return;
				if (n.getData() instanceof ServiceVO) {					
					//tbvService.setInput(n.getData());					
				}

			}

		});

		tv.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection s = (IStructuredSelection)event.getSelection();
				Node n = (Node)s.getFirstElement();
				TreeViewer ttvv = (TreeViewer)event.getViewer();
				ttvv.setExpandedState(n, !ttvv.getExpandedState(n));

				if (n.getChildren().size() <= 0) {					
					new OpenServiceAction(aw, (ServiceVO)n.getData()).run();
				}
			}

		});

		MenuManager menu_manager = new MenuManager();
		tv.getTree().setMenu(menu_manager.createContextMenu(tv.getTree()));

		NewServiceAction nsa = new NewServiceAction(aw);
		dse = new DeleteServiceAction(aw);
		sata = new SaveAsTemplateAction(aw);
		ModifyServiceAction rsa = new ModifyServiceAction(aw);
		CopyServiceAction csa = new CopyServiceAction(aw);
		ApplyTemplateAction appt = new ApplyTemplateAction(aw);

		menu_manager.add(nsa);
		tv.addSelectionChangedListener(nsa);
		menu_manager.add(csa);
		tv.addSelectionChangedListener(csa);
		menu_manager.add(rsa);
		tv.addSelectionChangedListener(rsa);
		
		menu_manager.add(new Separator());
		
		
		menu_manager.add(appt);
		tv.addSelectionChangedListener(appt);
		
		
		menu_manager.add(sata);
		tv.addSelectionChangedListener(sata);
		
		menu_manager.add(new Separator());
		
		menu_manager.add(dse);
		tv.addSelectionChangedListener(dse);


		return tv.getControl();
	}

	public void reload() {
		reload(null);
	}
	public void reload(ServiceVO revealService) {
		SimpleDateFormat dateKey = new SimpleDateFormat("yyyy MM MMMMM");
		SimpleDateFormat mf = new SimpleDateFormat("yyyyMM");
		Node refreshNode = null; 
		Node currentService = null;
		String currentMonthYear = mf.format(new Date());
		String revealKey = revealService != null && revealService.getDateTime() != null ? dateKey.format(revealService.getDateTime()) : "";
		ServiceDAO serviceDAO = ServiceLocator.getInstance().getServiceDAO();		
		Node root = new Node(null,null); //root
		Map<String,Node> map = new HashMap<String,Node>();

		
		
		List expanded = new ArrayList();
		
		Node n2 = null;
		Node n = root.addChild(new Node(null,"Templates"));
		map.put("Templates", n);
		Iterator<ServiceVO> it = serviceDAO.getServices(true).iterator();
		while (it.hasNext()) {
			ServiceVO sv = it.next();
			if (sv.getDateTime() == null) {
				//use templates node
				n = map.get("Templates");
				if (sv.getType() != null) {
					n2 = map.get(sv.getType());
					if (n2 == null) {
						n2 = n.addChild(new Node(null,sv.getType()));
						map.put(sv.getType(), n2);
					}
					n = n2;
				}
				n.addChild(new Node(null,sv));
			} else {
				
				String key = dateKey.format(sv.getDateTime());
				//Find month node
				if (map.containsKey(key)) {
					n = map.get(key);
				} else {
					//Add the year node first
					String yearKey = new SimpleDateFormat("yyyy").format(sv.getDateTime());
					if (map.containsKey(yearKey)) {
						n = map.get(yearKey);
					} else {
						n = root.addChild(new Node(sv.getDateTime(),null));
						map.put(yearKey, n);
					}
					n = n.addChild(new Node(sv.getDateTime(),null));
					if (key.equalsIgnoreCase(revealKey)) {
						refreshNode = n;
					}		
					if (revealService == null && mf.format(sv.getDateTime()).equals(mf.format(new Date()))) {
						refreshNode = n;
					}
					map.put(key, n);
				}
				//Add service to it
				Node sn = new Node(sv.getDateTime(), sv);
				n.addChild(sn);
				if (revealService == null && mf.format(sv.getDateTime()).equals(currentMonthYear)) {
					currentService = sn;
				}
			}

		}

		tv.setInput(root);
		
		if (currentService != null) {
			tv.expandToLevel(currentService, TreeViewer.ALL_LEVELS);
		}
		
		if (refreshNode != null) {			
			tv.expandToLevel(refreshNode, TreeViewer.ALL_LEVELS);	
		}
		
		
	}

	public Node getMonthNode(ServiceVO service) {
		SimpleDateFormat df = new SimpleDateFormat("MM MMMMM");
		String serviceKey = "Templates";
		if (service.getDateTime() != null) {
			serviceKey = df.format(service.getDateTime());
		}
		Node root = (Node)tv.getInput();
		Iterator<Node> it = root.getChildren().iterator();
		while (it.hasNext()) {
			Node n = it.next();
			if (n.getData() != null && serviceKey.equals(n.getData().toString())) {
				return n;
			}

		}
		return null;

	}

	public void refresh() {
		tv.refresh();
		
	}



}
