package com.tuyennguyen.trivia_quiz.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.SkuDetails
import com.tuyennguyen.trivia_quiz.databinding.ItemSkuDetailBinding

class SkuDetailAdapter(private val onItemSkuDetailClickListener: OnItemSkuDetailClickListener) :
    RecyclerView.Adapter<SkuDetailAdapter.ViewHolder>() {
    var skuDetails: MutableList<SkuDetails> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(val binding: ItemSkuDetailBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSkuDetailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val skuDetail = skuDetails[position]
        with(holder.binding) {
            tvTitle.text = skuDetail.title
            tvPrice.text = "Price: " + skuDetail.price
            root.setOnClickListener {
                onItemSkuDetailClickListener.onItemSkuDetailClick(skuDetail)
            }
        }
    }

    override fun getItemCount(): Int {
        return skuDetails.size
    }

    interface OnItemSkuDetailClickListener {
        fun onItemSkuDetailClick(skuDetail: SkuDetails)
    }
}