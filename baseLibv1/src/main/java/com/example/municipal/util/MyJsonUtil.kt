package com.example.municipal.util

import com.example.municipal.model.RootResponse
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

object MyJsonUtil {
    fun <T> covertObjListToStrArray(list: List<T>): Array<String?>? {
        val gson = Gson()
        val strings = arrayOfNulls<String>(list.size)
        for (i in list.indices) {
            strings[i] = gson.toJson(list[i])
        }
        return strings
    }
    fun getStatusJsonObject(keys: Array<String?>): JsonObject {
        val `object` = JsonObject()
        val jsonArray = JsonArray()
        for (k in keys) {
            jsonArray.add(k)
        }
        `object`.add("status", jsonArray)
        return `object`
    }
    fun <T> covertStrArrToObjList(strings: Array<String?>, className: T): List<T>? {
        val gson = Gson()
        val list: MutableList<T> = ArrayList()
        for (string in strings) {
            list.add(gson.fromJson(string, className as Type))
        }
        return list
    }

    fun <T> getPojoFromStr(className: T, str: String?): Any? {
        return Gson().fromJson(str, className as Type)
    }
    fun <T> getStrFromPojo(className: T): String? {
        return Gson().toJson( className )
    }
    fun covertStringArrToList(jsonStringArray: String):List<String> {
        val converter = Gson()
        val type = object : TypeToken<List<String?>?>() {}.type
        return converter.fromJson(jsonStringArray, type)
    }
    fun covertStringListToStringJsonArr(jsonStringArray: List<String>):String {
        val converter = Gson()
        return converter.toJson(jsonStringArray)
    }

    fun gsonObjToJSONObj_convertor(jsonObject: JsonObject): JSONObject? {
        try {
            return JSONObject(jsonObject.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }

    fun <T> getPojoFromJsonObj(className: T, jsonObject: JsonObject): T {
        return Gson().fromJson(jsonObject.toString(), className as Type)
    }
    fun <T> getJsonFromPojoObj(className: T): String {
        return Gson().toJson(className)
    }

    fun <T> getPojoFromJsonObj(jsonArray: JsonArray): List<T>? {
        return Gson().fromJson(jsonArray.toString(), object : TypeToken<List<T>?>() {}.type)
    }

    fun isResponseSuccess(jsonObject: JsonObject): Boolean {
        return if (jsonObject.has("response") && jsonObject.getAsJsonObject("response") != null) {
            jsonObject.getAsJsonObject("response")
                .getAsJsonObject("data") != null && jsonObject.getAsJsonObject("response")["statusCode"].asInt == 200
        } else false
    }
    fun <T> getDataPojoFromDataJson(jsonObject: JsonElement, className: T): Any? {
        return try {
                if (!jsonObject.isJsonNull) {
                    return if (jsonObject is JsonObject) {
                        getPojoFromJsonObj(className, jsonObject.getAsJsonObject())
                    } else {
                        getPojoFromJsonArr(jsonObject.asJsonArray, className)
                    }
                }
                null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun <T> getDataPojoFromRes(jsonObject: JsonObject, className: T): Any? {
        return try {
            if (jsonObject.has("response") && jsonObject.getAsJsonObject("response") != null) {
                val j2 = jsonObject.getAsJsonObject("response")["data"]
                if (!j2.isJsonNull) {
                    return if (j2 is JsonObject) {
                        getPojoFromJsonObj(className, j2.getAsJsonObject())
                    } else {
                        getPojoFromJsonArr(j2.asJsonArray, className)
                    }
                }
                null
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun <T> getPojoFromJsonArr(jsonArray: JsonArray, neededClass: T): Any? {
        val listType = TypeToken.getParameterized(ArrayList::class.java, neededClass as Type).type
        return Gson().fromJson(jsonArray.toString(), listType)
    }

    fun getErrorBodyMessage(str: String?): String? {
        if (isJSONValid(str)) {
            val jsonObject = Gson().fromJson(str, JsonObject::class.java)
            if (jsonObject.has("response") && jsonObject.getAsJsonObject("response") != null) {
                return jsonObject.getAsJsonObject("response")["message"].asString
            }
        } else {
            return str
        }
        return "Error : Unknown"
    }
    fun <T> getPojoFromData(jsonObject: JsonElement, className: T, key: String?): Any? {
        return try {
            if (!jsonObject.isJsonNull ) {
//                val j2 = jsonObject.asJsonObject[key]
//                if (!j2.isJsonNull) {
                    return if (jsonObject is JsonObject) {
                        getPojoFromJsonObj(className, jsonObject.getAsJsonObject())
                    } else {
                        getPojoFromJsonArr(jsonObject.asJsonArray, className)
                    }
//                }
            } else {
                null
            }
        } catch (e: java.lang.Exception) {
            null
        }
    }

    fun isJSONValid(test: String?): Boolean {
        try {
            JSONObject(test)
        } catch (ex: JSONException) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                JSONArray(test)
            } catch (ex1: JSONException) {
                return false
            }
        }
        return true
    }

    fun getActiveStatusJsonObject(): JsonObject {
        val jsonObject = JsonObject()
        val jsonArray = JsonArray()
        jsonArray.add("Active")
        jsonObject.add("status", jsonArray)
        return jsonObject
    }
    fun getStatusJsonObject(str: String): JsonObject {
        val jsonObject = JsonObject()
        val jsonArray = JsonArray()
        jsonArray.add(str)
        jsonObject.add("status", jsonArray)
        return jsonObject
    }
    fun getStatusJsonObject(str: List<String>): JsonObject {
        val jsonObject = JsonObject()
        val jsonArray = JsonArray()
        for (s in str) {
            jsonArray.add(s)
        }
        jsonObject.add("status", jsonArray)
        return jsonObject
    }
    fun makeOnePremInArr(jsonObject: JsonObject,key:String,value: String):JsonObject {
        val jsonArray = JsonArray()
        if(value.isNotEmpty()) {
            jsonArray.add(value)
        }
        jsonObject.add(key, jsonArray)
        return jsonObject
    }
    fun addKeyAndValuesInJsonObj(
        `object`: JsonObject,
        key: String?,
        values: Array<String?>
    ): JsonObject? {
        val jsonArray = JsonArray()
        for (k in values) {
            jsonArray.add(k)
        }
        `object`.add(key, jsonArray)
        return `object`
    }


    fun <T> getStringFromPojo(className: T): String? {
        return Gson().toJson(className)
    }

    fun getSingleFileUrlFromStringRes(s: String?): String? {
//        Type listType = TypeToken.getParameterized(, (Type) object).getType();
//        String s=new Gson().toJson(object,listType);
        val jsonObject = JsonParser.parseString(s) as JsonObject
        val dataJson = jsonObject.getAsJsonObject("response").getAsJsonObject("data")
        return if (dataJson != null) {
            dataJson["uri"].asString
        } else ""
    }
    fun getSingleFileUrlFromRootResponse(rootResponse: RootResponse): String? {
        val strr=Gson().toJson(rootResponse)
        val jsonObject = JsonParser.parseString(strr) as JsonObject
        val dataJson = jsonObject.getAsJsonObject("response").getAsJsonObject("data")
        return if (dataJson != null) {
            dataJson["uri"].asString
        } else ""
    }
}