package com.organist4j.view.prefs;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;

import com.organist4j.util.O4J;

public class FieldEditorPageRehearsalReport extends FieldEditorPreferencePage {
	public FieldEditorPageRehearsalReport() {
		super(GRID);
	}
	@Override
	protected void createFieldEditors() {
		 
		StringFieldEditor sfe = new StringFieldEditor(O4J.PREF_LARGE_REH_TRIGGER,"Highlight items with rehearsal weeks greater than",getFieldEditorParent());
		sfe.setEmptyStringAllowed(false);
		//sfe.setStringValue("26");
		addField(sfe);
		
		sfe = new StringFieldEditor(O4J.PREF_LOOK_AHEAD_WEEKS_BIG_ITEMS,"Look for large rehearsal items for weeks",getFieldEditorParent());
		sfe.setEmptyStringAllowed(false);
		//sfe.setStringValue("26");
		addField(sfe);
		
		sfe = new StringFieldEditor(O4J.MAX_REH_LOOK_AHEAD_WEEKS,"Look ahead weeks for rehearsal report generally",getFieldEditorParent());
		sfe.setEmptyStringAllowed(false);
		//sfe.setStringValue("26");
		addField(sfe);
	}

}
