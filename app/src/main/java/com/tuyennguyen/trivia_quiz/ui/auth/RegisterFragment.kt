package com.tuyennguyen.trivia_quiz.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tuyennguyen.trivia_quiz.MyApplication
import com.tuyennguyen.trivia_quiz.R
import com.tuyennguyen.trivia_quiz.databinding.FragmentRegisterBinding
import com.tuyennguyen.trivia_quiz.ui.home.HomeFragment
import com.tuyennguyen.trivia_quiz.utils.Navigator
import com.tuyennguyen.trivia_quiz.utils.snack


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private val dialogInProgress by lazy {
        Navigator.buildDialogInProgress(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        val TAG = RegisterFragment::class.simpleName

        @JvmStatic
        fun newInstance() = RegisterFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etEmail.addTextChangedListener { value ->
            value?.let {
                val email = it.toString()
                isValidEmail(email)
            }
        }

        binding.etPassword.addTextChangedListener { value ->
            value?.let {
                val password = it.toString()
                isValidPassword(password)
            }
        }

        binding.etConfirmPassword.addTextChangedListener { value ->
            value?.let {
                val confirmPassword = it.toString()
                val password = binding.etPassword.text.toString()
                isValidConfirmPassword(confirmPassword, password)
            }
        }

        binding.btRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            if (isValidEmail(email) && isValidPassword(password) && isValidConfirmPassword(confirmPassword, password)) {
                viewModel.register(email, password)
            }
        }

        binding.fabBack.setOnClickListener {
            Navigator.pop(requireActivity().supportFragmentManager)
        }

        val userRepository = (requireActivity().application as MyApplication).userRepository
        viewModel = ViewModelProvider(this, RegisterViewModelFactory(userRepository)).get(RegisterViewModel::class.java)

        viewModel.registerState.observe(viewLifecycleOwner, {
            when (it) {
                RegisterState.Success -> {
                    if (dialogInProgress.isShowing) {
                        dialogInProgress.dismiss()
                    }
                    Navigator.pop(requireActivity().supportFragmentManager, LoginFragment.TAG)
                    Navigator.push(requireActivity().supportFragmentManager, HomeFragment.newInstance(), HomeFragment.TAG)
                }

                RegisterState.Failure -> {
                    if (dialogInProgress.isShowing) {
                        dialogInProgress.dismiss()
                    }
                    binding.root.snack(
                        "Email already exists!",
                        1000,
                        ContextCompat.getColor(requireActivity(), R.color.red)
                    )
                }

                RegisterState.InProgress -> {
                    dialogInProgress.show()
                }
            }
        })
    }

    private fun isValidEmail(email: String): Boolean {
        return when {
            email.isEmpty() -> {
                binding.viewEmail.error = "Email can't be empty"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.viewEmail.error = "Invalid email"
                false
            }
            else -> {
                binding.viewEmail.error = null
                true
            }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        return when {
            password.isEmpty() -> {
                binding.viewPassword.error = "Password can't be empty"
                false
            }
            password.length < 6 -> {
                binding.viewPassword.error = "Password min 6 characters"
                false
            }
            else -> {
                binding.viewPassword.error = null
                true
            }
        }
    }

    private fun isValidConfirmPassword(confirmPassword: String, password: String): Boolean {
        return when {
            confirmPassword != password -> {
                binding.viewConfirmPassword.error = "Confirm password invalid"
                false
            }
            else -> {
                binding.viewConfirmPassword.error = null
                true
            }
        }
    }
}