package com.jtoru.project2.Model

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class ImageUploadInfo (
    var imageName: String? = "",
    var imageURL: String? = ""
)








