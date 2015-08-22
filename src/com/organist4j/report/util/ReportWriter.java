package com.organist4j.report.util;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class ReportWriter {
	int row = 0;
	GC gc = null;
	Point dpi = null;
	Rectangle trim = null;
	int leftMargin = 0;
	int topMargin = 0;
	
	public ReportWriter(GC gc,Rectangle trim,Point dpi) {
		this.gc = gc;
		this.trim = trim;
		this.dpi = dpi;
		leftMargin = (dpi.x / 3) + trim.x; // one inch from left side of
		// paper
		topMargin = dpi.y / 2 + trim.y; // one-half inch from top edge
		// of paper
	}
	public void printfooter(String text) {
		//Need to find the bottom, less the font
		//And centre it
		int x = (trim.width - gc.textExtent(text).x) / 2;
		gc.drawText(text, x, trim.height - gc.getFontMetrics().getHeight());
		
	}
	public void println(String text) {
		println(text,false);
	}
	public void drawImage(Image image) {
		gc.drawImage(image, leftMargin, topMargin + row);
		row += image.getBounds().height;
	}
	public void println(String text,boolean underline) {
		gc.drawText(text, leftMargin, topMargin + row);			
		if (underline) {
			underline(text);
		}
		row += gc.getFontMetrics().getHeight();
	}
	private void underline(String text) {
		Point p = gc.textExtent(text);
		gc.drawLine(leftMargin, topMargin + row + gc.getFontMetrics().getHeight() - 3, leftMargin + p.x, topMargin + row +  gc.getFontMetrics().getHeight() - 3);
		gc.drawLine(leftMargin, topMargin + row + gc.getFontMetrics().getHeight() - 2, leftMargin + p.x, topMargin + row +  gc.getFontMetrics().getHeight() - 2);
		gc.drawLine(leftMargin, topMargin + row + gc.getFontMetrics().getHeight() - 1, leftMargin + p.x, topMargin + row +  gc.getFontMetrics().getHeight() - 1);
	}
	public void printcols(String[] texts) {
		printcols(texts,false);
	}
	public void printcols(String[] texts,boolean underline) {
		//Available line width is
		int totalLineWidth = trim.width - (leftMargin * 2);
		int columnWidth = totalLineWidth / texts.length;
		int x = leftMargin;
		for (int i=0;i<texts.length;i++) {
			gc.drawText(texts[i], x, topMargin + row);
			if (underline) {
				Point p = gc.textExtent(texts[i]);
				gc.drawLine(x, topMargin + row + gc.getFontMetrics().getHeight() - 1, x + p.x, topMargin + row +  gc.getFontMetrics().getHeight() - 1);

			}
			x += columnWidth;
		}
		row += gc.getFontMetrics().getHeight();
	}
	
	public void printcols224(String[] texts,boolean underline) {
		//Available line width is
		int totalLineWidth = trim.width - (leftMargin * 2);
		int columnWidth = totalLineWidth / (texts.length + 1) ;
		int x = leftMargin;
		for (int i=0;i<texts.length;i++) {
			gc.drawText(texts[i], x, topMargin + row);
			if (underline) {
				Point p = gc.textExtent(texts[i]);
				gc.drawLine(x, topMargin + row + gc.getFontMetrics().getHeight() - 1, x + p.x, topMargin + row +  gc.getFontMetrics().getHeight() - 1);

			}
			
			x += columnWidth;
		}
		row += gc.getFontMetrics().getHeight();
	}
	
	public void crlf() {
		row += gc.getFontMetrics().getHeight();
	}
	public void setFont(Font font) {
		gc.setFont(font);
	}
	public void hr() {
		int totalLineWidth = trim.width - (leftMargin * 2);
		int x = leftMargin;
		gc.drawLine(x, topMargin + row, totalLineWidth, topMargin + row);
		row += gc.getFontMetrics().getHeight() / 4;
	}
}
