package com.portal.job.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkExperienceUtitlity {

	private static Logger log = LoggerFactory
			.getLogger(WorkExperienceUtitlity.class);

	// cannot be initialized
	private WorkExperienceUtitlity() {
	}

	public static int getWorkExperienceInMonths(List<ExperiencePeriod> periodList)
			throws Exception {
		log.info("zzzzzzzzz");
		if(periodList.isEmpty())
			return 0;
		List<Date> startDates = periodList.stream()
				.map(period -> period.getStartDate()).sorted()
				.collect(Collectors.toList());
		log.info("sorted start date are  = "+startDates.toString());
		List<Date> endDates = periodList.stream()
				.map(period -> period.getEndDate()).sorted() 
				.collect(Collectors.toList());
		
		log.info("sorted end dates are = "+endDates.toString());
		List<DateWrapper> mergedDates = mergeSortedDates(startDates, endDates);
		log.info("merged dates are "+mergedDates.toString());
		if (mergedDates.get(mergedDates.size() - 1).getType() != DateType.EndDate)
			throw new Exception("Date not in correct format"
					+ mergedDates.toString());

		DateWrapper indexed = null;
		int numEndsDates = 0;
		int totalSum = 0;
		Collections.reverse(mergedDates);
		log.info("reversed merged dates are "+mergedDates);
		for(DateWrapper d : mergedDates) {
			if (d.getType() == DateType.EndDate) {
				numEndsDates++;
				if (indexed == null) {
					indexed = d;
				}
				
			} else {
				numEndsDates--;
				if (numEndsDates == 0) {
					log.info("totalSum before = "+ totalSum);
					totalSum = totalSum
							+ DateUtility.getDifferenceInMonths(d.getD(),indexed.getD());
					log.info("difference = "+DateUtility.getDifferenceInMonths(d.getD(),indexed.getD()));
					log.info("total sum after "+totalSum);
					indexed = null;
				}
			}
			log.info("numOfEndDates = "+numEndsDates + "  indexed = "+indexed);
		}
		log.info("zzzzzzzzz total Sum = "+totalSum);
		return totalSum;
	}

	public static class ExperiencePeriod {
		private Date startDate;
		private Date endDate;

		@Override
		public String toString() {
			return "ExperiencePeriod [startDate=" + startDate + ", endDate="
					+ endDate + "]";
		}

		public ExperiencePeriod(Date startDate, Date endDate) {
			super();
			this.startDate = (startDate.compareTo(endDate) <= 0) ? startDate
					: endDate;
			this.endDate = (startDate.compareTo(endDate) > 0) ? startDate
					: endDate;
		}

		public Date getStartDate() {
			return startDate;
		}

		public Date getEndDate() {
			return endDate;
		}
	}
	

	private static List<DateWrapper> mergeSortedDates(List<Date> startDates,
			List<Date> endDates) throws Exception {
		int i = 0;
		int j = 0;
		List<DateWrapper> mergedList = new ArrayList<DateWrapper>();
		while (i < startDates.size()  && j < endDates.size() ) {
			if (startDates.get(i).compareTo(endDates.get(j)) <= 0) {
				mergedList.add(new DateWrapper(startDates.get(i),
						DateType.StartDate));
				i++;
				continue;
			}
			mergedList.add(new DateWrapper(endDates.get(j), DateType.EndDate));
			j++;

		}
		if (i < startDates.size() && j < endDates.size())
			throw new Exception("Something wrong with comparing the dates i = "
					+ i + " j = " + j);

		if (i < startDates.size()) {
			mergedList.addAll(startDates
					.subList(i, startDates.size())
					.stream()
					.map(startDate -> new DateWrapper(startDate,
							DateType.StartDate)).collect(Collectors.toList()));
		}
		if (j < endDates.size()) {
			mergedList.addAll(endDates.subList(j, endDates.size()).stream()
					.map(endDate -> new DateWrapper(endDate, DateType.EndDate))
					.collect(Collectors.toList()));
		}
		return mergedList;
	}

	

	private static class DateWrapper {
		private Date d;
		private DateType type;

		public Date getD() {
			return d;
		}

		@Override
		public String toString() {
			return "DateWrapper [d=" + d + ", type=" + type + "]";
		}

		public DateType getType() {
			return type;
		}

		public DateWrapper(Date d, DateType type) {
			this.d = d;
			this.type = type;
		}

	}

	private enum DateType {
		StartDate, EndDate;
	}

}
