package com.github.maxopoly.milo_magier;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

public class PDFDebugger {
	
	public static void main(String [] args) {
		PDDocument pdfDoc;
        try {
			pdfDoc = PDDocument.load(new File("src/main/resources/template.pdf"));
	        PDDocumentCatalog docCatalog = pdfDoc.getDocumentCatalog();
			PDAcroForm acroForm = docCatalog.getAcroForm();
			int i = 0;
			for(PDField field : acroForm.getFields()) {
				System.out.println(field.getFullyQualifiedName() + " : " + i);
				try {
				field.setValue(String.valueOf(i++));
				}
				catch (IllegalArgumentException e) {
					System.out.println(e);
				}
				
			}
			
			pdfDoc.save(new File("src/main/resources/filled.pdf"));
			 pdfDoc.close();
		} catch (InvalidPasswordException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
	} 
	}

}
