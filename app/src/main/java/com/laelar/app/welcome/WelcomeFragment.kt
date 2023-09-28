package com.laelar.app.welcome

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.laelar.app.R
import com.laelar.app.databinding.FragmentWelcomeBinding
import com.laelar.app.requireDeviceId
import com.laelar.core.AppIds
import com.laelar.core.LicenseManager
import com.laelar.core.adapters.BlockAdapter
import com.laelar.core.assets.Icons
import com.laelar.core.models.Block
import com.laelar.core.openMessenger
import com.naulian.anhance.createLLManager
import com.naulian.anhance.navigateTo
import com.naulian.anhance.toastError
import com.naulian.firex.ifLogin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.fragment_welcome) {
    companion object {
        @Suppress("unused")
        val TAG: String = WelcomeFragment::class.java.simpleName
    }

    private lateinit var itemAdapter: BlockAdapter
    private lateinit var loutManager: LinearLayoutManager

    private var viewBinding: FragmentWelcomeBinding? = null
    private val binding get() = viewBinding!!

    private val mainFragment
        get() = WelcomeFragmentDirections
            .actionWelcomeFragmentToMainFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ifLogin { navigateTo(mainFragment) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentWelcomeBinding.bind(view)

        initialize()
        binding.loadUi()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    //==============================================================================================
    private fun initialize() {
        val list = listOf(
            Block(
                header = "Laelar Python",
                centered = true,
                body = """
                    ကြိုဆိုပါတယ်။ Laelar Python ကသင့်ကို Python programming language သင်ပေးသွားမှာပါ။
                """.trimIndent(),
                code = """
                    print('Hello, User!')  
                    # output
                    # Hello, User!
                """.trimIndent(),
                language = "python",
                extra = "Laelar Python ကို တစ်သက်လုံးစာ ၁၀၀၀၀ ဘဲသတ်မှတ်ထားပါတယ်။ PFM page ကနေစရင်းသွင်းနိုင်ပါတယ်။"
            ),

            Block(
                code = requireDeviceId(),
                copyable = true,
                language = "Device ID",
                button = "Continue",
                icon = Icons.messenger,
                link = "108409453913492",
                onLink = { link -> openMessenger(link) },
                extra = "Device ID ကို copy ပြီး page messenger မှတစ်ဆင့်စရင်းသွင်းပါ။ စရင်းသွင်းပြီးရင် continue ကိုနှိပ်ပြီးစလေ့လာလို့ရပါပြီ။",
                onClick = { login() }
            ),
        )

        loutManager = createLLManager()
        itemAdapter = BlockAdapter()
        itemAdapter.submitList(list)
        itemAdapter.onClick { onClick?.invoke(this) }
    }

    private fun FragmentWelcomeBinding.loadUi() {
        recyclerPage.adapter = itemAdapter
        recyclerPage.layoutManager = loutManager

        loadScreen.setBgColorRes(R.color.background)
    }

    private fun login() {
        binding.loadScreen.show()
        val deviceId = requireDeviceId()
        LicenseManager.check(AppIds.app, deviceId) { result ->
            binding.loadScreen.hide()
            result.onSuccess { navigateTo(mainFragment) }
            result.onFailure { toastError(requireContext(), it) }
        }
    }
}