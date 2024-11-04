package com.example.priority.view.calculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.priority.R
import com.example.priority.databinding.FragmentCalculatorBinding
import androidx.cardview.widget.CardView

class CalculatorFragment : Fragment() {
    private lateinit var binding: FragmentCalculatorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vehicle card clicks
        binding.cvMotorcycle.setOnClickListener { selectVehicleCard(binding.cvMotorcycle) }
        binding.cvCar.setOnClickListener { selectVehicleCard(binding.cvCar) }
        binding.cvBus.setOnClickListener { selectVehicleCard(binding.cvBus) }
        binding.cvTrain.setOnClickListener { selectVehicleCard(binding.cvTrain) }

        // Fuel card clicks
        binding.cvBensin.setOnClickListener { selectFuelCard(binding.cvBensin) }
        binding.cvSolar.setOnClickListener { selectFuelCard(binding.cvSolar) }
        binding.cvListrik.setOnClickListener { selectFuelCard(binding.cvListrik) }

        binding.tvJenisBahanBakar.visibility = View.GONE
        binding.radioGroupFuel.visibility = View.GONE

        // Add TextWatcher to distance input
        binding.edtJarak.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calculateEmissions()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun selectVehicleCard(selectedCard: CardView) {
        binding.tvJenisBahanBakar.visibility = View.VISIBLE
        val cardViews = listOf(binding.cvMotorcycle, binding.cvCar, binding.cvBus, binding.cvTrain)
        val textViews = listOf(binding.tvMotorcycle, binding.tvCar, binding.tvBus, binding.tvTrain)

        // Reset fuel card selections
        val fuelCardViews = listOf(binding.cvBensin, binding.cvSolar, binding.cvListrik)
        val fuelTextViews = listOf(binding.tvBensin, binding.tvSolar, binding.tvListrik)

        for (i in fuelCardViews.indices) {
            fuelCardViews[i].setBackgroundResource(R.color.white)
            fuelTextViews[i].setTextColor(resources.getColor(R.color.blue_ligth))
            fuelCardViews[i].isSelected = false
        }

        for (i in cardViews.indices) {
            if (cardViews[i] == selectedCard) {
                cardViews[i].setBackgroundResource(R.color.blue_ligth)
                textViews[i].setTextColor(resources.getColor(R.color.white))
                cardViews[i].isSelected = true
                showFuelOptions(i)
            } else {
                cardViews[i].setBackgroundResource(R.color.white)
                textViews[i].setTextColor(resources.getColor(R.color.blue_ligth))
                cardViews[i].isSelected = false
            }
        }

        // Reset emission result when vehicle changes
        binding.edtJumlah.setText("")
        calculateEmissions()
    }

    private fun selectFuelCard(selectedCard: CardView) {
        val cardViews = listOf(binding.cvBensin, binding.cvSolar, binding.cvListrik)
        val textViews = listOf(binding.tvBensin, binding.tvSolar, binding.tvListrik)
        val radioButtons = listOf(
            binding.radioButtonBensin,
            binding.radioButtonSolar,
            binding.radioButtonListrik
        )

        for (i in cardViews.indices) {
            if (cardViews[i] == selectedCard) {
                cardViews[i].setBackgroundResource(R.color.blue_ligth)
                textViews[i].setTextColor(resources.getColor(R.color.white))
                cardViews[i].isSelected = true  // Set selected state
                radioButtons[i].isChecked = true
            } else {
                cardViews[i].setBackgroundResource(R.color.white)
                textViews[i].setTextColor(resources.getColor(R.color.blue_ligth))
                cardViews[i].isSelected = false  // Clear selected state
                radioButtons[i].isChecked = false
            }
        }
        calculateEmissions()  // Recalculate when fuel type changes
    }

    private fun showFuelOptions(selectedIndex: Int) {
        binding.radioGroupFuel.visibility = View.GONE

        binding.cvBensin.visibility = View.GONE
        binding.cvSolar.visibility = View.GONE
        binding.cvListrik.visibility = View.GONE

        binding.radioButtonBensin.isChecked = false
        binding.radioButtonSolar.isChecked = false
        binding.radioButtonListrik.isChecked = false

        when (selectedIndex) {
            0 -> { // Motorcycle selected
                binding.radioGroupFuel.visibility = View.VISIBLE
                binding.cvBensin.visibility = View.VISIBLE
                binding.cvListrik.visibility = View.VISIBLE
            }
            1 -> { // Car selected
                binding.radioGroupFuel.visibility = View.VISIBLE
                binding.cvBensin.visibility = View.VISIBLE
                binding.cvSolar.visibility = View.VISIBLE
                binding.cvListrik.visibility = View.VISIBLE
            }
            2 -> { // Bus selected
                binding.radioGroupFuel.visibility = View.VISIBLE
                binding.cvListrik.visibility = View.VISIBLE
                binding.cvSolar.visibility = View.VISIBLE
            }
            3 -> { // Train selected
                binding.radioGroupFuel.visibility = View.VISIBLE
                binding.cvListrik.visibility = View.VISIBLE
            }
            else -> {
                binding.radioGroupFuel.visibility = View.GONE
            }
        }
    }

    private fun calculateEmissions() {
        val distanceText = binding.edtJarak.text.toString()
        if (distanceText.isBlank()) {
            binding.edtJumlah.setText("")
            return
        }

        val distance = distanceText.toDoubleOrNull() ?: return
        val emissionFactor = when {
            binding.cvMotorcycle.isSelected && binding.radioButtonBensin.isChecked -> 0.035
            binding.cvMotorcycle.isSelected && binding.radioButtonListrik.isChecked -> 0.021
            binding.cvCar.isSelected && binding.radioButtonListrik.isChecked -> 0.031
            binding.cvCar.isSelected && binding.radioButtonBensin.isChecked -> 0.088
            binding.cvCar.isSelected && binding.radioButtonSolar.isChecked -> 0.136
            binding.cvBus.isSelected && binding.radioButtonListrik.isChecked -> 0.006
            binding.cvBus.isSelected && binding.radioButtonSolar.isChecked -> 0.008
            binding.cvTrain.isSelected && binding.radioButtonListrik.isChecked -> 0.028
            else -> 0.0
        }

        val emissions = distance * emissionFactor
        Log.d("polusi", "calculateEmissions: $emissions")
        binding.edtJumlah.setText(emissions.toString())
    }
}