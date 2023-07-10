package com.example.sharesbank.adapter
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharesbank.R
import com.example.sharesbank.model.Portfolio
import com.example.sharesbank.databinding.PortfolioItemBinding

class PortfolioAdapter(val listener: Listener):RecyclerView.Adapter<PortfolioAdapter.PortfolioHolder>() {

    val portfolioList = ArrayList<Portfolio>()

    class PortfolioHolder(item: View):RecyclerView.ViewHolder(item) {
        val binding = PortfolioItemBinding.bind(item)
        fun bind(portfolio: Portfolio, listener: Listener) = with(binding){
            portfolioNameRC.text = portfolio.name
            portfolioNumberRC.text = portfolio.number.toString()
            //binding.imageView2.setIma
            itemView.setOnClickListener {
                listener.onClickMove(portfolio)
            }
            //imageDelete.setOnClickListener{
              //  listener.onClickDel(portfolio)
            //}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.portfolio_item, parent, false)
        return PortfolioHolder(view)
    }

    interface Listener{
        fun onClickMove(pdfData : Portfolio)
        fun onClickDel(pdf: Portfolio)
    }

    override fun getItemCount(): Int {
        return portfolioList.size
    }
    fun clear(){
        portfolioList.clear()
    }
    override fun onBindViewHolder(holder: PortfolioHolder, position: Int) {
        holder.bind(portfolioList[position], listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addPortfolio(portfolio: Portfolio){
        portfolioList.add(portfolio)
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun delete(portfolio: Portfolio){
        portfolioList.remove(portfolio)
        notifyDataSetChanged()
    }

}