package com.github.maxopoly.milo_magier;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

public class App 
{
    public static void main( String[] args ) {    
    	PDDocument pdfDoc;
        try {
			pdfDoc = PDDocument.load(new File("src/main/resources/template.pdf"));
		} catch (InvalidPasswordException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
        PDDocumentCatalog docCatalog = pdfDoc.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
int counter = 0;
        for(PDField field : acroForm.getFields()) {
        	try {
        		try  {
        		System.out.println(field.getFullyQualifiedName() + " = " + counter );
				field.setValue("test" + counter++);
        		}
        		catch (IllegalArgumentException e) {
        			
        		}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        try {
			pdfDoc.save(new File("src/main/resources/filled.pdf"));
			 pdfDoc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        
        
    }
}
