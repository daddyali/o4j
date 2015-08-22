package com.organist4j.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.widgets.Display;

import com.db4o.Db4o;

import com.db4o.ObjectContainer;
import com.db4o.config.Configuration;
import com.db4o.defragment.Defragment;

import com.organist4j.dao.DAO;
import com.organist4j.dao.MusicDAO;
import com.organist4j.dao.ServiceDAO;
import com.organist4j.view.music.item.MusicItemBrowser;
import com.organist4j.view.prefs.FieldEditorPageGeneral;
import com.organist4j.view.prefs.FieldEditorPageRehearsalReport;
import com.organist4j.view.prefs.FieldEditorPageReports;
import com.organist4j.view.service.explorer.ServiceExplorer;
import com.organist4j.view.service.item.ServiceItemBrowser;

public class ServiceLocator {
	static Logger logger = Logger.getLogger(ServiceLocator.class);

	private static ServiceLocator singleton = null;
	private static ObjectContainer db = null;
	private static Connection conn = null;
	private static HashMap<String, DAO> daos = new HashMap<String, DAO>();
	private ResourceBundle bundle = null;
	private PreferenceManager prefMgr = null;
	private PreferenceStore ps = null;
	private Map<String, Object> objectCache = new HashMap<String, Object>();
	private ImageRegistry imgRef;
	private static Connection dconn = null;

	public String driver = "org.apache.derby.jdbc.EmbeddedDriver";

	private ServiceLocator() {



	}



	public void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		PropertyConfigurator.configure(O4J.LOG4J_PROPS_NAME);



		//Sort out preference store etc;
		prefMgr = new PreferenceManager();

		PreferenceNode general = new PreferenceNode("general","General",null,FieldEditorPageGeneral.class.getName());
		prefMgr.addToRoot(general);
		PreferenceNode reports = new PreferenceNode("reports","Reports",null,FieldEditorPageReports.class.getName());
		prefMgr.addToRoot(reports);
		PreferenceNode rehearsalrpt = new PreferenceNode("rehrpt","Rehearsal Report",null,FieldEditorPageRehearsalReport.class.getName());
		reports.add(rehearsalrpt);
		
		
		ps = new PreferenceStore("o4j.properties");
		try {
			//Set defaults
			ps.setDefault(O4J.PREF_LOOK_AHEAD_WEEKS_BIG_ITEMS, 1);
			ps.setDefault(O4J.PREF_LARGE_REH_TRIGGER, 2);
			ps.setDefault(O4J.MAX_REH_LOOK_AHEAD_WEEKS, 14);
			ps.setDefault(O4J.PREF_DB_FILENAME, "data.o4j");
			ps.load();
		} catch (IOException e) {

		}

		try {
			String backup = ps.getString(O4J.PREF_DB_FILENAME) + "-" + new SimpleDateFormat("yyyyMM").format(new Date()) + ".backup";
			Defragment.defrag(ps.getString(O4J.PREF_DB_FILENAME),backup);
			logger.info("Monthly Defrag'd database of " + ps.getString(O4J.PREF_DB_FILENAME) + " to " + backup);
		} catch (IOException e1) {
			logger.error("Unable to do monthly defrag database of " + ps.getString(O4J.PREF_DB_FILENAME) + ": " + e1.getMessage());
		}

		loadDatabase(ps.getString(O4J.PREF_DB_FILENAME));



