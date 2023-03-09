package jose.nunez.mydigimind.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import jose.nunez.mydigimind.R
import jose.nunez.mydigimind.databinding.FragmentHomeBinding
import jose.nunez.mydigimind.ui.Task
import java.util.zip.Inflater

class HomeFragment : Fragment() {

    private var adaptadorTareas: AdaptadorTareas? = null
    private var _binding: FragmentHomeBinding? = null

    companion object {

        var task = ArrayList<Task>()
        var first = true
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        if (first) {
            fillTasks()
            first = false
        }
        adaptadorTareas = AdaptadorTareas(root.context, task)
        val gridView: GridView = root.findViewById(R.id.gridview)


        gridView.adapter = adaptadorTareas


        return root
    }


    fun fillTasks() {
        task.add(Task("Practice 1", arrayListOf("Monday", "Sunday"), "17:30"))
    }


    private class AdaptadorTareas : BaseAdapter {
        var tasks = ArrayList<Task>()
        var contexto: Context? = null

        constructor(contexto: Context, tasks: ArrayList<Task>) {
            this.contexto = contexto
            this.tasks = tasks

        }

        override fun getCount(): Int {
            return this.tasks.size
        }

        override fun getItem(position: Int): Any {
            return this.tasks[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var task = tasks[position]
            var inflador = LayoutInflater.from(contexto)

            var vista = inflador.inflate(R.layout.task_view, null)


            var tv_title: TextView = vista.findViewById(R.id.tv_title)
            var tv_time: TextView = vista.findViewById(R.id.tv_time)
            var tv_days: TextView = vista.findViewById(R.id.tv_days)


            tv_title.text = task.title
            tv_time.text = task.time
            tv_days.text = task.days.toString()


            return vista


        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}