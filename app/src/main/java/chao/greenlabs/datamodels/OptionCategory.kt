package chao.greenlabs.datamodels

data class OptionCategory(var title: String, val optionList: ArrayList<Option>)

data class Option(var title: String, var addPrice: String, var isSelected: Boolean = false)