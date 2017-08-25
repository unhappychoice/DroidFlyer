package com.unhappychoice.droidflyer.presentation.view

import android.content.Context
import android.util.AttributeSet
import com.github.salomonbrys.kodein.instance
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import com.unhappychoice.droidflyer.extension.splitByComma
import com.unhappychoice.droidflyer.infrastructure.bitflyer.model.average
import com.unhappychoice.droidflyer.infrastructure.bitflyer.model.wholeSize
import com.unhappychoice.droidflyer.presentation.presenter.OrderPresenter
import com.unhappychoice.droidflyer.presentation.style.DefaultStyle
import com.unhappychoice.droidflyer.presentation.view.core.BaseView
import com.unhappychoice.norimaki.extension.subscribeNext
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.order_view.view.*

class OrderView(context: Context?, attr: AttributeSet?) : BaseView(context, attr) {
    val presenter: OrderPresenter by instance()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.takeView(this)

        setupStyle()

        presenter.currentPrice.asObservable()
            .subscribeNext { currentPrice.text = "${it.toLong().splitByComma()} JPY" }
            .addTo(bag)

        presenter.currentPrice.asObservable()
            .subscribeNext {
                val color = if (presenter.profit() >= 0L) DefaultStyle.greenColor else DefaultStyle.redColor
                profit.text = presenter.profit().splitByComma()
                profit.setTextColor(color)
                balance.text = "${(presenter.balance.value.toLong() + presenter.profit()).splitByComma()} JPY"
            }.addTo(bag)

        presenter.position.asObservable()
            .subscribeNext {
                when(Math.abs(it.wholeSize())) {
                    0.0 -> position.text = "No position"
                    else -> position.text = "${it.average().toLong().splitByComma()} JPY / ${it.wholeSize()} ɃFX"
                }
            }.addTo(bag)

        presenter.buyPrice.asObservable()
            .subscribeNext { buyPrice.text = it.splitByComma() }
            .addTo(bag)

        presenter.sellPrice.asObservable()
            .subscribeNext { sellPrice.text = it.splitByComma() }
            .addTo(bag)

        presenter.amount.asObservable()
            .subscribeNext { amount.setText(it.toString()) }
            .addTo(bag)

        buyButton.clicks()
            .subscribeNext { presenter.buy() }
            .addTo(bag)

        sellButton.clicks()
            .subscribeNext { presenter.sell() }
            .addTo(bag)

        clearButton.clicks()
            .subscribeNext { presenter.clearPosition() }
            .addTo(bag)

        dotOneButton.clicks()
            .subscribeNext { presenter.size.value = 0.1 }
            .addTo(bag)

        dotFiveButton.clicks()
            .subscribeNext { presenter.size.value = 0.5 }
            .addTo(bag)

        oneButton.clicks()
            .subscribeNext { presenter.size.value = 1.0 }
            .addTo(bag)

        fiveButton.clicks()
            .subscribeNext { presenter.size.value = 5.0 }
            .addTo(bag)

        tenButton.clicks()
            .subscribeNext { presenter.size.value = 10.0 }
            .addTo(bag)

        plusButton.clicks()
            .subscribeNext { presenter.increment() }
            .addTo(bag)

        minusButton.clicks()
            .subscribeNext { presenter.decrement() }
            .addTo(bag)

        amount.textChanges()
            .subscribeNext { presenter.amount.setWithoutEvent(it.toString().toDouble()) }
            .addTo(bag)
    }

    override fun onDetachedFromWindow() {
        presenter.dropView(this)
        super.onDetachedFromWindow()
    }

    private fun setupStyle() {
        header.setBackgroundColor(DefaultStyle.darkerPrimaryColor)
        currentPrice.setTextColor(DefaultStyle.accentColor)
        position.setTextColor(DefaultStyle.accentColor)
        balance.setTextColor(DefaultStyle.accentColor)

        dotOneButton.setTextColor(DefaultStyle.accentColor)
        dotFiveButton.setTextColor(DefaultStyle.accentColor)
        oneButton.setTextColor(DefaultStyle.accentColor)
        fiveButton.setTextColor(DefaultStyle.accentColor)
        tenButton.setTextColor(DefaultStyle.accentColor)

        amount.setTextColor(DefaultStyle.accentColor)
        amount.setTextColor(DefaultStyle.accentColor)
        plusButton.setBackgroundColor(DefaultStyle.primaryColor)
        plusButton.setTextColor(DefaultStyle.accentColor)
        minusButton.setBackgroundColor(DefaultStyle.primaryColor)
        minusButton.setTextColor(DefaultStyle.accentColor)

        buyPrice.setTextColor(DefaultStyle.darkerAccentColor)
        sellPrice.setTextColor(DefaultStyle.darkerAccentColor)
        buyButton.setBackgroundColor(DefaultStyle.buyColor)
        buyButtonText.setTextColor(DefaultStyle.accentColor)
        sellButton.setBackgroundColor(DefaultStyle.sellColor)
        sellButtonText.setTextColor(DefaultStyle.accentColor)
        clearButton.setBackgroundColor(DefaultStyle.darkerPrimaryColor)
        clearButton.setTextColor(DefaultStyle.accentColor)
    }
}
