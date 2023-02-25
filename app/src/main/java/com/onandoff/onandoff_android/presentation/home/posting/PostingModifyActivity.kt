package com.onandoff.onandoff_android.presentation.home.posting

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.FeedData
import com.onandoff.onandoff_android.data.model.FeedReadData
import com.onandoff.onandoff_android.data.model.FeedResponse
import com.onandoff.onandoff_android.databinding.ActivityPostingModifyBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostingModifyActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostingModifyBinding
    private val feedInterface : FeedInterface? = RetrofitClient.getClient()?.create(FeedInterface::class.java)
    var categoryId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostingModifyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val feedId = intent.getIntExtra("feedId",-1)
        val profileId = intent.getIntExtra("profileId", -1)

        // 기존 게시물 내용 불러오기
        getPostingByFeedId(profileId,feedId)

        binding.btnPostingModify.setOnClickListener{
            // 게시물 수정
            modifyPosting(profileId)
            Log.d("profileId", "onCreate: -------------------$profileId-------------------")
        }
        binding.btnCamera.setOnClickListener{
            // 사진 추가
        }
        binding.btnBefore.setOnClickListener {
            // 이전으로
            val builder = AlertDialog.Builder(this)
            builder.setMessage("글 수정을 취소하시겠습니까?")
                .setPositiveButton("네") { _, _ -> finish()}
                .setNegativeButton("아니오") { _, _ ->}
            builder.show()
        }
        binding.btnCategory.setOnClickListener {
            // 카테고리 선택 fragment 띄우기
            val bottomPostingCategoryFragment = PostingCategoryFragment{
                when (it) {
                    0 -> binding.textCategory.text = "문화 및 예술"
                    1 -> binding.textCategory.text = "스포츠"
                    2 -> binding.textCategory.text = "자기계발"
                    3 -> binding.textCategory.text = "기타"
                }
                categoryId = it
            }
            bottomPostingCategoryFragment.show(supportFragmentManager,bottomPostingCategoryFragment.tag)
        }
    }

    private fun getPostingByFeedId(profileId: Int,feedId: Int) {
        val call = feedInterface?.readFeedResponse(feedId, profileId)
        call?.enqueue(object : Callback<FeedReadData> {
            override fun onResponse(call: Call<FeedReadData>, response: Response<FeedReadData>) {
                when(response.code()) {
                    200 -> {
                        Log.d("readFeed", "onResponse: Success + ${response.body().toString()}")
                        if(response.body()!=null) {
                            binding.textContent.setText(response.body()!!.feedContent)
                            binding.textHashtag.setText(response.body()!!.hashTagList.toString())
                        }
                    }
                }
            }

            override fun onFailure(call: Call<FeedReadData>, t: Throwable) {
                Log.d("TAG", "onFailure: addFeed error")
            }
        })
    }

    private fun modifyPosting(profileId: Int){
        val hashTag = binding.textHashtag.text.toString()
        val hashTagList = hashTag.split(" #", " ", "#")

        val content = binding.textContent.text.toString()
        val isSecret = when(binding.checkboxSecret.isChecked) {
            false -> "PUBLIC"
            true -> "PRIVATE"
        }

        val data = FeedData(profileId, categoryId, hashTagList, content, isSecret)

        val call = feedInterface?.updateFeedResponse(data)
        call?.enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                when(response.code()) {
                    200 -> {
                        Log.d("updateFeed", "onResponse: ${response.body().toString()}")
                    }
                }
            }

            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                Log.d("TAG", "onFailure: addFeed error")
            }
        })
    }
}