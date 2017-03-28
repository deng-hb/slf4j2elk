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

    public static void main(String... args) {

        for (int i = 0; i < 1000; i++) {

            logger.debug("asdasdasdsadsaasdasds" + RandomStringUtils.random(9));
            logger.info("asdasdasdsadsaasdasds" + RandomStringUtils.random(9));
            logger.warn("asdasdasdsadsaasdasds" + RandomStringUtils.random(9));

        }
//        HttpUtils.send("http://localhost:31311/example","{\"sa\":\"asd\",\"sa3\":\"asd\",\"sa33\":\"asd\"}");
    }


}
