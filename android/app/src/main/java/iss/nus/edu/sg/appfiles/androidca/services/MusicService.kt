package iss.nus.edu.sg.appfiles.androidca.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import iss.nus.edu.sg.appfiles.androidca.R

class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action

        when(action) {
            "pause" -> {
                mediaPlayer?.pause()
                return START_NOT_STICKY
            }
            "resume" -> {
                mediaPlayer?.start()
                return START_NOT_STICKY
            }
        }

        val musicType = intent?.getStringExtra("music") ?: "login"

        mediaPlayer?.stop()
        mediaPlayer?.release()

        val musicResource = when(musicType) {
            "login" -> R.raw.kahoot
            "play" -> R.raw.magnetic
            else -> R.raw.kahoot
        }

        mediaPlayer = MediaPlayer.create(this, musicResource)
        mediaPlayer?.isLooping = true
        mediaPlayer?.setOnErrorListener { _, _, _ ->
            mediaPlayer?.release()
            stopSelf()
            true
        }
        mediaPlayer?.start()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        super.onDestroy()
    }
}