package fr.pm1g1.arboris.ui.screens.tree

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.pm1g1.arboris.core.network.ITreeApiService
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.core.network.RetrofitClient
import fr.pm1g1.arboris.data.model.arboris.Tree
import fr.pm1g1.arboris.data.repository.TreeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TreeViewModel : ViewModel() {
    private val treeRepository : TreeRepository

    private val _trees = MutableStateFlow<List<Tree>>(emptyList())
    val trees: StateFlow<List<Tree>> = _trees.asStateFlow()

     init {
         treeRepository = TreeRepository(RetrofitClient.instance.create(ITreeApiService::class.java))
     }

    fun setTrees(newTrees: List<Tree>) {
        _trees.value = newTrees
    }

    fun insertNewTree(treeName: String, onSuccess: (Tree) -> Unit = {}) {
        viewModelScope.launch{
            when(val result = treeRepository.insertNewTree(treeName)) {
                is NetworkResult.Success -> {
                    //TODO: afficher un message de succès
                    onSuccess(result.data)
                }
                is NetworkResult.Error -> {
                    //TODO: afficher un message d'erreur

                }

                is NetworkResult.Loading -> {
                    //TODO: afficher un indicateur de chargement
                }
            }

        }
     }
}