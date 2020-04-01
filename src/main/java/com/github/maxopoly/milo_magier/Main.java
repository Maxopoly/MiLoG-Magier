package com.github.maxopoly.milo_magier;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import com.github.maxopoly.milo_magier.document.KITDocumentFiller;
import com.github.maxopoly.milo_magier.generator.SimpleWorkLoadGenerator;

public class Main {
	
	public static void main(String[] args) {
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
        Configuration genConfig = new Configuration(TimeUnit.HOURS.toMillis(20), TimeUnit.HOURS.toMillis(1), TimeUnit.HOURS.toMillis(2),
				TimeUnit.HOURS.toMillis(6), TimeUnit.HOURS.toMillis(10), TimeUnit.HOURS.toMillis(18), false,
				TimeUnit.HOURS.toMillis(3), 2019, 11, Arrays.asList("Verschiedenes", "Windows 10", "Dokumentation", "Beratung","Email Probleme", "Windows Probleme", "SCC", "Querbeet","Computer","Wartung","Software updates", "Hardware Probleme", "Citavi", "Backups", "Mitarbeiter Support"));
		KITDocumentFiller docFiler = new KITDocumentFiller();
		SimpleWorkLoadGenerator generator = new SimpleWorkLoadGenerator();
		KITSheetConfig config = new KITSheetConfig(genConfig, "Max Maxopoly", "", "Spaßinstitut IT", "11,99", false);
		docFiler.fill(pdfDoc, generator, config);
		
		try {
			pdfDoc.save(new File("src/main/resources/filled.pdf"));
			 pdfDoc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
