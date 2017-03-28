package com.denghb.slf4j2elk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private static Logger logger = LoggerFactory.getLogger(AppTest.class);


    public static void main(String... args) {

        for (int i = 0; i < 10000; i++) {
            logger.error("error" + i, new Exception("abcdefg"));
            logger.trace("trace" + i);
            logger.debug("debug" + i);
            logger.info("info" + i);
            logger.warn("warn" + i);

        }
//        HttpUtils.send("http://localhost:31311","{\"a\":\"a\",\"b\":\"b\",\"c\":\"c\"}");
    }


}
