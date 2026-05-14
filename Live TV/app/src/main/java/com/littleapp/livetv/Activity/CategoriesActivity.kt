package com.littleapp.livetv.Activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.littleapp.livetv.Adapter.CategoryAdapter
import com.littleapp.livetv.Model.Category
import com.littleapp.livetv.Service.ChannelDataService
import com.littleapp.livetv.Service.ChannelDataService.OnDataResponse
import com.littleapp.livetv.R
import com.littleapp.livetv.Unit.DATA
import com.littleapp.livetv.Unit.THEME
import com.littleapp.livetv.databinding.ActivityLiveTvCategoriesBinding
import org.json.JSONException
import org.json.JSONObject

class CategoriesActivity : AppCompatActivity() {

    private var binding: ActivityLiveTvCategoriesBinding? = null
    var categoryAdapter: CategoryAdapter? = null
    var categoryList: MutableList<Category>? = null
    var dataService: ChannelDataService? = null
    var context: Context = this@CategoriesActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityLiveTvCategoriesBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.setText(R.string.categories)
        dataService = ChannelDataService(this)
        categoryList = ArrayList()
        categoryAdapter = CategoryAdapter(context, categoryList!!)
        binding!!.recyclerView.adapter = categoryAdapter

        binding!!.toolbar.back.setOnClickListener { onBackPressed() }
        dataService!!.getChannelData("http://" + DATA.IP_LIVE_TV + "/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&categories=all",
            object : OnDataResponse {
                override fun onResponse(response: JSONObject) {
                    for (i in 0 until response.length()) {
                        try {
                            val categoryData = response.getJSONObject(i.toString())
                            val category = Category(
                                categoryData.getInt("id"),
                                categoryData.getString("name"),
                                categoryData.getString("image_url")
                            )
                            categoryList!!.add(category)
                            categoryAdapter!!.notifyDataSetChanged()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onError(error: String?) {}
            })
    }
}