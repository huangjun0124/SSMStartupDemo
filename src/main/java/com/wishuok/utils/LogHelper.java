package com.wishuok.utils;

import org.apache.log4j.Logger;

public class LogHelper {
    private static Logger logger = Logger.getLogger("ssmcustomlog");
    public static void LogInfo(String log){
        logger.info(log);
    }

    public static void LogError(String log){
        logger.error(log);
    }
}
