package com.organist4j.model.lectionary;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.O4J;
import com.organist4j.util.ServiceLocator;

public class DefaultLectionary extends Lectionary {

	@Override
	public Color getSeasonBackgroundColor(ServiceVO service) {
		try {
			if (service == null) return null;
			if (service.getName().toUpperCase().startsWith("LENT 4")) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA);
			}
			if (service.getName().toUpperCase().startsWith("ADVENT 3")) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA);
			}
			if (service.getName().toUpperCase().startsWith("LENT")) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_DARK_MAGENTA);
			}
			if (service.getName().toUpperCase().startsWith("ASH WEDNESDAY")) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_DARK_MAGENTA);
			}
			if (service.getName().toUpperCase().startsWith("ADVENT")) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_DARK_MAGENTA);
			}
			if (service.getName().toUpperCase().indexOf("CHRISTMAS") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			}
			if (service.getName().toUpperCase().equals("EPIPHANY")) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			}
			if (service.getName().toUpperCase().equals("MAUNDY THURSDAY")) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
			}
			if (service.getName().toUpperCase().indexOf("BAPTISM OF THE LORD") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			}
			if (service.getName().toUpperCase().indexOf("CHRIST THE KING") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			}
			if (service.getName().toUpperCase().indexOf("EASTER") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			}
			if (service.getName().toUpperCase().indexOf("SAINTS") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			}
			if (service.getName().toUpperCase().indexOf("TRINITY") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			}
			if (service.getName().toUpperCase().indexOf("ASCENSION") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			}
			if (service.getName().toUpperCase().indexOf("CORPUS CHRISTI") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			}
			if (service.getName().toUpperCase().indexOf("GOOD FRIDAY") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
			}
			if (service.getName().toUpperCase().indexOf("SOULS") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
			}
			if (service.getName().toUpperCase().indexOf("PALM SUNDAY") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
			}
			if (service.getName().toUpperCase().indexOf("PATRONAL") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
			}
			if (service.getName().toUpperCase().indexOf("PENTECOST") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
			}
			if (service.getType() != null && service.getType().toUpperCase().indexOf("FUNERAL") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
			}
			if (service.getType() != null && service.getType().toUpperCase().indexOf("REQUIEM") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
			}
			if (service.getName().toUpperCase().indexOf("MARY") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
			}
			if (service.getName().toUpperCase().indexOf("ASSUMPTION") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
			}
			if (service.getName().toUpperCase().indexOf("MAY DEVOTION") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
			}
			return Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
		} catch (Exception e) {
			return Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
		}
	}

	@Override
	public Color getSeasonForegroundColor(ServiceVO service) {
		try {
			if (service == null) return null;
			if (service.getName().toUpperCase().startsWith("LENT")) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
			}
			if (service.getName().toUpperCase().startsWith("ASH")) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
			}
			if (service.getName().toUpperCase().startsWith("ADVENT")) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
			}
			if (service.getName().toUpperCase().indexOf("GOOD FRIDAY") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
			}
			if (service.getType() != null && service.getType().toUpperCase().indexOf("FUNERAL") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
			}
			if (service.getType() != null && service.getType().toUpperCase().indexOf("REQUIEM") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
			}
			if (service.getName().toUpperCase().indexOf("SOULS") >= 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
			}
			return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		} catch (Exception e) {
			return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		}
	}

	@Override
	public Image getImage(ServiceVO service) {
		try {
			if (service == null) return null;
			if (service.getType() != null && service.getType().toUpperCase().indexOf("REQUIEM") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_FUNERAL);
			}
			if (service.getType() != null && service.getType().toUpperCase().indexOf("AAW") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_AAM);
			}
			if (service.getType() != null && service.getType().toUpperCase().indexOf("AAM") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_AAM);
			}
			if (service.getType() != null && service.getType().toUpperCase().indexOf("STATIONS") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_STATIONS);
			}
			if (service.getType() != null && service.getType().toUpperCase().indexOf("BENEDICTION") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_BENEDICTION);
			}
			if (service.getName().toUpperCase().startsWith("LENT 4")) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_LENT);
			}

			if (service.getName().toUpperCase().startsWith("LENT")) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_LENT);
			}
			if (service.getName().toUpperCase().startsWith("ASH WEDNESDAY")) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_LENT);
			}
			if (service.getName().toUpperCase().startsWith("ADVENT")) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_ADVENT);
			}
			if (service.getName().toUpperCase().indexOf("CHRISTMAS") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_CHRISTMAS);
			}
			if (service.getName().toUpperCase().equals("EPIPHANY")) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_EPIPHANY);
			}
			if (service.getName().toUpperCase().equals("MAUNDY THURSDAY")) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_MAUNDYTHURSDAY);
			}
			if (service.getName().toUpperCase().indexOf("BAPTISM OF THE LORD") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_BOTL);
			}
			if (service.getName().toUpperCase().indexOf("CHRIST THE KING") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_CHRISTTHEKING);
			}
			if (service.getName().toUpperCase().indexOf("EASTER") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_EASTER);
			}
			if (service.getName().toUpperCase().indexOf("SAINTS") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_ALLSAINTS);
			}
			if (service.getName().toUpperCase().indexOf("TRINITY") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_TRINITY);
			}
			if (service.getName().toUpperCase().indexOf("ASCENSION") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_ASCENSION);
			}
			if (service.getName().toUpperCase().indexOf("CORPUS CHRISTI") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_CORPUSCHRISTI);
			}
			if (service.getName().toUpperCase().indexOf("GOOD FRIDAY") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_GOODFRIDAY);
			}
			if (service.getName().toUpperCase().indexOf("SOULS") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_FUNERAL);
			}
			if (service.getName().toUpperCase().indexOf("PALM SUNDAY") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_PALMSUNDAY);
			}
			if (service.getName().toUpperCase().indexOf("PATRONAL") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_PATRONAL);
			}
			if (service.getName().toUpperCase().indexOf("DEDICATION") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_DEDICATION);
			}
			if (service.getName().toUpperCase().indexOf("PENTECOST") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_PENTECOST);
			}
			if (service.getType() != null && service.getType().toUpperCase().indexOf("FUNERAL") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_FUNERAL);
			}
			if (service.getName().toUpperCase().indexOf("MARY") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_MARY);
			}
			if (service.getName().toUpperCase().indexOf("ASSUMPTION") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_MARY);
			}
			if (service.getName().toUpperCase().indexOf("MAY DEVOTION") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_MARY);
			}
			if (service.getName().toUpperCase().indexOf("EVENSONG") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_EVENSONG);
			}
			if (service.getType() != null && service.getType().toUpperCase().indexOf("WEDDING") >= 0) {
				return ServiceLocator.getInstance().getImgRef().get(O4J.IMG_WEDDING);
			}

			return  ServiceLocator.getInstance().getImgRef().get(O4J.IMG_MASS);
		} catch (Exception e) {
			return null;
		}
	}

}
