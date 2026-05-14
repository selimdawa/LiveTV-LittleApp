package com.littleapp.livetv.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.littleapp.livetv.Model.Category
import com.littleapp.livetv.Unit.CLASS
import com.littleapp.livetv.Unit.VOID
import com.littleapp.livetv.databinding.ItemLiveTvCategoryBinding

class CategoryAdapter(private val context: Context, categoryList: List<Category?>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var categoryList: List<Category?>
    private var binding: ItemLiveTvCategoryBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemLiveTvCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = categoryList[position]!!.name
        VOID.Glide(context, categoryList[position]!!.image_url, holder.image)
        holder.itemView.setOnClickListener { v: View ->
            VOID.IntentExtraChannel(
                v.context, CLASS.LIVE_TV_CATEGORIES_DETAILS, "category", categoryList[position]
            )
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var title: TextView

        init {
            image = binding!!.image
            title = binding!!.name
        }
    }

    companion object {
        var categoryList: List<Category?>? = null
    }

    init {
        this.categoryList = categoryList
    }
}