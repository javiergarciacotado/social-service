package co.tide.exercise.response;

import java.util.*;

public class ResponseHeaders {

    private final List<ResponseHeader> headers;

    public ResponseHeaders() {
        this.headers = new ArrayList<>();
    }

    public ResponseHeaders(ResponseHeader responseHeader) {
        this(Collections.singletonList(responseHeader));
    }

    private ResponseHeaders(List<ResponseHeader> responseHeaders) { this.headers = responseHeaders; }

    public List<ResponseHeader> getHeaders() {
        return headers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResponseHeaders that = (ResponseHeaders) o;

        return headers.equals(that.headers);

    }

    @Override
    public int hashCode() {
        return headers.hashCode();
    }
}
