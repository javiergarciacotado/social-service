package co.tide.exercise.error;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum ProblemType {

    INVALID_PARAM("invalid-param"),
    SQL_EXCEPTION("sql-exception"),
    BAD_REQUEST("bad-request");

    private final String code;

    ProblemType(String code) {
        this.code = code;
    }
}
