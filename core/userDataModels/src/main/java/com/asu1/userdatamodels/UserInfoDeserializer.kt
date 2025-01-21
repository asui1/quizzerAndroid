package com.asu1.userdatamodels

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class UserInfoDeserializer: JsonDeserializer<UserInfo> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): UserInfo {
        val jsonObject = json.asJsonObject
        val nickname = jsonObject.get("Nickname").asString
        val tagsJsonArray = jsonObject.getAsJsonArray("Tags")
        val tagsList: Set<String> = tagsJsonArray.map { it.asString }.toSet()
        val agreed = jsonObject.get("Agreed").asBoolean
        return UserInfo(nickname, tagsList, agreed)
    }
}
