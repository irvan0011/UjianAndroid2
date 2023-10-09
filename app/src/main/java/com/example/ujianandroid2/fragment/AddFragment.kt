package com.example.ujianandroid2.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.ujianandroid2.R
import com.example.ujianandroid2.apiservices.APIConfig
import com.example.ujianandroid2.model.ResponseSuccess
import com.example.ujianandroid2.model.TrxpinjamanItem
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: TrxpinjamanItem? = null
    lateinit var addAddress:EditText
    lateinit var addName:EditText
    lateinit var addOS:EditText
    lateinit var txtTitle:TextView
    lateinit var btnAddData:Button
    lateinit var progressBar:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getParcelable(ARG_PARAM2,TrxpinjamanItem::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addAddress= view.findViewById(R.id.AddAddress)
        addName=view.findViewById(R.id.AddName)
        addOS=view.findViewById(R.id.AddOS)
        btnAddData=view.findViewById(R.id.btnAddData)
        txtTitle=view.findViewById(R.id.txtTitle)
        progressBar=view.findViewById(R.id.progressBar3)
        btnAddData.setOnClickListener(View.OnClickListener {
        })
        if (param1=="update"){
            txtTitle.text="Update Data"
            addAddress.setText(param2?.alamat )
            addName.setText(param2?.namaLengkap)
            addOS.setText(param2?.jumlahOutstanding)
            btnAddData.setOnClickListener(View.OnClickListener {
                updateData(TrxpinjamanItem(param2?.id,addName.text.toString(),addAddress.text.toString(),addOS.text.toString()))
            })
        }else if(param1=="add"){
            txtTitle.text="Tambah Data"
            btnAddData.setOnClickListener(View.OnClickListener {
                addData(TrxpinjamanItem(
                    null,
                    addName.text.toString(),
                    addAddress.text.toString(),
                    addOS.text.toString()))
            })
        }
        showProgresBar(false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: TrxpinjamanItem?) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putParcelable(ARG_PARAM2, param2)
                }
            }
    }


    fun toRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }
    fun showProgresBar(boolean: Boolean){
        if (boolean){
            progressBar.visibility = View.VISIBLE
            progressBar.animate()
        }else{
            progressBar.visibility = View.GONE
        }
    }
    fun addData(data : TrxpinjamanItem){
        val client = APIConfig.getApiService()
            .addDataUser(
                toRequestBody(data.namaLengkap.toString()),
                toRequestBody(data.alamat.toString()),
                toRequestBody(data.jumlahOutstanding.toString()))
        progressBar.visibility=View.VISIBLE
        Toast.makeText(context, data.namaLengkap.toString(), Toast.LENGTH_SHORT).show()
        client.enqueue(object : Callback<ResponseSuccess> {
            override fun onResponse(
                call: Call<ResponseSuccess>,
                response: Response<ResponseSuccess>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    showProgresBar(false)
                    Log.e("INFO", "onSuccess: ${responseBody.message}")
                    parentFragmentManager.popBackStackImmediate()
                }
            }
            override fun onFailure(call: Call<ResponseSuccess>, t: Throwable) {
                showProgresBar(false)
                Log.e("INFO", "onFailure: ${t.message.toString()}")
            }
        })


    }
    fun addDataPinjaman (data : TrxpinjamanItem){


        val client = APIConfig.getApiService()
            .addDataPinjaman(toRequestBody(data.namaLengkap.toString()),
                toRequestBody(data.alamat.toString()),
                toRequestBody(data.jumlahOutstanding.toString())
            )

        client.enqueue(object : Callback<ResponseSuccess> {
            override fun onResponse(
                call: Call<ResponseSuccess>,
                response: Response<ResponseSuccess>
            ) {

                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Log.e("INFO", "onSuccess: ${responseBody.message}")
                    parentFragmentManager.popBackStackImmediate()
                }
            }

            override fun onFailure(call: Call<ResponseSuccess>, t: Throwable) {
                Log.e("INFO", "onFailure: ${t.message.toString()}")
            }
        })
    }
    fun updateData(data : TrxpinjamanItem){
        val client = APIConfig.getApiService()
            .updateDataUser(toRequestBody(data.id.toString()),toRequestBody(data.namaLengkap.toString()),
                toRequestBody(data.alamat.toString()),
                toRequestBody(data.jumlahOutstanding.toString())
                )
        progressBar.visibility=View.VISIBLE
        client.enqueue(object : Callback<ResponseSuccess> {
            override fun onResponse(
                call: Call<ResponseSuccess>,
                response: Response<ResponseSuccess>
            ) {

                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    showProgresBar(false)
                    Log.e("INFO", "onSuccess: ${responseBody.message}")
                    parentFragmentManager.popBackStackImmediate()
                }
            }

            override fun onFailure(call: Call<ResponseSuccess>, t: Throwable) {
                showProgresBar(false)
                Log.e("INFO", "onFailure: ${t.message.toString()}")
            }
        })


    }

}