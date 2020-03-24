package com.github.maxopoly.milo_magier;

public abstract class SheetConfig {
	
	private Configuration generalConfig;
	
	public SheetConfig(Configuration generalConfig) {
		this.generalConfig = generalConfig;
	}
	
	public Configuration getGeneralConfiguration() {
		return generalConfig;
	}

}
