package com.example.occupath

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.occupath.databinding.FragmentFeedBinding
import com.google.firebase.firestore.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentFeedBinding
    private lateinit var feedArrayList : ArrayList<Post>
    private lateinit var feedAdapter : new_adapter
    //variable to use the firebase data feature
    private lateinit var database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        //enable options menu in this fragment
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)

        feedArrayList = arrayListOf()
        feedAdapter = new_adapter(feedArrayList)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        get_information()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_file, menu);

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when(item.itemId) {
            R.id.new_post -> {
                this.startActivity(Intent(requireContext(),PhotoSharingActivity::class.java))
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        binding = FragmentFeedBinding.inflate(layoutInflater,container,false)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = feedAdapter
        binding.recyclerView.setHasFixedSize(true)
        return binding.root
    }

    private fun get_information() {
        database = FirebaseFirestore.getInstance()

        database.collection("Post").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { snaphot, exception ->
            if (snaphot != null) {
                if(!snaphot.isEmpty) {
                    val documents = snaphot.documents
                    feedArrayList.clear()
                    for (document in documents) {
                        val comment = document.get("comment") as String
                        val image = document.get("imageUrl") as String
                        val name = document.get("name") as String
                        feedArrayList.add(Post(comment, image, name))
                    }
                }
                feedAdapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FeedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FeedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}