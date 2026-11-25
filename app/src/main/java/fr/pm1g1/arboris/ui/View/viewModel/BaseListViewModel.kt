package fr.pm1g1.arboris.ui.View.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.pm1g1.arboris.R
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.core.network.RetrofitClient
import fr.pm1g1.arboris.data.model.shared.PaginResult
import fr.pm1g1.arboris.data.model.shared.QueryParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseListViewModel<T, U, V>(application: Application) :
    AndroidViewModel(application) {

    private val _listState = MutableStateFlow<UiState<List<T>>>(UiState.Loading)
    val listState = _listState.asStateFlow()

    private val _itemState = MutableStateFlow<UiState<U>>(UiState.Loading)
    val itemState = _itemState.asStateFlow()

    private var currentPage = 1
    private var totalPages = 1
    private var currentTreeId: Int? = null
    private val allItems = mutableListOf<T>()

    private val unknownErrorMessage: String
        get() = getApplication<Application>().getString(R.string.error_unknown)

    protected abstract suspend fun fetchItems(
        queryParams: QueryParams
    ): NetworkResult<PaginResult<T>>

    protected abstract suspend fun fetchItemById(id: Int): NetworkResult<U>

    abstract suspend fun save(id: Int, item: V): NetworkResult<U>

    abstract suspend fun create(item: V): NetworkResult<U>

    init {
        try {
            RetrofitClient.initialize(application)
        } catch (_: Exception) {
        }
    }

    fun loadItems(treeId: Int?) {
        viewModelScope.launch {
            currentPage = 1
            currentTreeId = treeId
            _listState.value = UiState.Loading

            when (val result = fetchItems(QueryParams(page = currentPage, treeId = currentTreeId))
            ) {
                is NetworkResult.Success -> {
                    val paginResult = result.data
                    totalPages = paginResult.totalPages
                    allItems.clear()
                    allItems.addAll(paginResult.items)
                    _listState.value = UiState.Success(allItems.toList())
                }

                is NetworkResult.Error -> {
                    _listState.value =
                        UiState.Error(result.exception.message ?: unknownErrorMessage)
                }

                is NetworkResult.Loading -> {
                    _listState.value = UiState.Loading
                }
            }
        }
    }

    fun loadItemsNext() {
        if (currentPage + 1 <= totalPages) {
            viewModelScope.launch {
                when (val result =
                    fetchItems(
                        QueryParams(page = ++currentPage, treeId = currentTreeId)
                    )
                ) {
                    is NetworkResult.Success -> {
                        val paginResult = result.data
                        allItems.addAll(paginResult.items)
                        _listState.value = UiState.Success(allItems.toList())
                    }

                    is NetworkResult.Error -> {
                        _listState.value =
                            UiState.Error(result.exception.message ?: unknownErrorMessage)
                    }

                    is NetworkResult.Loading -> {
                        _listState.value = UiState.Loading
                    }
                }
            }
        }
    }

    fun loadItemById(id: Int) {
        viewModelScope.launch {
            _itemState.value = UiState.Loading

            when (val result = fetchItemById(id)) {
                is NetworkResult.Success -> {
                    _itemState.value = UiState.Success(result.data)
                }

                is NetworkResult.Error -> {
                    _itemState.value =
                        UiState.Error(result.exception.message ?: unknownErrorMessage)
                }

                is NetworkResult.Loading -> {
                    _itemState.value = UiState.Loading
                }
            }
        }
    }
}
