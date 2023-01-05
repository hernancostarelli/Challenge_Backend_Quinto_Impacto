package com.api.challenge.model.enums;

public enum ERole {

    ADMIN,
    TEACHER,
    STUDENT

    /*ADMIN("Admin"),
    TEACHER("Teacher"),
    STUDENT("Student");

    private final String roleString;

    ERole(String roleString) {
        this.roleString = roleString;
    }

    @Override
    public String toString() {
        return roleString;
    }

    public static ERole typeOrValue(String value) {
        for(ERole type : ERole.class.getEnumConstants()) {
            if(type.toString().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("ERole NOT FOUND");
    }*/
}