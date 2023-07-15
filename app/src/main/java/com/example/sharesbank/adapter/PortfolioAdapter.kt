package com.example.sharesbank.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharesbank.R
import com.example.sharesbank.model.Portfolio
import com.example.sharesbank.databinding.PortfolioItemBinding
import com.example.sharesbank.model.Share
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PortfolioAdapter(val listener: Listener) :
    RecyclerView.Adapter<PortfolioAdapter.PortfolioHolder>() {

    private val portfolioList = ArrayList<Portfolio>()

    class PortfolioHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = PortfolioItemBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind(portfolio: Portfolio, listener: Listener) = with(binding) {
            portfolioNameRC.text = portfolio.name
            portfolioNumberRC.text = portfolio.number.toString()
            //binding.imageView2.setIma
            settingsPortfolio.setOnClickListener {
                listener.onClickDel(portfolio)
            }
            portfolioLayout.setOnClickListener {
                listener.onClickMove(portfolio)
            }
            var sharesCounter: Int = 0
            portfolio.shares.forEach { sharesCounter += it.number }
            portfolioNumberRC.text = "$sharesCounter шт."
        }
    }

    fun updatePrices() {
        runBlocking { launch { portfolioList.forEach {
            itt ->
             {
                itt.shares.forEach { Web.getSharePriceAsync(it, itt) }
            }
        } } }
    }
    fun deleteAt(position: Int): Portfolio {
        var portfolio = portfolioList[position]
        portfolioList.removeAt(position)
        return portfolio
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.portfolio_item, parent, false)
        return PortfolioHolder(view)
    }

    interface Listener {
        fun onClickMove(portfolio: Portfolio)
        fun onClickDel(portfolio: Portfolio)
    }

    override fun getItemCount(): Int {
        return portfolioList.size
    }

    fun clear() {
        portfolioList.clear()
    }

    override fun onBindViewHolder(holder: PortfolioHolder, position: Int) {
        holder.bind(portfolioList[position], listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addPortfolio(portfolio: Portfolio) {
        portfolioList.add(portfolio)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun delete(portfolio: Portfolio) {
        portfolioList.remove(portfolio)
        notifyDataSetChanged()
    }

}