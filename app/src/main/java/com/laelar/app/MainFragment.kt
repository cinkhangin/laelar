package com.laelar.app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.laelar.app.databinding.FragmentMainBinding
import com.laelar.core.adapters.BookAdapter
import com.laelar.core.openTelegram
import com.naulian.anhance.createLLManager
import com.naulian.anhance.loadData
import com.naulian.anhance.navigateTo
import com.naulian.anhance.observe
import com.naulian.anhance.onMenuItemClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    @Suppress("unused")
    private val tag = MainFragment::class.java.simpleName

    private lateinit var itemAdapter: BookAdapter
    private lateinit var loManager: LinearLayoutManager

    private var viewBinding: FragmentMainBinding? = null
    private val binding get() = viewBinding!!

    private val viewModel: MainViewModel by activityViewModels()

    private fun navigateToBook(bookId: String) {
        val direction = MainFragmentDirections
            .actionMainFragmentToCourseFragment(bookId)
        navigateTo(direction)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentMainBinding.bind(view)

        viewModel.checkData()
        initialize()
        binding.loadUi()
        loadData { observeChanges() }
    }

    private fun initialize() {
        loManager = createLLManager()
        itemAdapter = BookAdapter()

        itemAdapter.onClick { navigateToBook(it.id) }
    }

    private fun FragmentMainBinding.loadUi() {
        topAppbar.onMenuItemClick {
            when (it) {
                R.id.action_telegram -> {
                    openTelegram()
                    true
                }

                else -> false
            }
        }

        recyclerCourses.layoutManager = loManager
        recyclerCourses.adapter = itemAdapter
    }

    private fun CoroutineScope.observeChanges() {
        observe(viewModel.loading) {
            if (it) binding.loadScreen.show()
            else binding.loadScreen.hide()
        }

        observe(viewModel.books) {
            itemAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}