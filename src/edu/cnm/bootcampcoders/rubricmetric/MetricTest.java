package edu.cnm.bootcampcoders.rubricmetric;


import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.LinkedList;

public class MetricTest {
	public static Integer safeParseInt(String integerData, String fieldName, Integer defaultValue) {
		Integer integer;
		try {
			integer = Integer.parseInt(integerData);
		} catch(NumberFormatException numberFormat) {
			System.err.println("WARNING: Invalid number of " + fieldName + ". Using default of " + defaultValue + ".");
			integer = defaultValue;
		}

		return(integer);
	}

	public static void main(String[] argv) {
		// get the number of simulations to run
		Integer numSimulations = 1024;
		if(argv.length >= 1) {
			numSimulations = safeParseInt(argv[0], "simulations", numSimulations);
		}

		// get the number of students to a group
		Integer groupSize = 4;
		if(argv.length >= 2) {
			groupSize = safeParseInt(argv[1], "students per group", groupSize);
		}

		// setup the random and statistical engines
		DescriptiveStatistics dataPoints = new DescriptiveStatistics();
		MersenneTwister mersenneTwister = new MersenneTwister();
		for(Integer i = 0; i < numSimulations; i++) {
			Double nextValue;
			Double sum = 0.0;
			Integer j;
			LinkedList<Double> blamePercentages = new LinkedList<Double>();

			// randomize the git blame percentages
			for(j = 0; j < groupSize - 1; j++) {
				do {
					nextValue = mersenneTwister.nextDouble();
				} while(nextValue + sum >= 1.0);
				sum = sum + nextValue;
				blamePercentages.add(nextValue);
			}

			// make the last percentage add to 100%
			nextValue = 1.0 - sum;
			blamePercentages.add(nextValue);

			// build up the groups
			LinkedList<Student> group = new LinkedList<Student>();
			for(Double blamePercent : blamePercentages) {
				Student nextStudent = new Student(blamePercent, 1, 1);
				group.add(nextStudent);
			}

			// calculate the blame metric for each student
			j = 0;
			for(Student student : group) {
				Double blameMetric = student.blameMetric(blamePercentages, j);
				dataPoints.addValue(Math.abs(blameMetric));
				j++;
			}
		}

		// print statistical summary
		System.out.println("Arithmetic Mean: " + dataPoints.getMean());
		System.out.println("Geometric mean: " + dataPoints.getGeometricMean());
		System.out.println("Standard deviation: " + dataPoints.getStandardDeviation());
		System.out.println("Sample size: " + dataPoints.getN());
	}
}
