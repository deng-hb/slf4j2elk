package com.denghb.slf4j2elk;

import com.denghb.utils.HttpUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private static Logger logger = LoggerFactory.getLogger(AppTest.class);

    private static final char[] chars = {'a', 'b', '1', '2', 'c', 'd', 'e',
            'f', '3'};

    public static void main(String... args) {

        for (int i = 0; i < 1000; i++) {
            logger.error("error" + RandomStringUtils.random(9, chars), new Exception("error"));

            logger.debug("debug" + RandomStringUtils.random(9, chars));
            logger.info("info" + RandomStringUtils.random(9, chars));
            logger.warn("warn" + RandomStringUtils.random(9, chars));

        }
//        HttpUtils.send("http://localhost:31311/example","{\"sa\":\"asd\",\"sa3\":\"asd\",\"sa33\":\"asd\"}");
    }


}
