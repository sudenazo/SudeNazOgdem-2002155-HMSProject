package com.example.sudenaz2002155


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.huawei.agconnect.api.AGConnectApi
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectAuthCredential
import com.huawei.hms.support.hwid.ui.HuaweiIdAuthButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<HuaweiIdAuthButton>(R.id.btn_sign_in)
        button.setOnClickListener {
            signIn()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AGConnectApi.getInstance().activityLifecycle().onActivityResult(requestCode,resultCode,data)
    }

    fun signIn(){
        AGConnectAuth.getInstance().signIn(this, AGConnectAuthCredential.HMS_Provider).addOnSuccessListener {
            Toast.makeText(this,"GIRIS BASARILI",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }


}







