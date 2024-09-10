package de.uol.swp.common.passwordHashing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

 class PasswordHashingTest {

    @Test
     void testHashPassword() {
        String password = "testpassword";
        String hashedPassword = PasswordHashing.hashPassword(password);

        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
    }

    @Test
     void testCompareCredentials() {
        String password = "testpassword";
        String hashedPassword = PasswordHashing.hashPassword(password);

        assertTrue(PasswordHashing.compareCredentials(password, hashedPassword));
        assertFalse(PasswordHashing.compareCredentials("wrongpassword", hashedPassword));
    }
}