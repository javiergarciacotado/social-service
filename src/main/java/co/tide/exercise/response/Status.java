package co.tide.exercise.response;


public enum Status {

    OK(200, "OK"),
    CREATED(201, "Created"),
    BAD_REQUEST(400, "Bad request"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String reason;

    Status(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }


}
