package com.example.subasta.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.subasta.data.repository.AuctionRepository

class AuctionViewModelFactory(
    private val repository: AuctionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuctionViewModel::class.java)) {
            return AuctionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}