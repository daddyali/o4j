package com.organist4j.util;

import java.sql.SQLException;
import java.util.Iterator;

import com.organist4j.model.service.ServiceVO;

public class DBServiceBrowser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 
		try {
			new DBServiceBrowser().run();
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			try {
				ServiceLocator.getInstance().dispose();
			} catch (SQLException e) {
				 
				e.printStackTrace();
			}
		}
	}
	
	private void run() throws Exception {
		ServiceLocator.getInstance().init();
		
		Iterator<ServiceVO> it = ServiceLocator.getInstance().getServiceDAO().getServices(true).iterator();
		while (it.hasNext()) {
			ServiceVO s = it.next();
			if (s.getName().equalsIgnoreCase("Wedding")) {
			System.out.println(s.getName() + ":" + s.getDateTime());
			}
		}
	}

}
