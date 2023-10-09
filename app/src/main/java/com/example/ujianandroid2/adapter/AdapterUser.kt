package com.example.ujianandroid2.adapter


import android.app.AlertDialog
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.ujianandroid2.R
import com.example.ujianandroid2.model.TrxpinjamanItem

class AdapterUser (var data : List<TrxpinjamanItem?>, private val clickListener: (TrxpinjamanItem) -> Unit,private val longCLickListener:(TrxpinjamanItem)->Unit) : RecyclerView.Adapter<AdapterUser.ViewHolder>() {

    // lateinit var data : List<TrxpinjamanItem?>
    fun setTodo(todo: List<TrxpinjamanItem?>?){
        if (todo != null) {
            data = todo
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = data.get(position)?.namaLengkap
        holder.txtAddress.text = data.get(position)?.alamat
        holder.txtOS.text = "Rp. "+data.get(position)?.jumlahOutstanding

        holder.btnEdit.setOnClickListener {
            clickListener(data.get(position)!!)
        }
        holder.btnDelete.setOnClickListener {
            val alertDialog = AlertDialog.Builder(holder.itemView.context)
                .setTitle("Hapus Data!")
                .setMessage("Apakah kamu ingin menghapus data?")
                .setPositiveButton("Ya"){dialog, which ->
                    longCLickListener(data.get(position)!!)
                    Log.e("INFO", "onFailure: ${data.get(position)!!}")
                }
                .setNegativeButton("Tidak",null)
                .create()
            alertDialog.show()
        }
//        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener{
//            override fun onLongClick(v: View?): Boolean {
//                val alertDialog = AlertDialog.Builder(holder.itemView.context)
//                    .setTitle("Hapus Data")
//                    .setMessage("Apakah anda yakin ingin menghapus data ini ?")
//                    .setPositiveButton("Ya"){dialog, which ->
//                        longCLickListener(data.get(position)!!)
//                        Log.e("INFO", "onFailure: ${data.get(position)!!}")
//                    }
//                    .setNegativeButton("Tidak",null)
//                    .create()
//                alertDialog.show()
//                return true
//            }
//        })
    }

    override fun getItemCount():Int = data.size
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtAddress = itemView.findViewById<TextView>(R.id.txtAddress)
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val txtOS = itemView.findViewById<TextView>(R.id.txtOS)
        val btnEdit = itemView.findViewById<ImageButton>(R.id.btnEdit)
        val btnDelete = itemView.findViewById<ImageButton>(R.id.btnDelete)
    }

}