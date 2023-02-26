package com.shiro.arturosalcedogagliardi.ui.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shiro.arturosalcedogagliardi.R
import com.shiro.arturosalcedogagliardi.databinding.ActivityMainBinding
import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.helpers.Constants
import com.shiro.arturosalcedogagliardi.helpers.extensions.hideKeyboard
import com.shiro.arturosalcedogagliardi.helpers.extensions.showDoubleDialog
import com.shiro.arturosalcedogagliardi.ui.character.CharacterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.contracts.ContractBuilder

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: CharactersAdapter

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            activityResult.data?.extras?.let { extras ->
                val characterUpdated =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        extras.getSerializable(Constants.CHARACTER, Character::class.java)
                    else extras.getSerializable(Constants.CHARACTER) as? Character

                characterUpdated?.let { newCharacter ->
                    val oldCharacter = adapter.currentList.find { it.id == newCharacter.id }
                    oldCharacter?.let { old ->
                        val newList = adapter.currentList.toMutableList()
                        val index = adapter.currentList.indexOf(old)
                        newList[index] = newCharacter
                        adapter.submitList(newList) {
                            adapter.notifyItemChanged(index)
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setContentView(binding.root)

        viewModel.getCharacters()

        initAdapter()
        initListeners()
        initObservers()
    }

    private fun initAdapter() {
        adapter = CharactersAdapter {
            launcher.launch(
                Intent(this, CharacterActivity::class.java)
                    .putExtra(Constants.CHARACTER, it)
            )
        }
        binding.recyclerCharacters.adapter = adapter
    }

    private fun initListeners() {
        binding.refreshLayout.apply {
            setOnRefreshListener {
                isRefreshing = false
                viewModel.refreshData()
            }
        }

        binding.recyclerCharacters.addOnScrollListener(
            object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    (binding.recyclerCharacters.layoutManager as? GridLayoutManager)
                        ?.let {
                            if (it.findLastCompletelyVisibleItemPosition() >= adapter.itemCount - 10
                                && viewModel.hastNext() && viewModel.isLoading.value == false) {
                                viewModel.getCharacters()
                            }
                        }
                    super.onScrolled(recyclerView, dx, dy)
                }
            }
        )
    }

    private fun initObservers() {
        viewModel.isLoading.observe(this) {
            binding.progressContainer.isVisible = it
        }

        viewModel.charactersList.observe(this) {
            adapter.submitList(it)
            adapter.notifyItemRangeChanged(0, adapter.itemCount)
        }

        viewModel.errorResourceId.observe(this) {
            if (it != 0) {
                showDoubleDialog(
                    mapOf(
                        Constants.DIALOG_TITLE to getString(R.string.error_title),
                        Constants.DIALOG_DESCRIPTION to getString(it)
                    )
                ) {
                    viewModel.clearError()
                }
            }
        }
    }
}