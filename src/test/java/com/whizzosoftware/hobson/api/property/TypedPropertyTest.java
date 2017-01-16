package com.whizzosoftware.hobson.api.property;

import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class TypedPropertyTest {
    @Test
    public void testEnumerationWithValidString() {
        TypedProperty tp = new TypedProperty.Builder("id", "name", "desc", TypedProperty.Type.STRING).enumerate("foo", "Foo").enumerate("bar", "Bar").build();
        assertEquals("id", tp.getId());
        assertEquals("name", tp.getName());
        assertEquals("desc", tp.getDescription());
        assertEquals(TypedProperty.Type.STRING, tp.getType());
        assertNotNull(tp.getEnumeration());
        assertEquals(2, tp.getEnumeration().size());
        assertEquals("Foo", tp.getEnumeration().get("foo"));
        assertEquals("Bar", tp.getEnumeration().get("bar"));
    }

    @Test
    public void testEnumerationWithInvalidString() {
        try {
            new TypedProperty.Builder("id", "name", "desc", TypedProperty.Type.DEVICE).enumerate("foo", "bar").build();
            fail("Should have thrown exception");
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testEnumerationWithValidInteger() {
        TypedProperty tp = new TypedProperty.Builder("id", "name", "desc", TypedProperty.Type.NUMBER).enumerate("10", "Ten").enumerate("20", "Twenty").build();
        assertNotNull(tp.getEnumeration());
        assertEquals(2, tp.getEnumeration().size());
        assertEquals("Ten", tp.getEnumeration().get("10"));
        assertEquals("Twenty", tp.getEnumeration().get("20"));
    }

    @Test
    public void testEnumerationWithInvalidNumber() {
        try {
            new TypedProperty.Builder("id", "name", "desc", TypedProperty.Type.NUMBER).enumerate("foo", "bar").build();
            fail("Should have thrown exception");
        } catch (IllegalArgumentException ignored) {}
    }
}
