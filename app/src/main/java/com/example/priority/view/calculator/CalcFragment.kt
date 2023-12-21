package com.example.priority.view.calculator

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.example.priority.R
import com.example.priority.databinding.FragmentCalcBinding
import com.example.priority.databinding.FragmentDashboardBinding
import com.example.priority.view.task.TaskFragment
import com.google.firebase.auth.FirebaseAuth

class CalcFragment : Fragment() {
    private lateinit var binding: FragmentCalcBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalcBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedButton: RadioButton = when (checkedId) {
                R.id.radioButtonBus -> binding.radioButtonBus
                R.id.radioButtonMotor -> binding.radioButtonMotor
                else -> binding.radioButtonCar
            }
            selectedButton.setBackgroundResource(getBackgroundResourceForSelectedButton(checkedId))

            // Optional: Reset unselected images
            for (button in listOf(binding.radioButtonBus, binding.radioButtonMotor, binding.radioButtonCar)) {
                if (button.id != checkedId) {
                    button.setBackgroundResource(getNormalBackgroundResourceForButton(button.id))
                }
            }

            // Set transportation variable
            val transportation = when (checkedId) {
                R.id.radioButtonBus -> 1
                R.id.radioButtonMotor -> 2
                else -> 3
            }

            binding.radioGroup2.setOnCheckedChangeListener { _, checkedId ->
                val selectedButton = when (checkedId) {
                    R.id.radioButtonFuel -> binding.radioButtonFuel
                    R.id.radioButtonDiesel -> binding.radioButtonDiesel
                    else -> error("Unexpected checked ID")
                }
                selectedButton.setBackgroundResource(getBackgroundResourceForSelectedButton2(checkedId))

                // Set transportation variable based on checked ID
                val fuel = if (checkedId == R.id.radioButtonFuel) 1 else 2 // Adjust IDs and values as needed
                binding.btnHitung.setOnClickListener{
                    val jarakValue = binding.edtJarak.text.toString().toDoubleOrNull() ?: 0.0
                    Log.d("jarak", "onViewCreated: $jarakValue")
                    val calculatedJumlah = (transportation * fuel) * jarakValue
                    Log.d("calculate", "onViewCreated: $calculatedJumlah")// Perform calculation and convert to string
                    binding.edtJumlah.setText(calculatedJumlah.toString())
                }

                // Optional: Reset unselected image
                val unselectedButton = if (checkedId == R.id.radioButtonFuel) binding.radioButtonDiesel else binding.radioButtonFuel
                unselectedButton.setBackgroundResource(getNormalBackgroundResourceForButton2(unselectedButton.id))
            }
        }

    }

    private fun getBackgroundResourceForSelectedButton(checkedId: Int): Int {
        return when (checkedId) {
            R.id.radioButtonBus -> R.drawable.bus_border
            R.id.radioButtonMotor -> R.drawable.motorbike_border
            else -> R.drawable.car_border
        }
    }

    private fun getBackgroundResourceForSelectedButton2(checkedId: Int): Int {
        return when (checkedId) {
            R.id.radioButtonFuel -> R.drawable.fuel_border
            else -> R.drawable.diesel_border
        }
    }

    private fun getNormalBackgroundResourceForButton(buttonId: Int): Int {
        return when (buttonId) {
            R.id.radioButtonBus -> R.drawable.bus
            R.id.radioButtonMotor -> R.drawable.motorbike
            else -> R.drawable.car
        }
    }

    private fun getNormalBackgroundResourceForButton2(buttonId: Int): Int {
        return when (buttonId) {
            R.id.radioButtonFuel -> R.drawable.fuel
            else -> R.drawable.diesel
        }
    }

}