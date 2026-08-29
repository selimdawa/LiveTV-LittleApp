package com.littleapp.livetv.Activity

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

    private var _binding: ActivityLiveTvCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
    private val categoryList = ArrayList<Category>()
    private var dataService: ChannelDataService? = null
    var context: Context = this@CategoriesActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityLiveTvCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.nameSpace.setText(R.string.categories)
        dataService = ChannelDataService(this)

        categoryAdapter = CategoryAdapter()
        binding.recyclerView.adapter = categoryAdapter

        binding.toolbar.back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        dataService?.getChannelData(
            "http://${DATA.IP_LIVE_TV}/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&categories=all",
            object : OnDataResponse {
                override fun onResponse(response: JSONObject) {
                    for (i in 0 until response.length()) {
                        try {
                            val categoryData = response.getJSONObject(i.toString())
                            val category = Category(
                                id = categoryData.getInt("id"),
                                name = categoryData.getString("name"),
                                imageUrl = categoryData.getString("image_url")
                            )
                            categoryList.add(category)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                    categoryAdapter.submitList(ArrayList(categoryList))
                }

                override fun onError(error: String?) {}
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}