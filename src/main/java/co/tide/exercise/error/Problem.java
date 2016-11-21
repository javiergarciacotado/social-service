package co.tide.exercise.error;

import java.io.Serializable;

/**
 * Class to describe problems during serialization/deserialization of requests
 */
public class Problem implements Serializable {

    private static final long serialVersionUID = 5827902773988067424L;

    private final String detail;

    private final ProblemType problemType;
    private final String title;

    public Problem(ProblemType problemType, String title, String detail) {
        this.problemType = problemType;
        this.title = title;
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public ProblemType getProblemType() {
        return problemType;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Problem problem = (Problem) o;

        return detail.equals(problem.detail) && problemType == problem.problemType && title.equals(problem.title);

    }

    @Override
    public int hashCode() {
        int result = detail.hashCode();
        result = 31 * result + problemType.hashCode();
        result = 31 * result + title.hashCode();
        return result;
    }
}
