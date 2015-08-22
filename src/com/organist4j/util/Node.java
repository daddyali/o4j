package com.organist4j.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Node {
	Node parent = null;
	List<Node> children = new ArrayList<Node>();
	Date date;
	Object data;
	int level = 0;
	
	public Node(Date date,Object data) {
		
		this.date = date;
	this.data = data;
	if (data == null) {
		data = date;
	}
	
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}



	public Node getParent() {
		return parent;
	}

	public List<Node> getChildren() {
		return children;
	}





	
	public Node addChild(Node child) {		
		children.add(child);
		child.setParent(this);
		child.setLevel(level + 1);
		return child;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Node setParent(Node parent) {
		this.parent = parent;
		return parent;
	}




	
	}
