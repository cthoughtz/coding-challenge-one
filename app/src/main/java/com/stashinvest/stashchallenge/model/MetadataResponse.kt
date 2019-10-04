package com.stashinvest.stashchallenge.model

import com.google.gson.annotations.SerializedName

data class MetadataResponse(
        @SerializedName("images")
        val metadata: List<Metadata>)
