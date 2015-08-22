package com.organist4j.test;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;

public class PersonCellLabelProvider extends CellLabelProvider implements ITableColorProvider   {
	


	int colTypeForToolTip = 0;
	
	public PersonCellLabelProvider(int colTypeForToolTip) {
		this.colTypeForToolTip = colTypeForToolTip;
	}

	public String getToolTipText(Object element) {
		Person person = (Person) element;

		switch (colTypeForToolTip) {
		case 0:
			return( person.getFirstName());
	
		case 1:
			return( person.getLastName());
		
		case 2:
			return( person.getGender());
		
		case 3:
			return( String.valueOf(person.isMarried()));
		
		default:
			throw new RuntimeException("Should not happen");
		}
		
	}

	public Point getToolTipShift(Object object) {
		return new Point(5, 5);
	}

//	public int getToolTipDisplayDelayTime(Object object) {
//		return 2000;
//	}

	public int getToolTipTimeDisplayed(Object object) {
		return 5000;
	}

	public void update(ViewerCell cell) {
		Person person = (Person) cell.getElement();
		
		switch (cell.getColumnIndex()) {
		case 0:
			cell.setText( person.getFirstName());
			break;
		case 1:
			cell.setText( person.getLastName());
			break;
		case 2:
			cell.setText( person.getGender());
			break;
		case 3:
			cell.setText( String.valueOf(person.isMarried()));
			break;
		default:
			throw new RuntimeException("Should not happen");
		}
		
		

	}

	@Override
	public Color getBackground(Object arg0, int arg1) {
		
		return null;
	}

	@Override
	public Color getForeground(Object arg0, int arg1) {
		 
		return null;
	}
	
	

	
	
	
	

}
