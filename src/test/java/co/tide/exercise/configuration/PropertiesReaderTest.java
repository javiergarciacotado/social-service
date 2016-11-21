package co.tide.exercise.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class PropertiesReaderTest {

    private static final String UNKNOWN_PROPERTY = "some.property";
    private static final String VALID_PROPERTY = "social.service.property";

    @Mock private PropertiesReader propertiesReader;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenAValidFileNameAndUnknownPropertyNameReturnEmpty() {
        when(propertiesReader.get(UNKNOWN_PROPERTY)).thenReturn("");
        assertEquals("", propertiesReader.get(UNKNOWN_PROPERTY));
    }

    @Test
    public void givenAValidFileNameAndKnownPropertyReturnValue() {
        when(propertiesReader.get(VALID_PROPERTY)).thenReturn("some value");
        assertEquals("some value", propertiesReader.get(VALID_PROPERTY));
    }

}