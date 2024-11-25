package com.fh.smarthouse.utils;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfig {
	public static void setup() throws IOException {
		FileHandler fileHandler = new FileHandler("C:\\\\Users\\\\bhava\\\\Desktop\\\\logs\\\\smartHouse.log", true);
		fileHandler.setFormatter(new SimpleFormatter());

		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.addHandler(fileHandler);

		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(consoleHandler);

		logger.setLevel(java.util.logging.Level.ALL);
	}
}


