package com.example.composenotesapp1.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.example.composenotesapp1.data.model.NoteModel
import com.google.gson.Gson

class NoteModelParamType : NavType<NoteModel>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): NoteModel? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): NoteModel {
        return Gson().fromJson(value, NoteModel::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: NoteModel) {
        bundle.putParcelable(key, value)
    }
}