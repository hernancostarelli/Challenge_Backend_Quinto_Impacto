package com.api.challenge.model.enums;

import java.text.MessageFormat;

public enum EExceptionMessage {

    ////////////////////////////////////////////////////////////////////////////////////////////
    // GENERAL EXCEPTION MESSAGE
    ////////////////////////////////////////////////////////////////////////////////////////////

    ID_NOT_FOUND("ID NOT FOUND"),
    ID_ALREADY_EXISTS("ID ALREADY EXISTS"),
    PARAM_NOT_FOUND("PARAM_NOT_FOUND"),
    REQUEST_WRONG_DATA("INVALID REQUEST"),
    RESPONSE_WRONG_DATA("INVALID RESPONSE"),
    DOCUMENT_ALREADY_EXISTS("DOCUMENT {0} ALREADY EXISTS"),
    EMAIL_ALREADY_EXISTS("EMAIL {0} ALREADY EXISTS"),

    ////////////////////////////////////////////////////////////////////////////////////////////
    // TEACHERS EXCEPTION MESSAGE
    ////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////
    // STUDENT EXCEPTION MESSAGE
    ////////////////////////////////////////////////////////////////////////////////////////////

    THE_STUDENT_NAME_CANNOT_BE_EMPTY_OR_BE_NULL("EL NOMBRE DEL ALUMNO NO PUEDE ESTAR VACÍO O SER NULO"),
    THE_STUDENT_SURNAME_CANNOT_BE_EMPTY_OR_BE_NULL("EL APELLIDO DEL ALUMNO NO PUEDE ESTAR VACÍO O SER NULO"),
    THE_STUDENT_DATE_OF_BIRTH_CANNOT_BE_EMPTY_OR_BE_NULL("LA FECHA DE NACIMIENTO DEL ALUMNO NO PUEDE ESTAR VACÍO O SER NULO"),
    STUDENT_NOT_FOUND("ALUMNO NO ENCONTRADO"),
    ERROR_WHEN_DISPLAYING_A_LIST_OF_ALL_STUDENTS("ERROR AL MOSTAR UNA LISTA DE TODOS LOS ALUMNOS"),
    ERROR_WHEN_DISPLAYING_ACTIVE_STUDENTS("ERROR AL MOSTAR UNA LISTA DE ALUMNOS ACTIVOS"),
    ERROR_WHEN_DISPLAYING_INACTIVE_STUDENTS("ERROR AL MOSTAR UNA LISTA DE ALUMNOS INACTIVOS"),
    THERE_IS_NO_STUDENT_BY_THAT_NAME("NO EXISTE UN ALUMNO CON ESE NOMBRE"),

    ////////////////////////////////////////////////////////////////////////////////////////////
    // COURSE EXCEPTION MESSAGE
    ////////////////////////////////////////////////////////////////////////////////////////////
    COURSE_NOT_FOUND("CURSO NO ENCONTRADO"),

    ERROR_WHEN_DISPLAYING_THE_LIST_OF_AVAILABLE_MONTHLY_FEES("ERROR WHEN DISPLAYING THE LIST OF AVAILABLE MONTHLY FEES"),
    ERROR_WHEN_DISPLAYING_THE_LIST_OF_AVAILABLE_DAILY_FEES("ERROR WHEN DISPLAYING THE LIST OF AVAILABLE DAILY FEES"),
    DAILY_FEE_NOT_FOUND("DAILY FEE NOT FOUND"),
    THE_MONTHLY_FEE_LIST_COULD_NOT_BE_REMOVED("THE MONTHLY FEE LIST COULD NOT BE REMOVED"),
    THE_MONTHLY_FEE_LIST_COULD_NOT_BE_RESTARTED("THE MONTHLY FEE LIST COULD NOT BE RESTARTED"),
    THE_DAILY_FEE_LIST_COULD_NOT_BE_REMOVED("THE DAILY FEE LIST COULD NOT BE REMOVED"),
    THE_DAILY_FEE_LIST_COULD_NOT_BE_RESTARTED("THE DAILY FEE LIST COULD NOT BE RESTARTED"),

    ////////////////////////////////////////////////////////////////////////////////////////////
    // INTERNAL
    ////////////////////////////////////////////////////////////////////////////////////////////

    INTERNAL_NOT_FOUND("INTERNAL NOT FOUND"),
    ERROR_WHEN_DISPLAYING_ACTIVE_INTERNALS("ERROR WHEN DISPLAYING ACTIVE INTERNAL"),
    ERROR_WHEN_DISPLAYING_INACTIVE_INTERNALS("ERROR WHEN DISPLAYING INACTIVE INTERNAL"),
    ERROR_WHEN_DISPLAYING_THE_LIST_OF_AVAILABLE_INTERNALS("ERROR WHEN DISPLAYING THE LIST OF AVAILABLE INTERNAL"),
    PASSWORDS_DO_NOT_MATCH("PASSWORDS DO NOT MATCH"),
    WRONG_PASSWORD("WRONG PASSWORD"),
    THE_INTERNAL_ALREADY_EXISTS_IN_THAT_WORKSHOP("THE INTERNAL ALREADY EXISTS IN THAT WORKSHOP"),
    INADEQUATE_ROLE_OF_THE_INTERNAL("INADEQUATE ROLE OF THE INTERNAL"),
    THE_INTERNAL_COULD_NOT_BE_ENABLED("THE INTERNAL COULD NOT BE ENABLED"),
    THE_INTERNAL_COULD_NOT_BE_DISABLED("THE INTERNAL COULD NOT BE DISABLED");

    private final String messageString;

    EExceptionMessage(String messageString) {
        this.messageString = messageString;
    }

    public String getMessage() {
        return messageString;
    }

    public String getMessage(String stringObject) {
        return MessageFormat.format(messageString, stringObject);
    }

    @Override
    public String toString() {
        return messageString;
    }

    public static EExceptionMessage typeOrValue(String value) {
        for (EExceptionMessage type : EExceptionMessage.class.getEnumConstants()) {
            if (type.toString().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("ENUM MESSAGE NOT FOUND");
    }
}