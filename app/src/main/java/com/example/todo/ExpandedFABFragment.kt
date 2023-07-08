package com.example.todo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExpandedFABFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExpandedFABFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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

        val thisFragment = inflater.inflate(R.layout.fragment_expanded_f_a_b, container, false)
        val fabView = thisFragment.findViewById<SpeedDialView>(R.id.expandedFAB)

        // добавляем кнопку перехода на экран создания напоминания
        fabView.addActionItem(
            SpeedDialActionItem.Builder(R.id.schedule_fab_schedule_icon, R.drawable.calendar_icon)
                .create()
        )

        // добавляем кнопку перехода на экран создания заметки
        fabView.addActionItem(
            SpeedDialActionItem.Builder(R.id.schedule_fab_note_icon, R.drawable.note_icon)
                .create()
        )

        // обработчик клика на кнопки создания заметки и напоминания
        fabView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.schedule_fab_note_icon -> {
                    val intent = Intent(context, NoteActivity::class.java)
                    startActivity(intent)
                    fabView.close() // To close the Speed Dial with animation
                    return@OnActionSelectedListener true // false will close it without animation
                }
                R.id.schedule_fab_schedule_icon -> {
                    val intent = Intent(context, ScheduleActivity::class.java)
                    startActivity(intent)
                    fabView.close() // To close the Speed Dial with animation
                    return@OnActionSelectedListener true // false will close it without animation
                }
            }
            false
        })

        // Inflate the layout for this fragment
        return thisFragment
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExpandedFABFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExpandedFABFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}