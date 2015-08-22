package com.organist4j.test;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class PersonLabelProvider implements ITableLabelProvider,ITableColorProvider {
	// We use icons
	// We use icons


	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// In case you don't like image just return null here

		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Person person = (Person) element;
		switch (columnIndex) {
		case 0:
			return person.getFirstName();
		case 1:
			return person.getLastName();
		case 2:
			return person.getGender();
		case 3:
			return String.valueOf(person.isMarried());
		default:
			throw new RuntimeException("Should not happen");
		}

	}

	@Override
	public void addListener(ILabelProviderListener arg0) {
		 
		
	}

	@Override
	public void dispose() {
		 
		
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		 
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		 
		
	}

	@Override
	public Color getBackground(Object arg0, int arg1) {
		 
	
		return Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
	}

	@Override
	public Color getForeground(Object arg0, int arg1) {
		 
		return null;
	}



}

