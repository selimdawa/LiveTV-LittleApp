package com.littleapp.livetv.Model

import java.io.Serializable

data class Category(
    var id: Int = 0,
    var name: String? = null,
    var imageUrl: String? = null
) : Serializable