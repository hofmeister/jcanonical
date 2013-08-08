package com.vonhof.jcanonical.targets;


import java.util.List;

public class NestedType {

    private AmountType amount;
    private IDType id;
    private List<NestedExample> examples;

    public AmountType getAmount() {
        return amount;
    }

    public void setAmount(AmountType amount) {
        this.amount = amount;
    }

    public IDType getId() {
        return id;
    }

    public void setId(IDType id) {
        this.id = id;
    }

    public List<NestedExample> getExamples() {
        return examples;
    }

    public void setExamples(List<NestedExample> examples) {
        this.examples = examples;
    }
}
