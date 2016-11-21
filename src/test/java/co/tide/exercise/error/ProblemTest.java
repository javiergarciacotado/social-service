package co.tide.exercise.error;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProblemTest {

    @Test
    public void givenAProblemShouldReturnCorrectValues() {
        Problem problem = new Problem(ProblemType.BAD_REQUEST, "Title test", "Some detail");
        assertEquals(ProblemType.BAD_REQUEST, problem.getProblemType());
        assertEquals("Title test", problem.getTitle());
        assertEquals("Some detail", problem.getDetail());
    }

}