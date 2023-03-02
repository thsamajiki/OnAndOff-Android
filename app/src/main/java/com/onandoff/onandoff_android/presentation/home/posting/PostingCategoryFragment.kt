package com.onandoff.onandoff_android.presentation.home.posting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.CategoryResponse
import com.onandoff.onandoff_android.databinding.FragmentPostingCategoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostingCategoryFragment(val itemClick: (Int) -> Unit) : BottomSheetDialogFragment(){
    private var _binding : FragmentPostingCategoryBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostingCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnPostingCategoryOut.setOnClickListener{
            // 창닫기
            dialog?.dismiss()
        }

        categoryAdapter = CategoryAdapter()
        binding.categoryList.layoutManager = LinearLayoutManager(context)
        binding.categoryList.adapter = categoryAdapter
        categoryAdapter.setItemClickListener(object : CategoryAdapter.OnItemClickListener {
            override fun onClick(v: View, categoryId: Int) {
                itemClick(categoryId)
                Log.d("categoryId", "onClick: $categoryId")
                dialog?.dismiss()
            }
        })
        getCategoryList()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }



    private fun getCategoryList() {
        val call = RetrofitClient.getClient()?.create(FeedInterface::class.java)?.getFeedCategoryResponse()
        call?.enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if (!response.body()?.result.isNullOrEmpty()){
                    categoryAdapter.setItems(response.body()!!.result)
                    categoryAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                //TODO("Not yet implemented")
            }
        })
    }
}