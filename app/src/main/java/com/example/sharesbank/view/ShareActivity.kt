package com.example.sharesbank.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sharesbank.R
import com.example.sharesbank.adapter.SharesAdapter
import com.example.sharesbank.adapter.Web
import com.example.sharesbank.data.DatabaseModule
import com.example.sharesbank.databinding.ActivityShareBinding
import com.example.sharesbank.model.Portfolio
import com.example.sharesbank.model.Share
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.DecimalFormat

class ShareActivity : AppCompatActivity(), SharesAdapter.Listener {
    private lateinit var portfolio: Portfolio
    var profit: Double = 0.0
    private lateinit var binding: ActivityShareBinding
    private val repository = DatabaseModule.provideMongoRepository(DatabaseModule.provideRealm())
    private val adapter = SharesAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        runBlocking {
            launch {
                portfolio =
                    intent.getStringExtra("portfolio")?.let { repository.getPortfolio(it) }!!
                portfolio.shares.asFlow()?.asLiveData()?.observe(this@ShareActivity) { it ->
                    adapter.clear()
                    profit = 0.0
                    it.list.forEach {
                        adapter.addShare(it)
                        profit += it.getProfit()
                    }
                    updateProfit()
                }
            }
        }
    }

    private fun init() = with(binding) {
        rcShare.layoutManager = GridLayoutManager(this@ShareActivity, 1)
        rcShare.adapter = adapter
        addShare.setOnClickListener {
            val infoActivity = Intent(this@ShareActivity, AddShareActivity::class.java)
            infoActivity.putExtra("portfolio", intent.getStringExtra("portfolio"))
            startActivity(infoActivity)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateProfit() {
        val df = DecimalFormat("#.#")
        var shareProfit: TextView = findViewById(R.id.shareProfit)
        var temp: String = ""
        if (profit > 0) temp = "+"
        shareProfit.text = "Доходность: $temp${df.format(profit)} ₽"
    }

    override fun onClickDel(share: Share) {
        adapter.delete(share)
        profit -= share.getProfit()
        updateProfit()
        runBlocking { launch { repository.deleteShare(share, portfolio) } }
    }

    override fun onBackPressed() {
        val mainActivity = Intent(this, PortfolioActivity::class.java)
        startActivity(mainActivity)
    }
}