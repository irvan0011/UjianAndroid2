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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ujianandroid2.R
import com.example.ujianandroid2.adapter.AdapterUser
import com.example.ujianandroid2.apiservices.APIConfig
import com.example.ujianandroid2.model.ResponseGetAllData
import com.example.ujianandroid2.model.ResponseSuccess
import com.example.ujianandroid2.model.TrxpinjamanItem
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var recyclerView: RecyclerView
    lateinit var adapterUser : AdapterUser
    lateinit var progressBar: ProgressBar
    lateinit var value: TextView
    lateinit var btnAdd:Button
    lateinit var btnSearch : TextView
    lateinit var txtSearch : EditText
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rview)
        progressBar=view.findViewById((R.id.progressBar))
        txtSearch=view.findViewById(R.id.txtSearch)
        btnSearch=view.findViewById(R.id.btnSearch)
        btnSearch.setOnClickListener(View.OnClickListener {
            search()
        })
        showProgresBar(true)
        getAllData()
        btnAdd=view.findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener(View.OnClickListener {
            parentFragmentManager.beginTransaction()
                .addToBackStack("add form")
                .replace(R.id.rootFragment, AddFragment.newInstance("add",null))
                .commit()
        })
//        permissions()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
//    fun permissions(){
//        if (EasyPermissions.hasPermissions(
//                requireContext(),
//                android.Manifest.permission.INTERNET)){
////            kode untuk menjalankan sebuah aktifitas (contoh CAMERA)
////            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
////            startActivityForResult(intent, 1)
//        }
//        else {
//            // Ask for one permission
//            EasyPermissions.requestPermissions(this, "Need permission for Internet",
//                0, android.Manifest.permission.INTERNET)
//        }
//    }
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

    fun getAllData(){
        val client = APIConfig.getApiService().getAllData()
        showProgresBar(true)
        client.enqueue(object : Callback<ResponseGetAllData> {
            override fun onResponse(
                call: Call<ResponseGetAllData>,
                response: Response<ResponseGetAllData>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    showProgresBar(false)
                       Log.e("INFO", "onSuccess: ${responseBody.data?.trxpinjaman}")
                    adapterUser = AdapterUser(responseBody.data?.trxpinjaman!!,{ user ->
                        parentFragmentManager.beginTransaction()
                            .addToBackStack("add form")
                            .replace(R.id.rootFragment, AddFragment.newInstance("update",user))
                            .commit()

                    } , { user ->
                        deleteDataUser(user)

                    })
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = adapterUser
                }
            }

            override fun onFailure(call: Call<ResponseGetAllData>, t: Throwable) {
                showProgresBar(false)
                Log.e("INFO", "onFailure: ${t.message.toString()}")
            }
        })
    }
    fun deleteDataUser(data : TrxpinjamanItem){
        val client = APIConfig.getApiService()
            .deleteDataUser(toRequestBody(data.id.toString()))
        showProgresBar(true)
        client.enqueue(object : Callback<ResponseSuccess> {
            override fun onResponse(
                call: Call<ResponseSuccess>,
                response: Response<ResponseSuccess>
            ) {
                Toast.makeText(context, data.id.toString(), Toast.LENGTH_SHORT).show()

                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    showProgresBar(false)
                    Log.e("INFO", "onSuccess: ${responseBody.message}")
                    getAllData()
                }
            }
            override fun onFailure(call: Call<ResponseSuccess>, t: Throwable) {
                showProgresBar(false)
                Log.e("INFO", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun search(){
        val client = APIConfig.getApiService().getAllDataByFilter("NamaLengkap","Like",txtSearch.text.toString(),"ASC")
        showProgresBar(true)
        client.enqueue(object : Callback<ResponseGetAllData> {
            override fun onResponse(
                call: Call<ResponseGetAllData>,
                response: Response<ResponseGetAllData>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    showProgresBar(false)
                    //   Log.e("INFO", "onSuccess: ${responseBody.data?.todolist}")
                    adapterUser = AdapterUser(responseBody.data?.trxpinjaman!!,{ item ->
                        parentFragmentManager.beginTransaction()
                            .addToBackStack("add form")
                            .replace(R.id.rootFragment, AddFragment.newInstance("update",item))
                            .commit()

                    } , { item ->
                        deleteDataUser(item)

                    })
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = adapterUser
                }
            }

            override fun onFailure(call: Call<ResponseGetAllData>, t: Throwable) {
                showProgresBar(false)
                Log.e("INFO", "onFailure: ${t.message.toString()}")
            }
        })
//http://localhost/cicool/api/todolist/all?filters[0][co][2][fl]=tugas&filters[0][co][2][op]=equal&filters[0][co][2][vl]=equal&sort_order=ASC
    }
}