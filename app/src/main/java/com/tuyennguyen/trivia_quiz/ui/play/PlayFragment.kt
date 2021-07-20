package com.tuyennguyen.trivia_quiz.ui.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tuyennguyen.trivia_quiz.MyApplication
import com.tuyennguyen.trivia_quiz.R
import com.tuyennguyen.trivia_quiz.adapters.AnswerAdapter
import com.tuyennguyen.trivia_quiz.databinding.FragmentPlayBinding
import com.tuyennguyen.trivia_quiz.entities.Category
import com.tuyennguyen.trivia_quiz.utils.Navigator
import com.tuyennguyen.trivia_quiz.utils.snack

class PlayFragment : Fragment(), AnswerAdapter.OnItemAnswerClickListener {
    private var category: Category? = null
    private var difficult: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getSerializable("category") as Category?
            difficult = it.getString("difficult")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPlayBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        val TAG = PlayFragment::class.simpleName

        @JvmStatic
        fun newInstance(category: Category, difficult: String) = PlayFragment().apply {
            arguments = Bundle().apply {
                putSerializable("category", category)
                putString("difficult", difficult)
            }
        }
    }

    private lateinit var binding: FragmentPlayBinding
    private lateinit var viewModel: PlayViewModel
    private val dialogInProgress by lazy {
        Navigator.buildDialogInProgress(requireActivity())
    }

    private val dialogCongratulation by lazy {
        Navigator.buildDialogCongratulations(requireActivity(), requireActivity().supportFragmentManager)
    }

    private val dialogLose by lazy {
        Navigator.buildDialogLose(requireActivity(), requireActivity().supportFragmentManager)
    }
    private val answerAdapter by lazy {
        AnswerAdapter(requireActivity(), this)
    }
    var answerSelected: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAnswer.apply {
            adapter = answerAdapter
        }

        binding.btSubmit.setOnClickListener {
            if(answerSelected != null){
                viewModel.answer(answerSelected!!)
            }
        }

        val questionRepository = (requireActivity().application as MyApplication).questionRepository
        val viewModelFactory = PlayViewModelFactory(questionRepository, category!!.id, difficult!!)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlayViewModel::class.java)

        viewModel.index.observe(viewLifecycleOwner, {

        })

        viewModel.score.observe(viewLifecycleOwner, {
            binding.tvScore.text = it.toString()
        })

        viewModel.live.observe(viewLifecycleOwner, {
            when (it) {
                0 -> {
                    binding.ivLive1.visibility = View.INVISIBLE
                }
                1 -> {
                    binding.ivLive2.visibility = View.INVISIBLE
                }
                2 -> {
                    binding.ivLive3.visibility = View.INVISIBLE
                }
            }
        })

        viewModel.playState.observe(viewLifecycleOwner, {
            when (it) {
                PlayState.InProgress -> {
                    if (!dialogInProgress.isShowing) dialogInProgress.show()
                }
                PlayState.Play -> {
                    if (dialogInProgress.isShowing) dialogInProgress.dismiss()
                }
                PlayState.Great -> {
                    binding.root.snack("Great", 700, ContextCompat.getColor(requireActivity(), R.color.great))
                }
                PlayState.Wrong -> {
                    binding.root.snack("Wrong", 700, ContextCompat.getColor(requireActivity(), R.color.red))
                }
                PlayState.Finished -> {
                    dialogCongratulation.show()
                }
                PlayState.Lose -> {
                    dialogLose.show()
                }
                else -> {
                    //Load question error
                }
            }
        })

        viewModel.currentQuestion.observe(viewLifecycleOwner, {
            binding.tvQuestion.text = HtmlCompat.fromHtml(it.question, HtmlCompat.FROM_HTML_MODE_LEGACY)
            val answers = mutableListOf<String>()
            answers.addAll(it.incorrectAnswers)
            answers.add(it.correctAnswer)
            answers.shuffle()
            answerAdapter.answers = answers
        })
    }

    override fun onItemAnswerClick(answer: String) {
        answerAdapter.answerSelected = answer
        answerSelected = answer
    }
}