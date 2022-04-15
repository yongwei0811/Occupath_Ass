package com.example.occupath

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.occupath.databinding.FragmentLiveTalkRoomBinding
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL

class LiveTalkRoomFragment : Fragment() {

    private lateinit var binding : FragmentLiveTalkRoomBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLiveTalkRoomBinding.inflate(layoutInflater,container,false)

        // get live talk details from previous fragment
       // val args = LiveTalkRoomFragmentArgs.fromBundle(requireArguments())

//        binding.textHostName.text = args.liveTalk.name.toString()
//        binding.textTalkTitle.text = args.liveTalk.talkTitle.toString()
//        binding.textTalkDesc.text = args.liveTalk.talkDesc.toString()
//
//        val topic = args.liveTalk.topic.toString()

        // go back to topic page
        binding.btnBack.setOnClickListener(){
        //    it.findNavController().navigate(LiveTalkRoomFragmentDirections.actionLiveTalkRoomFragmentToLiveTalkTopicFragment(topic))
        }

        binding.btnJoin.setOnClickListener(){
            val name = binding.textHostName.text.toString()
            val talkTitle = binding.textTalkTitle.text.toString()

            joinLiveTalk(talkTitle, name)
        }

        return binding.root
    }

    // join live talk
    fun joinLiveTalk(talkTitle: String, name : String) {

        val roomName = "$talkTitle by $name"

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

        JitsiMeetActivity.launch(activity, options)
    }

}