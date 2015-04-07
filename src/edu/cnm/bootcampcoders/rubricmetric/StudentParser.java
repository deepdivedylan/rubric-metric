package edu.cnm.bootcampcoders.rubricmetric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

public class StudentParser {
	public static void main(String[] argv) {
		BufferedReader input = null;
		String stdin = null;

		// read the CSV data from standard input
		try {
			input = new BufferedReader(new InputStreamReader(System.in));
			StringBuilder builder = new StringBuilder();
			String line;
			while((line = input.readLine()) != null) {
				builder.append(line);
				builder.append("\n");
			}
			stdin = builder.toString();
		} catch(IOException io) {
			System.err.println("Unable to parse input: " + io.getMessage());
			System.exit(1);
		} finally {
			// close standard input gracefully
			try {
				if(input != null) {
					input.close();
				}
			} catch(IOException io) {
				System.err.println("Unable to close stdin: " + io.getMessage());
				System.exit(1);
			}
		}

		// take the parsed data and analyze it
		try {
			Integer i = 0;
			List<Student> studentList = Student.parseStudentCSVData(stdin);
			List<Double> blameList = studentList.stream().map(Student::getBlamePercentage).collect(Collectors.toList());
			for(Student student : studentList) {
				DecimalFormat decimalFormat = new DecimalFormat("0.0000");
				System.out.println(student.getName() + "," + decimalFormat.format(student.blameMetric(blameList, i)));
				i++;
			}
		} catch(IOException io) {
			System.err.println("Unable to parse CSV data: " + io.getMessage());
		}
	}
}
