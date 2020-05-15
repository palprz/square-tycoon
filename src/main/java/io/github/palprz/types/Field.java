package io.github.palprz.types;

import com.vaadin.flow.component.html.Div;

import java.util.StringJoiner;

public class Field {

    private Div element;
    private FieldMetadata fieldMetadata;

    public Field() {
    }

    public Div getElement() {
        return element;
    }

    public void setElement(Div element) {
        this.element = element;
    }

    public FieldMetadata getFieldMetadata() {
        return fieldMetadata;
    }

    public void setFieldMetadata(FieldMetadata fieldMetadata) {
        this.fieldMetadata = fieldMetadata;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Field.class.getSimpleName() + "[", "]")
                .add("element=" + element)
                .add("fieldMetadata=" + fieldMetadata)
                .toString();
    }
}
