package com.laelar.app.books

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.laelar.app.R
import com.laelar.app.databinding.FragmentBookBinding
import com.laelar.core.adapters.ChapterAdapter
import com.laelar.core.assets.Chapters
import com.naulian.anhance.createLLManager
import com.naulian.anhance.navigateTo
import com.naulian.anhance.setUpPopBackStack

class BookFragment : Fragment(R.layout.fragment_book) {
    private lateinit var itemAdapter: ChapterAdapter
    private lateinit var loutManager: LinearLayoutManager

    private var viewBinding: FragmentBookBinding? = null
    private val binding get() = viewBinding!!

    private val args: BookFragmentArgs by navArgs()
    private val bookId get() = args.bookId

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentBookBinding.bind(view)

        initialize()
        binding.loadUi()
    }

    private fun initialize() {
        loutManager = createLLManager()
        itemAdapter = ChapterAdapter()
        itemAdapter.onClick {
            val direction = BookFragmentDirections
                .actionCourseFragmentToChapterFragment(it.id)
            navigateTo(direction)
        }

        val chapters = Chapters.list(bookId)
        itemAdapter.submitList(chapters)
    }

    private fun FragmentBookBinding.loadUi() {
        setUpPopBackStack(topAppbar)

        recyclerChapters.adapter = itemAdapter
        recyclerChapters.layoutManager = loutManager
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}