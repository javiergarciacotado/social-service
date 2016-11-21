package co.tide.exercise.configuration;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class PropertiesReaderIT {

    private static final String UNKNOWN_PROPERTY = "some.property";
    private static final String VALID_PROPERTY = "social.service.property";
    private static final String PROPERTIES_FILE = "/application.properties";

    @Test(expected = NullPointerException.class)
    public void givenNullFileNameThrowNullPointerException() throws IOException {
        PropertiesReader propertiesReader = new PropertiesReader(null);
        assertNull(propertiesReader);
        fail("Shoul have thrown exception");
    }

    @Test
    public void givenAEmptyFileNameReturnEmptyPropertyValue() throws IOException {
        PropertiesReader propertiesReader = new PropertiesReader("");
        assertEquals("", propertiesReader.get(UNKNOWN_PROPERTY));
    }

    @Test
    public void givenAValidFileNameAndUnknownPropertyNameReturnEmpty() throws IOException {
        PropertiesReader propertiesReader = new PropertiesReader(PROPERTIES_FILE);
        assertEquals("", propertiesReader.get(UNKNOWN_PROPERTY));
    }

    @Test
    public void givenAValidFileNameAndKnownPropertyReturnValue() throws IOException {
        PropertiesReader propertiesReader = new PropertiesReader(PROPERTIES_FILE);
        assertEquals("test", propertiesReader.get(VALID_PROPERTY));
    }


}
