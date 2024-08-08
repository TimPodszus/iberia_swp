package de.uol.swp.common;

import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SerializationTestHelperTest {

    @Test
    void checkNonSerializable() {
        assertThrows(RuntimeException.class, () ->
                SerializationTestHelper.checkSerializableAndDeserializable(new NotSerializable(), NotSerializable.class));
    }

    @Test
    void checkSerializable() {
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable("Hallo", String.class));
    }

    private static class NotSerializable implements Serializable {
        private final Thread thread = new Thread();
    }


}
