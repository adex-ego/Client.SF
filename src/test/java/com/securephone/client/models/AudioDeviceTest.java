package com.securephone.client.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for AudioDevice model.
 */
public class AudioDeviceTest {

    private AudioDevice device;

    @Before
    public void setUp() {
        device = new AudioDevice("dev-001", "Microphone Logitech", 48000f, 1);
    }

    @Test
    public void testGetId() {
        assertEquals("ID should match constructor", "dev-001", device.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("Name should match constructor", "Microphone Logitech", device.getName());
    }

    @Test
    public void testGetSampleRate() {
        assertEquals("Sample rate should match constructor", 48000f, device.getSampleRate(), 0.001f);
    }

    @Test
    public void testGetChannels() {
        assertEquals("Channels should match constructor", 1, device.getChannels());
    }

    @Test
    public void testToString() {
        String str = device.toString();
        assertTrue("toString should contain ID", str.contains("dev-001"));
        assertTrue("toString should contain name", str.contains("Microphone Logitech"));
        assertTrue("toString should contain sample rate", str.contains("48000"));
    }

    @Test
    public void testAudioDeviceStereo() {
        AudioDevice stereo = new AudioDevice("speaker-002", "USB Speaker", 44100f, 2);
        assertEquals("Stereo device should have 2 channels", 2, stereo.getChannels());
        assertEquals("Sample rate should be 44100", 44100f, stereo.getSampleRate(), 0.001f);
    }

    @Test
    public void testAudioDeviceHighSampleRate() {
        AudioDevice highRate = new AudioDevice("dac-003", "Hi-Fi DAC", 192000f, 2);
        assertEquals("Hi-Fi device should have 192kHz", 192000f, highRate.getSampleRate(), 0.001f);
    }

    @Test
    public void testEqualsMethod() {
        AudioDevice same = new AudioDevice("dev-001", "Microphone Logitech", 48000f, 1);
        // Note: AudioDevice doesn't override equals(), so this tests object identity
        assertFalse("Different objects should not be equal (no equals override)", device.equals(same));
        assertTrue("Same object should be equal to itself", device.equals(device));
    }
}
