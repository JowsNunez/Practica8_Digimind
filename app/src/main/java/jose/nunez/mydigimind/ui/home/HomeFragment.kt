package jose.nunez.mydigimind.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jose.nunez.mydigimind.R
import jose.nunez.mydigimind.databinding.FragmentHomeBinding
import jose.nunez.mydigimind.ui.Task
import java.util.zip.Inflater

class HomeFragment : Fragment() {

    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth

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
            //fillTasks()
            first = false
        }
        adaptadorTareas = AdaptadorTareas(root.context, task)
        val gridView: GridView = root.findViewById(R.id.gridview)

        storage= FirebaseFirestore.getInstance()
        usuario=FirebaseAuth.getInstance()

        gridView.adapter = adaptadorTareas

        var txtDireccion:TextView=root.findViewById(R.id.etDireccion)
        var txtTelefono:TextView=root.findViewById(R.id.etTelefono)


        var txtEmail:TextView=root.findViewById(R.id.etCorreo)
        txtEmail.text = usuario.currentUser?.email.toString()
        var save:Button =root.findViewById(R.id.btnGuardar)
        save.setOnClickListener {

            storage.collection("usuarios").document(txtEmail.text.toString())
                .set(
                    hashMapOf("email" to txtEmail.text.toString(),
                    "direccion" to txtDireccion.text.toString(),
                    "telefono" to txtTelefono.text.toString())
                ).addOnSuccessListener {
                    Toast.makeText(root.context,"Se guardo con exito",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(root.context,"Fallo al guardar",Toast.LENGTH_SHORT).show()
                }
        }
        var delete:Button =root.findViewById(R.id.btnEliminar)

        delete.setOnClickListener {
            storage.collection("usuarios").document(txtEmail.text.toString()).delete().addOnSuccessListener {
                Toast.makeText(root.context,"Se guardo con exito",Toast.LENGTH_SHORT).show()

            }
        }

        var docRef=storage.collection("usuarios").document(txtEmail.text.toString())

        docRef.get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val document = task.result
                if(document!=null){
                        txtDireccion.setText(document.getString("direccion"))
                        txtTelefono.setText(document.getString("telefono"))
                    }else{
                        Toast.makeText(root.context,"No Se encontro",Toast.LENGTH_SHORT).show()

                    }

            }else{
                Toast.makeText(root.context,"Fallo Get",Toast.LENGTH_SHORT).show()

            }
        }


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