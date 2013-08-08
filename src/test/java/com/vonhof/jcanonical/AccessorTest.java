package com.vonhof.jcanonical;


import com.vonhof.jcanonical.targets.NestedExample;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class AccessorTest {



    @Test
    public void can_get_nested_value() {
        NestedExample nestedExample = new NestedExample();

        ExampleCanonicalAccessor acc = JAccesssor.a(nestedExample);

        assertEquals(new BigDecimal("1234"),acc.getAmount());
    }


    @Test
    public void can_set_nested_value() {
        NestedExample nestedExample = new NestedExample();

        ExampleCanonicalAccessor acc = JAccesssor.a(nestedExample);

        assertEquals(new BigDecimal("1234"), acc.getAmount());

        acc.setAmount(new BigDecimal("4321"));

        assertEquals(new BigDecimal("4321"), acc.getAmount());
    }


}
