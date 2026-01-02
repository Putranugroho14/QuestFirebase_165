package com.example.firebase.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase.modeldata.Siswa
import com.example.firebase.repositori.RepositorySiswa
import kotlinx.coroutines.launch

sealed interface StatusUiSiswa {
    data class Success(val siswa: List<Siswa>) : StatusUiSiswa
    object Error : StatusUiSiswa
    object Loading : StatusUiSiswa
}

class HomeViewModel(private val repositorySiswa: RepositorySiswa) : ViewModel() {
    var statusUiSiswa: StatusUiSiswa by mutableStateOf(StatusUiSiswa.Loading)
        private set

    init { loadSiswa() }

    fun loadSiswa() {
        viewModelScope.launch {
            statusUiSiswa = try {
                StatusUiSiswa.Success(repositorySiswa.getDataSiswa())
            } catch (e: Exception) { StatusUiSiswa.Error }
        }
    }
}