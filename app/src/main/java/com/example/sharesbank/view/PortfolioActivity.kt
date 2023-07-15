package com.example.sharesbank.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.sharesbank.R
import com.example.sharesbank.adapter.PortfolioAdapter
import com.example.sharesbank.adapter.SwipeToDeleteCallback
import com.example.sharesbank.data.DatabaseModule
import com.example.sharesbank.data.MongoRepository
import com.example.sharesbank.databinding.ActivityPortfolioBinding
import com.example.sharesbank.model.Portfolio
import com.example.sharesbank.model.Share
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.nio.file.Files.delete

class PortfolioActivity : AppCompatActivity(), PortfolioAdapter.Listener {
    lateinit var dialog: Dialog
    lateinit var dialogPortfolio: Dialog
    var profitOld = 0.0
    var profitCurrent = 0.0
    private lateinit var binding: ActivityPortfolioBinding
    private val repository = DatabaseModule.provideMongoRepository(DatabaseModule.provideRealm())
    private val adapter = PortfolioAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPortfolioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //dialog = Dialog(this)
        dialogPortfolio = Dialog(this)
        init()
        repository.getPortfolios().asLiveData().observe(this) { it ->
            adapter.clear()
            it.forEach {
                adapter.addPortfolio(it)
                //profitOld += it.getProfit()
            }
            //adapter.updatePrices()
            //it.forEach { profitCurrent += it.getProfit() }

        }
        //if (!intent.hasExtra("status"))
        //    showDialog()
    }

    @SuppressLint("SetTextI18n")
    private fun showDialog() {
        if (profitCurrent - profitOld >= 0) {
            dialog.setContentView(R.layout.dialog_up)
        } else {
            dialog.setContentView(R.layout.dialog_down)
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var last: TextView = dialog.findViewById(R.id.profitSinceLast)
        last.text = (profitCurrent - profitOld).toString() + " $"
        dialog.show()
    }

    private fun init() = with(binding) {
        rcView.layoutManager = GridLayoutManager(this@PortfolioActivity, 1)
        rcView.adapter = adapter
        addPorfolio.setOnClickListener {
            val infoActivity = Intent(this@PortfolioActivity, AddPortfolioActivity::class.java)
            startActivity(infoActivity)
        }
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                delete(position)
                rcView.adapter?.notifyItemRemoved(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rcView)
    }

    fun delete(position: Int) {
        var portfolio = adapter.deleteAt(position)
        runBlocking {
            launch {
                portfolio.shares.forEach { repository.deleteShare(it, portfolio) }
                repository.deletePortfolio(portfolio._id)
            }
        }
    }

    private fun showDialogEdit(portfolio: Portfolio) {
        dialogPortfolio.setContentView(R.layout.activity_edit_portfolio)
        dialogPortfolio.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var saveButton: ImageButton = dialogPortfolio.findViewById(R.id.saveEditedPortfolio)
        var newName: EditText = dialogPortfolio.findViewById(R.id.editedName)
        saveButton.setOnClickListener {
            runBlocking {
                launch {
                    repository.updatePortfolioName(portfolio.name, newName.text.toString())
                }
                runOnUiThread { dialogPortfolio.dismiss() }
            }
        }
        dialogPortfolio.show()
    }

    override fun onClickMove(portfolio: Portfolio) {
        val infoActivity = Intent(this@PortfolioActivity, ShareActivity::class.java)
        infoActivity.putExtra("portfolio", portfolio.name)
        startActivity(infoActivity)
    }

    override fun onClickDel(portfolio: Portfolio) {
        showDialogEdit(portfolio)
    }
}