package com.tuyennguyen.trivia_quiz.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tuyennguyen.trivia_quiz.R
import com.tuyennguyen.trivia_quiz.databinding.ItemCategoryBinding
import com.tuyennguyen.trivia_quiz.entities.Category

class CategoryAdapter(private val context: Context, private val onItemCategoryClickListener: OnItemCategoryClickListener) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    var categories = emptyList<Category>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var categorySelected: Category? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        with(holder.binding) {
            if(category.id == categorySelected?.id){
                tvCategory.setTextColor(ContextCompat.getColor(context, R.color.white))
                root.setBackgroundResource(R.drawable.bg_item_category_selected)
            }
            else{
                tvCategory.setTextColor(ContextCompat.getColor(context, R.color.text))
                root.setBackgroundResource(R.drawable.bg_item_category_unselected)
            }
            tvCategory.text = category.name
            root.setOnClickListener {
                onItemCategoryClickListener.onItemCategoryClick(category)
            }
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    interface OnItemCategoryClickListener {
        fun onItemCategoryClick(category: Category)
    }
}