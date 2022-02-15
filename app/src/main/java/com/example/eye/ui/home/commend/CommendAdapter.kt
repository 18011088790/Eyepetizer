package com.example.eye.ui.home.commend

import android.app.Activity
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.BuildConfig
import com.example.eye.Const
import com.example.eye.R
import com.example.eye.event.RefreshEvent
import com.example.eye.event.SwitchPagesEvent
import com.example.eye.extension.*
import com.example.eye.logic.model.HomePageRecommend
import com.example.eye.ui.common.holder.*
import com.example.eye.ui.community.CommunityFragment
import com.example.eye.ui.home.daily.DailyAdapter
import com.example.eye.ui.login.LoginActivity
import com.example.eye.ui.newdetail.NewDetailActivity
import com.example.eye.util.ActionUrlUtil
import com.example.eye.util.GlobalUtil
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import org.greenrobot.eventbus.EventBus

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 11:11
 *
 * @Description:
 *
 */
class CommendAdapter(val fragment: CommendFragment, val dataList: List<HomePageRecommend.Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = dataList.size

    override fun getItemViewType(position: Int) =
        RecyclerViewHelp.getItemViewType(dataList[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecyclerViewHelp.getViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataList[position]

        when (holder) {
            is TextCardViewHeader5ViewHolder -> {
                holder.tvTitle5.text = item.data.text
                if (item.data.actionUrl != null) holder.ivInto5.visible() else holder.ivInto5.gone()
                if (item.data.follow != null) holder.tvFollow.visible() else holder.tvFollow.gone()
                holder.tvFollow.setOnClickListener { start<LoginActivity>(fragment.activity) }
                setOnClickListener(holder.tvTitle5,
                    holder.ivInto5) {
                    ActionUrlUtil.process(fragment,
                        item.data.actionUrl,
                        item.data.text)
                }
            }
            is TextCardViewHeader7ViewHolder -> {
                holder.tvTitle7.text = item.data.text
                holder.tvRightText7.text = item.data.rightText
                setOnClickListener(holder.tvRightText7, holder.ivInto7) {
                    ActionUrlUtil.process(fragment,
                        item.data.actionUrl,
                        "${item.data.text},${item.data.rightText}")
                }
            }
            is TextCardViewHeader8ViewHolder -> {
                holder.tvTitle8.text = item.data.text
                holder.tvRightText8.text = item.data.rightText
                setOnClickListener(holder.tvRightText8, holder.ivInto8) {
                    ActionUrlUtil.process(fragment,
                        item.data.actionUrl,
                        item.data.text)
                }
            }
            is TextCardViewFooter2ViewHolder -> {
                holder.tvFooterRightText2.text = item.data.text
                setOnClickListener(holder.tvFooterRightText2,
                    holder.ivTooterInto2) {
                    ActionUrlUtil.process(fragment,
                        item.data.actionUrl,
                        item.data.text)
                }
            }
            is TextCardViewFooter3ViewHolder -> {
                holder.tvFooterRightText3.text = item.data.text
                setOnClickListener(holder.tvRefresh,
                    holder.tvFooterRightText3,
                    holder.ivTooterInto3) {
                    if (this == holder.tvRefresh) {
                        "${holder.tvRefresh.text},${GlobalUtil.getString(R.string.currently_not_supported)}".showToast()
                    } else if (this == holder.tvFooterRightText3 || this == holder.ivTooterInto3) {
                        ActionUrlUtil.process(fragment, item.data.actionUrl, item.data.text)
                    }
                }
            }
            is FollowCardViewHolder -> {
                holder.ivVideo.load(item.data.content.data.cover.feed, 4f)
                holder.ivAvatar.load(item.data.header.icon)
                holder.tvVideoDuration.text =
                    item.data.content.data.duration.conversionVideoDuration()
                holder.tvDescription.text = item.data.header.description
                holder.tvTitle.text = item.data.header.title
                if (item.data.content.data.ad) holder.tvLabel.visible() else holder.tvLabel.gone()
                if (item.data.content.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) holder.ivChoiceness.visible() else holder.ivChoiceness.gone()
                holder.ivShare.setOnClickListener {
                    showDialogShare(fragment.activity,
                        "${item.data.content.data.title}：${item.data.content.data.webUrl.raw}")
                }
                holder.itemView.setOnClickListener {
                    item.data.content.data.run {
                        if (ad || author == null) {
                            NewDetailActivity.start(fragment.activity, id)
                        } else {
                            NewDetailActivity.start(
                                fragment.activity,
                                NewDetailActivity.VideoInfo(id,
                                    playUrl,
                                    title,
                                    description,
                                    category,
                                    library,
                                    consumption,
                                    cover,
                                    author,
                                    webUrl)
                            )
                        }
                    }
                }
            }
            is BannerViewHolder -> {
                holder.ivPicture.load(item.data.image, 4f)
                holder.itemView.setOnClickListener {
                    ActionUrlUtil.process(fragment,
                        item.data.actionUrl,
                        item.data.title)
                }
            }
            is Banner3ViewHolder -> {
                holder.ivPicture.load(item.data.image, 4f)
                holder.ivAvatar.load(item.data.header.icon)
                holder.tvTitle.text = item.data.header.title
                holder.tvDescription.text = item.data.header.description
                if (item.data.label?.text.isNullOrEmpty()) holder.tvLabel.invisible() else holder.tvLabel.visible()
                holder.tvLabel.text = item.data.label?.text ?: ""
                holder.itemView.setOnClickListener {
                    ActionUrlUtil.process(fragment,
                        item.data.actionUrl,
                        item.data.header.title)
                }
            }
            is InformationCardFollowCardViewHolder -> {
                holder.ivCover.load(item.data.backgroundImage,
                    4f,
                    RoundedCornersTransformation.CornerType.TOP)
                holder.recyclerView.setHasFixedSize(true)
                if (holder.recyclerView.itemDecorationCount == 0) {
                    holder.recyclerView.addItemDecoration(InformationCardFollowCardItemDecoration())
                }
                holder.recyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
                holder.recyclerView.adapter = InformationCardFollowCardAdapter(fragment.activity,
                    item.data.actionUrl,
                    item.data.titleList)
                holder.itemView.setOnClickListener {
                    ActionUrlUtil.process(fragment,
                        item.data.actionUrl)
                }
            }
            is VideoSmallCardViewHolder -> {
                holder.ivPicture.load(item.data.cover.feed, 4f)
                holder.tvDescription.text =
                    if (item.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) "#${item.data.category} / 开眼精选" else "#${item.data.category}"
                holder.tvTitle.text = item.data.title
                holder.tvVideoDuration.text = item.data.duration.conversionVideoDuration()
                holder.ivShare.setOnClickListener {
                    showDialogShare(fragment.activity,
                        "${item.data.title}：${item.data.webUrl.raw}")
                }
                holder.itemView.setOnClickListener {
                    item.data.run {
                        NewDetailActivity.start(
                            fragment.activity,
                            NewDetailActivity.VideoInfo(id,
                                playUrl,
                                title,
                                description,
                                category,
                                library,
                                consumption,
                                cover,
                                author,
                                webUrl)
                        )
                    }
                }
            }
            is UgcSelectedCardCollectionViewHolder -> {
                holder.tvTitle.text = item.data.header.title
                holder.tvRightText.text = item.data.header.rightText
                holder.tvRightText.setOnClickListener {
                    EventBus.getDefault()
                        .post(SwitchPagesEvent(com.example.eye.ui.community.commend.CommendFragment::class.java))
                    EventBus.getDefault().post(RefreshEvent(CommunityFragment::class.java))
                }
                item.data.itemList.forEachIndexed { index, it ->
                    when (index) {
                        0 -> {
                            holder.ivCoverLeft.load(it.data.url,
                                4f,
                                RoundedCornersTransformation.CornerType.LEFT)
                            if (!it.data.urls.isNullOrEmpty() && it.data.urls.size > 1) holder.ivLayersLeft.visible()
                            holder.ivAvatarLeft.load(it.data.userCover)
                            holder.tvNicknameLeft.text = it.data.nickname
                        }
                        1 -> {
                            holder.ivCoverRightTop.load(it.data.url,
                                4f,
                                RoundedCornersTransformation.CornerType.TOP_RIGHT)
                            if (!it.data.urls.isNullOrEmpty() && it.data.urls.size > 1) holder.ivLayersRightTop.visible()
                            holder.ivAvatarRightTop.load(it.data.userCover)
                            holder.tvNicknameRightTop.text = it.data.nickname
                        }
                        2 -> {
                            holder.ivCoverRightBottom.load(it.data.url,
                                4f,
                                RoundedCornersTransformation.CornerType.BOTTOM_RIGHT)
                            if (!it.data.urls.isNullOrEmpty() && it.data.urls.size > 1) holder.ivLayersRightBottom.visible()
                            holder.ivAvatarRightBottom.load(it.data.userCover)
                            holder.tvNicknameRightBottom.text = it.data.nickname
                        }
                    }
                }
                holder.itemView.setOnClickListener { R.string.currently_not_supported.showToast() }
            }
            is TagBriefCardViewHolder -> {
                holder.ivPicture.load(item.data.icon, 4f)
                holder.tvDescription.text = item.data.description
                holder.tvTitle.text = item.data.title
                if (item.data.follow != null) holder.tvFollow.visible() else holder.tvFollow.gone()
                holder.tvFollow.setOnClickListener { start<LoginActivity>(fragment.activity) }
                holder.itemView.setOnClickListener { "${item.data.title},${GlobalUtil.getString(R.string.currently_not_supported)}".showToast() }
            }
            is TopicBriefCardViewHolder -> {
                holder.ivPicture.load(item.data.icon, 4f)
                holder.tvDescription.text = item.data.description
                holder.tvTitle.text = item.data.title
                holder.itemView.setOnClickListener { "${item.data.title},${GlobalUtil.getString(R.string.currently_not_supported)}".showToast() }
            }
            is AutoPlayVideoAdViewHolder -> {
                item.data.detail?.run {
                    holder.ivAvatar.load(item.data.detail.icon)
                    holder.tvTitle.text = item.data.detail.title
                    holder.tvDescription.text = item.data.detail.description
                    startAutoPlay(fragment.activity,
                        holder.videoPlayer,
                        position,
                        url,
                        imageUrl,
                        TAG,
                        object : GSYSampleCallBack() {
                            override fun onPrepared(url: String?, vararg objects: Any?) {
                                super.onPrepared(url, *objects)
                                GSYVideoManager.instance().isNeedMute = true
                            }

                            override fun onClickBlank(url: String?, vararg objects: Any?) {
                                super.onClickBlank(url, *objects)
                                ActionUrlUtil.process(fragment, item.data.detail.actionUrl)
                            }
                        })
                    setOnClickListener(holder.videoPlayer.thumbImageView, holder.itemView) {
                        ActionUrlUtil.process(fragment, item.data.detail.actionUrl)
                    }
                }
            }
            else -> {
                holder.itemView.gone()
                if (BuildConfig.DEBUG) "${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}".showToast()
            }
        }
    }

    class InformationCardFollowCardAdapter(
        val activity: Activity,
        val actionUrl: String?,
        val dataList: List<String>,
    ) :
        RecyclerView.Adapter<InformationCardFollowCardAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNews = view.findViewById<TextView>(R.id.tvNews)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): InformationCardFollowCardAdapter.ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_information_card_type_item, parent, false))
        }

        override fun getItemCount() = dataList.size

        override fun onBindViewHolder(
            holder: InformationCardFollowCardAdapter.ViewHolder,
            position: Int,
        ) {
            val item = dataList[position]
            holder.tvNews.text = item
            holder.itemView.setOnClickListener { ActionUrlUtil.process(activity, actionUrl) }
        }
    }

    class InformationCardFollowCardItemDecoration : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State,
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val count = parent.adapter?.itemCount //item count
            count?.run {
                when (position) {
                    0 -> {
                        outRect.top = dp2px(18f)
                    }
                    count.minus(1) -> {
                        outRect.top = dp2px(13f)
                        outRect.bottom = dp2px(18f)
                    }
                    else -> {
                        outRect.top = dp2px(13f)
                    }
                }
            }
        }
    }


    companion object {
        const val TAG = "CommendAdapter"

        fun startAutoPlay(
            activity: Activity,
            player: GSYVideoPlayer,
            position: Int,
            playUrl: String,
            coverUrl: String,
            playTag: String,
            callBack: GSYSampleCallBack? = null,
        ) {
            player.run {
                //防止错位设置
                setPlayTag(playTag)
                //设置播放位置防止错位
                setPlayPosition(position)
                //音频焦点冲突时是否释放
                setReleaseWhenLossAudio(false)
                //设置循环播放
                setLooping(true)
                //增加封面
                val cover = ImageView(activity)
                cover.scaleType = ImageView.ScaleType.CENTER_CROP
                cover.load(coverUrl, 4f)
                cover.parent?.run { removeView(cover) }
                setThumbImageView(cover)
                //设置播放过程中的回调
                setVideoAllCallBack(callBack)
                //设置播放URL
                setUp(playUrl, false, null)
            }
        }
    }
}