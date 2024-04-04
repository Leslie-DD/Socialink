package com.leslie.socialink.login.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.githang.statusbar.StatusBarCompat
import com.leslie.socialink.R
import com.leslie.socialink.launcher.MainActivity
import com.leslie.socialink.login.model.LoginAction
import com.leslie.socialink.login.model.LoginViewModel
import com.leslie.socialink.utils.BarUtils
import com.leslie.socialink.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var tvSign: TextView
    private lateinit var tvForget: TextView
    private lateinit var studentIDLogin: TextView
    private lateinit var etUser: EditText
    private lateinit var etPwd: EditText
    private lateinit var btLogin: Button

    private val viewModel: LoginViewModel by viewModels()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        BarUtils.setStatusBarLightMode(this, true)
        StatusBarCompat.setStatusBarColor(this, Color.WHITE)
        init()
        event()
    }

    private fun init() {
        tvSign = findViewById(R.id.tvSign)
        etUser = findViewById(R.id.etUser)
        etPwd = findViewById(R.id.etPwd)
        btLogin = findViewById(R.id.btLogin)
        tvForget = findViewById(R.id.tvForget)
        studentIDLogin = findViewById(R.id.studentidlogin)
        studentIDLogin.visibility = View.VISIBLE
    }

    private fun event() {
        btLogin.setOnClickListener {
            val phone = etUser.getText().toString()
            val pwd = etPwd.getText().toString()
            if (TextUtils.isEmpty(phone)) {
                Utils.toastShort(this@LoginActivity, "请先输入账号")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(pwd)) {
                Utils.toastShort(this@LoginActivity, "请先输入密码")
                return@setOnClickListener
            }
            viewModel.onAction(LoginAction.RequestLogin(phone, pwd))
        }
        tvSign.setOnClickListener { startActivity(Intent(this@LoginActivity, SignActivity::class.java)) }
        tvForget.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgetPwdActivity::class.java).putExtra("type", 1))
        }
        studentIDLogin.setOnClickListener { startActivity(Intent(this@LoginActivity, StudentIdLoginActivity::class.java)) }

        lifecycleScope.launch {
            viewModel.logoutDialog.collect {
                Utils.toastShort(this@LoginActivity, it.second)
            }
        }

        lifecycleScope.launch {
            viewModel.logoutResult.collect {
                Utils.toastShort(this@LoginActivity, "登录成功，开启你的 Socialink 之旅")
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}
