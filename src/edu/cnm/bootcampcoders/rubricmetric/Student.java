package edu.cnm.bootcampcoders.rubricmetric;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;

public class Student {
	protected Double blamePercentage;
	protected Integer ticketsCompleted;
	protected Integer ticketsAssigned;

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
	public Student(Double newBlamePercentage, Integer newTicketsCompleted, Integer newTicketsAssigned) throws IllegalArgumentException {
		try {
			setBlamePercentage(newBlamePercentage);
			setTicketsAssigned(newTicketsAssigned);
			setTicketsCompleted(newTicketsCompleted);
		} catch(IllegalArgumentException illegalArgument) {
			throw(new IllegalArgumentException(illegalArgument.getMessage(), illegalArgument));
		}
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
