package chao.greenlabs.datamodels

data class CustomerData(
    val customerId: String?,
    val soldDataList: ArrayList<SoldData>?
) {
    companion object {
        fun createEmptyData(): CustomerData {
            return CustomerData(null, null)
        }
    }
}