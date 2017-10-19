package com.abc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbcTest {
    private Logger log = LoggerFactory.getLogger(AbcTest.class);

    public void say() {
        log.debug("abc");
    }
}
