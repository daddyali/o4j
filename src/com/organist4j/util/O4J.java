package com.organist4j.util;

public class O4J {
	
	public static final double APPLICATION_VERSION = 2.11;
	public static final String APPLICATION_NAME = "Organist4j";
	public static final String APPLICATION_WEBSITE_URL = "http://sites.google.com/site/organist4j";
	
	/**
	 * HISTORY
	 * Version	Date
	 * 2.9		22/04/12	Added tool tips to Service Actions
	 * 						Fixed bug where null service name caused probs
	 * 						Forced service name to be mandatory
	 * 2.10     1/11/12 	Removed line spacing from the Service Report
	 */
	

	public static final String BUNDLE_NAME = "o4jresources";
	public static final String LOG4J_PROPS_NAME = "log4j.properties";
	
	public static final String DERBY_JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	
	public static final String DND_SERVICE_ITEM_BROWSER = "S";
	public static final String DND_MUSIC_ITEM_BROWSER = "M";
	public static final String DND_SERVICE_PLAN = "P";
	
	public static final int COLEDIT_NONE = 0;
	public static final int COLEDIT_TEXT = 1;
	public static final int COLEDIT_BOOL = 2;
	
	
	
	public static final String PREF_TUNE_USE_WARN_WKS = "o4j.check.tune.use.wks";
	public static final String PREF_ITEM_USE_WARN_WKS = "o4j.check.item.use.wks";
	public static final String PREF_CHURCH_NAME = "church.name";
	public static final String PREF_DB_FILENAME = "o4j.db.filename";
	public static final String PREF_LOOK_AHEAD_WEEKS_BIG_ITEMS = "o4j.reh.rpt.look.ahead.big.items";
	public static final String MAX_REH_LOOK_AHEAD_WEEKS = "o4j.reh.rpt.look.ahead.weeks";
	public static final String PREF_LARGE_REH_TRIGGER = "o4j.reh.rpt.big.item.weeks.defn";
	
	public static final String WINDOW_WIDTH = "o4j.window.width";
	public static final String WINDOW_HEIGHT = "o4j.window.height";
	public static final String WINDOW_LOCX = "o4j.window.locx";;
	public static final String WINDOW_LOCY = "o4j.window.locy";
	
	public static final String IMG_TICK = "tick";
	public static final String IMG_CROSS = "cross";
	public static final String IMG_ICON = "appicon";
	public static final String IMG_WARN = "warn";
	public static final String IMG_REDFLAG = "redflag";
	public static final String IMG_WHITEFLAG = "whiteflag";
	
	public static final String IMG_MARY = "mary";
	public static final String IMG_LENT = "lent";
	public static final String IMG_TRINITY = "trinity";
	public static final String IMG_STATIONS = "stations";
	public static final String IMG_PENTECOST = "pentecost";
	public static final String IMG_PALMSUNDAY = "palm";
	public static final String IMG_MAUNDYTHURSDAY = "maunday";
	public static final String IMG_GOODFRIDAY = "goodfri";
	public static final String IMG_AAM = "aam";
	public static final String IMG_EASTER = "easter";
	public static final String IMG_BENEDICTION = "benny";
	public static final String IMG_MASS = "mass";
	public static final String IMG_EPIPHANY = "epiphany";
	public static final String IMG_EVENSONG = "evensong";
	public static final String IMG_FUNERAL = "funeral";
	public static final String IMG_ADVENT = "advent";
	public static final String IMG_ALLSAINTS = "allsaints";
	public static final String IMG_BOTL = "botl";
	public static final String IMG_CHRISTTHEKING = "xttheking";
	public static final String IMG_ASCENSION = "ascension";
	public static final String IMG_CHRISTMAS = "christmas";
	public static final String IMG_WEDDING = "wedding";
	public static final String IMG_CORPUSCHRISTI = "corpuschristi";
	public static final String IMG_PATRONAL = "patronal";
	public static final String IMG_DEDICATION = "dedication";
	
	
	
	//To do list

	//TODO Externalise lectionary colour rules etc
	//TODO Lectionary dates. Ability to create Service FROM the service list (ie pre-populates date and service name, multi select, with def times
	//TODO Lectionary has reading refs (with browser linking to online bible), gospel acc, psalms
	//TODO Lectionary ability to filter Music Items by psalm or season for example in Lecitonary (or by previously used for same type)
	//TODO Need Lectionary, LectionaryService (with a date and name, year (ABC), psalm, gospel acc, 1st reading etc, colour, picture, season) - each item then has a text element associated
	//TODO Can be in lower section next to MusicItem browser etc
	//TODO You then drag the LectionaryService onto the ServicePlan at the top - new section for it.
	//TODO Each Lectionary
	

	//TODO Provide drop down from existing values on Book Name and Acronym

	
	//TODO Make background shading in MusicItems alternate so adjoining related groups are obvious
	
	
	

}
