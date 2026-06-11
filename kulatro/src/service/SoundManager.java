package service;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Manages game audio using the Singleton design pattern to ensure a single 
 * point of control for music and sound effects across all screens.
 * @author Muhammed Cagan Goktas
 */
public class SoundManager {
	private boolean soundOn = true;
	private boolean musicOn = true;
	private static SoundManager instance;
	
	private Clip bgmClip;
	private Clip clickClip;
	
	/**
     * Private constructor to prevent external instantiation, enforcing the Singleton pattern.
     * Loads audio files from the resources directory.
     */
    private SoundManager() {
    	try {
            File clickFile = new File("resources/sounds/click.wav");
            if (clickFile.exists()) {
                AudioInputStream clickIn = AudioSystem.getAudioInputStream(clickFile);
                clickClip = AudioSystem.getClip();
                clickClip.open(clickIn);
            }

            File bgmFile = new File("resources/sounds/background_music.wav");
            if (!bgmFile.exists()) {
            	System.out.println("G");
            }
            if (bgmFile.exists()) {
                AudioInputStream bgmIn = AudioSystem.getAudioInputStream(bgmFile);
                bgmClip = AudioSystem.getClip();
                bgmClip.open(bgmIn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the background music on a continuous loop if music is enabled.
     */
    public void startBackgroundMusic() {
        if (bgmClip != null && musicOn) {
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        }
    }

    /**
     * Immediately stops the background music if it is currently playing.
     */
    public void stopBackgroundMusic() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
        }
    }
    
    /**
     * Plays a short click sound effect once, resetting its position to the start if needed.
     */
	public void playClickSound() {
		if (!soundOn || clickClip == null) return;
	
		try {
			clickClip.setFramePosition(0);
			clickClip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
     * Returns the single instance of SoundManager, creating it if it does not yet exist.
     * @return the shared {@link SoundManager} instance
     */
	public static SoundManager getInstance() {
		if (instance == null) instance = new SoundManager();
		return instance;
	}
	
	/**
     * Enables or disables sound effects globally.
     * @param soundOn true to enable sounds, false to mute
     */
	public void setSoundOn(boolean soundOn) {
        this.soundOn = soundOn;
    }
}
