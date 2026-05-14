package com.littleapp.livetv.Unit

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.littleapp.livetv.Model.Category
import com.littleapp.livetv.R

object VOID {
    fun Intent1(context: Context, c: Class<*>?) {
        val intent = Intent(context, c)
        context.startActivity(intent)
    }

    fun IntentExtraChannel(context: Context, c: Class<*>?, key: String?, value: Category?) {
        val intent = Intent(context, c)
        intent.putExtra("categoryName", DATA.EMPTY)
        intent.putExtra(key, value)
        context.startActivity(intent)
    }

    fun Glide(context: Context?, Url: String?, Image: ImageView) {
        try {
            Glide.with(context!!).load(Url).placeholder(R.color.image_profile).into(Image)
        } catch (e: Exception) {
            Image.setImageResource(R.color.image_profile)
        }
    }
}