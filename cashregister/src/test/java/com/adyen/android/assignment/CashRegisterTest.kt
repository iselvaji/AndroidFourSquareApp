package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin
import com.adyen.android.assignment.money.MonetaryElement
import org.junit.Test

class CashRegisterTest {

    @Test
    fun testTransaction() {

        val change = Change()
        change.add(Bill.FIVE_HUNDRED_EURO, 1)
        val cashRegister = CashRegister(change)

        val amountPaid = Change()
        amountPaid.add(Bill.FIVE_HUNDRED_EURO, 1)


        val expectedResult = Change()
        expectedResult.add(Bill.TWO_HUNDRED_EURO, 1)
        expectedResult.add(Bill.ONE_HUNDRED_EURO, 1)
        expectedResult.add(Bill.FIFTY_EURO, 1)

        val actualResult = cashRegister.performTransaction(15000L, amountPaid)

        assert(actualResult == expectedResult)

    }

    @Test(expected = CashRegister.TransactionException::class)
    fun testTransactionExceptionWhenRegisterHasLessAmount() {

        val change = Change()
        change.add(Bill.ONE_HUNDRED_EURO, 1)
        val cashRegister = CashRegister(change)

        val amountPaid = Change()
        amountPaid.add(Bill.FIVE_HUNDRED_EURO, 1)

        cashRegister.performTransaction(15000L, amountPaid)
    }

    @Test
    fun testNoCashToReturn() {

        val change = Change()
        change.add(Bill.ONE_HUNDRED_EURO, 1)
        val cashRegister = CashRegister(change)

        val amountPaid = Change()
        amountPaid.add(Bill.ONE_HUNDRED_EURO, 1)

        val actualResult = cashRegister.performTransaction(10000L, amountPaid)

        val expectedResult = Change.none()
        assert(actualResult == expectedResult)
    }

    @Test
    fun testCoinsToReturn() {

        val change = Change()
        change.add(com.adyen.android.assignment.money.Coin.TWENTY_CENT, 1)
        val cashRegister = CashRegister(change)

        val amountPaid = Change()
        amountPaid.add(com.adyen.android.assignment.money.Coin.FIVE_CENT, 1)

        val actualResult = cashRegister.performTransaction(2, amountPaid)

        val expectedResult = Change()
        expectedResult.add(com.adyen.android.assignment.money.Coin.TWO_CENT, 1)
        expectedResult.add(com.adyen.android.assignment.money.Coin.ONE_CENT, 1)

        assert(actualResult == expectedResult)
    }

    @Test
    fun testBillAndCoinToReturn() {

        val change = Change()
        change.add(Bill.FIFTY_EURO, 1)
        change.add(com.adyen.android.assignment.money.Coin.FIFTY_CENT, 5)
        val cashRegister = CashRegister(change)

        val amountPaid = Change()
        amountPaid.add(Bill.TEN_EURO, 4)
        amountPaid.add(com.adyen.android.assignment.money.Coin.FIVE_CENT, 6)

        val actualResult = cashRegister.performTransaction(2015, amountPaid)

        val expectedResult = Change()
        expectedResult.add(Bill.TWENTY_EURO, 1)
        expectedResult.add(com.adyen.android.assignment.money.Coin.TEN_CENT, 1)
        expectedResult.add(com.adyen.android.assignment.money.Coin.FIVE_CENT, 1)

        assert(actualResult == expectedResult)
    }

    @Test(expected = CashRegister.TransactionException::class)
    fun testTransactionExceptionWhenRegisterHasNegativeAmount() {
        val change = Change()
        change.add(NegativeCoin.ZERO, 1)
        val cashRegister = CashRegister(change)

        val amountPaid = Change()
        amountPaid.add(Bill.FIVE_HUNDRED_EURO, 1)

        cashRegister.performTransaction(15000L, amountPaid)
    }

    @Test(expected = CashRegister.TransactionException::class)
    fun testTransactionExceptionWhenNoAmountPaid() {
        val change = Change()
        change.add(Bill.TWENTY_EURO, 1)
        val cashRegister = CashRegister(change)

        val amountPaid = Change()
        amountPaid.add(NegativeCoin.ZERO, 1)

        cashRegister.performTransaction(1500L, amountPaid)
    }

    enum class NegativeCoin(override val minorValue: Int) :
        MonetaryElement {
        ZERO(0) }
}
