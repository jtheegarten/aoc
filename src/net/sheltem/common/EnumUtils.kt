package net.sheltem.common


enum class MathOperation(val sign: String) {
    ADD("+"),
    MULTIPLY("*"),
    SUBSTRACT("-"),
    DIVIDE("/");

    fun opposite() =
        when (this) {
            ADD -> SUBSTRACT
            MULTIPLY -> DIVIDE
            SUBSTRACT -> ADD
            DIVIDE -> MULTIPLY
        }

    companion object {
        fun fromSign(value: String) = entries.first { it.sign == value }
    }
}
