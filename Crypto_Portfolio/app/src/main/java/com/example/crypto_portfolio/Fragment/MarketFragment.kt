package com.example.crypto_portfolio.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.crypto_portfolio.databinding.FragmentMarketBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


class MarketFragment : Fragment() {
    lateinit var  binding: FragmentMarketBinding

    private  lateinit var list: List<CryptoCurrency>

    private lateinit var apapter : MarketAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMarketBinding.inflate(layoutInflater)

        list= listOf()

        apapter= MarketAdapter(requireContext(), list , "market")
        binding.currencyRecyclerView.adapter=apapter
        lifecycleScope.launch (Dispatchers.IO){
            val  res = ApiUtlites.getInstance().create(ApiInterface::class.java).getMarketData()


            if(res.body()!= null){
                withContext(Dispatchers.Main){
                    list=res.body()!!.data.cryptoCurrencyList

                    apapter.updateData(list)
                    binding.spinKitView.visibility = GONE
                }


            }
        }



        searchCoin()


        return binding.root
    }
        lateinit var searchtext: String
    private fun searchCoin() {
        binding.searchEditText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
               searchtext = p0.toString().toLowerCase()

                updateRecyclerView()
            }


        })
    }

    private fun updateRecyclerView() {
        val data = ArrayList<CryptoCurrency>()

        for (item in list){
            val coinName = item.name.lowercase(Locale.getDefault())
            val coinSymbol = item.symbol.lowercase(Locale.getDefault())

            if (coinName.contains(searchtext)|| coinSymbol.contains(searchtext)){
                data.add(item)
            }
        }
        apapter.updateData(data)
    }

}