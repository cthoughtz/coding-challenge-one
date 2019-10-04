package com.stashinvest.stashchallenge.model

import com.google.gson.annotations.SerializedName

data class DisplaySize(
        @SerializedName("is_watermarked")
        val isWatermarked: Boolean,
        val name: String,
        val uri: String)
