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
import com.tuyennguyen.trivia_quiz.databinding.FragmentLoginBinding
import com.tuyennguyen.trivia_quiz.ui.home.HomeFragment
import com.tuyennguyen.trivia_quiz.utils.Navigator
import com.tuyennguyen.trivia_quiz.utils.snack


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private val dialogInProgress by lazy {
        Navigator.buildDialogInProgress(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        val TAG = LoginFragment::class.simpleName

        @JvmStatic
        fun newInstance() = LoginFragment()
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

        binding.btLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (isValidEmail(email) && isValidPassword(password)) {
                viewModel.login(email, password)
            }
        }

        binding.tvCreateAccount.setOnClickListener {
            Navigator.push(requireActivity().supportFragmentManager, RegisterFragment.newInstance(), RegisterFragment.TAG)
        }

        val authRepository = (requireActivity().application as MyApplication).userRepository
        viewModel = ViewModelProvider(this, LoginViewModelFactory(authRepository)).get(LoginViewModel::class.java)

        viewModel.loginStatus.observe(viewLifecycleOwner, {
            when (it) {
                LoginStatus.Success -> {
                    if (dialogInProgress.isShowing) {
                        dialogInProgress.dismiss()
                    }
                    Navigator.pop(requireActivity().supportFragmentManager)
                    Navigator.push(requireActivity().supportFragmentManager, HomeFragment.newInstance(), HomeFragment.TAG)
                }

                LoginStatus.Failure -> {
                    if (dialogInProgress.isShowing) {
                        dialogInProgress.dismiss()
                    }
                    binding.root.snack(
                        "Email or password incorrect!",
                        1000,
                        ContextCompat.getColor(requireActivity(), R.color.red)
                    )
                }

                LoginStatus.InProgress -> {
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

}