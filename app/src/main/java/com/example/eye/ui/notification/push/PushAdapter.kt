package com.example.eye.ui.notification.push

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eye.R
import com.example.eye.extension.inflate
import com.example.eye.extension.load
import com.example.eye.logic.model.PushMessage
import com.example.eye.util.ActionUrlUtil
import com.example.eye.util.DateUtil

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 20:39
 *
 * @Description:
 *
 */
class PushAdapter(val fragment: PushFragment, var dataList: List<PushMessage.Message>) :
    RecyclerView.Adapter<PushAdapter.ViewHolder>() {

    override fun getItemCount()=dataList.size

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvContent: TextView = view.findViewById(R.id.tvContent)
        val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder=ViewHolder(R.layout.item_notification_push.inflate(parent))
        holder.itemView.setOnClickListener {
            val item = dataList[holder.bindingAdapterPosition]
            ActionUrlUtil.process(fragment, item.actionUrl, item.title)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       dataList[position].run {
           holder.ivAvatar.load(icon){error(R.mipmap.ic_launcher)}
           holder.tvTitle.text=title
           holder.tvTime.text=DateUtil.getConvertedDate(date)
           holder.tvContent.text=content
       }
    }

}