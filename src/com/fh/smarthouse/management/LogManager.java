package com.fh.smarthouse.management;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogManager {
	private static Logger logger;

	static {
		try {
			logger = Logger.getLogger("SmartHouseLogger");
			String logFilePath = "C:\\\\Users\\\\bhava\\\\Desktop\\\\logs\\SmartHouseLog.txt";
			FileHandler fileHandler = new FileHandler(logFilePath, true);
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);
			logger.setUseParentHandlers(false);

		} catch (IOException e) {
			System.err.println("Failed to initialize logger: " + e.getMessage());
		}
	}

	// Provide a public method to retrieve the logger instance

	public static Logger getLogger() {
		return logger;
	}

}
