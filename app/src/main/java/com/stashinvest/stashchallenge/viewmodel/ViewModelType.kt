package com.stashinvest.stashchallenge.viewmodel

enum class ViewModelType {
    STASH_IMAGE;

    val id: Int
        get() = ordinal
}
