package com.securephone.client.audio;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for OpusCodec encode/decode.
 */

public class OpusCodecTest {
    private OpusCodec codec;

    @Before
    public void setUp() {
        codec = new OpusCodec();
    }

    @Test
    public void testEncodeNotNull() {
        byte[] pcm = new byte[]{1, 2, 3, 4, 5};
        byte[] encoded = codec.encode(pcm);
        assertNotNull("Encoded result should not be null", encoded);
    }

    @Test
    public void testDecodeNotNull() {
        byte[] compressed = new byte[]{10, 20, 30};
        byte[] decoded = codec.decode(compressed);
        assertNotNull("Decoded result should not be null", decoded);
    }

    @Test
    public void testEncodeEmptyArray() {
        byte[] pcm = new byte[]{};
        byte[] encoded = codec.encode(pcm);
        assertArrayEquals("Empty input should return empty output", new byte[]{}, encoded);
    }

    @Test
    public void testDecodeEmptyArray() {
        byte[] compressed = new byte[]{};
        byte[] decoded = codec.decode(compressed);
        assertArrayEquals("Empty input should return empty output", new byte[]{}, decoded);
    }

    @Test
    public void testEncodeNull() {
        byte[] encoded = codec.encode(null);
        assertArrayEquals("Null input should return empty byte array", new byte[]{}, encoded);
    }

    @Test
    public void testDecodeNull() {
        byte[] decoded = codec.decode(null);
        assertArrayEquals("Null input should return empty byte array", new byte[]{}, decoded);
    }

    @Test
    public void testEncodePreservesLength() {
        byte[] pcm = new byte[960]; // 20ms @ 48kHz 16-bit mono
        byte[] encoded = codec.encode(pcm);
        assertEquals("Current stub preserves length", 960, encoded.length);
    }

    @Test
    public void testDecodePreservesLength() {
        byte[] compressed = new byte[480]; // Example compressed size
        byte[] decoded = codec.decode(compressed);
        assertEquals("Current stub preserves length", 480, decoded.length);
    }

    @Test
    public void testEncodeDecode() {
        byte[] original = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        byte[] encoded = codec.encode(original);
        byte[] decoded = codec.decode(encoded);
        assertArrayEquals("Round-trip encode/decode should preserve data (stub)", original, decoded);
    }
}
