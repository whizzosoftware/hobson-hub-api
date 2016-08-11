package com.whizzosoftware.hobson.api.property;

import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class TypedPropertyTest {
    @Test
    public void testEnumerationWithValidString() {
        TypedProperty tp = new TypedProperty.Builder("id", "name", "desc", TypedProperty.Type.STRING).enumeration(new String[] {"foo", "bar"}).build();
        assertEquals("id", tp.getId());
        assertEquals("name", tp.getName());
        assertEquals("desc", tp.getDescription());
        assertEquals(TypedProperty.Type.STRING, tp.getType());
        assertNotNull(tp.getEnumeration());
        assertEquals(2, tp.getEnumeration().length);
        assertEquals("foo", tp.getEnumeration()[0]);
        assertEquals("bar", tp.getEnumeration()[1]);
    }

    @Test
    public void testEnumerationWithInvalidString() {
        try {
            new TypedProperty.Builder("id", "name", "desc", TypedProperty.Type.STRING).enumeration(new Integer[]{1, 2}).build();
            fail("Should have thrown exception");
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testEnumerationWithValidInteger() {
        TypedProperty tp = new TypedProperty.Builder("id", "name", "desc", TypedProperty.Type.NUMBER).enumeration(new Integer[] {1,2}).build();
        assertNotNull(tp.getEnumeration());
        assertEquals(2, tp.getEnumeration().length);
        assertEquals(1, tp.getEnumeration()[0]);
        assertEquals(2, tp.getEnumeration()[1]);
    }

    @Test
    public void testEnumerationWithInvalidNumber() {
        try {
            new TypedProperty.Builder("id", "name", "desc", TypedProperty.Type.NUMBER).enumeration(new String[]{"foo", "bar"}).build();
            fail("Should have thrown exception");
        } catch (IllegalArgumentException ignored) {}
        try {
            new TypedProperty.Builder("id", "name", "desc", TypedProperty.Type.NUMBER).enumeration(new Double[]{1.0, 2.0}).build();
            fail("Should have thrown exception");
        } catch (IllegalArgumentException ignored) {}
    }
}
