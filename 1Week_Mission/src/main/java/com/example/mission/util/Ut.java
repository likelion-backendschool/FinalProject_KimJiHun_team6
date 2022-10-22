package com.example.mission.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.stereotype.Component;

@Component
public class Ut {
	public static class url{
		public static String modifyQueryParam(String url, String paramName, String paramValue) {
			if (!url.contains("?")) {
				url += "?";
			}
			if (!url.endsWith("?") && !url.endsWith("&")) {
				url += "&";
			}
			url += paramName + "=" + paramValue;
			return url;
		}
		public static String encode(String str) {
			try {
				return URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return str;
			}
		}
	}
}
