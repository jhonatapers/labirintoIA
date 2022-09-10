package com.jhonatapers.labirinto.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy HH_mm_ss");
		Date date = new Date();
		
		String aham = dateFormat.format(date);

		return aham;
	}
}
