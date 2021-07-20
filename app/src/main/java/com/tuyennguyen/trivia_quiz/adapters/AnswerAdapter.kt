package com.tuyennguyen.trivia_quiz.adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.tuyennguyen.trivia_quiz.R
import com.tuyennguyen.trivia_quiz.databinding.ItemAnswerBinding

class AnswerAdapter(private val context: Context, private val onItemAnswerClickListener: OnItemAnswerClickListener) :
    RecyclerView.Adapter<AnswerAdapter.ViewHolder>() {
    var answers = mutableListOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var answerSelected: String? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(val binding: ItemAnswerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val answer = answers[position]
        with(holder.binding) {
            when (position) {
                0 -> {
                    tvIndex.text = "A"
                }
                1 -> {
                    tvIndex.text = "B"
                }
                2 -> {
                    tvIndex.text = "C"
                }
                else -> {
                    tvIndex.text = "D"
                }
            }
            tvAnswer.text = HtmlCompat.fromHtml(answer, HtmlCompat.FROM_HTML_MODE_COMPACT)

            if (answer == answerSelected) {
                root.setBackgroundResource(R.drawable.bg_item_answer_selected)
                tvIndex.setTextColor(ContextCompat.getColor(context, R.color.white))
                tvAnswer.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                root.setBackgroundResource(R.drawable.bg_item_answer_unselected)
                tvIndex.setTextColor(ContextCompat.getColor(context, R.color.text))
                tvAnswer.setTextColor(ContextCompat.getColor(context, R.color.text))
            }

            root.setOnClickListener {
                onItemAnswerClickListener.onItemAnswerClick(answer)
            }
        }
    }

    override fun getItemCount(): Int {
        return answers.size
    }

    interface OnItemAnswerClickListener {
        fun onItemAnswerClick(answer: String)
    }
}