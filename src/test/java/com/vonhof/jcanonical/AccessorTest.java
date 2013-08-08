package com.vonhof.jcanonical;


import com.vonhof.jcanonical.targets.NestedExample;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;

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

    @Test
    public void can_convert_to_and_from_canonical() {
        NestedExample nestedExample = new NestedExample();

        ExampleCanonicalAccessor acc = JAccesssor.a(nestedExample);

        acc.setAmount(new BigDecimal("1234"));
        acc.setId("1");
        acc.setExamples(Collections.singletonList(new NestedExample()));

        ExampleCanonical exampleCanonical = acc.toCanonical();

        NestedExample nestedExample2 = new NestedExample();

        ExampleCanonicalAccessor acc2 = JAccesssor.a(nestedExample2);

        assertNull("Value should be null", acc2.getAmount());

        acc2.fromCanonical(exampleCanonical);

        assertEquals("Value has been set",new BigDecimal("1234"), acc2.getAmount());

        assertNotNull("Properties has been populated", nestedExample2.getSome());

        assertEquals("Delegate has been correctly updated",new BigDecimal("1234"),
                nestedExample2.getSome().getDeeply().getNested().getAmount().getValue());

    }

}
