package view;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

final class AudioPlayer {
    private static final float MUSIC_VOLUME_DB = 8f;

    private final Clip bump;
    private final Clip music;

    // ------------------------------------------------------------------
    // Construction
    // ------------------------------------------------------------------

    AudioPlayer() {
        this.bump = load("/audio/bump.wav");
        this.music = load("/audio/dp-league.wav");
        setVolume(this.music, MUSIC_VOLUME_DB);
    }

    // ------------------------------------------------------------------
    // Playback
    // ------------------------------------------------------------------

    // --- player thread calls this; restarts the clip, silent without a sound device
    void playBump() {
        if (this.bump == null) {
            return;
        }
        this.bump.stop();
        this.bump.setFramePosition(0);
        this.bump.start();
    }

    void startMusic() {
        if (this.music == null) {
            return;
        }
        this.music.setFramePosition(0);
        this.music.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // ------------------------------------------------------------------
    // Internals
    // ------------------------------------------------------------------

    // --- returns null if there is no sound device
    private static Clip load(String resource) {
        URL url = AudioPlayer.class.getResource(resource);
        if (url == null) {
            throw new IllegalArgumentException("Sound not found: " + resource);
        }
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
            return clip;
        } catch (LineUnavailableException e) {
            System.err.println("No audio device, sound disabled: " + e.getMessage());
            return null;
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new IllegalStateException("Cannot load " + resource, e);
        }
    }

    private static void setVolume(Clip clip, float decibels) {
        if (clip == null || !clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            return;
        }
        FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gain.setValue(Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), decibels)));
    }
}
