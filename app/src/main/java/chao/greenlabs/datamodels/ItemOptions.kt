package chao.greenlabs.datamodels

data class ItemOptions(val title: String, val optionList: ArrayList<Option>)

data class Option(val title: String, val addPrice: String, var isSelected: Boolean = false)