package com.organist4j.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;
import com.organist4j.model.service.ServiceElementVO;
import com.organist4j.model.service.ServiceItemVO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.ServiceLocator;

public class ServiceDAO extends DAO {
	Logger logger = Logger.getLogger(ServiceDAO.class);
	public ServiceDAO(ObjectContainer db) {
		super(db);
		cleanup();
	}

	private void cleanup() {
		ServiceElementVO se = new ServiceElementVO(null, null);
		List <ServiceElementVO> cpl = db.queryByExample(se);
    	Iterator<ServiceElementVO> it = cpl.iterator();
    	int cnt = 0;
    	while (it.hasNext()) {
    		se = it.next();
    		if (se.getService() == null) {
    			db.delete(se);
    			cnt++;
    		}
//    		//Convert the ServiceItem
//    		if (se.getServiceItem() == null) {
//    			ServiceItemVO si = this.getServiceItem(se.getName());
//    			if (si == null) {
//    				logger.debug("Not able to find " + se.getName());
//    			} else {
//    				se.setServiceItem(si);
//    				this.updateServiceElement(se);
//    				logger.debug("Changed " + se.getName() + " to " + si.getName());
//    				
//    			}
//    		}
      	}
    	logger.info("Deleted " + cnt + " orphaned ServiceElementVOs");
    	
    	
	}

	/**
	 * Get the Service for the specified service
	 * @param name
	 * @return
	 */
	public ServiceVO getService(ServiceVO serviceKey) {
		
		
		ServiceVO sv = new ServiceVO();
//		sv.setTemplate(false);
		sv.setName(serviceKey.getName());		
		if (serviceKey.getDateTime() != null) {
			sv.setDateTime(serviceKey.getDateTime());
		}
		if (serviceKey.getType() != null) {
			sv.setType(serviceKey.getType());
		}
		List <ServiceVO> cpl = db.queryByExample(sv);
		if (cpl != null && cpl.size() > 0) {
			ServiceVO s = (ServiceVO)cpl.iterator().next();
			return s;
		} else {
			return null;
		}
	}
	
	public List<ServiceVO> getServices(boolean loadTemplatesToo) {
		
		ServiceVO sv = new ServiceVO();
		
		if (loadTemplatesToo) {
			sv.setTemplate(null);
		} else {
			sv.setTemplate(new Boolean(false));
		}
		List <ServiceVO> cpl = db.queryByExample(sv);
		logger.debug(cpl.size() + " services.");
		return cpl;
	}
	
//	public ServiceVO getServiceTemplate(String name) {
//		ServiceVO sv = new ServiceVO();
//		sv.setTemplate(true);
//		sv.setName(name);
//		List <ServiceVO> cpl = db.queryByExample(sv);
//		if (cpl != null && cpl.size() > 0) {
//			return cpl.iterator().next();
//		} else {
//			return null;
//		}
//	}
	
	public List<ServiceVO> getServiceTemplates() {
		ServiceVO sv = new ServiceVO();
		sv.setTemplate(true);
		List <ServiceVO> cpl = db.queryByExample(sv);
		return cpl;
	}
	
	public void updateService(ServiceVO service) {
		logger.debug("Storing service " + service.getName());
		db.store(service);
		db.commit();
	}

	public void updateServiceElement(ServiceElementVO se) {
		logger.debug("Storing se " + se.getName());
		db.store(se);
		db.commit();
	}
	
	public List<ServiceElementVO> getServiceElements() {
		
		List <ServiceElementVO> cpl = db.queryByExample(ServiceElementVO.class);
		return cpl;
	}
	

	public void deleteService(ServiceVO service) {
		//Delete any service elements
		Iterator<ServiceElementVO> it = service.getServiceElements().iterator();
		while (it.hasNext()) {
			ServiceElementVO se = it.next();
			this.deleteServiceElement(se);
		}
		db.delete(service.getServiceElements());
		db.delete(service);
		db.commit();
	}

	public List<ServiceItemVO> getServiceItems() {
		ServiceItemVO sv = new ServiceItemVO(null,null,null);		
		List <ServiceItemVO> cpl = db.queryByExample(sv);
		return cpl;
		

	}

	public void deleteServiceElement(ServiceElementVO se) {
		db.delete(se);
		db.commit();
		//recalc any music items
		try {
	    ServiceLocator.getInstance().getMusicDAO().updateMusicItem(se.getItem(),true);
	    ServiceLocator.getInstance().getMusicDAO().updateMusicItem(se.getTuneItem(),true);
		} catch (Exception ig) {
			ig.printStackTrace();
		}
	}

	public void updateServiceItem(ServiceItemVO serviceItem) {
		db.store(serviceItem);
		db.commit();
		
	}
	
	public void deleteServiceItem(ServiceItemVO serviceItem) {
		db.delete(serviceItem);
		db.commit();
		
	}

	public ServiceItemVO getServiceItem(String name) {
		ServiceItemVO sv = new ServiceItemVO(name,null,null);		
		List <ServiceItemVO> cpl = db.queryByExample(sv);
		return cpl.iterator().next();
	}

	public List<ServiceVO> getFutureServices() {
		final Date now = new Date();
		List<ServiceVO> services = db.query(new Predicate<ServiceVO>() {
			public boolean match(ServiceVO service) {
				return service.getDateTime() != null && !service.getDateTime().before(now);
			}
		});
		
		logger.debug(services.size() + " services.");
		return services;
	}

	public boolean isServiceNameExists(String name) {
		final String thename = name;
		List<ServiceVO> services = db.query(new Predicate<ServiceVO>() {
			public boolean match(ServiceVO service) {
				return service.getName().equalsIgnoreCase(thename);
			}
		});
		return services != null && services.size() > 0;
	}

	

}
