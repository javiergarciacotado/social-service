package co.tide.exercise.response;

public class Response {

    private final Status status;
    private final ResponseHeaders responseHeaders;
    private final Body body;

    public Response(Status status, ResponseHeaders responseHeaders, Body body) {
        this.status = status;
        this.responseHeaders = responseHeaders;
        this.body = body;

    }

    public Status getStatus() {
        return status;
    }

    public ResponseHeaders getResponseHeaders() {
        return responseHeaders;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Response response = (Response) o;

        return status == response.status && responseHeaders.equals(response.responseHeaders) && body.equals(response.body);

    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + responseHeaders.hashCode();
        result = 31 * result + body.hashCode();
        return result;
    }
}
