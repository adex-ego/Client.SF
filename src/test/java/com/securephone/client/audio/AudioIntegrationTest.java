package com.securephone.client.audio;

import java.util.Scanner;

/**
 * Interactive integration test for audio capture and playback.
 *
 * This test requires: - Audio input device (microphone) - Audio output device
 * (speakers/headphones)
 *
 * Run with: java -cp . com.securephone.client.audio.AudioIntegrationTest
 */

public class AudioIntegrationTest {

    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("   AUDIO INTEGRATION TEST");
        System.out.println("=====================================\n");

        try {
            testAudioBuffer();
            testAudioCaptureAndPlayback();
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n✓ All tests completed.");
    }

    /**
     * Test 1: Basic AudioBuffer push/poll
     */
    private static void testAudioBuffer() {
        System.out.println("TEST 1: AudioBuffer push/poll");
        System.out.println("------------------------------");
        
        AudioBuffer buffer = new AudioBuffer(5);
        
        // Push 3 frames
        byte[] frame1 = new byte[]{1, 2, 3};
        byte[] frame2 = new byte[]{4, 5, 6};
        byte[] frame3 = new byte[]{7, 8, 9};
        
        buffer.push(frame1);
        buffer.push(frame2);
        buffer.push(frame3);
        
        System.out.println("Pushed 3 frames. Buffer size: " + buffer.size());
        
        // Poll frames
        byte[] polled = buffer.poll();
        System.out.println("Polled frame: [" + arrayToString(polled) + "]. Buffer size: " + buffer.size());
        
        buffer.clear();
        System.out.println("Cleared buffer. Size: " + buffer.size());
        System.out.println("✓ AudioBuffer test passed.\n");
    }

    /**
     * Test 2: Audio capture and playback
     * 
     * User scenario:
     * 1. Press ENTER to start recording (5 seconds)
     * 2. Speak into microphone
     * 3. Program immediately plays back what you said
     */
    private static void testAudioCaptureAndPlayback() throws Exception {
        System.out.println("TEST 2: Audio Capture and Playback");
        System.out.println("-----------------------------------");
        
        // Create buffer for audio data
        AudioBuffer buffer = new AudioBuffer(100);
        
        // Create capture and player
        AudioCapture capture = new AudioCapture(buffer);
        AudioPlayer player = new AudioPlayer(buffer);
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\nMicrophone test:");
        System.out.println("1. Press ENTER to start recording");
        System.out.println("2. Speak clearly into your microphone");
        System.out.println("3. Wait 5 seconds, then recording stops automatically");
        System.out.print("\nPress ENTER to begin: ");
        scanner.nextLine();
        
        try {
            System.out.println("\n🎤 RECORDING... (5 seconds)");
            capture.start();
            
            // Record for 5 seconds
            Thread.sleep(5000);
            
            System.out.println("⏹️  Recording stopped.");
            capture.stop();
            
            System.out.println("Buffer contains " + buffer.size() + " frames.");
            
            // Now play back
            System.out.println("\n🔊 PLAYBACK...");
            player.start();
            
            // Play until buffer is empty
            int lastSize = buffer.size();
            int noChangeCount = 0;
            
            while (buffer.size() > 0 && noChangeCount < 5) {
                Thread.sleep(200);
                int currentSize = buffer.size();
                if (currentSize == lastSize) {
                    noChangeCount++;
                } else {
                    noChangeCount = 0;
                }
                lastSize = currentSize;
            }
            
            Thread.sleep(500); // Final drain
            player.stop();
            
            System.out.println("✓ Playback completed.");
            
        } finally {
            capture.stop();
            player.stop();
        }
        
        System.out.println("\n✓ Audio capture/playback test passed.\n");
    }

    private static String arrayToString(byte[] arr) {
        if (arr == null) return "null";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(arr.length, 10); i++) {
            if (i > 0) sb.append(", ");
            sb.append(arr[i]);
        }
        if (arr.length > 10) sb.append("...");
        return sb.toString();
    }
}
