package com.example.sharesbank.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.asLiveData
import com.example.sharesbank.R
import com.example.sharesbank.data.DatabaseModule
import com.example.sharesbank.model.Portfolio
import com.example.sharesbank.model.Share
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddShareActivity : AppCompatActivity() {
    private val repository = DatabaseModule.provideMongoRepository(DatabaseModule.provideRealm())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_share)

    }

    fun saveShare(view: View) {
        var ticker: EditText = findViewById(R.id.ticker)
        var price: EditText = findViewById(R.id.Price)
        var number: EditText = findViewById(R.id.Number)
        when {
            ticker.text.isEmpty() -> Toast.makeText(
                applicationContext,
                "Введите тикер акции",
                Toast.LENGTH_SHORT
            ).show()
            price.text.isEmpty() -> Toast.makeText(
                applicationContext,
                "Введите среднюю цену акции",
                Toast.LENGTH_SHORT
            ).show()
            number.text.isEmpty() -> Toast.makeText(
                applicationContext,
                "Введите количество акций",
                Toast.LENGTH_SHORT
            ).show()
            number.text.toString().toInt() <= 0 -> Toast.makeText(
                applicationContext,
                "Введите корректное кол-во акций",
                Toast.LENGTH_SHORT
            ).show()
            price.text.toString().toDouble() <= 0.0 -> Toast.makeText(
                applicationContext,
                "Введите корректную цену акций",
                Toast.LENGTH_SHORT
            ).show()
            else -> run {
                var share: Share = Share()
                share.name = ticker.text.toString()
                share.number = number.text.toString().toInt()
                share.price = price.text.toString().toDouble()
                runBlocking {
                    launch {
                        val portfolio =
                            intent.getStringExtra("portfolio")
                                ?.let { repository.getPortfolio(it) }
                        if (portfolio != null) {
                            repository.insertShare(share, portfolio)
                        }
                        //portfolio?.shares?.size?.let { Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show() }
                    }
                }
                val infoActivity = Intent(this, ShareActivity::class.java)
                infoActivity.putExtra("portfolio", intent.getStringExtra("portfolio"))
                startActivity(infoActivity)
            }

        }
    }
}