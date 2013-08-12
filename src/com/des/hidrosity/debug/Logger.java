package com.des.hidrosity.debug;

import com.des.hidrosity.constants.GameConstants;

public class Logger {

	public static void log(String s) {
		if (GameConstants.DEBUG) {
			System.out.println(s);
		}
	}
	
}
