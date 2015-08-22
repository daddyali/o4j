package com.organist4j.view.music.editor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.organist4j.model.music.MusicItemVO;
import com.organist4j.util.ContentCreator;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.music.item.MusicItemBrowser;

public class MusicItemEditorSummary implements ContentCreator {

	Text bookName;
	Text bookAcronym;
	Text name;
	Text tune;
	Text meter;
	Text number;
	Text psalm;
	Text notes;
	Text author;
	Text composer;
	Text response;

	Text ldu;
	Text tldu;
	Text rehearsals;
	Button bookNameCheck;
	Button bookAcronymCheck;
	Button nameCheck;
	Button tuneCheck;
	Button meterCheck;
	Button numberCheck;
	Button psalmCheck;
	Button notesCheck;
	Button lduCheck;
	Button tlduCheck;
	Button authorCheck;
	Button composerCheck;
	Button responseCheck;
	Button rehearsalsCheck;

	Button prev;
	Button next;
	Button newb;

	boolean createNewItem = false;
	boolean bulkChange = false;
	
	int bookNameLength = 0;

	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

	List<MusicItemVO> items = null;
	MusicItemVO item = null;
	public MusicItemEditorSummary(List<MusicItemVO> items) {
		this.items = items;
		if (items.size() <= 1) {
			if (items.size() > 0) {
				item = items.get(0);
			}
			bulkChange = false;
		} else {
			bulkChange = true;
		}
	}

	public boolean isCreateNewItem() {
		return createNewItem;
	}

	public void setCreateNewItem(boolean createNewItem) {
		this.createNewItem = createNewItem;

	}

	protected void setMusicItemFromSelection() {
		MusicItemBrowser tv = (MusicItemBrowser)ServiceLocator.getInstance().getMusicItemBrowser();
		IStructuredSelection sel = (IStructuredSelection) tv.getTbvItemBrowser().getSelection();
		item = (MusicItemVO)sel.getFirstElement();
	}

	protected void loadData() {

		bookName.setText("");
		bookAcronym.setText("");
		number.setText("");
		psalm.setText("");
		tune.setText("");
		name.setText("");
		meter.setText("");
		notes.setText("");
		composer.setText("");
		author.setText("");
		response.setText("");
		ldu.setText("");
		tldu.setText("");
		rehearsals.setText("");
		if (!bulkChange) {
			//Singleton mode
			if (item.getBookName() != null)
				bookName.setText(item.getBookName());
			if (item.getBookAcronym() != null)
				bookAcronym.setText(item.getBookAcronym());	
			if (item.getNumber() != null)
				number.setText(item.getNumber());
			if (item.getPsalm() != null)
				psalm.setText(item.getPsalm());
			if (item.getName() != null)
				name.setText(item.getName());
			if (item.getTune() != null)
				tune.setText(item.getTune());
			if (item.getMeter() != null)
				meter.setText(item.getMeter());
			if (item.getNotes() != null)
				notes.setText(item.getNotes());
			if (item.getComposer() != null)
				composer.setText(item.getComposer());
			if (item.getAuthor() != null)
				author.setText(item.getAuthor());
			if (item.getResponse() != null)
				response.setText(item.getResponse());


			if (item.getLastDateTimeUsed() != null)
				ldu.setText(df.format(item.getLastDateTimeUsed()));
			if (item.getTuneLastDateTimeUsed() != null)
				tldu.setText(df.format(item.getTuneLastDateTimeUsed()));
			if (item.getRehearsalsNeeded() != null) {
				rehearsals.setText(item.getRehearsalsNeeded());
			}
			
			if (createNewItem) {
				prev.setVisible(false);
				//				prev.setEnabled(false);
			} else {
				prev.setEnabled(true);
			}

			if (createNewItem) {
				next.setText("Save and New");
			} else {
				next.setText("Next");
			}


			bookNameCheck.setVisible(false);
			bookAcronymCheck.setVisible(false);
			numberCheck.setVisible(false);
			psalmCheck.setVisible(false);
			tuneCheck.setVisible(false);
			nameCheck.setVisible(false);
			meterCheck.setVisible(false);
			notesCheck.setVisible(false);
			authorCheck.setVisible(false);
			composerCheck.setVisible(false);
			responseCheck.setVisible(false);
			lduCheck.setVisible(false);
			tlduCheck.setVisible(false);
			rehearsalsCheck.setVisible(false);

		} else {
			//Bulk mode
			bookName.setEnabled(false);
			bookAcronym.setEnabled(false);
			number.setEnabled(false);
			psalm.setEnabled(false);
			tune.setEnabled(false);
			name.setEnabled(false);
			meter.setEnabled(false);
			notes.setEnabled(false);
			author.setEnabled(false);
			composer.setEnabled(false);
			response.setEnabled(false);
			ldu.setEnabled(false);
			tldu.setEnabled(false);
			rehearsals.setEnabled(false);
			bookNameCheck.setVisible(true);
			bookAcronymCheck.setVisible(true);
			numberCheck.setVisible(true);
			psalmCheck.setVisible(true);
			tuneCheck.setVisible(true);
			nameCheck.setVisible(true);
			meterCheck.setVisible(true);
			notesCheck.setVisible(true);
			authorCheck.setVisible(true);
			composerCheck.setVisible(true);
			responseCheck.setVisible(true);
			lduCheck.setVisible(true);
			tlduCheck.setVisible(true);
			rehearsalsCheck.setVisible(true);

			prev.setEnabled(false);
			next.setEnabled(false);


		}



	}

