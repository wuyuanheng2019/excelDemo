package com.example.util.enums;

public enum SelectEnum {

    STUDENT_REPORT(
            "name","age"
    );

    private String[] fields;

    SelectEnum(String... columns){
        fields = columns;
    }

    public String[] getFields() {
        return this.fields;
    }
}
