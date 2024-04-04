package com.leslie.socialink.me.ui

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.leslie.socialink.R
import com.leslie.socialink.SocialinkApplication
import com.leslie.socialink.activity.mine.AttentionActivity
import com.leslie.socialink.activity.mine.AuthenticationActivity
import com.leslie.socialink.activity.mine.BaseInfoActivity
import com.leslie.socialink.activity.mine.FeedBackActivity
import com.leslie.socialink.activity.mine.MyCollectActivity
import com.leslie.socialink.activity.mine.MyFootprintActivity
import com.leslie.socialink.activity.mine.MyPullTheBlackActivity
import com.leslie.socialink.activity.mine.MyQuestionActivity1
import com.leslie.socialink.activity.mine.MySayActivity
import com.leslie.socialink.activity.mine.MyTeamActivity
import com.leslie.socialink.activity.mine.SettingActivity
import com.leslie.socialink.activity.oldsecond.MygoodActivity
import com.leslie.socialink.adapter.listview.ItemAdapter
import com.leslie.socialink.bean.ItemBean
import com.leslie.socialink.entity.RefUserInfo
import com.leslie.socialink.login.ui.ForgetPwdActivity
import com.leslie.socialink.login.ui.LoginActivity
import com.leslie.socialink.me.model.MeFragmentAction
import com.leslie.socialink.me.model.MeViewModel
import com.leslie.socialink.me.utils.ItemsUtil
import com.leslie.socialink.network.Constants
import com.leslie.socialink.network.entity.UserInfoBean
import com.leslie.socialink.utils.ImageUtils
import com.leslie.socialink.utils.Utils
import com.leslie.socialink.view.ArcImageView
import com.leslie.socialink.view.CircleView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class MeFragment : Fragment() {
    private lateinit var view: View
    private lateinit var ivBg: ArcImageView
    private lateinit var ivHead: CircleView
    private lateinit var headView: View
    private lateinit var tvName: TextView
    private lateinit var tvDetail: TextView
    private lateinit var tvLevel: TextView
    private lateinit var tvSchool: TextView
    private lateinit var xiangyuMoney: TextView
    private var certFlag = -1 //认证状态
    private lateinit var lv: ListView
    private var data: List<ItemBean>? = null
    private var adapter: ItemAdapter? = null
    private lateinit var ivEditor: ImageView
    private lateinit var current: TextView
    private lateinit var llSay: LinearLayout
    private lateinit var llQuestion: LinearLayout
    private lateinit var llNotice: LinearLayout
    private lateinit var llSecondhand: LinearLayout

    private var settingClub = 0
    private var settingAsk = 0

    private val viewModel: MeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG, "(onCreateView) uid = " + Constants.uid)
        view = inflater.inflate(R.layout.fragment_me, container, false)
        EventBus.getDefault().register(this)

        headView = LayoutInflater.from(context).inflate(R.layout.mehead, null)

        data = ItemsUtil.initItems()
        adapter = ItemAdapter(context, data)
        lv = view.findViewById(R.id.lv)
        lv.setAdapter(adapter)
        lv.addHeaderView(headView)

        xiangyuMoney = headView.findViewById(R.id.xiangyuMoney)
        llSay = headView.findViewById(R.id.llSay)
        llQuestion = headView.findViewById(R.id.llQuestion)
        llNotice = headView.findViewById(R.id.llnotice)
        llSecondhand = headView.findViewById(R.id.llSecondhand)
        current = headView.findViewById(R.id.current)
        ivEditor = headView.findViewById(R.id.ivEditor)
        ivEditor.visibility = View.INVISIBLE
        ivBg = headView.findViewById(R.id.ivBg)
        ivHead = headView.findViewById(R.id.ivHead)
        tvName = headView.findViewById(R.id.tvName)
        tvName.text = "点击头像登录"
        tvSchool = headView.findViewById(R.id.tvSchool)
        tvDetail = headView.findViewById(R.id.tvDetail)
        tvLevel = headView.findViewById(R.id.tvLevel)
        event()
        initCollect()
        return view
    }

    private fun initCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userInfoFlow
                .collect {
                    if (it == null) {
                        ivEditor.visibility = View.INVISIBLE
                        tvName.text = "点击头像登录"
//                        data = ItemsUtil.initItems()
//                        adapter?.setData(data)
//                        adapter?.notifyDataSetChanged()
//                        lvEventInit()
                        return@collect
                    }
                    ivEditor.visibility = View.VISIBLE
                    val userInfoValue: UserInfoBean = it
                    settingAsk = userInfoValue.settingAsk
                    settingClub = userInfoValue.getSettingClub()
                    if (userInfoValue.header != null && userInfoValue.header.isNotEmpty()) {
                        Glide.with(activity).load(Constants.BASE_URL + userInfoValue.header).asBitmap().into(target)
                        Glide.with(activity).load(Constants.BASE_URL + userInfoValue.header).asBitmap().into(ivHead)
                        ivBg.setBackgroundColor(Color.WHITE)
                    } else {
                        Glide.with(activity).load(R.mipmap.head2).asBitmap().into(ivHead)
                    }
                    tvName.text = userInfoValue.nickname
                    tvSchool.text = userInfoValue.getCollege()
                    tvDetail.text =
                        "经验值：" + (userInfoValue.experience - userInfoValue.totalExperience) + "/" + userInfoValue.needExperience
                    tvLevel.text = "LV" + userInfoValue.grade
                    certFlag = userInfoValue.certFlag
                    setStarLine(current, userInfoValue.needExperience, userInfoValue.totalExperience, userInfoValue.experience)
                    when (certFlag) {
                        0 -> data?.get(4)?.tip = "未认证"
                        1 -> data?.get(4)?.tip = "审核中"
                        2 -> data?.get(4)?.tip = "验证未通过"
                        3 -> data?.get(4)?.tip = "验证通过"
                    }
                    adapter?.notifyDataSetChanged()

                    xiangyuMoney.text = userInfoValue.money.toString()
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.logoutDialog.collect {
                Log.i(TAG, "collect logoutDialog")
                AlertDialog.Builder(requireContext())
                    .setTitle("退出登录")
                    .setMessage("确定退出当前账号吗")
                    .setNegativeButton("取消") { dialogInterface: DialogInterface, i1: Int ->
                        dialogInterface.dismiss() //销毁对话框
                    }
                    .setPositiveButton("确定") { dialog1: DialogInterface, which: Int ->
                        dialog1.dismiss()
                        viewModel.onAction(MeFragmentAction.Logout)
                    }
                    .create()
                    .show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.goToLogin.collect {
                SocialinkApplication.getInstance().finishAll()
                activity?.startActivity(Intent(context, LoginActivity::class.java))
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refUserInfo(refUserInfo: RefUserInfo?) {
        Log.d(TAG, "onReceive: RefUserInfo")
        viewModel.reFetchUserInfo()
    }

    private fun event() {
        ivEditor.setOnClickListener {
            viewModel.userInfoBean?.let { startActivity(Intent(activity, BaseInfoActivity::class.java).putExtra("userinfobean", it)) }
        }
        ivHead.setOnClickListener {
            viewModel.onAction(MeFragmentAction.HeaderClick)
        }
        llSay.setOnClickListener { startActivity(Intent(context, MySayActivity::class.java)) }
        llQuestion.setOnClickListener { startActivity(Intent(context, MyQuestionActivity1::class.java)) }
        llNotice.setOnClickListener { startActivity(Intent(context, AttentionActivity::class.java)) }
        llSecondhand.setOnClickListener { startActivity(Intent(context, MygoodActivity::class.java)) }
        lvEventInit()
    }

    private fun lvEventInit() {
        lv.onItemClickListener = AdapterView.OnItemClickListener { _, _, originPosition: Int, _ ->
            val realPosition = originPosition - 1
            when (realPosition) {
                0 -> startActivity(Intent(context, MyTeamActivity::class.java))
                1 -> startActivity(Intent(context, MyCollectActivity::class.java))
                2 -> startActivity(Intent(context, MyFootprintActivity::class.java))
                3 -> startActivity(Intent(context, MyPullTheBlackActivity::class.java))
                4 -> {
                    if (certFlag == -1) {
                        return@OnItemClickListener
                    }
                    startActivity(Intent(context, AuthenticationActivity::class.java).putExtra("certFlag", certFlag))
                }

                5 -> startActivity(Intent(context, FeedBackActivity::class.java))
                6 -> startActivity(Intent(context, ForgetPwdActivity::class.java))
                7 -> startActivity(
                    Intent(context, SettingActivity::class.java)
                        .putExtra("settingAsk", settingAsk)
                        .putExtra("settingClub", settingClub).apply {
                            viewModel.userInfoBean?.userLabels?.let { putExtra("userLabelsBeans", it) }
                        }
                )

                8 -> viewModel.onAction(MeFragmentAction.LoginOrOut)
            }
        }
    }

    // 动态设置Textview长度
    private fun setStarLine(tv: TextView, n: Int, t: Int, e: Int) {
        tv.post {
            val maxLength = Utils.dip2px(requireContext(), 130f)
            val lp = tv.layoutParams as RelativeLayout.LayoutParams
            val width = (maxLength * ((e - t) * 1.0f / n)).toInt()
            if (width in 1..9) {
                lp.width = width + 10
            } else {
                lp.width = width
            }
            tv.setLayoutParams(lp)
        }
    }

    private val target: SimpleTarget<Bitmap> = object : SimpleTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
            val newBitmap = ImageUtils.stackBlur(resource, 25)
            ivBg.setScaleType(ImageView.ScaleType.CENTER_CROP)
            ivBg.setImageBitmap(newBitmap)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    companion object {
        private const val TAG = "[MeFragment]"
    }
}
