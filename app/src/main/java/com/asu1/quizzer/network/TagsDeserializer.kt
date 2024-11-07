package com.asu1.quizzer.model

import android.util.Base64
import com.asu1.quizzer.util.Logger
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class UserInfoDeserializer: JsonDeserializer<UserInfo>{
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): UserInfo {
        val jsonObject = json.asJsonObject
        val nickname = jsonObject.get("Nickname").asString
        val tagsJsonArray = jsonObject.getAsJsonArray("Tags")
        val tagsList: Set<String> = tagsJsonArray.map { it.asString }.toSet()
        val agreed = jsonObject.get("Agreed").asBoolean
        return UserInfo(nickname, tagsList, agreed)
    }
}

class QuizCardListDeserializer : JsonDeserializer<List<QuizCard>> {
    private val quizCardDeserializer = QuizCardDeserializer()

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): List<QuizCard> {
        val jsonArray = json.asJsonArray
        return jsonArray.map { quizCardDeserializer.deserialize(it, QuizCard::class.java, context) }
    }
}
class QuizCardDeserializer : JsonDeserializer<QuizCard> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): QuizCard {
        val jsonObject = json.asJsonObject

        val id = jsonObject.get("id").asString
        val title = jsonObject.get("title").asString
        var tags = jsonObject.get("tags").asJsonArray.map { it.asString }
        val creator = jsonObject.get("creator").asString
        val imageString = jsonObject.get("image").asString
        val image = Base64.decode(imageString, Base64.DEFAULT)
        val imageOrNull = if (image.size < 5) null else image
        val count = jsonObject.get("count").asInt
        val description = if (jsonObject.has("description")) jsonObject.get("description").asString else ""

        return QuizCard(id, title, tags, creator, imageOrNull, count, description)
    }
}

class DeserializationException(message: String, cause: Throwable) : Exception(message, cause)