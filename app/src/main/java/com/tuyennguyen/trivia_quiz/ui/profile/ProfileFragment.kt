package com.tuyennguyen.trivia_quiz.ui.profile

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tuyennguyen.trivia_quiz.MyApplication
import com.tuyennguyen.trivia_quiz.R
import com.tuyennguyen.trivia_quiz.databinding.FragmentProfileBinding
import com.tuyennguyen.trivia_quiz.ui.auth.LoginFragment
import com.tuyennguyen.trivia_quiz.ui.home.HomeFragment
import com.tuyennguyen.trivia_quiz.utils.Navigator
import com.tuyennguyen.trivia_quiz.utils.State
import com.tuyennguyen.trivia_quiz.utils.snack


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        val TAG = ProfileFragment::class.simpleName

        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            binding.ivAvatar.setImageURI(it)
        }
    }

    private val dialogInProgress by lazy {
        Navigator.buildDialogInProgress(requireActivity())
    }

    private var userScore: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivAvatar.isEnabled = false
        binding.etFullName.isEnabled = false
        binding.btUpdate.text = "Update"

        binding.fabBack.setOnClickListener {
            Navigator.pop(requireActivity().supportFragmentManager)
        }

        binding.btLogout.setOnClickListener {
            viewModel.logout()
            Navigator.pop(requireActivity().supportFragmentManager, HomeFragment.TAG)
            Navigator.push(requireActivity().supportFragmentManager, LoginFragment.newInstance(), LoginFragment.TAG)
        }

        binding.ivAvatar.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.btUpdate.setOnClickListener {
            if (binding.btUpdate.text == "Update") {
                viewModel.changeProfileState(ProfileState.Edit)
                binding.btUpdate.text == "Confirm"
                binding.etFullName.requestFocus()
            } else {
                val bitmap = (binding.ivAvatar.drawable as BitmapDrawable).bitmap
                val fullName = binding.etFullName.text.toString()
                if (fullName.isEmpty()) {
                    binding.root.snack("Full name can't be empty", 700, ContextCompat.getColor(requireActivity(), R.color.red))
                    return@setOnClickListener
                }
                viewModel.updateProfile(bitmap, fullName, userScore)
            }
        }

        val userRepository = (requireActivity().application as MyApplication).userRepository
        viewModel = ViewModelProvider(this, ProfileViewModelFactory(userRepository)).get(ProfileViewModel::class.java)

        viewModel.getUser().observe(viewLifecycleOwner, {
            when (it.state) {
                State.Success -> {
                    if (it.data?.avatar?.isEmpty() == true) {
                        binding.ivAvatar.setImageResource(R.drawable.ic_profile)
                    } else {
                        Glide.with(this).load(it.data?.avatar).into(binding.ivAvatar)
                    }
                    binding.etFullName.setText(it.data?.fullName)
                    it.data?.score?.let { score ->
                        userScore = score
                    }
                }
            }
        })

        viewModel.profileState.observe(viewLifecycleOwner, {
            when (it) {
                ProfileState.Edit -> {
                    binding.ivAvatar.isEnabled = true
                    binding.etFullName.isEnabled = true
                    binding.btUpdate.text = "Confirm"
                }
                ProfileState.Success -> {
                    binding.ivAvatar.isEnabled = false
                    binding.etFullName.isEnabled = false
                    binding.btUpdate.text = "Update"

                    if (dialogInProgress.isShowing) {
                        dialogInProgress.dismiss()
                    }
                    binding.root.snack("Update success", 700, ContextCompat.getColor(requireActivity(), R.color.green))

                }
                ProfileState.Failure -> {
                    if (dialogInProgress.isShowing) {
                        dialogInProgress.dismiss()
                    }
                    binding.root.snack("Update failure, try again!", 700, ContextCompat.getColor(requireActivity(), R.color.red))
                }

                ProfileState.Initial -> {
                    if (dialogInProgress.isShowing) {
                        dialogInProgress.dismiss()
                    }
                }

                else -> {
                    dialogInProgress.show()
                }
            }
        })
    }
}