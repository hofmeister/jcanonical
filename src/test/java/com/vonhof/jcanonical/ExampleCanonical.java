package com.vonhof.jcanonical;


import com.vonhof.jcanonical.targets.NestedExample;

import java.math.BigDecimal;
import java.util.List;

@Canonical(NestedExample.class)
public class ExampleCanonical {
    @CanonicalField("Some/Deeply/Nested/Id/value")
    String id;

    @CanonicalField("Some/Deeply/Nested/Amount/value")
    BigDecimal amount;

    @CanonicalField("Some/Deeply/Nested/Examples")
    List<NestedExample> examples;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public List<NestedExample> getExamples() {
        return examples;
    }

    public void setExamples(List<NestedExample> examples) {
        this.examples = examples;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExampleCanonical that = (ExampleCanonical) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (examples != null ? !examples.equals(that.examples) : that.examples != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (examples != null ? examples.hashCode() : 0);
        return result;
    }
}
