package com.securephone.client.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for UserSession model.
 */

public class UserSessionTest {
    private UserSession session;
    private AudioDevice device;

    @Before
    public void setUp() {
        session = new UserSession();
        device = new AudioDevice("mic-1", "Default Mic", 48000f, 1);
    }

    @Test
    public void testDefaultConstructor() {
        UserSession empty = new UserSession();
        assertNull("Default constructor should have null userId", empty.getUserId());
        assertNull("Default constructor should have null username", empty.getUsername());
        assertFalse("Default constructor should not be connected", empty.isConnected());
    }

    @Test
    public void testParameterizedConstructor() {
        UserSession user = new UserSession("user-123", "alice");
        assertEquals("User ID should be set", "user-123", user.getUserId());
        assertEquals("Username should be set", "alice", user.getUsername());
        assertFalse("Initially not connected", user.isConnected());
    }

    @Test
    public void testSetUserId() {
        session.setUserId("user-456");
        assertEquals("User ID should be updated", "user-456", session.getUserId());
    }

    @Test
    public void testSetUsername() {
        session.setUsername("bob");
        assertEquals("Username should be updated", "bob", session.getUsername());
    }

    @Test
    public void testSetConnected() {
        session.setConnected(true);
        assertTrue("Connected should be true", session.isConnected());
        
        session.setConnected(false);
        assertFalse("Connected should be false", session.isConnected());
    }

    @Test
    public void testSetAudioDevice() {
        session.setAudioDevice(device);
        assertNotNull("Audio device should be set", session.getAudioDevice());
        assertEquals("Audio device ID should match", "mic-1", session.getAudioDevice().getId());
    }

    @Test
    public void testAudioDeviceNull() {
        assertNull("Default audio device should be null", session.getAudioDevice());
    }

    @Test
    public void testToString() {
        session.setUserId("user-789");
        session.setUsername("charlie");
        session.setConnected(true);
        session.setAudioDevice(device);
        
        String str = session.toString();
        assertTrue("toString should contain userId", str.contains("user-789"));
        assertTrue("toString should contain username", str.contains("charlie"));
        assertTrue("toString should contain connected status", str.contains("connected=true"));
    }

    @Test
    public void testToStringNoDevice() {
        session.setUserId("user-999");
        session.setUsername("dave");
        
        String str = session.toString();
        assertTrue("toString should indicate no device", str.contains("none"));
    }

    @Test
    public void testFullSessionLifecycle() {
        // Simulate user login
        session.setUserId("user-001");
        session.setUsername("eve");
        
        // Select audio device
        AudioDevice mic = new AudioDevice("builtin-mic", "Built-in Microphone", 48000f, 1);
        session.setAudioDevice(mic);
        
        // Connect
        session.setConnected(true);
        
        // Verify state
        assertEquals("user-001", session.getUserId());
        assertEquals("eve", session.getUsername());
        assertTrue(session.isConnected());
        assertEquals("builtin-mic", session.getAudioDevice().getId());
    }
}
