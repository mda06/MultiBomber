package com.mda.bomb.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.regex.Pattern;

public class IPUtils {
	public static boolean isValidIP(final String ip) {
		Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	    return PATTERN.matcher(ip).matches();
	}
	
	public static String getMyIP() {
		try {
			for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
				for (InetAddress address : Collections.list(iface.getInetAddresses())) {
					if(!address.isLoopbackAddress() && !address.isLinkLocalAddress()) {
						return address.getHostAddress().trim();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
			
		return "Unknown";
	}
}
