package co.tide.exercise.response;

public class Body {

    private final Object detail;

    public Body(Object detail) {
        this.detail = detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Body body = (Body) o;

        return detail.equals(body.detail);

    }

    @Override
    public int hashCode() {
        return detail.hashCode();
    }
}
