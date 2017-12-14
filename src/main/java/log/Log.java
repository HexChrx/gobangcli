package log;

import org.apache.log4j.Logger;

public class Log {
    private final static Logger LOGGER = Logger.getLogger("");
    /**
     * 调试日志
     * @param object 消息
     */
    public static void logD(Object object) {
        LOGGER.debug(object);
        //System.out.println(object);
    }

    /**
     * 错误日志
     * @param object 消息
     */
    public static void logE(Object object) {
        LOGGER.error(object);
    }
}
