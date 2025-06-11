package com.duczxje.domain.entity

import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

enum class PeerRole(val value: Int) {
    SENDER(0),
    RECEIVER(1),
}

data class PeerEntity(
    val id: String,
    val role: PeerRole,
    val ip: String,
    val port: Int,
) {
    companion object {
        fun encode(data: PeerEntity): ByteArray {
            val output = ByteArrayOutputStream()
            val dataOutputStream = DataOutputStream(output)

            fun writeString(str: String) {
                val utf8 = str.toByteArray(Charsets.UTF_8)
                dataOutputStream.writeInt(utf8.size)
                dataOutputStream.write(utf8)
            }

            writeString(data.id)
            dataOutputStream.writeInt(data.role.value)
            writeString(data.ip)
            dataOutputStream.writeInt(data.port)

            return output.toByteArray()
        }
    }
}