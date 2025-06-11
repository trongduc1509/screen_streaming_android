package com.duczxje.domain.entity

data class FrameEntity(
    val payload: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FrameEntity

        return payload.contentEquals(other.payload)
    }

    override fun hashCode(): Int {
        return payload.contentHashCode()
    }
}