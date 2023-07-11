package com.example.sharesbank.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sharesbank.adapter.SharesAdapter
import com.example.sharesbank.data.DatabaseModule
import com.example.sharesbank.databinding.ActivityShareBinding
import com.example.sharesbank.model.Share
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ShareActivity : AppCompatActivity(), SharesAdapter.Listener {
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
                val portfolio =
                    intent.getStringExtra("portfolio")?.let { repository.getPortfolio(it) }
                portfolio?.shares?.asFlow()?.asLiveData()?.observe(this@ShareActivity) { it ->
                    adapter.clear()
                    it.list.forEach { adapter.addShare(it) }
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

    override fun onClickDel(share: Share) {
        TODO("Not yet implemented")
    }
}