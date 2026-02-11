package com.example.deviceusagetracker.data.mapper

import com.example.deviceusagetracker.domain.model.AppCategory

object AppCategoryMapper {

    fun getCategory(packageName: String): AppCategory {
        return when (packageName) {

            // Social
            "com.instagram.android",
            "com.facebook.katana",
            "com.snapchat.android" -> AppCategory.SOCIAL

            // Entertainment
            "com.google.android.youtube",
            "com.netflix.mediaclient",
            "in.startv.hotstar" -> AppCategory.ENTERTAINMENT

            // Education
            "com.google.android.apps.classroom",
            "org.khanacademy.android" -> AppCategory.EDUCATION

            else -> AppCategory.OTHER
        }
    }
}
