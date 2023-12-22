package com.example.priority.view.calculator

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.priority.R
import com.example.priority.data.ResultState
import com.example.priority.databinding.FragmentCalcBinding
import com.example.priority.databinding.FragmentDashboardBinding
import com.example.priority.view.CameraViewModel
import com.example.priority.view.ViewModelFactory
import com.example.priority.view.main.DashboardFragment
import com.example.priority.view.task.TaskFragment
import com.google.firebase.auth.FirebaseAuth

class CalcFragment : Fragment() {
    private lateinit var binding: FragmentCalcBinding
    private val viewModel by viewModels<CaclViewModel>()
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
            val model_name = when (checkedId) {
                R.id.radioButtonBus -> "Bus"
                R.id.radioButtonMotor -> "Motor"
                else -> "Mobil"
            }

            binding.radioGroup2.setOnCheckedChangeListener { _, checkedId ->
                val selectedButton = when (checkedId) {
                    R.id.radioButtonFuel -> binding.radioButtonFuel
                    R.id.radioButtonDiesel -> binding.radioButtonDiesel
                    else -> error("Unexpected checked ID")
                }
                selectedButton.setBackgroundResource(getBackgroundResourceForSelectedButton2(checkedId))

                // Set transportation variable based on checked ID
                val bbm = if (checkedId == R.id.radioButtonFuel) "Bensin" else "Solar" // Adjust IDs and values as needed
                binding.btnHitung.setOnClickListener{
                    viewModel.postEmisi(model_name,bbm).observe(viewLifecycleOwner){result ->
                        if (result != null) {
                            when (result) {
                                is ResultState.Loading -> {
//                                    showLoading(true)
                                }
                                is ResultState.Success -> {
                                    binding.edtJumlah.setText(result.data.hasilEmisi.toString())

                                }
                                is ResultState.Error -> {
//                                    showLoading(false)
                                }

                                else -> {}
                            }
                        }
                    }
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