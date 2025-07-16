package com.example.priority.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.priority.data.ResultState
import com.example.priority.data.response.AqiResponse
import com.example.priority.data.response.Current
import com.example.priority.data.response.Data
import com.example.priority.data.response.Pollution
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DashboardViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setUp() {
        viewModel = DashboardViewModel()
    }

    @Test
    fun `getAqi should return success with correct AQI data`() {
        val observer: Observer<in ResultState<AqiResponse>?> = Observer {}

        try {
            viewModel.getAqiTest(10.0, 20.0, "dummy-api-key").observeForever(observer)
            val result = viewModel.getAqiTest(10.0, 20.0, "dummy-api-key").value
            assertNotNull(result)
            assertTrue(result is ResultState.Success)
        } finally {
            viewModel.getAqiTest(10.0, 20.0, "dummy-api-key").removeObserver(observer)
        }
    }

}