		loadDAOs(db);





	}

	public void loadDatabase(String fileName) throws SQLException {
		if (db != null) {

			dispose();

		}
		//db40 7 code
		Configuration config = Db4o.newConfiguration();
		config.updateDepth(3);
		db=Db4o.openFile(config,fileName);
	}

	public void loadImages() {
		imgRef = new ImageRegistry(Display.getCurrent());

		imgRef.put(O4J.IMG_TICK, ImageDescriptor.createFromFile(null, "images/icon_accept.gif"));
		imgRef.put(O4J.IMG_CROSS, ImageDescriptor.createFromFile(null, "images/action_stop.gif"));
		imgRef.put(O4J.IMG_REDFLAG, ImageDescriptor.createFromFile(null, "images/flag_red.gif"));
		imgRef.put(O4J.IMG_WHITEFLAG, ImageDescriptor.createFromFile(null, "images/flag_white.gif"));
		imgRef.put(O4J.IMG_WARN, ImageDescriptor.createFromFile(null, "images/icon_alert.gif"));
		imgRef.put(O4J.IMG_ICON, ImageDescriptor.createFromFile(null, "images/Orange1_.png"));
		imgRef.put(O4J.IMG_MARY, ImageDescriptor.createFromFile(null, "images/mary.jpg"));
		imgRef.put(O4J.IMG_LENT, ImageDescriptor.createFromFile(null, "images/lent.jpg"));
		imgRef.put(O4J.IMG_TRINITY, ImageDescriptor.createFromFile(null, "images/trinity.jpg"));
		imgRef.put(O4J.IMG_STATIONS, ImageDescriptor.createFromFile(null, "images/stations.jpg"));
		imgRef.put(O4J.IMG_PENTECOST, ImageDescriptor.createFromFile(null, "images/pentecost.jpg"));
		imgRef.put(O4J.IMG_PALMSUNDAY, ImageDescriptor.createFromFile(null, "images/palmsunday.jpg"));
		imgRef.put(O4J.IMG_MAUNDYTHURSDAY, ImageDescriptor.createFromFile(null, "images/maundythursday.jpg"));
		imgRef.put(O4J.IMG_GOODFRIDAY, ImageDescriptor.createFromFile(null, "images/goodfriday.jpg"));
		imgRef.put(O4J.IMG_EASTER, ImageDescriptor.createFromFile(null, "images/easter.jpg"));
		imgRef.put(O4J.IMG_AAM, ImageDescriptor.createFromFile(null, "images/aam.jpg"));
		imgRef.put(O4J.IMG_BENEDICTION, ImageDescriptor.createFromFile(null, "images/benediction.jpg"));
		imgRef.put(O4J.IMG_MASS, ImageDescriptor.createFromFile(null, "images/mass.gif"));
		
		imgRef.put(O4J.IMG_EPIPHANY, ImageDescriptor.createFromFile(null, "images/epiphany.jpg")); //
		imgRef.put(O4J.IMG_EVENSONG, ImageDescriptor.createFromFile(null, "images/evensong.jpg")); //
		imgRef.put(O4J.IMG_FUNERAL, ImageDescriptor.createFromFile(null, "images/funeral.gif")); //
		imgRef.put(O4J.IMG_ADVENT, ImageDescriptor.createFromFile(null, "images/advent.jpg")); //
		imgRef.put(O4J.IMG_CHRISTMAS, ImageDescriptor.createFromFile(null, "images/christmas.jpg")); //
		imgRef.put(O4J.IMG_WEDDING, ImageDescriptor.createFromFile(null, "images/wedding.jpg")); //
		imgRef.put(O4J.IMG_CORPUSCHRISTI, ImageDescriptor.createFromFile(null, "images/corpuschristi.jpg")); //
		imgRef.put(O4J.IMG_ASCENSION, ImageDescriptor.createFromFile(null, "images/ascension.jpg")); //
		imgRef.put(O4J.IMG_CHRISTTHEKING, ImageDescriptor.createFromFile(null, "images/christtheking.jpg")); //
		imgRef.put(O4J.IMG_BOTL, ImageDescriptor.createFromFile(null, "images/botl.jpg")); //
		imgRef.put(O4J.IMG_ALLSAINTS, ImageDescriptor.createFromFile(null, "images/allsaints.jpg")); //
		imgRef.put(O4J.IMG_PATRONAL, ImageDescriptor.createFromFile(null, "images/patronal.jpg"));
		imgRef.put(O4J.IMG_DEDICATION, ImageDescriptor.createFromFile(null, "images/dedication.jpg"));
		

	}


	public ImageRegistry getImgRef() {
		return imgRef;
	}



	public void setImgRef(ImageRegistry imgRef) {
		this.imgRef = imgRef;
	}



	private void loadDAOs(ObjectContainer db) {
		daos.put("ServiceDAO", new ServiceDAO(db));
		daos.put("MusicDAO", new MusicDAO(db));

	}
	public synchronized static ServiceLocator getInstance() {
		if (singleton == null) {
			singleton = new ServiceLocator();
		}
		return singleton;
	}


	private DAO getDAO(String name) {
		return daos.get(name);
	}

	public void dispose() throws SQLException {
		try { db.commit(); db.close(); } catch (Exception ig) {}
		try { conn.close(); } catch (Exception ig) {}
	}

	public String getString(String key) {
		if (bundle == null) {
			return key;
		} else {
			return bundle.getString(key);
		}
	}

	public ServiceDAO getServiceDAO() {
		return (ServiceDAO)getDAO("ServiceDAO");				
	}
	public MusicDAO getMusicDAO() {
		return (MusicDAO)getDAO("MusicDAO");				
	}


	public PreferenceStore getPreferenceStore() {
		return ps;
	}

	public PreferenceManager getPreferenceManager() {
		return prefMgr;
	}



	public MusicItemBrowser getMusicItemBrowser() {
		return (MusicItemBrowser)objectCache.get("MusicItemBrowser");
	}

	public void setMusicItemBrowser(MusicItemBrowser mib) {
		objectCache.put("MusicItemBrowser",mib);
	}

	public ServiceItemBrowser getServiceItemBrowser() {
		return (ServiceItemBrowser)objectCache.get("ServiceItemBrowser");
	}

	public void setServiceItemBrowser(ServiceItemBrowser sib) {
		objectCache.put("ServiceItemBrowser", sib);

	}

	public ServiceExplorer getServiceExplorer() {
		return (ServiceExplorer)objectCache.get("ServiceExplorer");
	}

	public void setServiceExplorer(ServiceExplorer sib) {
		objectCache.put("ServiceExplorer", sib);

	}


	//	private void initDerbyDatabase() throws Exception {
	//		
	//		//Load derby driver
	//		Class.forName(driver).newInstance(); 
	//		String protocol = "jdbc:derby:"; 
	//		Properties props = new Properties();
	//		dconn = DriverManager.getConnection(protocol + "derbyDB;create=true", props);
	//		
	//		//DB VERSION 1
	//		
	//		//Does it have version table?
	//		String version = getVersion("o4j_version");
	//		if (version == null) {
	//			logger.debug("No version table exists. Creating...");
	//			version = createVersionTable();
	//		}
	//		
	//		//Get latest version
	//		if (version == null) throw new Exception("Unable to establish db version");
	//		logger.debug("Current DB Version is " + version);
	//		
	//		
	//		//DB VERSION 2
	//		
	//		
	//	
	//	}

	private String getVersion(String tableName) {
		java.sql.Statement s = null;
		java.sql.ResultSet rs = null;
		try {
			s = dconn.createStatement();
			rs = s.executeQuery("select max(v_version) from " + tableName);
			if (rs.next()) {
				return rs.getString("v_version");
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (s!=null)
				try {
					s.close();
				} catch (SQLException e) {

				}
		}
	}

	private String createVersionTable() throws Exception {
		java.sql.Statement s = null;
		try {
			s = dconn.createStatement();

			try {s.execute("drop table o4j_version");
			logger.debug("Version table dropped");} catch (Exception ignore) {}

			s.execute("create table o4j_version (v_version varchar(10),d_upgrade_date date)");
			logger.debug("Version table created");

			s.execute("insert into o4j_version values ('1',CURRENT_DATE)");
			return "1";

		} catch (Exception e) {
			throw e;
		} finally {
			if (s!=null)
				try {
					s.close();
				} catch (SQLException e) {

				}
		}
	}

}
