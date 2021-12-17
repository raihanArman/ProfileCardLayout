package com.raihanarman.profilecardlayout

data class UserProfile(
    val name: String,
    val status: Boolean,
    val drawable: Int
)

val userProfileList = arrayListOf(
    UserProfile("John Doe", true, R.drawable.ic_launcher_background),
    UserProfile("Anna", false, R.drawable.ic_launcher_background),
)