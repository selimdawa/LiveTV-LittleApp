package com.littleapp.livetv.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.littleapp.livetv.Adapter.ChannelAdapter
import com.littleapp.livetv.Model.Channel
import com.littleapp.livetv.Service.ChannelDataService
import com.littleapp.livetv.Service.ChannelDataService.OnDataResponse
import com.littleapp.livetv.R
import com.littleapp.livetv.Unit.CLASS
import com.littleapp.livetv.Unit.DATA
import com.littleapp.livetv.Unit.THEME
import com.littleapp.livetv.Unit.VOID
import com.littleapp.livetv.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var bigSliderAdapter: ChannelAdapter
    private lateinit var newsChannelAdapter: ChannelAdapter
    private lateinit var sportsChannelAdapter: ChannelAdapter
    private lateinit var enterChannelAdapter: ChannelAdapter

    private val channelList = ArrayList<Channel>()
    private val newsChannels = ArrayList<Channel>()
    private val sportsChannel = ArrayList<Channel>()
    private val enterChannel = ArrayList<Channel>()

    private var service: ChannelDataService? = null
    var context: Context = this@MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.nameSpace.setText(R.string.live_tv)
        service = ChannelDataService(this)

        setupRecyclerViews()
        setupClickListeners()
        loadAllChannels()
    }

    private fun setupRecyclerViews() {
        binding.bigSliderList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        bigSliderAdapter = ChannelAdapter("slider")
        binding.bigSliderList.adapter = bigSliderAdapter

        binding.newsChannelList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        newsChannelAdapter = ChannelAdapter("details")
        binding.newsChannelList.adapter = newsChannelAdapter

        binding.sportsChannelList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        sportsChannelAdapter = ChannelAdapter("details")
        binding.sportsChannelList.adapter = sportsChannelAdapter

        binding.enterChannelList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        enterChannelAdapter = ChannelAdapter("details")
        binding.enterChannelList.adapter = enterChannelAdapter
    }

    private fun setupClickListeners() {
        binding.toolbar.categories.setOnClickListener {
            VOID.Intent1(context, CLASS.LIVE_TV_CATEGORIES)
        }
        binding.more.setOnClickListener { v: View ->
            startCategoryDetailActivity(v.context, "News")
        }
        binding.more2.setOnClickListener { v: View ->
            startCategoryDetailActivity(v.context, "Sports")
        }
        binding.more3.setOnClickListener { v: View ->
            startCategoryDetailActivity(v.context, "Entertainment")
        }
    }

    private fun startCategoryDetailActivity(ctx: Context, categoryName: String) {
        val i = Intent(ctx, CLASS.LIVE_TV_CATEGORIES_DETAILS)
        i.putExtra("categoryName", categoryName)
        ctx.startActivity(i)
    }

    private fun loadAllChannels() {
        getSliderData("http://${DATA.IP_LIVE_TV}/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&channels=all")
        getNewsChannels("http://${DATA.IP_LIVE_TV}/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&cat=News")
        getSportsChannel("http://${DATA.IP_LIVE_TV}/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&cat=Sports")
        getEnterChannel("http://${DATA.IP_LIVE_TV}/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&cat=Entertainment")
    }

    private fun parseChannel(channelData: JSONObject): Channel {
        return Channel(
            id = channelData.getInt("id"),
            name = channelData.getString("name"),
            description = channelData.getString("description"),
            thumbnail = channelData.getString("thumbnail"),
            liveUrl = channelData.getString("live_url"),
            facebook = channelData.getString("facebook"),
            twitter = channelData.getString("twitter"),
            youtube = channelData.getString("youtube"),
            website = channelData.getString("website"),
            category = channelData.getString("category")
        )
    }

    fun getSliderData(url: String?) {
        service?.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        channelList.add(parseChannel(channelData))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                bigSliderAdapter.submitList(ArrayList(channelList))
            }

            override fun onError(error: String?) {}
        })
    }

    fun getNewsChannels(url: String?) {
        service?.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        newsChannels.add(parseChannel(channelData))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                newsChannelAdapter.submitList(ArrayList(newsChannels))
            }

            override fun onError(error: String?) {}
        })
    }

    fun getSportsChannel(url: String?) {
        service?.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        sportsChannel.add(parseChannel(channelData))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                sportsChannelAdapter.submitList(ArrayList(sportsChannel))
            }

            override fun onError(error: String?) {}
        })
    }

    fun getEnterChannel(url: String?) {
        service?.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        enterChannel.add(parseChannel(channelData))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                enterChannelAdapter.submitList(ArrayList(enterChannel))
            }

            override fun onError(error: String?) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}