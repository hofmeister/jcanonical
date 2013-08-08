package com.vonhof.jcanonical;


import com.vonhof.jcanonical.targets.NestedExample;

import java.math.BigDecimal;
import java.util.List;

@Canonical(NestedExample.class)
public class ExampleCanonical {
    @CanonicalField("Some/Deeply/Nested/ID/value")
    String ID;

    @CanonicalField("Some/Deeply/Nested/Amount/value")
    BigDecimal amount;

    @CanonicalField("Some/Deeply/Nested/Examples")
    List<NestedExample> examples;
}
