package com.littleapp.livetv.Activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.littleapp.livetv.Adapter.ChannelAdapter
import com.littleapp.livetv.Model.Category
import com.littleapp.livetv.Model.Channel
import com.littleapp.livetv.Service.ChannelDataService
import com.littleapp.livetv.Service.ChannelDataService.OnDataResponse
import com.littleapp.livetv.Unit.DATA
import com.littleapp.livetv.Unit.THEME
import com.littleapp.livetv.databinding.ActivityLiveTvCategoryDetailsBinding
import org.json.JSONException
import org.json.JSONObject

class CategoryDetailsActivity : AppCompatActivity() {

    private var binding: ActivityLiveTvCategoryDetailsBinding? = null
    var adapter: ChannelAdapter? = null
    var channels: MutableList<Channel>? = null
    var dataService: ChannelDataService? = null
    var context: Context = this@CategoryDetailsActivity
    var categoryName: String? = null
    var category: Category? = null
    var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityLiveTvCategoryDetailsBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        channels = ArrayList()
        dataService = ChannelDataService(this)
        categoryName = intent.getStringExtra("categoryName")

        if (categoryName == null || categoryName == "") {
            category = intent.getSerializableExtra("category") as Category?
            binding!!.toolbar.nameSpace.text = category!!.name
            url =
                "http://" + DATA.IP_LIVE_TV + "/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&cat=" + category!!.name
        } else {
            binding!!.toolbar.nameSpace.text = categoryName
            url =
                "http://" + DATA.IP_LIVE_TV + "/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&cat=" + categoryName
        }
        adapter = ChannelAdapter(channels!!, "details")
        binding!!.recyclerView.adapter = adapter
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }
        dataService!!.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        val c = Channel()
                        c.id = channelData.getInt("id")
                        c.name = channelData.getString("name")
                        c.description = channelData.getString("description")
                        c.thumbnail = channelData.getString("thumbnail")
                        c.live_url = channelData.getString("live_url")
                        c.facebook = channelData.getString("facebook")
                        c.twitter = channelData.getString("twitter")
                        c.youtube = channelData.getString("youtube")
                        c.website = channelData.getString("website")
                        c.category = channelData.getString("category")
                        channels!!.add(c)
                        adapter!!.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onError(error: String?) {}
        })
    }
}