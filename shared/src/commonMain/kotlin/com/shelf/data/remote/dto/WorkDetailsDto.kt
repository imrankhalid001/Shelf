package com.shelf.data.remote.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class WorkDetailsDto(
    @SerialName("key")
    val key: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    @Serializable(with = DescriptionSerializer::class)
    val description: String? = null,
    @SerialName("subjects")
    val subjects: List<String> = emptyList(),
    @SerialName("covers")
    val covers: List<Long> = emptyList()
)

object DescriptionSerializer : KSerializer<String?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("DescriptionSerializer", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): String? {
        val jsonDecoder = decoder as? JsonDecoder ?: return null
        val element = jsonDecoder.decodeJsonElement()
        return when (element) {
            is JsonPrimitive -> element.content
            is JsonObject -> element["value"]?.jsonPrimitive?.content
            else -> null
        }
    }

    override fun serialize(encoder: Encoder, value: String?) {
        if (value != null) encoder.encodeString(value)
    }
}
