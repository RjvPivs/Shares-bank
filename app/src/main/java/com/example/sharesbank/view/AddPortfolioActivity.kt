package com.example.sharesbank.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.sharesbank.R
import com.example.sharesbank.data.DatabaseModule
import com.example.sharesbank.model.Portfolio
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddPortfolioActivity : AppCompatActivity() {
    private val repository = DatabaseModule.provideMongoRepository(DatabaseModule.provideRealm())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_portfolio)
    }

    fun savePortfolio(view: View) {
        var portfolioName: EditText = findViewById(R.id.ticker)
        if (portfolioName.text.isEmpty()) Toast.makeText(
            applicationContext,
            "Введите название портфеля",
            Toast.LENGTH_SHORT
        ).show()
        else {
            var portfolio: Portfolio = Portfolio()
            portfolio.name = portfolioName.text.toString()
            runBlocking {
                launch {
                    if (repository.getPortfolio(portfolio.name)==null){
                        repository.insertPortfolio(portfolio)
                        val infoActivity = Intent(this@AddPortfolioActivity, PortfolioActivity::class.java)
                        infoActivity.putExtra("status", false)
                        startActivity(infoActivity)
                    }
                    else {
                        Toast.makeText(
                            applicationContext,
                            "Такой портфель уже существует!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}