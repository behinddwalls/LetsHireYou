package com.portal.job.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javafx.util.Builder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.portal.job.constants.DateConstants;

public final class DateUtility {
	private static Logger log = LoggerFactory
			.getLogger(DateUtility.class);

	public static String getStringFromDate(final Date date) {
		if (null == date) {
			return StringUtils.EMPTY;
		}
		return new SimpleDateFormat(DateConstants.DD_MM_YYYY_FORMAT)
				.format(date);
	}

	public static String getStringFromDate(final Date date,
			final String dateFormat) {
		if (null == date) {
			return StringUtils.EMPTY;
		}
		return new SimpleDateFormat(dateFormat).format(date);
	}

	public static Date getDateFromString(final String dateInString)
			throws ParseException {
		return DateUtils.parseDate(dateInString,
				DateConstants.DD_MM_YYYY_FORMAT);
	}

	public static Date getDateFromString(final String dateInString,
			final String dateFormat) throws ParseException {
		log.info("zzzzz dateInString:"+dateInString+"###");
		return DateUtils.parseDate(dateInString, dateFormat);
	}

	// returns the new date after adding the numbe of years
	public static Date getDateAfterYears(Date date, int numberOfYearsToAdd) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, numberOfYearsToAdd);
		return cal.getTime();
	}
	
	public static int getDifferenceInMonths(Date startDate, Date endDate) {
		DateTime jodaD1 = new DateTime(startDate);
		DateTime jodaD2 = new DateTime(endDate);
		return Months.monthsBetween(jodaD1, jodaD2).getMonths();
	}
	
	public static class DateBuilder implements Builder<Date>{
		

		private int year;
		private int month = 1;
		private int day = 1;
		
		public DateBuilder setYear(int year){
			this.year = year;
			return this;
		}
		
		public DateBuilder setMonth(int month){
			this.month = month;
			return this;
		}
		
		public DateBuilder setDateOfMonth(int day){
			this.day = day;
			return this;
		}

		public Date build() {
			Calendar cal = Calendar.getInstance();
			cal.set(year, month, day);
			return cal.getTime();			
		}
		
		
		
	}
}
