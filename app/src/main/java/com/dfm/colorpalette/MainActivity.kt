package com.dfm.colorpalette

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var randomFragment: RandomFragment = RandomFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.frame_container, randomFragment, "InspirationFragment").hide(randomFragment).commit()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.show(randomFragment).commit()
    }
}
