package android.example.tiptime

import android.content.Context
import android.example.tiptime.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding                   /*first time usage of lateinit...it is a keyword..or say it's a promise that we will initialize the variable before using it
                                                                    otherwise the app will crash*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityMainBinding.inflate(layoutInflater)               //initializing the binding object
        setContentView(binding.root)                                        //set the content of the activity to the root of the hierarchy of views
        binding.calculateButton.setOnClickListener { calculateTip() }           //setting the onclicklistener on the calculate button
        //pay attetntion to the syntax of generating reference for each view
        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(
                view,
                keyCode
            )
        }
    }

    private fun calculateTip() {
        val stringInTextField =
            binding.costOfServiceEditText.text.toString()                       //converting the editable to tostring because toDouble can act only on strings
        val cost = stringInTextField.toDoubleOrNull()
        if (cost == null) {
            binding.tipResult.text = " "
            return
        }

        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }
        var tip = cost * tipPercentage            //note the usage of var instead of val
        val roundUp =
            binding.roundUpSwitch.isChecked                       //Checking for the switch is on or off: returns true /false
        if (roundUp) {
            tip = kotlin.math.ceil(tip)
        }
        NumberFormat.getCurrencyInstance()                          //for formatting into currencies around the world
        val formattedTip = NumberFormat.getCurrencyInstance()
            .format(tip)               //notice the change in strings.xml
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}