package com.github.maxopoly.milo_magier.document;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.github.maxopoly.milo_magier.SheetConfig;
import com.github.maxopoly.milo_magier.generator.IWorkLoadGenerator;

public interface IDocumentFiller <T extends SheetConfig> {
	
	public void fill(PDDocument document, IWorkLoadGenerator generator, T config);

}
