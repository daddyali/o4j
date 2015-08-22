package com.organist4j.view.prefs;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;

import com.organist4j.util.O4J;

public class FieldEditorPageGeneral extends FieldEditorPreferencePage {

	public FieldEditorPageGeneral() {
		super(GRID);
	}
	
	@Override
	protected void createFieldEditors() {
		StringFieldEditor sfe = new StringFieldEditor(O4J.PREF_CHURCH_NAME,"Church Name",getFieldEditorParent());
		sfe.setEmptyStringAllowed(false);
		sfe.setStringValue("My Church");
		addField(sfe);
		
		sfe = new StringFieldEditor(O4J.PREF_ITEM_USE_WARN_WKS,"Warn if Item Used within (weeks)",getFieldEditorParent());
		sfe.setEmptyStringAllowed(false);
		sfe.setStringValue("26");
		addField(sfe);
		
		sfe = new StringFieldEditor(O4J.PREF_ITEM_USE_WARN_WKS,"Warn if Tune Used within (weeks)",getFieldEditorParent());
		sfe.setEmptyStringAllowed(false);
		sfe.setStringValue("26");
		addField(sfe);
		
		sfe = new StringFieldEditor(O4J.PREF_DB_FILENAME,"DB Filename",getFieldEditorParent());
		sfe.setEmptyStringAllowed(false);
		sfe.setStringValue("myTestDB");
		addField(sfe);
	}

}
