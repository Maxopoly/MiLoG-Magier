package com.github.maxopoly.milo_magier;

public class KITSheetConfig extends SheetConfig {

	private String name;
	private String personalNumber;
	private String institute;
	private String hourlyWage;
	private boolean gf;
	
	public KITSheetConfig(Configuration generalConfig, String name, String personalNumber, String institute, String hourlyWage, boolean gf) {
		super(generalConfig);
		this.name = name;
		this.personalNumber = personalNumber;
		this.institute = institute;
		this.hourlyWage = hourlyWage;
		this.gf = gf;
	}
	
	/**
	 * @return Name of the worker
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Poor translation of "Personalnummer"
	 * @return Internal number KIT knows your job by
	 */
	public String getPersonalNumber() {
		return personalNumber;
	}
	
	/**
	 * @return The institute or organizational unit you work for
	 */
	public String getInstitute() {
		return institute;
	}
	
	/**
	 * Provided as a string, because its more comfortable and we are never doing any math on it
	 * @return How much you should be earning per hour
	 */
	public String getHourlyWage() {
		return hourlyWage;
	}
	
	/**
	 * No idea what this means, but it's in the sheet
	 * @return
	 */
	public boolean isGF() {
		return gf;
	}
}
