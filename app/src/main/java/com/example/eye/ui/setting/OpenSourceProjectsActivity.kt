package com.example.eye.ui.setting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eye.R
import com.example.eye.extension.dp2px
import com.example.eye.extension.inflate
import com.example.eye.ui.common.ui.BaseActivity
import com.example.eye.ui.common.ui.WebViewActivity
import com.example.eye.ui.common.view.SimpleDividerDecoration
import com.example.eye.util.GlobalUtil
import kotlinx.android.synthetic.main.activity_open_source_projects.*
import kotlinx.android.synthetic.main.layout_title__bar.*

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 22:24
 *
 * @Description:开源项目列表界面
 *
 */
class OpenSourceProjectsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_source_projects)
    }

    override fun setupViews() {
        super.setupViews()
        tvTitle.text = GlobalUtil.getString(R.string.open_source_project_list)
        val layoutManager = LinearLayoutManager(this)
        val adapter = OpenSourceProjectsAdapter(this, getProjectList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(SimpleDividerDecoration(this, dp2px(0.5f), ContextCompat.getColor(this, R.color.gray)))
    }

    private fun getProjectList() = ArrayList<OpenSourceProject>().apply {
        add(OpenSourceProject("Retrofit", "https://github.com/square/retrofit"))
        add(OpenSourceProject("Glide", "https://github.com/bumptech/glide"))
        add(OpenSourceProject("OkHttp", "https://github.com/square/okhttp"))
        add(OpenSourceProject("Gson", "https://github.com/google/gson"))
        add(OpenSourceProject("Glide Transformations", "https://github.com/wasabeef/glide-transformations"))
        add(OpenSourceProject("Eventbus", "https://github.com/greenrobot/EventBus"))
        add(OpenSourceProject("Permissionx", "https://github.com/guolindev/PermissionX"))
        add(OpenSourceProject("FlycoTabLayout", "https://github.com/H07000223/FlycoTabLayout"))
        add(OpenSourceProject("SmartRefreshLayout", "https://github.com/scwang90/SmartRefreshLayout"))
        add(OpenSourceProject("BannerViewPager", "https://github.com/zhpanvip/BannerViewPager"))
        add(OpenSourceProject("Immersionbar", "https://github.com/gyf-dev/ImmersionBar"))
        add(OpenSourceProject("PhotoView", "https://github.com/chrisbanes/PhotoView"))
        add(OpenSourceProject("Circleimageview", "https://github.com/hdodenhof/CircleImageView"))
        add(OpenSourceProject("GSYVideoPlayer", "https://github.com/CarGuo/GSYVideoPlayer"))
        add(OpenSourceProject("VasSonic", "https://github.com/Tencent/VasSonic"))
        add(OpenSourceProject("Leakcanary", "https://github.com/square/leakcanary"))
        add(OpenSourceProject("Kotlinx Coroutines", "https://github.com/Kotlin/kotlinx.coroutines"))
    }

    inner class OpenSourceProjectsAdapter(val activity: Activity, private val projectList: List<OpenSourceProject>) :
        RecyclerView.Adapter<OpenSourceProjectsAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = R.layout.item_open_source_project.inflate(parent)
            val holder = ViewHolder(view)
            holder.itemView.setOnClickListener {
                val position = holder.bindingAdapterPosition
                val project = projectList[position]
                WebViewActivity.start(activity, project.name, project.url, true)
            }
            holder.url.setTextIsSelectable(true)
            return holder
        }

        override fun getItemCount(): Int = projectList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = projectList[position]
            holder.name.text = item.name
            holder.url.text = item.url
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            var name: TextView = view.findViewById(R.id.name)

            var url: TextView = view.findViewById(R.id.url)

        }
    }

    inner class OpenSourceProject(val name: String, val url: String)

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, OpenSourceProjectsActivity::class.java)
            context.startActivity(intent)
        }

    }

}