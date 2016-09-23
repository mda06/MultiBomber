package com.mda.bomb.util;

import java.util.regex.Pattern;

public class IPUtils {
	public static boolean isValidIP(final String ip) {
		Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	    return PATTERN.matcher(ip).matches();
	}
}
