package org.jannocessor.config;


import com.vonhof.jcanonical.Canonical;
import com.vonhof.jcanonical.CanonicalProcessor;
import org.jannocessor.model.structure.JavaClass;
import org.jannocessor.processor.annotation.Annotated;
import org.jannocessor.processor.annotation.Types;

public class Processors {

    @Annotated(Canonical.class)
    @Types(JavaClass.class)
    public CanonicalProcessor willProcessMyAnnotatedClasses() {
        return new CanonicalProcessor();
    }
}
