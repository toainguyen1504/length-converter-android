package com.example.lengthconverterandroid

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val unitSelectTop  = findViewById<ConstraintLayout>(R.id.unitSelectTop)
        val unitTopTxt   = findViewById<TextView>(R.id.unitTopTxt)

        val unitSelectBottom  = findViewById<ConstraintLayout>(R.id.unitSelectBottom)
        val unitBottomTxt   = findViewById<TextView>(R.id.unitBottomTxt)

        val inputTop = findViewById<EditText>(R.id.inputTop)
        val inputBottom = findViewById<EditText>(R.id.inputBottom)
        val convertBtn = findViewById<ImageButton>(R.id.convertBtn)

        setupSelect(unitSelectTop, unitTopTxt)
        setupSelect(unitSelectBottom, unitBottomTxt)

        // handle convert btn
        convertBtn.setOnClickListener {
            val inputStr = inputTop.text.toString()

            if (inputStr.isEmpty()) {
                Toast.makeText(this, "Please enter value!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fromUnit = unitTopTxt.text.toString()
            val toUnit = unitBottomTxt.text.toString()

            val inputValue = inputStr.toDouble()


            // convert back to meters
            val inMetre = when (fromUnit) {
                "Metre (m)" -> inputValue
                "Millimetre (mm)" -> inputValue / 1000
                "Mile (dặm)" -> inputValue * 1609.34
                "Foot (ft)" -> inputValue * 0.3048
                else -> inputValue
            }

            // convert from meters to target unit
            val result = when (toUnit) {
                "Metre (m)" -> inMetre
                "Millimetre (mm)" -> inMetre * 1000
                "Mile (dặm)" -> inMetre / 1609.34
                "Foot (ft)" -> inMetre / 0.3048
                else -> inMetre
            }

            // Format result
            val df = DecimalFormat("#.##")
            val resultStr = df.format(result)

            inputBottom.setText(resultStr)

        }

    }

    private fun setupSelect(clickView: ConstraintLayout, targetText: TextView) {
        clickView.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menuInflater.inflate(R.menu.unit_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                targetText.text = item.title
                true
            }

            popup.show()

        }
    }
}