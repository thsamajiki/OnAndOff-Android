package com.onandoff.onandoff_android.presentation.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.user.ProfileInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.ProfileRequest
import com.onandoff.onandoff_android.data.model.ProfileResponse
import com.onandoff.onandoff_android.databinding.ActivityProfileCreateBinding
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.Camera
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileCreateActivity:AppCompatActivity() {
    private lateinit var binding: ActivityProfileCreateBinding
    //권한 가져오기

    val isValid = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (checkPermission(Camera.STORAGE_PERMISSION, Camera.PERM_STORAGE)) {
            setViews()
        }
        binding.tvFinish.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            val personas = binding.etPersonas.text.toString()
            val statusmsg =binding.etOneline.text.toString()
            val userId = prefs.getSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_USERID,0)
            if(binding.etPersonas.length() !=0){
                binding.viewPersonas.setBackgroundColor(this.getColor(R.color.errorColor))
                binding.tvPersonasError.setBackgroundColor(this.getColor(R.color.errorColor))

            }else if(binding.etNickname.length() !=0){
                    binding.viewNickname.setBackgroundColor(this.getColor(R.color.errorColor))
                    binding.tvPersonasError.setBackgroundColor(this.getColor(R.color.errorColor))
            }
            else{
                val profileInterface: ProfileInterface? = RetrofitClient.getClient()?.create(ProfileInterface::class.java)
                val user = ProfileRequest(userId,nickname,personas,"fddf",statusmsg)
                val call = profileInterface?.profileCreate(user)
                call?.enqueue(object: Callback<ProfileResponse>{
                    override fun onResponse(
                        call: Call<ProfileResponse>,
                        response: Response<ProfileResponse>
                    ){

                    }
                    override fun onFailure(call: Call<ProfileResponse>, t: Throwable){

                    }
                })
            }
        }

    }

    private fun setViews() {
        //카메라 버튼을 클릭하면
        binding.ivAddBackground.setOnClickListener {
            //카메라 열기
            openCamera()
        }
    }

    private fun openCamera() {
        //카메라 권한 확인
        if (checkPermission(Camera.CAMERA_PERMISSION, Camera.PERM_CAMERA)) {
            //권한이 있으면 카메라를 실행시킵니다.
            val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, Camera.FLAG_REQ_CAMERA)
        }
    }

    //권한 체크 메소드
    fun checkPermission(permissions: Array<out String>, flag: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                //만약 권한이 승인되어 있지 않다면 권한승인 요청을 사용에 화면에 호출합니다.
                if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(this, permissions, flag)
                    return false
                }
            }
        }
        return true
    }

    //checkPermission() 에서 ActivityCompat.requestPermissions 을 호출한 다음 사용자가 권한 허용여부를 선택하면 해당 메소드로 값이 전달 됩니다.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Camera.PERM_STORAGE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "저장소 권한을 승인하지않으면 앱을 실행할수없습니다", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                        return
                    }
                }
                //카메라 호출 메소드
                setViews()
            }
            Camera.PERM_CAMERA -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "카메라 권한을 승인해야지만 카메라를 사용할 수 있습니다.", Toast.LENGTH_SHORT)
                            .show()
                        return
                    }
                }
                openCamera()
            }
        }
    }

    //startActivityForResult 을 사용한 다음 돌아오는 결과값을 해당 메소드로 호출합니다.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Camera.FLAG_REQ_CAMERA -> {
                    if (data?.extras?.get("data") != null) {
                        //카메라로 방금 촬영한 이미지를 미리 만들어 놓은 이미지뷰로 전달 합니다.
                        val bitmap = data?.extras?.get("data") as Bitmap
                        binding.ivProfileBackground.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }
}