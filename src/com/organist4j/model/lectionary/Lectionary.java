package com.organist4j.model.lectionary;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.organist4j.model.service.ServiceVO;

public abstract class Lectionary {
	
	public static Lectionary getDefault() {
		return new DefaultLectionary();
		
	}

	public abstract Color getSeasonBackgroundColor(ServiceVO serviceVO);
	
	public abstract Color getSeasonForegroundColor(ServiceVO serviceVO);

	public abstract Image getImage(ServiceVO service);

}
