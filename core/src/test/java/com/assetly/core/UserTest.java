package com.assetly.core;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserTest {
    private static final UUID ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final String EMAIL = "user@gmail.com";
    private static final String DISPLAY_NAME = "user";
    private static final Instant CREATED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void exposesTheValuesItWasCreatedWith() {
        User user = new User(ID, EMAIL, DISPLAY_NAME, CREATED_AT);

        assertAll(
            () -> assertEquals(ID, user.id()),
            () -> assertEquals(DISPLAY_NAME, user.displayName()),
            () -> assertEquals(EMAIL, user.email()),
            () -> assertEquals(CREATED_AT, user.createdAt())
        );
    }

    @Test
    void nullIdIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
            () -> new User(null, EMAIL, DISPLAY_NAME, CREATED_AT)
        );
        assertEquals("id cannot be null", e.getMessage());
    }

    @Test
    void nullEmailIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
            () -> new User(ID, null, DISPLAY_NAME, CREATED_AT)
        );
        assertEquals("email cannot be null", e.getMessage());
    }

    @Test
    void nullDisplayNameIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
            () -> new User(ID, EMAIL, null, CREATED_AT)
        );
        assertEquals("displayName cannot be null", e.getMessage());
    }

    @Test
    void nullCreatedAtIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
            () -> new User(ID, EMAIL, DISPLAY_NAME, null)
        );
        assertEquals("createdAt cannot be null", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t"})
    void blankDisplayNameIsNotAllowed(String displayName) {
        var e = assertThrows(IllegalArgumentException.class,
            () -> new User(ID, EMAIL, displayName, CREATED_AT)
        );
        assertEquals("displayName cannot be blank", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t"})
    void blankEmailIsNotAllowed(String email) {
        var e = assertThrows(IllegalArgumentException.class,
            () -> new User(ID, email, DISPLAY_NAME, CREATED_AT)
        );
        assertEquals("email cannot be blank", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"user", "@", "user@", "@gmail.com", "user@@gmail.com"})
    void malformedEmailIsNotAllowed(String email) {
        var e = assertThrows(IllegalArgumentException.class,
            () -> new User(ID, email, DISPLAY_NAME, CREATED_AT)
        );
        assertEquals("email must contain a single @ between a name and a domain", e.getMessage());
    }

    @Test
    void emailIsTrimmedAndLowercased() {
        User user = new User(ID, "  User@Gmail.com  ", DISPLAY_NAME, CREATED_AT);
        assertEquals("user@gmail.com", user.email());
    }

    @Test
    void usersWhoseEmailsDifferOnlyInCaseAndSpacesAreEqual() {
        User first = new User(ID, EMAIL, DISPLAY_NAME, CREATED_AT);
        User second = new User(ID, "  User@Gmail.com ", DISPLAY_NAME, CREATED_AT);
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void usersWithDifferentIdsAreNotEqual() {
        User first = new User(ID, EMAIL, DISPLAY_NAME, CREATED_AT);
        User second = new User(UUID.fromString("22222222-2222-2222-2222-222222222222"),
            EMAIL, DISPLAY_NAME, CREATED_AT);
        assertNotEquals(first, second);
    }
}
