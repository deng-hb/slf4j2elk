package com.denghb.slf4j2elk;

import com.denghb.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private static Logger logger = LoggerFactory.getLogger(AppTest.class);

    public static void main(String... args) {
//        logger.debug("asdasdasdsadsaas");
        HttpUtils.send("http://localhost:31311/example","{\"sa\":\"asd\",\"sa3\":\"asd\",\"sa33\":\"asd\"}");
    }

}
