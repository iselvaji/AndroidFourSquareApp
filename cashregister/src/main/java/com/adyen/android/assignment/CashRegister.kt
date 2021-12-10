package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin

/**
 * The CashRegister class holds the logic for performing transactions.
 *
 * @param change The change that the CashRegister is holding.
 */
class CashRegister(private val change: Change) {
    /**
     * Performs a transaction for a product/products with a certain price and a given amount.
     *
     * @param price The price of the product(s).
     * @param amountPaid The amount paid by the shopper.
     *
     * @return The change for the transaction.
     *
     * @throws TransactionException If the transaction cannot be performed.
     */
    @Throws(TransactionException::class)
    fun performTransaction(price: Long, amountPaid: Change): Change {

        println("performTransaction begins - price : $price amountPaid : ${amountPaid.total}")

        if(amountPaid.total <= 0L) {
            println("amountPaid cannot be zero or less..")
            throw TransactionException(message = "AmountPaid cannot have a negative cost")
        }

        if(price <= 0L) {
            println("Price cannot be zero or less..")
            throw TransactionException(message = "Price cannot have a negative cost")
        }

        var cashBack = amountPaid.total - price
        val changeToReturn = Change.none()

        val regIterator = change.getElements().iterator()
        while(regIterator.hasNext()) {
            val nextVal = regIterator.next()
            println("register has " + nextVal.toString() + " " + change.getCount(nextVal))
        }

        println("cashBack : $cashBack")

        if(cashBack > 0 && cashBack > change.total) {
            println("Register has less cash than required..")
            throw TransactionException(message = "Register has less cash than required")
        }

        while(cashBack > Coin.ONE_CENT.minorValue) {
            enumValues<Bill>().forEach {
                if (cashBack >= it.minorValue) {
                    // if(change.getCount(it) != 0)
                    changeToReturn.add(it, 1)
                    cashBack -= it.minorValue
                }
            }
            enumValues<Coin>().forEach {
                if (cashBack >= it.minorValue) {
                    changeToReturn.add(it, 1)
                    cashBack -= it.minorValue
                }
            }
        }

        val iterator = changeToReturn.getElements().iterator()

        var changeToReturnText = ""
        while(iterator.hasNext()) {
            val nextVal = iterator.next()
            changeToReturnText = changeToReturnText + nextVal.toString() + ":" + changeToReturn.getCount(nextVal) + " "
        }
        println("change to return - $changeToReturnText")

        return changeToReturn
    }

    class TransactionException(message: String, cause: Throwable? = null) : Exception(message, cause)
}
