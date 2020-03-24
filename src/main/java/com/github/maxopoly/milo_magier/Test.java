package com.github.maxopoly.milo_magier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import com.github.maxopoly.milo_magier.generator.SimpleWorkLoadGenerator;

public class Test {

	public static void main(String[] args) {
		SimpleWorkLoadGenerator gen = new SimpleWorkLoadGenerator();
		Configuration config = new Configuration(TimeUnit.HOURS.toMillis(20), TimeUnit.HOURS.toMillis(1), TimeUnit.HOURS.toMillis(2),
				TimeUnit.HOURS.toMillis(6), TimeUnit.HOURS.toMillis(10), TimeUnit.HOURS.toMillis(18), false,
				TimeUnit.HOURS.toMillis(3), 2020, 3, Arrays.asList("Computer","Maintenance","Software updates"));
		for(WorkActivity work : gen.generateWork(config)) {
			System.out.println(work);
		}
	}

}
