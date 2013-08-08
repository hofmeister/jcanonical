package com.vonhof.jcanonical;


import com.vonhof.jcanonical.targets.NestedExample;
import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class AccessorTest {

    @Test
    public void can_access_simple_nested_value() {
        NestedExample nestedExample = new NestedExample();

        ExampleCanonicalAccessor acc = JAccesssor.a(nestedExample);

        assertNull("Value should be null",acc.getAmount());

        assertNull("No properties has been populated",nestedExample.getSome());

        acc.setAmount(new BigDecimal("1234"));

        assertEquals("Value has been set",new BigDecimal("1234"), acc.getAmount());

        assertNotNull("Properties has been populated", nestedExample.getSome());



        assertEquals("Delegate has been correctly updated",new BigDecimal("1234"),
                nestedExample.getSome().getDeeply().getNested().getAmount().getValue());
    }

}
