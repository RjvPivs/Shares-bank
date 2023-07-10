package com.example.sharesbank.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sharesbank.R
import com.example.sharesbank.adapter.PortfolioAdapter
import com.example.sharesbank.data.DatabaseModule
import com.example.sharesbank.data.MongoRepository
import com.example.sharesbank.databinding.ActivityPortfolioBinding
import com.example.sharesbank.model.Portfolio

class PortfolioActivity : AppCompatActivity(), PortfolioAdapter.Listener {
    private lateinit var binding: ActivityPortfolioBinding
    private val repository = DatabaseModule.provideMongoRepository(DatabaseModule.provideRealm())
    private val adapter = PortfolioAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPortfolioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        repository.getPortfolios().asLiveData().observe(this) { it ->
            adapter.clear()
            it.forEach { adapter.addPortfolio(it) }
        }
    }

    private fun init() = with(binding) {
        rcView.layoutManager = GridLayoutManager(this@PortfolioActivity, 1)
        rcView.adapter = adapter
        addPorfolio.setOnClickListener {
            val infoActivity = Intent(this@PortfolioActivity, AddPortfolioActivity::class.java)
            startActivity(infoActivity)
        }
    }

    override fun onClickMove(pdfData: Portfolio) {
        TODO("Not yet implemented")
    }

    override fun onClickDel(pdf: Portfolio) {
        TODO("Not yet implemented")
    }
}