package com.denghb.slf4j2elk;

import com.abc.AbcTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private static Logger logger = LoggerFactory.getLogger(AppTest.class);


    public static void main(String... args) throws IOException {

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            logger.error("error" + i, new Exception("abcdefg"));
            logger.trace("trace" + i);
            logger.debug("debug" + i);
            logger.info("info" + i);
            logger.warn("warn" + i);

        }

        AbcTest abc = new AbcTest();
        abc.say();

        System.out.println(System.currentTimeMillis() - start);
//        HttpUtils.send("http://localhost:31311","{\"a\":\"a\",\"b\":\"b\",\"c\":\"c\"}");
//        FileUtils.checkExist("/tmp/asd/asdas.log");

        String client = "don't  touch me for website";
        if (null == client || 0 != client.indexOf("don't touch me")) {
            System.out.println("sada");
            return;
        }

    }


}
