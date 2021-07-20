package com.tuyennguyen.trivia_quiz.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tuyennguyen.trivia_quiz.MyApplication
import com.tuyennguyen.trivia_quiz.R
import com.tuyennguyen.trivia_quiz.adapters.CategoryAdapter
import com.tuyennguyen.trivia_quiz.databinding.FragmentHomeBinding
import com.tuyennguyen.trivia_quiz.entities.Category
import com.tuyennguyen.trivia_quiz.ui.play.PlayFragment
import com.tuyennguyen.trivia_quiz.ui.profile.ProfileFragment
import com.tuyennguyen.trivia_quiz.utils.Navigator
import com.tuyennguyen.trivia_quiz.utils.Resource
import com.tuyennguyen.trivia_quiz.utils.State
import com.tuyennguyen.trivia_quiz.utils.snack

class HomeFragment : Fragment(), CategoryAdapter.OnItemCategoryClickListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        val TAG = HomeFragment::class.simpleName

        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    private lateinit var binding: FragmentHomeBinding
    private val categoryAdapter by lazy {
        CategoryAdapter(requireActivity(), this)
    }
    private val dialogInProgress by lazy {
        Navigator.buildDialogInProgress(requireActivity())
    }

    private val categoriesObserver = Observer<Resource<List<Category>>> {
        when (it.state) {
            State.Success -> {
                if (dialogInProgress.isShowing) dialogInProgress.dismiss()
                it.data?.let { value ->
                    categoryAdapter.categories = value
                }
                binding.tvTryAgain.visibility = View.INVISIBLE
            }
            State.Failure -> {
                if (dialogInProgress.isShowing) dialogInProgress.dismiss()
                binding.tvTryAgain.visibility = View.VISIBLE
            }
            else -> {
                if (!dialogInProgress.isShowing) dialogInProgress.show()
                binding.tvTryAgain.visibility = View.INVISIBLE
            }
        }
    }

    private lateinit var viewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCategory.adapter = categoryAdapter

        binding.ivProfile.setOnClickListener {
            Navigator.push(requireActivity().supportFragmentManager, ProfileFragment.newInstance(), ProfileFragment.TAG)
        }

        binding.tvTryAgain.setOnClickListener {
            viewModel.getCategories().observe(viewLifecycleOwner, categoriesObserver)
        }

        binding.btStart.setOnClickListener {
            val categorySelected = viewModel.categorySelected.value
            if (categorySelected == null) {
                it.snack("Please choose category to start", 700, ContextCompat.getColor(requireActivity(), R.color.accent))
                return@setOnClickListener
            }
            Navigator.push(
                requireActivity().supportFragmentManager,
                PlayFragment.newInstance(categorySelected, getDifficult()),
                PlayFragment.TAG
            )
        }

        val userRepository = (requireActivity().application as MyApplication).userRepository
        val categoryRepository = (requireActivity().application as MyApplication).categoryRepository
        viewModel = ViewModelProvider(this, HomeViewModelFactory(userRepository, categoryRepository)).get(HomeViewModel::class.java)

        viewModel.getUser().observe(viewLifecycleOwner, {
            when (it.state) {
                State.Success -> {
                    binding.tvFullName.text = it.data?.fullName
                    binding.tvScore.text = it.data?.score.toString()
                    if (it.data?.avatar?.isEmpty() == true) {
                        binding.ivProfile.setImageResource(R.drawable.ic_profile)
                    } else {
                        Glide.with(this).load(it.data?.avatar).into(binding.ivProfile)
                    }
                }
            }
        })

        viewModel.getCategories().observe(viewLifecycleOwner, categoriesObserver)

        viewModel.categorySelected.observe(viewLifecycleOwner, {
            categoryAdapter.categorySelected = it
        })
    }

    private fun getDifficult(): String {
        return when {
            binding.rbEasy.isChecked -> {
                "Easy"
            }
            binding.rbMedium.isChecked -> {
                "Medium"
            }
            else -> {
                "Hard"
            }
        }
    }

    override fun onItemCategoryClick(category: Category) {
        viewModel.setCategorySelected(category)
    }
}