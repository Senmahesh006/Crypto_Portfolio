package com.example.crypto_portfolio.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.crypto_portfolio.API.ApiInterface
import com.example.crypto_portfolio.API.ApiUtlites
import com.example.crypto_portfolio.Adapter.MarketAdapter
import com.example.crypto_portfolio.Model.CryptoCurrency
import com.example.crypto_portfolio.R
import com.example.crypto_portfolio.databinding.FragmentDetailsBinding
import com.example.crypto_portfolio.databinding.FragmentWatchListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WatchListFragment : Fragment() {

    lateinit var  binding : FragmentWatchListBinding

    private lateinit var watchlist :ArrayList<String>

    private lateinit var WatchlistItem : ArrayList<CryptoCurrency>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWatchListBinding.inflate(layoutInflater)

        readData()

        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtlites.getInstance().create(ApiInterface::class.java)
                .getMarketData()
            if (res.body() != null ){
                withContext(Dispatchers.Main){
                    WatchlistItem = ArrayList()
                    WatchlistItem.clear()


                    for (watchData in watchlist){
                        for (item in res.body()!!.data.cryptoCurrencyList){
                            if (watchData == item.symbol){
                                WatchlistItem.add(item)
                            }
                        }
                    }

                    binding.spinKitView.visibility = GONE
                    binding.watchlistRecyclerView.adapter=
                        MarketAdapter(requireContext(),WatchlistItem,"watchlist")
                }
            }
        }

        return binding.root
    }

    private fun readData() {
        val sharedPreferences = requireContext()
            .getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val  gson = Gson()
        val json = sharedPreferences.getString("watchlist",ArrayList<String>().toString())

        val type = object : TypeToken<ArrayList<String>>(){}.type
        watchlist = gson.fromJson(json,type)
    }


}