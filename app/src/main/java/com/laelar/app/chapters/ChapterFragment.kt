package com.laelar.app.chapters

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.laelar.app.R
import com.laelar.app.databinding.FragmentChapterBinding
import com.laelar.core.adapters.BlockAdapter
import com.laelar.core.models.Block
import com.naulian.anhance.createLLManager
import com.naulian.anhance.loadData
import com.naulian.anhance.observe
import com.naulian.anhance.setUpPopBackStack
import kotlinx.coroutines.CoroutineScope

class ChapterFragment : Fragment(R.layout.fragment_chapter) {
    private lateinit var itemAdapter: BlockAdapter
    private lateinit var loutManager: LinearLayoutManager

    private val args: ChapterFragmentArgs by navArgs()
    private val chapterId get() = args.chapterId

    private var viewBinding: FragmentChapterBinding? = null
    private val binding get() = viewBinding!!

    private val viewModel: ChapterViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadChapter(chapterId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentChapterBinding.bind(view)

        initialize()
        binding.loadUi()
        loadData { observeChanges() }
    }

    private fun initialize() {
        loutManager = createLLManager()
        itemAdapter = BlockAdapter()
        itemAdapter.onClick { onClick?.invoke(this) }
    }

    private fun FragmentChapterBinding.loadUi() {
        setUpPopBackStack(topAppbar)

        recyclerPage.adapter = itemAdapter
        recyclerPage.layoutManager = loutManager

        loadScreen.setBgColorRes(R.color.background)
    }

    private fun CoroutineScope.observeChanges() {
        observe(viewModel.chapter) { chapter ->
            val blocks = chapter.blocks.toMutableList()
            if(blocks.isEmpty()) return@observe

            val action = if (chapter.learned)
                Block(button = "Unlearned", onClick = { viewModel.unlearned(chapterId) })
            else Block(button = "Learned", onClick = { viewModel.learn(chapterId) })

            blocks.add(action)
            itemAdapter.submitList(blocks)
            binding.topAppbar.title = if (chapter.learned) "Learned ☺️" else "😃"

            viewModel.view(chapterId)
            viewModel.viewBlocks(chapter.blocks)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}