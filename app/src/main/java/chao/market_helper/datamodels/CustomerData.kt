package chao.market_helper.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import chao.market_helper.utils.DateTimeUtils

@Entity
data class CustomerData(
    @PrimaryKey val customerId: String,
    var soldDataList: ArrayList<SoldItem>?,
    var memo: String,
    var discount: Int = 0,
    var total: Int = 0,
    var marketId: Int

) {
    companion object {
        fun createNewCustomer(marketId: Int): CustomerData {
            return CustomerData(DateTimeUtils.getCustomerId(), arrayListOf(), "", 0, 0, marketId)
        }
    }

    data class SoldItem(
        val name: String,
        val price: String,
        var count: Int,
        var optionCategory: ArrayList<OptionCategory> = arrayListOf()
    )
}