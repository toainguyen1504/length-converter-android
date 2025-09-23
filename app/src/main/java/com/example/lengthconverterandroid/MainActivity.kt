package com.example.lengthconverterandroid

import android.os.Bundle
import android.text.InputFilter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private var lastClickTime: Long = 0
    private val CLICK_INTERVAL = 1000

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

        // validate input
        val inputFilter = InputFilter { source, _, _, _, _, _ ->
            // Regex: only accept (0-9) numbers and .
            if (source.matches(Regex("[0-9.]*"))) {
                source
            } else {
                ""
            }
        }
        inputTop.filters = arrayOf(inputFilter)

        setupSelect(unitSelectTop, unitTopTxt)
        setupSelect(unitSelectBottom, unitBottomTxt)

        // handle convert btn
        convertBtn.setOnClickListener {
            val currentTime = System.currentTimeMillis()

            // limit click
            if (currentTime - lastClickTime < CLICK_INTERVAL) {

                return@setOnClickListener
            }
            lastClickTime = currentTime

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
                "Metre" -> inputValue
                "Millimetre" -> inputValue / 1000
                "Mile" -> inputValue * 1609.34
                "Foot" -> inputValue * 0.3048
                else -> inputValue
            }

            // convert from meters to target unit
            val result = when (toUnit) {
                "Metre" -> inMetre
                "Millimetre" -> inMetre * 1000
                "Mile" -> inMetre / 1609.34
                "Foot" -> inMetre / 0.3048
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