package com.github.maxopoly.milo_magier.document;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;

import com.github.maxopoly.milo_magier.Configuration;
import com.github.maxopoly.milo_magier.KITSheetConfig;
import com.github.maxopoly.milo_magier.WorkActivity;
import com.github.maxopoly.milo_magier.generator.IWorkLoadGenerator;

public class KITDocumentFiller implements IDocumentFiller<KITSheetConfig> {

	@Override
	public void fill(PDDocument document, IWorkLoadGenerator generator, KITSheetConfig config) {
		PDDocumentCatalog docCatalog = document.getDocumentCatalog();
		PDAcroForm acroForm = docCatalog.getAcroForm();
		Configuration genConfig = config.getGeneralConfiguration();
		if (config.isGF()) {
			setValue(acroForm, "undefined_2", "X");
		}
		else {
			setValue(acroForm, "UB", "On");
		}

		//these are correct, KIT PDFs are messed up
		setValue(acroForm, "GF", config.getName());
		setValue(acroForm, "undefined", String.valueOf(formatLeadingZero(genConfig.getMonth())));
		setValue(acroForm, "Jahr", String.valueOf(genConfig.getYear()));

		setValue(acroForm, "Vertraglich vereinbarte Arbeitszeit", formatDuration(genConfig.getTimeToWork()));
		setValue(acroForm, "Stundensatz", config.getHourlyWage());
		setValue(acroForm, "Institut", config.getInstitute());
		setValue(acroForm, "Personalnummer", config.getPersonalNumber());

		setValue(acroForm, "Übertrag vom Vormonat", "0:00");
		setValue(acroForm, "Übertrag in den Folgemonat", "0:00");
		setValue(acroForm, "Urlaub anteilig", "0:00");
		setValue(acroForm, "monatliche SollArbeitszeit", formatDuration(genConfig.getTimeToWork()));
		setValue(acroForm, "Summe", formatDuration(genConfig.getTimeToWork()));

		int counter = 1;
		for (WorkActivity activity : generator.generateWork(config.getGeneralConfiguration())) {
			setValue(acroForm, "Tätigkeit Stichwort ProjektRow" + (counter + 1), activity.getDescription());
			setValue(acroForm, "ttmmjjRow" + counter,
					formatDay(activity.getDay(), genConfig.getMonth(), genConfig.getYear()));
			setValue(acroForm, "hhmmRow" + counter, formatDuration(activity.getRelativeBeginningTime()));
			setValue(acroForm, "hhmmRow" + counter+"_2", formatDuration(activity.getRelativeEndTime()));
			setValue(acroForm, "hhmmRow" + counter+"_3", formatDuration(activity.getBreakTime()));
			setValue(acroForm, "hhmmRow" + counter+"_4", formatDuration(activity.getWorkingTime()));
			counter++;
		}
		addImage(document, "src/main/resources/signature.png", 430, 170);
	}

	private static String formatDay(int dayOfMonth, int month, int year) {
		StringBuilder result = new StringBuilder();
		result.append(formatLeadingZero(dayOfMonth));
		result.append(".");
		result.append(formatLeadingZero(month));
		result.append(".");
		result.append(year);
		return result.toString();
	}

	private static String formatDuration(long duration) {
		// round to minutes
		duration /= 1000 * 60;
		long minutes = duration % 60;
		return String.format("%d:%s", duration / 60, minutes == 0 ? "00" : String.valueOf(minutes));
	}

	private static void setValue(PDAcroForm acroForm, String key, String value) {
		try {
			acroForm.getField(key).setValue(value);
		} catch (IOException e) {
			System.out.println("Failed to set value in sheet");
			e.printStackTrace();
		}
		System.out.println("Setting " + key + " to " + value);
	}
	
	private static void addImage(PDDocument doc, String path, int wOffset, int hOffset) {
		PDPage page = doc.getPage(0);
		try {
			PDImageXObject imgObject = PDImageXObject.createFromFile(path, doc);
			PDPageContentStream contents = new PDPageContentStream(doc, page, true, true);
	        contents.drawImage(imgObject, wOffset, hOffset);
	        contents.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 	
	private static String formatLeadingZero(int num) {
		if (num <= 9) {
			return "0" + num;
		}
		return String.valueOf(num);
	}

}
