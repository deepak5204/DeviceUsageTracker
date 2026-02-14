package com.example.deviceusagetracker.data.mapper

import com.example.deviceusagetracker.domain.model.AppCategory

object AppCategoryMapper {

    fun getCategory(packageName: String): AppCategory {
        return when (packageName) {

            // Social
            "com.instagram.android",
            "com.facebook.katana",
            "com.whatsapp",
            "com.snapchat.android" -> AppCategory.SOCIAL_MEDIA

            // Entertainment
            "com.google.android.youtube",
            "com.netflix.mediaclient",
            "in.startv.hotstar" -> AppCategory.ENTERTAINMENT

            // Education
            "com.google.android.apps.classroom",
            "org.khanacademy.android",
            "com.github.android" -> AppCategory.EDUCATION

            else -> AppCategory.OTHER
        }
    }
}
