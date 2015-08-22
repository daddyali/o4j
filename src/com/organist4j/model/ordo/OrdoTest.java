package com.organist4j.model.ordo;

import java.text.SimpleDateFormat;

import org.apache.log4j.PropertyConfigurator;

import com.organist4j.util.O4J;

public class OrdoTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new OrdoTest().runtest();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	private void runtest() throws Exception {
		
		PropertyConfigurator.configure(O4J.LOG4J_PROPS_NAME);
		OrdoVO ordo = new OrdoVO();
		ordo.generate(new SimpleDateFormat("ddMMyyyy").parse("27112011"));
		ordo.debug();
	}

}
