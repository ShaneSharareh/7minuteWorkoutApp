package com.example.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_b_m_i.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    var bmiType = "standard";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_m_i)

        ArrayAdapter.createFromResource(
            this,
            R.array.bmi_type,
            android.R.layout.simple_spinner_item).also {
                adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
        }


        btn_bmi_calculation.setOnClickListener{
            if(et_height.getText().toString().trim().length >0 && et_weight.getText().toString().trim().length>0){
                tv_bmiNumber.setText(calculateBMI().toString())
            }
        }




    }

    fun calculateBMI(): BigDecimal? {
        val height:Float = et_height.text.toString().toFloat()
        val weight:Float = et_weight.text.toString().toFloat()
        val bmi = weight/(height*height)
        if(bmi<18.5){
            tv_bmiCondition.setText("Underweight")
        }
        else if(18.5<bmi&& bmi<24.9){
            tv_bmiCondition.setText("Healthy Weight")
        }
        else if(25.0 < bmi && bmi < 29.9){
            tv_bmiCondition.setText("Overweight")
        }
        else{
            tv_bmiCondition.setText("Obese")
        }
        return BigDecimal( bmi.toDouble() ).setScale(2, RoundingMode.HALF_EVEN)

    }

    fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {

    }

    fun onNothingSelected(parent: AdapterView<*>) {

    }
}


