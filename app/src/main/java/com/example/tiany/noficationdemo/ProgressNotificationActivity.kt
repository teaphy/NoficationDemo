package com.example.tiany.noficationdemo

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_progress_notification.*

class ProgressNotificationActivity : AppCompatActivity() {

    // 进度条的最大值为100
    private val PROGRESS_MAX = 100
    // 进度条的当前值，默认为0
    private var PROGRESS_CURRENT = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_notification)

        setListener()
    }

    private fun setListener() {
        // 确定的进度通知
        btnDeterminate.setOnClickListener {
            // 模拟下载
            Thread({
                while (true) {
                    // 随着操作进行，不断调用setProgress（max，progress，false）以更新获的取进度值并重新发出通知
                    displayProgressNotification("Picture Download",
                            "Download in progress", PROGRESS_MAX, PROGRESS_CURRENT, false)
                    if (PROGRESS_CURRENT == PROGRESS_MAX) {
                        // 操作完成后，更新通知文本以显示操作已完成
                        // 移除进度条，调用setProgress（0，0，false）
                        displayProgressNotification("Picture Download",
                                "下载成功！", 0, 0, false)
                        break
                    }
                    PROGRESS_CURRENT++
                    Thread.sleep(100)
                }
            }).start()
        }

        btnIndeterminate.setOnClickListener {
            val builder = NotificationCompat.Builder(this, channelId)
            builder.apply {
                setContentInfo("Picture Download")
                setContentText("This is a indeterminate progress")
                setSmallIcon(R.drawable.ic_fg)
                setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_fg))
                priority = NotificationCompat.PRIORITY_LOW
                // 设置当前进度
                // 第一个参数为进度条的最大值
                // 第二个参数是当前完成进度
                // 第三个参数表示是否为确定的进度条
                // indeterminate为false时进度为确定，即操作的进度可以估算
                // 反之，进度为不可确认的，即操作的进度不可以估算
                setProgress(0, 0, true)
            }

            val nmc = NotificationManagerCompat.from(this)
            nmc.notify(0, builder.build())
        }
    }

    private fun displayProgressNotification(title: String ,content: String,maxPro: Int,
                                            currentPro: Int, indeterminate: Boolean) {
        val builder = NotificationCompat.Builder(this, channelId)
        builder.apply {
            setContentInfo(title)
            setContentText(content)
            setSmallIcon(R.drawable.ic_fg)
            setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_fg))
            priority = NotificationCompat.PRIORITY_LOW
            // 设置当前进度
            // 第一个参数为进度条的最大值
            // 第二个参数是当前完成进度
            // 第三个参数表示是否为确定的进度条
            // indeterminate为false时进度为确定，即操作的进度可以估算
            // 反之，进度为不可确认的，即操作的进度不可以估算
            setProgress(maxPro, currentPro, indeterminate)
        }

        val nmc = NotificationManagerCompat.from(this)
        nmc.notify(0, builder.build())
    }
}
