package com.jtoru.project2.Model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class EducationUploadInfo (
    var educationGrade: String? = "",
    var educationInfo: String? = ""
)