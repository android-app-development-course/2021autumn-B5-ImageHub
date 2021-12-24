package com.hyosakura.imagehub.ui.screens

import com.hyosakura.imagehub.R

enum class Screen(
    val stringId: Int = R.string.picture,
    val iconId: Int = R.drawable.ic_outline_image_24,
    val spIconID: Int = R.drawable.ic_baseline_image_24
) {
    Main(
        stringId = R.string.picture,
        iconId = R.drawable.ic_outline_image_24,
        spIconID = R.drawable.ic_baseline_image_24
    ),
    Search(
        stringId = R.string.search,
        iconId = R.drawable.ic_outline_search_24,
        spIconID = R.drawable.ic_baseline_search_24
    ),
    Library(
        stringId = R.string.library,
        iconId = R.drawable.ic_outline_folder_24,
        spIconID = R.drawable.ic_baseline_folder_24
    ),
    SearchResults(),
    Folder(),
    Label(),
    LabelImage(),
    Tip(),
    Trash(),
    Detail();

    companion object {
        fun fromRoute(route: String?): Screen =
            when (route?.substringBefore("/")) {
                Library.name -> Library
                Main.name -> Main
                Search.name -> Search
                SearchResults.name -> SearchResults
                Folder.name -> Folder
                Label.name -> Label
                LabelImage.name -> LabelImage
                Tip.name -> Tip
                Trash.name -> Trash
                Detail.name -> Detail
                else -> Main
            }
    }
}