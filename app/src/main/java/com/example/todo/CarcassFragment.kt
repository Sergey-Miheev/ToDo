package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CarcassFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CarcassFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var switchButton: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_carcass, container, false)
        switchButton = view.findViewById(R.id.switchButton)

        initSwitchButton()

        // Inflate the layout for this fragment
        return view
    }

    private fun initSwitchButton() {
        var isNotesActivity: Boolean? = activity?.intent?.extras?.getBoolean("isNotes")
        if (isNotesActivity == null) {
            isNotesActivity = true
        }
        if (isNotesActivity == false) {
            switchButton.isChecked = false
        }
        switchButton.setOnCheckedChangeListener { _, _: Boolean ->
            if (isNotesActivity) {
                val intent = Intent(context, ScheduleListActivity::class.java)
                intent.putExtra("isNotes", false)
                startActivity(intent)
            } else {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("isNotes", true)
                startActivity(intent)
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
         * @return A new instance of fragment CarcassFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CarcassFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}