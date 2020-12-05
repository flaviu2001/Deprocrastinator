package com.enigmatix.deprocrastinator.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.enigmatix.deprocrastinator.ExperienceManipulator
import com.enigmatix.deprocrastinator.R
import com.enigmatix.deprocrastinator.UserInfoManipulator
import com.enigmatix.deprocrastinator.database.DbXP
import com.enigmatix.deprocrastinator.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var currentXp: LiveData<DbXP>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_task, R.id.nav_about_me), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        currentXp = ExperienceManipulator.getCurrentXP(applicationContext)
        currentXp.observe(this){
            var xp = 0L
            if (it != null)
                xp = it.xp
            navView.getHeaderView(0).findViewById<TextView>(R.id.level).text = applicationContext.getString(
                R.string.level_format
            ).format(xp / 100)
            navView.getHeaderView(0).findViewById<ProgressBar>(R.id.progressBar).progress = xp.toInt()%100

        }
        UserInfoManipulator.nameLiveData.observe(this){
            if (it != null)
                navView.getHeaderView(0).findViewById<TextView>(R.id.name).text = it
        }
        if (UserInfoManipulator.getName(this) == null){
            val layout = layoutInflater.inflate(R.layout.popup, null)
            val alert = AlertDialog.Builder(this).setView(layout).show()
            layout.findViewById<Button>(R.id.apply).setOnClickListener {
                val name = layout.findViewById<EditText>(R.id.nameEdit).text.toString()
                if (name.isEmpty()) {
                    Toast.makeText(applicationContext, "hi", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                UserInfoManipulator.setName(this, name)
                alert.dismiss()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}