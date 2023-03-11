package com.onandoff.onandoff_android.presentation.mypage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonElement
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.feed.MyFeedService
import com.onandoff.onandoff_android.data.api.user.ProfileInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.*
import com.onandoff.onandoff_android.databinding.FragmentMypageBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID
import com.onandoff.onandoff_android.util.SharePreference
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class MypageFragment: Fragment(){
    private lateinit var binding: FragmentMypageBinding
    private var writeList = ArrayList<MyPosting>()
    private val TAG = "Mypage"
    lateinit var profile:ProfileListResultResponse
    lateinit var mainActivity: MainActivity
    var feedList = ArrayList<FeedResponseData>()
    var currentDate:LocalDateTime = LocalDateTime.now()
    var parsingDate:String = currentDate.year.toString()+"년 "+currentDate.monthValue.toString()+"월"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)
        binding.tvMypageDate.text = parsingDate
        binding.ivMypageDateBack.setOnClickListener{
            currentDate = currentDate.minusMonths(1)
            parsingDate = currentDate.year.toString()+"년 "+currentDate.monthValue.toString()+"월"
            Log.d("time",parsingDate)
            binding.tvMypageDate.text = parsingDate
        }
        binding.ivMypageDateForward.setOnClickListener{
            currentDate =  currentDate.plusMonths(1)
            parsingDate = currentDate.year.toString()+"년 "+currentDate.monthValue.toString()+"월"
            Log.d("time",parsingDate)
            binding.tvMypageDate.text = parsingDate
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
//        setupListeners()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 2. Context를 액티비티로 형변환해서 할당
        mainActivity = context as MainActivity
    }
    private fun setupView(){

        getProfileData()
        getFeedData()

        binding.tvMypageEdit.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("nickName", profile.profileName)
            bundle.putString("personaName", profile.personaName)
            bundle.putString("profileImg", profile.profileImgUrl)
            bundle.putString("statusMsg", profile.statusMessage)
            val editFragemnt = ProfileEditFragment()
            editFragemnt.arguments = bundle
            mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_main,editFragemnt)
                .commit()
        }
    }
    fun getProfileData(){
        val profileService: ProfileInterface? = RetrofitClient.getClient()?.create(
            ProfileInterface::class.java)
        val profileId = prefs.getSharedPreference(SHARED_PREFERENCE_NAME_PROFILEID,0)
        //날을 어떻게 넣을지 고민해봐야될듯
        val call = profileService?.getMyProfile(profileId)
        call?.enqueue(object: Callback<getMyProfileResponse> {
            override fun onResponse(
                call: Call<getMyProfileResponse>,
                response: Response<getMyProfileResponse>
            ){
                Log.d(TAG,"api 호출")
                profile = response.body()?.result!!
                binding.profile = profile
                //마이페이지가 처음 splash 되었을때 오늘날에 해당하는
                //년, 월의 게시글 목록이 나옴


            }
            override fun onFailure(call: Call<getMyProfileResponse>, t: Throwable){

            }
        })
    }
    private fun getFeedData(){
        val myfeedService: MyFeedService? = RetrofitClient.getClient()?.create(
            MyFeedService::class.java)
        val profileId = prefs.getSharedPreference(SHARED_PREFERENCE_NAME_PROFILEID,0)
        var myfeedDate:String = "01"
        //날을 어떻게 넣을지 고민해봐야될듯
        if(currentDate.monthValue < 10){
            myfeedDate = "0"+currentDate.monthValue.toString()
        }else{
            myfeedDate = currentDate.monthValue.toString()
        }
        val call = myfeedService?.getMyFeed(profileId,profileId,currentDate.year, myfeedDate,1)
        call?.enqueue(object: Callback<getFeedResponeData> {
            override fun onResponse(
                call: Call<getFeedResponeData>,
                response: Response<getFeedResponeData>
            ){

                val tmpFeedList = response.body()?.result?.feedArray
                if (tmpFeedList != null) {
                    for(item in tmpFeedList){
                        feedList.apply{add(item)}
                    }
                }
                onInitRecyclerView()
            }
            override fun onFailure(call: Call<getFeedResponeData>, t: Throwable){

            }
        })
    }
    private fun onInitRecyclerView(){
        Log.d("feed","RecyclerView init")
        val mypageRVAdapter =MypageRVAdapter(feedList, mainActivity)
        binding.rvProfileList.adapter = mypageRVAdapter;
        binding.rvProfileList.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true);
        Log.d("feed","${feedList.size}")

    }
}
