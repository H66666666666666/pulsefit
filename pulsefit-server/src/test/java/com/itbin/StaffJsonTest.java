package com.itbin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itbin.pojo.Staff;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies Staff JSON serialization omits password and deserialization accepts it.
 */
class StaffJsonTest {

    private final ObjectMapper mapper = new ObjectMapper();
    // Enable Java 8 time module support for LocalDate/LocalDateTime fields
    {
        mapper.findAndRegisterModules();
    }

    @Test
    void passwordOmittedOnSerialization() throws Exception {
        Staff staff = new Staff();
        staff.setId(1);
        staff.setUsername("coach1");
        staff.setPassword("secret123");
        staff.setName("张三");

        String json = mapper.writeValueAsString(staff);

        assertFalse(json.contains("password"), "Serialized JSON must not contain 'password' key");
        assertFalse(json.contains("secret123"), "Serialized JSON must not leak password value");
        assertTrue(json.contains("coach1"), "Serialized JSON should contain non-sensitive fields");
        assertTrue(json.contains("张三"), "Serialized JSON should contain non-sensitive fields");
    }

    @Test
    void passwordAcceptedOnDeserialization() throws Exception {
        String json = "{\"username\":\"coach1\",\"password\":\"secret123\",\"name\":\"张三\"}";

        Staff staff = mapper.readValue(json, Staff.class);

        assertEquals("coach1", staff.getUsername());
        assertEquals("secret123", staff.getPassword(), "Deserialized Staff must have password set");
        assertEquals("张三", staff.getName());
    }

    @Test
    void toStringExcludesPassword() {
        Staff staff = new Staff();
        staff.setId(1);
        staff.setUsername("coach1");
        staff.setPassword("secret123");
        staff.setName("张三");

        String str = staff.toString();

        assertFalse(str.contains("secret123"), "toString() must not contain password value");
        assertFalse(str.contains("password"), "toString() must not contain password field");
        assertTrue(str.contains("coach1"), "toString() should contain non-sensitive fields");
    }
}
