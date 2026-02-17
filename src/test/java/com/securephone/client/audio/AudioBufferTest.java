package com.securephone.client.audio;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for AudioBuffer thread-safety and correctness.
 */
public class AudioBufferTest {

    private AudioBuffer buffer;

    @Before
    public void setUp() {
        buffer = new AudioBuffer(10);
    }

    @Test
    public void testPushAndPoll() {
        byte[] frame = {1, 2, 3, 4};
        assertTrue("Push should succeed", buffer.push(frame));

        byte[] polled = buffer.poll();
        assertNotNull("Polled frame should not be null", polled);
        assertArrayEquals("Frame should match", frame, polled);
    }

    @Test
    public void testPollEmpty() {
        byte[] polled = buffer.poll();
        assertNull("Poll on empty buffer should return null", polled);
    }

    @Test
    public void testCapacity() {
        for (int i = 0; i < 10; i++) {
            assertTrue("Push should succeed within capacity", buffer.push(new byte[]{(byte) i}));
        }
        assertFalse("Push should fail when full", buffer.push(new byte[]{11}));
    }

    @Test
    public void testSize() {
        assertEquals("Initial size should be 0", 0, buffer.size());

        buffer.push(new byte[]{1});
        assertEquals("Size should be 1 after push", 1, buffer.size());

        buffer.push(new byte[]{2});
        assertEquals("Size should be 2 after second push", 2, buffer.size());

        buffer.poll();
        assertEquals("Size should be 1 after poll", 1, buffer.size());
    }

    @Test
    public void testClear() {
        buffer.push(new byte[]{1});
        buffer.push(new byte[]{2});
        buffer.push(new byte[]{3});

        assertEquals("Size should be 3", 3, buffer.size());
        buffer.clear();
        assertEquals("Size should be 0 after clear", 0, buffer.size());
    }

    @Test
    public void testMultiThreadedPush() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                buffer.push(new byte[]{(byte) i});
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 5; i < 10; i++) {
                buffer.push(new byte[]{(byte) i});
                try {
                    Thread.sleep(15);
                } catch (InterruptedException ignored) {
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        assertEquals("Buffer should contain 10 frames from two threads", 10, buffer.size());
    }

    @Test
    public void testPushWithTimeout() throws InterruptedException {
        boolean pushed = buffer.push(new byte[]{1}, 100);
        assertTrue("Push with timeout should succeed", pushed);
    }

    @Test
    public void testPollWithTimeout() throws InterruptedException {
        buffer.push(new byte[]{1, 2, 3});
        byte[] polled = buffer.poll(100);
        assertNotNull("Poll with timeout should return frame", polled);
        assertArrayEquals("Frame should match", new byte[]{1, 2, 3}, polled);
    }
}
