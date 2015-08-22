package com.organist4j.dao;

import java.sql.Connection;

import com.db4o.ObjectContainer;

public class DAO {

	protected ObjectContainer db = null;
	protected Connection conn = null;
	
	public DAO(ObjectContainer db) {
		this.db = db;
	}
	
	public DAO(Connection conn) {
		this.conn = conn;
	}
	

}
