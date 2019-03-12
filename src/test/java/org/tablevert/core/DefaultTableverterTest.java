package org.tablevert.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DefaultTableverterTest {

    private final DefaultTableverter defaultTableverter = new DefaultTableverter();

    @Test
    public void returnsDummyDummyString() {
        String dummy = defaultTableverter.getDummyDummyString();
        Assertions.assertTrue(dummy.contains("dummy string"));
        System.out.println("gloob ick nich");
    }

}
