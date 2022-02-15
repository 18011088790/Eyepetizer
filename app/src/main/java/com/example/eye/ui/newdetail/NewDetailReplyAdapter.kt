package com.example.eye.ui.newdetail

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.BuildConfig
import com.example.eye.Const
import com.example.eye.R
import com.example.eye.extension.*
import com.example.eye.logic.model.VideoReplies
import com.example.eye.ui.common.holder.RecyclerViewHelp
import com.example.eye.ui.common.holder.TextCardViewHeader4ViewHolder
import com.example.eye.util.DateUtil
import com.example.eye.util.GlobalUtil

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 15:07
 *
 * @Description:视频详情-评论列表绑定的适配器
 *
 */
class NewDetailReplyAdapter(val activity: NewDetailActivity, val dataList: List<VideoReplies.Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = dataList.size

    override fun getItemViewType(position: Int) = when {
        dataList[position].type == "reply" && dataList[position].data.dataType == "ReplyBeanForClient" -> REPLY_BEAN_FOR_CLIENT_TYPE
        else -> RecyclerViewHelp.getItemViewType(dataList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        REPLY_BEAN_FOR_CLIENT_TYPE -> ReplyViewHolder(R.layout.item_new_detail_reply_type.inflate(parent))
        else -> RecyclerViewHelp.getViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataList[position]
        when (holder) {
            is TextCardViewHeader4ViewHolder -> {
                holder.tvTitle4.text = item.data.text
                if (item.data.actionUrl != null && item.data.actionUrl.startsWith(Const.ActionUrl.REPLIES_HOT)) {
                    //热门评论
                    holder.ivInto4.visible()
                    holder.tvTitle4.layoutParams = (holder.tvTitle4.layoutParams as LinearLayout.LayoutParams).apply { setMargins(0, dp2px(30f), 0, dp2px(6f)) }
                    setOnClickListener(holder.tvTitle4, holder.ivInto4) { R.string.currently_not_supported.showToast() }
                } else {
                    //相关推荐、最新评论
                    holder.tvTitle4.layoutParams = (holder.tvTitle4.layoutParams as LinearLayout.LayoutParams).apply { setMargins(0, dp2px(24f), 0, dp2px(6f)) }
                    holder.ivInto4.gone()
                }
            }
            is ReplyViewHolder -> {
                holder.groupReply.gone()
                holder.ivAvatar.load(item.data.user?.avatar ?: "")
                holder.tvNickName.text = item.data.user?.nickname
                holder.tvLikeCount.text = if (item.data.likeCount == 0) "" else item.data.likeCount.toString()
                holder.tvMessage.text = item.data.message
                holder.tvTime.text = getTimeReply(item.data.createTime)
                holder.ivLike.setOnClickListener { R.string.currently_not_supported.showToast() }

                item.data.parentReply?.run {
                    holder.groupReply.visible()
                    holder.tvReplyUser.text = String.format(GlobalUtil.getString(R.string.reply_target), user?.nickname)
                    holder.ivReplyAvatar.load(user?.avatar ?: "")
                    holder.tvReplyNickName.text = user?.nickname
                    holder.tvReplyMessage.text = message
                    holder.tvShowConversation.setOnClickListener { R.string.currently_not_supported.showToast() }
                }
            }
            else -> {
                holder.itemView.gone()
                if (BuildConfig.DEBUG) "${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}".showToast()
            }
        }
    }

    private fun getTimeReply(dateMillis: Long): String {
        val currentMillis = System.currentTimeMillis()
        val timePast = currentMillis - dateMillis
        if (timePast > -DateUtil.MINUTE) { // 采用误差一分钟以内的算法，防止客户端和服务器时间不同步导致的显示问题
            when {
                timePast < DateUtil.DAY -> {
                    var pastHours = timePast / DateUtil.HOUR
                    if (pastHours <= 0) {
                        pastHours = 1
                    }
                    return DateUtil.getDate(dateMillis, "HH:mm")
                }
                else -> return DateUtil.getDate(dateMillis, "yyyy/MM/dd")
            }
        } else {
            return DateUtil.getDate(dateMillis, "yyyy/MM/dd HH:mm")
        }
    }

    inner class ReplyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        val tvNickName = view.findViewById<TextView>(R.id.tvNickName)
        val ivLike = view.findViewById<ImageView>(R.id.ivLike)
        val tvLikeCount = view.findViewById<TextView>(R.id.tvLikeCount)
        val tvMessage = view.findViewById<TextView>(R.id.tvMessage)
        val tvTime = view.findViewById<TextView>(R.id.tvTime)

        val groupReply = view.findViewById<Group>(R.id.groupReply)
        val tvReplyUser = view.findViewById<TextView>(R.id.tvReplyUser)
        val ivReplyAvatar = view.findViewById<ImageView>(R.id.ivReplyAvatar)
        val tvReplyNickName = view.findViewById<TextView>(R.id.tvReplyNickName)
        val tvReplyMessage = view.findViewById<TextView>(R.id.tvReplyMessage)
        val tvShowConversation = view.findViewById<TextView>(R.id.tvShowConversation)

    }

    companion object {
        const val TAG = "NewDetailReplyAdapter"
        const val REPLY_BEAN_FOR_CLIENT_TYPE = Const.ItemViewType.MAX
    }
}