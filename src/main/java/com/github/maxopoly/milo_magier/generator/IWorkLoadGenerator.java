package com.github.maxopoly.milo_magier.generator;

import java.util.List;

import com.github.maxopoly.milo_magier.Configuration;
import com.github.maxopoly.milo_magier.WorkActivity;

public interface IWorkLoadGenerator {
	
	public List<WorkActivity> generateWork(Configuration config);

}
