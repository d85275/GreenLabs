package chao.greenlabs.utils

object InputChecker {

    fun validItem(name: String, price: String): Boolean {
        if (name.isEmpty() || price.isEmpty()) return false
        if (price.length >= 7) return false
        return true
    }

    fun validMarketItem(name: String, location: String, fee: String): Boolean {
        if (name.isEmpty() || location.isEmpty() || fee.isEmpty()) return false
        if (fee.length >= 7) return false
        return true
    }

    fun validInput(vararg input:String): Boolean {
        input.forEach {
            if (it.isEmpty()) return false
        }
        return true
    }
}