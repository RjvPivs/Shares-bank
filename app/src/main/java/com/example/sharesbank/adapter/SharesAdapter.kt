package com.example.sharesbank.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharesbank.R
import com.example.sharesbank.databinding.ShareItemBinding
import com.example.sharesbank.model.Share

class SharesAdapter (val listener: Listener): RecyclerView.Adapter<SharesAdapter.SharesHolder>() {

    val sharesList = ArrayList<Share>()

    class SharesHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = ShareItemBinding.bind(item)
        @SuppressLint("SetTextI18n")
        fun bind(share: Share, listener: Listener) = with(binding){
            shareName.text = share.name
            shareNumber.text = share.number.toString()
            sharePrice.text = share.price.toString() + " ₽"
            shareSettings.setOnClickListener{
                listener.onClickDel(share)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.share_item, parent, false)
        return SharesHolder(view)
    }

    interface Listener{
        fun onClickDel(share: Share)
    }

    override fun getItemCount(): Int {
        return sharesList.size
    }
    fun clear(){
        sharesList.clear()
    }
    override fun onBindViewHolder(holder: SharesHolder, position: Int) {
        holder.bind(sharesList[position], listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addShare(share: Share){
        sharesList.add(share)
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun delete(share: Share){
        sharesList.remove(share)
        notifyDataSetChanged()
    }

}