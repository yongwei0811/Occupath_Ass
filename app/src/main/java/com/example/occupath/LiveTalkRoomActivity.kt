package com.example.occupath

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.occupath.databinding.ActivityCreateLiveTalkBinding
import com.example.occupath.databinding.ActivityLiveTalkRoomBinding
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL
import android.content.Intent
import org.jitsi.meet.sdk.JitsiMeetView


class LiveTalkRoomActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLiveTalkRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveTalkRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var talkTitle = intent.getStringExtra("talkTitle")
        var username = intent.getStringExtra("username")

        var roomName = talkTitle + " By " + username

        val url = URL("https://meet.jit.si")

        val options = JitsiMeetConferenceOptions.Builder()
            .setServerURL(url)
            .setRoom(roomName)
            .setAudioMuted(true)
            .setVideoMuted(true)
            .setAudioOnly(false)
            .setWelcomePageEnabled(false)
            .setConfigOverride("requireDisplayName", false)
            .setFeatureFlag("tile-view.enabled",false)
            .build()

        JitsiMeetActivity.launch(this, options)
    }

}