package com.moodle.analytics.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Utility class for working with JSON
 * 
 * @author aleksandar.kovachev
 */
@Slf4j
public class RestUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static <T> T jsonToObject(String json, Class<T> type) {
		try {
			return objectMapper.readValue(json, type);
		} catch (IOException e) {
			log.error("Error has occurred while parsing json to object: {}", e.getMessage());
		}
		return null;
	}

	public static String objectToJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (IOException e) {
			log.error("Error has occurred while parsing object to json: {}", e.getMessage());
		}
		return null;
	}

}
