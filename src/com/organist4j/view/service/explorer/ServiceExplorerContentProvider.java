package com.organist4j.view.service.explorer;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.organist4j.util.Node;

public class ServiceExplorerContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getChildren(Object parentElement) {				
		Node n = (Node)parentElement;		
		return n.getChildren().toArray();
	}

	@Override
	public Object getParent(Object element) {
		Node n = (Node)element;	
		return n.getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		Node n = (Node)element;
		return n.getChildren().size() > 0;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		Node n = (Node)inputElement;
		return n.getChildren().toArray();
		
	}

	@Override
	public void dispose() {
		 
		
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		 
		
	}

}
