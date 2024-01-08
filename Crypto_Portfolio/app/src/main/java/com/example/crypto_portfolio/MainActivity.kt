package com.example.crypto_portfolio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputBinding
import android.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import com.example.crypto_portfolio.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private  lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val  navHostFormat = supportFragmentManager.findFragmentById(R.id.fragmentcontainerview)
        val navController = navHostFormat!!.findNavController()

        val popupMenu = PopupMenu(this,null)
        popupMenu.inflate(R.menu.bottom_nav_menu)
        binding.bottomBar.setupWithNavController(popupMenu.menu,navController)


    }
}