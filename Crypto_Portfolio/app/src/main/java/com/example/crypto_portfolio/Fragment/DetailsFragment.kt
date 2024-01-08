package com.example.crypto_portfolio.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.withStarted
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.crypto_portfolio.Model.CryptoCurrency
import com.example.crypto_portfolio.R
import com.example.crypto_portfolio.databinding.FragmentDetailsBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DetailsFragment : Fragment() {


    lateinit var  binding : FragmentDetailsBinding

    private  val item : DetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentDetailsBinding.inflate(layoutInflater)


        val data : CryptoCurrency = item.data!!

        setupDetails(data)
        loadchat(data)

        setButtonOnclick(data)

        addToWatchList(data)




        return binding.root
    }
    var watchlist : ArrayList<String>? = null
    var watchlistIsChecked = false

    private fun addToWatchList(data: CryptoCurrency) {
       readData()
        watchlistIsChecked = if (watchlist!!.contains(data.symbol)){
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
            true
        }else{
                binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
                false
        }

        binding.addWatchlistButton.setOnClickListener {
            watchlistIsChecked = if (!watchlistIsChecked){
                if (!watchlist!!.contains(data.symbol)){
                    watchlist!!.add(data.symbol)
                }
                storeData()
                binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
                true
            }else{
                binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
                watchlist!!.remove(data.symbol)
                storeData()
                false
            }
        }
    }

    private  fun storeData(){
        val sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(watchlist)
        editor.putString("watchlist",json)
        editor.apply()
    }

    private fun readData() {
        val sharedPreferences = requireContext()
            .getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val  gson = Gson()
        val json = sharedPreferences.getString("watchlist",ArrayList<String>().toString())

        val type = object : TypeToken<ArrayList<String>>(){}.type
        watchlist = gson.fromJson(json,type)
    }

    private fun setButtonOnclick(item: CryptoCurrency) {

        val onemonth = binding.button
        val oneweek = binding.button1
        val oneday = binding.button2
        val fourHour = binding.button3
        val oneHour = binding.button4
        val fiftenMin = binding.button5


        val clickListener = View.OnClickListener {

            when(it.id){
                fiftenMin.id -> loadchatData(it,"15",item,oneday,onemonth,oneweek,oneHour,fourHour)
                oneHour.id -> loadchatData(it,"1h",item,oneday,onemonth,oneweek,fiftenMin,fourHour)
                fourHour.id -> loadchatData(it,"4h",item,oneday,onemonth,oneweek,oneHour,fiftenMin)
                oneday.id -> loadchatData(it,"D",item,fiftenMin,onemonth,oneweek,oneHour,fourHour)
                oneweek.id -> loadchatData(it,"W",item,oneday,onemonth,fiftenMin,oneHour,fourHour)
                onemonth.id -> loadchatData(it,"M",item,oneday,fiftenMin,oneweek,oneHour,fourHour)
            }
        }
        fiftenMin.setOnClickListener(clickListener)
        fourHour.setOnClickListener(clickListener)
        oneHour.setOnClickListener(clickListener)
        oneday.setOnClickListener(clickListener)
        oneweek.setOnClickListener(clickListener)
        onemonth.setOnClickListener(clickListener)

    }



    private fun loadchatData(
        it: View?,
        s: String,
        item: CryptoCurrency,
        oneday: AppCompatButton,
        onemonth: AppCompatButton,
        oneweek: AppCompatButton,
        oneHour: AppCompatButton,
        fourHour: AppCompatButton
    ) {
        disableButton(oneday,onemonth,oneweek,fourHour,oneHour)
        it!!.setBackgroundResource(R.drawable.active_button)
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detailChangeTextView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)

        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol" + item.symbol
                .toString() + "USD&interval="+s+"&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg="+
                    "F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features="+
                    "[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"





        )

    }

    private fun disableButton(
        oneday: AppCompatButton,
        onemonth: AppCompatButton,
        oneweek: AppCompatButton,
        fourHour: AppCompatButton,
        oneHour: AppCompatButton,
    ) {
        oneday.background = null
        onemonth.background = null
        oneweek.background = null
        fourHour.background = null
        oneHour.background = null



    }


    private fun loadchat(item: CryptoCurrency) {

        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detailChangeTextView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)

        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol" + item.symbol
                .toString() + "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies="+
                    "[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides="+
                    "{}&overrides={}&enabled_features=[]&disabled_features="+
                    "[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"

        )

    }

    private fun setupDetails(data: CryptoCurrency) {
        binding.detailSymbolTextView.text = data.symbol


        Glide.with(requireContext()).load( "https://s2.coinmarketcap.com/static/img/coins/64x64/"+data.id+".png"
        ).thumbnail(Glide.with(requireContext()).load(R.drawable.spinner))
            .into(binding.detailImageView)


        binding.detailPriceTextView.text =
            "${String.format("$%.4f",data.quotes[0].price)}"

        if (data.quotes!![0].percentChange24h>0){
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.green))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
            binding.detailChangeTextView.text="+${String.format("%.02f",data.quotes[0].percentChange24h)}%"
        }else{
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.red))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)
            binding.detailChangeTextView.text="${String.format("%.02f",data.quotes[0].percentChange24h)}%"
        }
    }


}