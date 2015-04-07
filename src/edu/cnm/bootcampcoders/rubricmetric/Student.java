package edu.cnm.bootcampcoders.rubricmetric;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Student {
	protected Double blamePercentage;
	protected Integer ticketsCompleted;
	protected Integer ticketsAssigned;
	protected String name;

	/**
	 * default constructor that creates an empty Student
	 *
	 * @throws IllegalArgumentException never - just declared for consistency with the full constructor
	 **/
	public Student() throws IllegalArgumentException {
		try {
			setBlamePercentage(0.0);
			setTicketsAssigned(1);
			setTicketsCompleted(0);
			setName("");
		} catch(IllegalArgumentException illegalArgument) {
			throw(new IllegalArgumentException(illegalArgument.getMessage(), illegalArgument));
		}
	}

	/**
	 * full constructor that creates a full Student
	 *
	 * @param newBlamePercentage percentage of the git blame the student earned
	 * @param newTicketsCompleted number of tickets the students completed
	 * @param newTicketsAssigned number of tickets assigned to the student
	 * @throws IllegalArgumentException if any of the parameters are out of bounds
	 */
	public Student(Double newBlamePercentage, Integer newTicketsCompleted, Integer newTicketsAssigned, String newName) throws IllegalArgumentException {
		try {
			setBlamePercentage(newBlamePercentage);
			setTicketsAssigned(newTicketsAssigned);
			setTicketsCompleted(newTicketsCompleted);
			setName(newName);
		} catch(IllegalArgumentException illegalArgument) {
			throw(new IllegalArgumentException(illegalArgument.getMessage(), illegalArgument));
		}
	}

	/**
	 * takes CSV data and generates a Container of Student records
	 *
	 * @param csvData string containing CSV data: {blameLines,studentName}
	 * @return Container of the parsed Student data
	 * @throws IOException on CSV read errors
	 * @throws IllegalArgumentException if CSV data contains the incorrect number of fields
	 **/
	public static List<Student> parseStudentCSVData(String csvData) throws IOException, IllegalArgumentException {
		CSVParser csvParser = CSVParser.parse(csvData, CSVFormat.DEFAULT);
		LinkedList<Student> students = new LinkedList<Student>();
		LinkedList<Integer> blameLines = new LinkedList<Integer>();

		// parse each CSV record
		for(CSVRecord csvRecord : csvParser) {
			if(csvRecord.size() != 2) {
				throw(new IllegalArgumentException("incorrect number of fields detected in CSV data"));
			}

			// populate the student and blame lists
			Student student = new Student();
			student.setName(csvRecord.get(0));
			students.add(student);
			blameLines.add(Integer.parseInt(csvRecord.get(1)));
		}

		// calculate each percentage
		Iterator<Integer> blameIterator = blameLines.iterator();
		Integer sum = blameLines.stream().mapToInt(Integer::intValue).sum();
		for(Student student : students) {
			Double percentage = blameIterator.next().doubleValue() / sum.doubleValue();
			student.setBlamePercentage(percentage);
		}

		return(students);
	}

	/**
	 * accessor method for blame percentage
	 *
	 * @return value of blame percentage
	 **/
	public Double getBlamePercentage() {
		return blamePercentage;
	}

	/**
	 * mutator method for blame percentage
	 *
	 * @param newBlamePercentage new value of blame percentage
	 * @throws IllegalArgumentException if blame percentage is not a valid percent
	 **/
	public void setBlamePercentage(Double newBlamePercentage) throws IllegalArgumentException {
		if(newBlamePercentage < 0.0 || newBlamePercentage > 1.0) {
			throw(new IllegalArgumentException("percentage must be on the interval [0, 1]"));
		}
		blamePercentage = newBlamePercentage;
	}

	/**
	 * accessor method for tickets completed
	 *
	 * @return value of tickets completed
	 **/
	public Integer getTicketsCompleted() {
		return ticketsCompleted;
	}

	/**
	 * mutator method for tickets completed
	 *
	 * @param newTicketsCompleted value of tickets completed
	 * @throws IllegalArgumentException if tickets completed is negative or exceeds tickets assigned
	 **/
	public void setTicketsCompleted(Integer newTicketsCompleted) throws IllegalArgumentException {
		if(newTicketsCompleted < 0) {
			throw(new IllegalArgumentException("tickets completed must be non negative"));
		}
		if(newTicketsCompleted > ticketsAssigned) {
			throw(new IllegalArgumentException("tickets completed cannot exceed tickets assigned"));
		}
		ticketsCompleted = newTicketsCompleted;
	}

	/**
	 * accessor method for tickets assigned
	 *
	 * @return value of tickets assigned
	 **/
	public Integer getTicketsAssigned() {
		return ticketsAssigned;
	}

	/**
	 * mutator method for tickets assigned
	 *
	 * @param newTicketsAssigned new value of tickets assigned
	 * @throws IllegalArgumentException if tickets assigned is negative or zero
	 **/
	public void setTicketsAssigned(Integer newTicketsAssigned) throws IllegalArgumentException {
		if(newTicketsAssigned <= 0) {
			throw(new IllegalArgumentException("tickets assigned must be positive"));
		}
		ticketsAssigned = newTicketsAssigned;
	}

	/**
	 * accessor method for name
	 *
	 * @return value of name
	 **/
	public String getName() {
		return name;
	}

	/**
	 * mutator method for name
	 *
	 * @param newName new value of name
	 **/
	public void setName(String newName) {
		name = newName;
	}

	/**
	 * calculates the blame metric
	 *
	 * @param gitBlamePercentages Collection of blame percentages for the entire group
	 * @param studentIndex which index in the Collection represents this student
	 * @return blame metric score
	 * @throws ArrayIndexOutOfBoundsException if the index exceeds the size of the Collection or is negative
	 **/
	public Double blameMetric(List<Double> gitBlamePercentages, Integer studentIndex) throws ArrayIndexOutOfBoundsException {
		if(studentIndex < 0 || studentIndex >= gitBlamePercentages.size()) {
			throw(new ArrayIndexOutOfBoundsException("studentIndex is out of bounds"));
		}

		// convert the list to a primitive array
		double gitBlamePercentagesArray[] = new double[gitBlamePercentages.size()];
		Integer i = 0;
		for(Double gitBlamePercent : gitBlamePercentages) {
			gitBlamePercentagesArray[i] = gitBlamePercent;
			i++;
		}

		// measure against the standard deviation
		DescriptiveStatistics percentages = new DescriptiveStatistics(gitBlamePercentagesArray);
		Double studentStandardDeviation = (gitBlamePercentagesArray[studentIndex] - percentages.getMean()) / percentages.getStandardDeviation();
		return(studentStandardDeviation);
	}

	/**
	 * calculates the ticket metric
	 *
	 * @return ticket metric score
	 **/
	public Double ticketMetric() {
		return(ticketsAssigned.doubleValue() / ticketsCompleted.doubleValue());
	}
}