	@Override
	public Control createContent(Composite parent) {
		Composite compPage = new Composite(parent,SWT.NONE);
		compPage.setLayout(new GridLayout(1,true));

		Composite comp = new Composite(compPage,SWT.NONE);
		comp.setLayout(new GridLayout(3,false));
		comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		//book name
		bookNameCheck = new Button(comp,SWT.CHECK);
		Label l = new Label(comp,SWT.NONE);
		l.setText("Book Name");
		bookName = new Text(comp,SWT.BORDER);
		bookName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		bookName.addFocusListener(new TextFocusListener());
		
		bookName.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				//Set the value of the Acronym
				Text bookName = (Text)e.widget;
				if (bookName.getText() != null && bookName.getText().trim().length() > 0) {
					String bookAcronymName = ServiceLocator.getInstance().getMusicDAO().findAcronymForBookName(bookName.getText());
					if (bookAcronymName != null) {
						((Text)bookName.getData()).setText(bookAcronymName);
					}
				}
				
			}});
		bookName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Text bookName = (Text)e.widget;
//				System.out.println(bookName.getText().length() + ":" + bookNameLength);
				if (bookName.getText().length() < bookNameLength) {
					bookNameLength = bookName.getText().length();
					return;
				}
				bookNameLength = bookName.getText().length();
				int selStart = bookName.getCaretPosition();
				String suggestedBookName = ServiceLocator.getInstance().getMusicDAO().findSuggestedBookName(bookName.getText());
				if (suggestedBookName != null && !suggestedBookName.equalsIgnoreCase(bookName.getText())) {
					bookName.setText(suggestedBookName);
					bookName.setSelection(selStart,bookName.getText().length());
				}
				
				
			}});
		
		bookNameCheck.addSelectionListener(new CheckToTextListener(bookName));

		bookAcronymCheck = new Button(comp,SWT.CHECK);
		l = new Label(comp,SWT.NONE);
		l.setText("Book Acronym");
		bookAcronym = new Text(comp,SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = 100;
		bookAcronym.setLayoutData(gd);
		bookAcronym.addFocusListener(new TextFocusListener());
		bookName.setData(bookAcronym);
		bookAcronymCheck.addSelectionListener(new CheckToTextListener(bookAcronym));

		numberCheck = new Button(comp,SWT.CHECK);
		l = new Label(comp,SWT.NONE);
		l.setText("Number");
		number = new Text(comp,SWT.BORDER);
		 gd = new GridData();
		gd.widthHint = 200;
		number.setLayoutData(gd);
		number.addFocusListener(new TextFocusListener());
		numberCheck.addSelectionListener(new CheckToTextListener(number));

		nameCheck = new Button(comp,SWT.CHECK);
		l = new Label(comp,SWT.NONE);
		l.setText("Title / First Line");
		name = new Text(comp,SWT.BORDER);
		name.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		name.addFocusListener(new TextFocusListener());
		nameCheck.addSelectionListener(new CheckToTextListener(name));

		tuneCheck = new Button(comp,SWT.CHECK);
		l = new Label(comp,SWT.NONE);
		l.setText("Tune");
		tune = new Text(comp,SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 200;
		tune.setLayoutData(gd);		
		tune.addFocusListener(new TextFocusListener());
		tuneCheck.addSelectionListener(new CheckToTextListener(tune));

		meterCheck = new Button(comp,SWT.CHECK);
		l = new Label(comp,SWT.NONE);
		l.setText("Meter");
		meter = new Text(comp,SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 200;
		meter.setLayoutData(gd);		
		meter.addFocusListener(new TextFocusListener());
		meterCheck.addSelectionListener(new CheckToTextListener(meter));
		
		composerCheck = new Button(comp,SWT.CHECK);
		l = new Label(comp,SWT.NONE);
		l.setText("Composer");
		composer = new Text(comp,SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 200;
		composer.setLayoutData(gd);
		composer.addFocusListener(new TextFocusListener());
		composerCheck.addSelectionListener(new CheckToTextListener(composer));
		
		authorCheck = new Button(comp,SWT.CHECK);
		l = new Label(comp,SWT.NONE);
		l.setText("Author");
		author = new Text(comp,SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 200;
		author.setLayoutData(gd);
		author.addFocusListener(new TextFocusListener());
		authorCheck.addSelectionListener(new CheckToTextListener(author));

		psalmCheck = new Button(comp,SWT.CHECK);
		l = new Label(comp,SWT.NONE);
		l.setText("Psalm");
		psalm = new Text(comp,SWT.BORDER);
		psalm.addFocusListener(new TextFocusListener());
		psalmCheck.addSelectionListener(new CheckToTextListener(psalm));
		
		responseCheck = new Button(comp,SWT.CHECK);
		l = new Label(comp,SWT.NONE);
		l.setText("Response");
		response = new Text(comp,SWT.BORDER);
		response.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		response.addFocusListener(new TextFocusListener());
		responseCheck.addSelectionListener(new CheckToTextListener(response));

		notesCheck = new Button(comp,SWT.CHECK);
		l = new Label(comp,SWT.NONE);
		l.setText("Notes");
		notes = new Text(comp,SWT.BORDER);
		notes.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		notes.addFocusListener(new TextFocusListener());
		notesCheck.addSelectionListener(new CheckToTextListener(notes));

		lduCheck = new Button(comp,SWT.CHECK);
		l = new Label(comp,SWT.NONE);
		l.setText("Last Used");
		ldu = new Text(comp,SWT.BORDER);
		ldu.setMessage("DD/MM/YYYY");
		lduCheck.addSelectionListener(new CheckToTextListener(ldu));

		tlduCheck = new Button(comp,SWT.CHECK);
		l = new Label(comp,SWT.NONE);
		l.setText("Tune Last Used");
		tldu = new Text(comp,SWT.BORDER);
		tldu.setMessage("DD/MM/YYYY");
		tlduCheck.addSelectionListener(new CheckToTextListener(tldu));
		
		rehearsalsCheck = new Button(comp,SWT.CHECK);
		l = new Label(comp,SWT.NONE);
		l.setText("Rehearsals Needed");
		rehearsals = new Text(comp,SWT.BORDER);
		rehearsals.addFocusListener(new TextFocusListener());
		rehearsalsCheck.addSelectionListener(new CheckToTextListener(rehearsals));

		Composite comp2 = new Composite(compPage,SWT.NONE);
		comp2.setLayout(new GridLayout(6,true));
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		comp2.setLayoutData(gd);

		prev = new Button(comp2,SWT.NONE);
		prev.setText("Previous");
		gd = new GridData();
		gd.widthHint = 100;
		prev.setLayoutData(gd);

		//b.setData(this);
		prev.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {


			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				MusicItemBrowser mib = ServiceLocator.getInstance().getMusicItemBrowser();

				int index = mib.getTbvItemBrowser().getTable().getSelectionIndex();
				index --;
				if (index < 0) index = 0;
				mib.getTbvItemBrowser().getTable().setSelection(index);

				//MusicItemEditorSummary mies = (MusicItemEditorSummary)e.widget.getData();
				setMusicItemFromSelection();
				loadData();
				//mies.loadData();
			}

		});

		next = new Button(comp2,SWT.NONE);
		gd = new GridData();
		gd.widthHint = 100;
		next.setLayoutData(gd);
		if (createNewItem) {
			next.setText("Save and New");
		} else {
			next.setText("Next");
		}

		//b.setData(this);
		next.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				 

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (createNewItem) {
					saveChanges();
					setNewMusicItemFromCurrent();
				} else {
					saveChanges();
					MusicItemBrowser mib = ServiceLocator.getInstance().getMusicItemBrowser();

					int index = mib.getTbvItemBrowser().getTable().getSelectionIndex();
					index ++;
					int numItems = mib.getTbvItemBrowser().getTable().getItems().length;
					if (index > (numItems - 1)) index = numItems - 1;
					mib.getTbvItemBrowser().getTable().setSelection(index);

					//MusicItemEditorSummary mies = (MusicItemEditorSummary)e.widget.getData();
					setMusicItemFromSelection();
				}
				loadData();

			}

		});

		newb = new Button(comp2,SWT.NONE);
		newb.setText("New");
		gd = new GridData();
		gd.widthHint = 100;
		newb.setLayoutData(gd);

		if (createNewItem) {
			newb.setEnabled(false);
		} else {
			newb.setEnabled(true);
		}
		newb.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				 

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				setNewMusicItemFromCurrent();

			}

		});
		newb.setVisible(false);
		l = new Label(comp2,SWT.NONE);

		Button b = new Button(comp2,SWT.NONE);
		b.setText("Ok");
		gd = new GridData();
		gd.widthHint = 100;
		b.setLayoutData(gd);

		b.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				 

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				//Save the changed music item
				saveChanges();
				Button b = (Button)e.widget;

				b.getShell().dispose();

			}

		});


		b = new Button(comp2,SWT.NONE);
		b.setText("Cancel");
		gd = new GridData();
		gd.widthHint = 100;
		b.setLayoutData(gd);

		b.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				 

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button)e.widget;
				b.getShell().dispose();

			}

		});

		if (createNewItem) {
			setNewMusicItemFromCurrent();
		}

		loadData();


		return compPage;
	}

	protected void setNewMusicItemFromCurrent() {
		MusicItemVO newItem = new MusicItemVO();
		newItem.setBookName(item.getBookName());
		newItem.setBookAcronym(item.getBookAcronym());
		item = newItem;
		createNewItem = true;
		loadData();
		name.setFocus();



	}

	protected void saveChanges() {
		//Save the changed music item
		if (bulkChange) {
			Iterator<MusicItemVO> it = items.iterator();
			while (it.hasNext()) {
				MusicItemVO item = it.next();
				if (bookNameCheck.getSelection()) item.setBookName(bookName.getText());
				if (bookAcronymCheck.getSelection()) item.setBookAcronym(bookAcronym.getText());	
				if (nameCheck.getSelection()) item.setName(name.getText());
				if (tuneCheck.getSelection()) item.setTune(tune.getText());
				if (meterCheck.getSelection()) item.setMeter(meter.getText());
				if (numberCheck.getSelection()) item.setNumber(number.getText());
				if (psalmCheck.getSelection()) item.setPsalm(psalm.getText());
				if (notesCheck.getSelection()) item.setNotes(notes.getText());
				if (authorCheck.getSelection()) item.setAuthor(author.getText());
				if (composerCheck.getSelection()) item.setComposer(composer.getText());
				if (responseCheck.getSelection()) item.setResponse(response.getText());
				if (rehearsalsCheck.getSelection()) item.setRehearsalsNeeded(rehearsals.getText());
				if (lduCheck.getSelection())
					try {
						item.setLastDateTimeUsed(df.parse(ldu.getText()));
					} catch (ParseException e) {
						item.setLastDateTimeUsed(null);
					}
					if (tlduCheck.getSelection())
						try {
							item.setTuneLastDateTimeUsed(df.parse(tldu.getText()));
						} catch (ParseException e) {
							item.setTuneLastDateTimeUsed(null);
						}

						ServiceLocator.getInstance().getMusicDAO().updateMusicItem(item);	
			}
		} else {
			item.setBookName(bookName.getText());
			item.setBookAcronym(bookAcronym.getText());	
			item.setName(name.getText());
			item.setTune(tune.getText());
			item.setMeter(meter.getText());
			item.setNumber(number.getText());
			item.setPsalm(psalm.getText());
			item.setNotes(notes.getText());
			item.setAuthor(author.getText());
			item.setComposer(composer.getText());
			item.setResponse(response.getText());
			item.setRehearsalsNeeded(rehearsals.getText());
			try {
				item.setLastDateTimeUsed(df.parse(ldu.getText()));
			} catch (ParseException e) {
				item.setLastDateTimeUsed(null);
			}
			try {
				item.setTuneLastDateTimeUsed(df.parse(tldu.getText()));
			} catch (ParseException e) {
				item.setTuneLastDateTimeUsed(null);	
			}
			ServiceLocator.getInstance().getMusicDAO().updateMusicItem(item);

		}

		ServiceLocator.getInstance().getMusicItemBrowser().refresh(true);

	}

	private class TextFocusListener implements FocusListener {



		@Override
		public void focusGained(FocusEvent e) {
			Text t = (Text)e.widget;
			t.setSelection(0,t.getText().length() + 1);

		}

		@Override
		public void focusLost(FocusEvent e) {
			 

		}

	}

	private class CheckToTextListener implements SelectionListener {
		Text text;
		public CheckToTextListener(Text text) {
			this.text = text;
		}
		public void widgetDefaultSelected(SelectionEvent e) {		
		}
		public void widgetSelected(SelectionEvent e) {
			Button c = (Button)e.widget;
			text.setEnabled(c.getSelection());
		}

	}
}
