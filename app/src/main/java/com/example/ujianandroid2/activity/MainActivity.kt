package com.example.ujianandroid2.activity

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.ujianandroid2.R
import com.example.ujianandroid2.fragment.AddFragment
import com.example.ujianandroid2.fragment.HomeFragment
import com.example.ujianandroid2.model.TrxpinjamanItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.rootFragment, HomeFragment.newInstance("",""))
                .commit()
        }
    }


}