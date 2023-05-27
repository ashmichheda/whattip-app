package com.example.whattip

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat


private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {

    private lateinit var baseAmountEditTxt: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tipPercentTxt: TextView
    private lateinit var tipAmountTxt: TextView
    private lateinit var totalAmountTxt: TextView
    private lateinit var tipDescTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        baseAmountEditTxt = findViewById(R.id.baseAmountEditTxt)
        seekBarTip = findViewById(R.id.seekBarTip)
        tipPercentTxt = findViewById(R.id.tipPercentTxt)
        tipAmountTxt = findViewById(R.id.tipAmountTxt)
        totalAmountTxt = findViewById(R.id.totalAmountTxt)
        tipDescTxt = findViewById(R.id.tipDescTxt)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tipPercentTxt.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)

        // make changes on the tip percent label when user changes seekBar
        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged: $progress")
                tipPercentTxt.text = "$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        // fetch user input when user enters base amount
        baseAmountEditTxt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                Log.i(TAG, "afterTextChanged: $text")
                computeTipAndTotal()
            }

        })
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tipDescTxt.text = tipDescription

        // Update the color based on the tipPercent
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int
        tipDescTxt.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        // 1. Get the value of base amount and tip percent
        if (baseAmountEditTxt.text.isEmpty()) {
            tipAmountTxt.text = ""
            totalAmountTxt.text = ""
            return
        }
        val baseAmount = baseAmountEditTxt.text.toString().toDouble()
        val tipPercent = seekBarTip.progress

        // 2. Compute the tip and total amount
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount

        // 3. Update the UI
        tipAmountTxt.text = "%.2f".format(tipAmount)
        totalAmountTxt.text = "%.2f".format(totalAmount)
    }
}