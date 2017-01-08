package com.project.HelpClasses;

import android.app.Activity;
import android.media.MediaPlayer;

/*
 * Simple media player class that allows us to
 * easily start, loop and stop an audio file
 */
public class MyMediaPlayer
        implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private MediaPlayer mp;
    private Activity callerActivity;
    private boolean shouldLoop;
    private int sound;

    public MyMediaPlayer(Activity activity, int sound, boolean shouldLoop) {
        this.callerActivity = activity;
        this.shouldLoop = shouldLoop;
        this.sound = sound;
    }

    public void start(boolean shouldLoop) {
        this.shouldLoop = shouldLoop;
        this.mp = MediaPlayer.create(callerActivity, sound);
        this.mp.setOnCompletionListener(this);
        this.mp.setOnPreparedListener(this);
    }

    public boolean isPlaying() {
        if (this.mp != null) {
            return this.mp.isPlaying();
        }
        return false;
    }

    public void stop() {
        this.shouldLoop = false;
        if (this.mp != null) {
            this.mp.stop();
        }
    }

    public void stopImmediately() {
        if (this.mp != null) {
            this.mp.stop();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (shouldLoop) {
            this.mp.start();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (!this.mp.isPlaying()) {
            mp.start();
        }
    }

    public void destroy() {
        if (this.mp != null) {
            this.mp.release();
        }
    }
}
