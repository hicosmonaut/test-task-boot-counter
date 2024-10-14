package com.test.task.bootcounter.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.task.bootcounter.data.source.local.db.entity.BootItem
import com.test.task.bootcounter.main.boot.BootUiItem
import com.test.task.bootcounter.main.boot.BootUiItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainViewModel(private val repository: BootUiItemRepository) : ViewModel() {

    private val _items = MutableStateFlow<List<BootUiItem>>(emptyList())
    val items: StateFlow<List<BootUiItem>> = _items.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allBootUiItems()
                .catch { e ->
                    _items.value = emptyList()
                }
                .collect { items ->
                    _items.value = items
                }
        }
    }

    fun test(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(
                BootItem(timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}