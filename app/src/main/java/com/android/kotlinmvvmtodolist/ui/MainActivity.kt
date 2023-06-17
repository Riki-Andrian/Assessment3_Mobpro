package com.android.kotlinmvvmtodolist.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.android.kotlinmvvmtodolist.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID = "updater"
        const val PERMISSION_REQUEST_CODE = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listenBackStackChange()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = getString(R.string.channel_desc)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager?
            manager?.createNotificationChannel(channel)
        }


        //setupActionBarWithNavController(findNavController(R.id.nav_host_fragment))
    }
    private fun listenBackStackChange() {
        // Get NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)

        // ChildFragmentManager of NavHostFragment
        val navHostChildFragmentManager = navHostFragment?.childFragmentManager

        navHostChildFragmentManager?.addOnBackStackChangedListener {

            val backStackEntryCount = navHostChildFragmentManager.backStackEntryCount
            val fragments = navHostChildFragmentManager.fragments
            val fragmentCount = fragments.size

            println("🎃 Main graph backStackEntryCount: $backStackEntryCount, fragmentCount: $fragmentCount, fragments: $fragments")

            Toast.makeText(
                this,
                "🎃 Main graph backStackEntryCount: $backStackEntryCount, fragmentCount: $fragmentCount, fragments: $fragments",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}