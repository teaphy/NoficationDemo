package com.example.tiany.noficationdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat

import kotlinx.android.synthetic.main.activity_main.*
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.widget.Toast
import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity() {

    // 设置通知ID
    private var notificationId = 1
    // 更新次数
    private var updateCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 只有在Android O以及上版本，设置通知频道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 用户可见的频道名称
            // 当系统区域设置更改时，可以通过监听Intent.ACTION_LOCALE_CHANGED广播来重命名此频道。
            val name = "tea"
            // 用户可见的频道描述
            val description = "This is a test channel"
            // 它表示了该频道的重要性级别，控制发布到此频道的中断通知的方式
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            // 创建NotificationChannel实例
            val channel = NotificationChannel(channelId, name, importance)
            channel.run{
                // 设置用户可见的频道描述
                setDescription(description)
                // 是否在在桌面icon右上角展示小圆点
                enableLights(true)
                // 小圆点颜色
                lightColor = Color.RED
                // 是否在久按桌面图标时显示此渠道信息
                setShowBadge(true)
            }

            val notificationManager = getSystemService(
                    NOTIFICATION_SERVICE) as NotificationManager
            // 向系统注册当前应用的通知渠道
            notificationManager.createNotificationChannel(channel)
        }

        setClickListener()
    }

    private fun setClickListener() {
        btnCustom.setOnClickListener({
            // 创建普通的通知
            // 在Android O以后，通知必须设置 channelId
            val nb: NotificationCompat.Builder = NotificationCompat.Builder(this).apply{
                // 设置小图标
                setSmallIcon(R.drawable.ic_fg)
                // 设置标题
                setContentTitle("Custom Notification - id = $notificationId")
                // 设置内容
                setContentText("This is just a demo for the custom notification!")
                // 设置优先级
                // 优先级决定了Android 7.1及更低版本的通知
                priority = NotificationCompat.PRIORITY_HIGH
                // 设置频道id
                setChannelId(channelId)

                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                // 参数1:上下文Context
                // 参数2: 发送者私有请求码
                // 参数3: intent对象
                // 参数4: 必须为FLAG_ONE_SHOT,FLAG_NO_CREATE,FLAG_CANCEL_CURRENT,FLAG_UPDATE_CURRENT中的一个i哦
                val pendingIntent = PendingIntent.getActivity(this@MainActivity, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                // 当用户直接从通知面板清除通知时，广播传递的PendingIntent
                // 例如： 当用户点击通知栏中的“全部清除”按钮或单独的“删除”按钮时，会发送PendingIntent。
                // 也就是说，通过用户的操作，不管是点击还是手动删除时都会发送此PendingIntent。
                // 但是，在NotificationManager调用cancel()方法清除通知时，将不会发送此PendingIntent
                setDeleteIntent(pendingIntent)
                // 设置点击通知时，广播的PendingIntent
                // 果您没有提供意图，现在可以通过调用
                // RemoteViews.setOnClickPendingIntentRemoteViews.setOnClickPendingIntent（int，PendingIntent）}
                // 设置单击事件，PendingIntents添加到单个视图中以启动。
                setContentIntent(pendingIntent)
                // 在用户点击时，是否自动消失
                // 如果通过setContentIntent方法设置PendingIntent,当通知在取消时广播PendingIntent
                // 如果未通过setContentIntent设在PendingIntent，此设置将无效
                // 这里官方文档有误
                setAutoCancel(true)
            }

            // 显示通知
            val mNotificationManager = NotificationManagerCompat.from(this)
            // id 用于以后更新或者删除通知
            mNotificationManager.notify(notificationId, nb.build())

            notificationId++
        })

        btnUpdate.setOnClickListener({
            // 创建普通的通知
            // 在Android O以后，通知必须设置 channelId
            val nb: NotificationCompat.Builder = NotificationCompat.Builder(this).apply{
                // 设置小图标
                setSmallIcon(R.drawable.ic_fg)
                // 设置标题
                setContentTitle("Update Notification - $updateCount")
                // 设置内容
                setContentText("This is just a demo for the update notification!")
                // 设置优先级
                // 优先级决定了Android 7.1及更低版本的通知
                priority = NotificationCompat.PRIORITY_HIGH
                setChannelId(channelId)
            }

            // 显示通知
            val mNotificationManager = NotificationManagerCompat.from(this)
            // id 用于以后更新或者删除通知
            mNotificationManager.notify(notificationId, nb.build())

            updateCount++
        })

        btnCancelSpecify.setOnClickListener({
            val mNmc = NotificationManagerCompat.from(this)
            // 删除指定id的Notification，默认时是最后一个通知
            mNmc.cancel(notificationId)

            notificationId--
        })

        btnCancelAll.setOnClickListener({
            val mNmc = NotificationManagerCompat.from(this)
            // 删除删除所有的命令
            mNmc.cancelAll()

        })

        btnTimeout.setOnClickListener({
            // 创建普通的通知
            // 在Android O以后，通知必须设置 channelId
            val nb: NotificationCompat.Builder = NotificationCompat.Builder(this).apply{
                // 设置小图标
                setSmallIcon(R.drawable.ic_fg)
                // 设置标题
                setContentTitle("Custom Notification - id = $notificationId")
                // 设置内容
                setContentText("This is just a demo for the custom notification!")
                // 设置优先级
                // 优先级决定了Android 7.1及更低版本的通知
                priority = NotificationCompat.PRIORITY_HIGH
                // 设置频道id
                setChannelId(channelId)

                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                // 参数1:上下文Context
                // 参数2: 发送者私有请求码
                // 参数3: intent对象
                // 参数4: 必须为FLAG_ONE_SHOT,FLAG_NO_CREATE,FLAG_CANCEL_CURRENT,FLAG_UPDATE_CURRENT中的一个i哦
                val pendingIntent = PendingIntent.getActivity(this@MainActivity, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                // 当用户直接从通知面板清除通知时，广播传递的PendingIntent
                // 例如： 当用户点击通知栏中的“全部清除”按钮或单独的“删除”按钮时，会发送PendingIntent。
                // 也就是说，通过用户的操作，不管是点击还是手动删除时都会发送此PendingIntent。
                // 但是，在NotificationManager调用cancel()方法清除通知时，将不会发送此PendingIntent
                setDeleteIntent(pendingIntent)
                // 设置点击通知时，广播的PendingIntent
                // 果您没有提供意图，现在可以通过调用
                // RemoteViews.setOnClickPendingIntentRemoteViews.setOnClickPendingIntent（int，PendingIntent）}
                // 设置单击事件，PendingIntents添加到单个视图中以启动。
                setContentIntent(pendingIntent)
                // 在用户点击时，是否自动消失
                // 如果通过setContentIntent方法设置PendingIntent,当通知在取消时广播PendingIntent
                // 如果未通过setContentIntent设在PendingIntent，此设置将无效
                // 这里官方文档有误
                setAutoCancel(true)
                // 如果通知未取消，设置指定的时间后自动取消
                setTimeoutAfter(10000)
            }

            // 显示通知
            val mNotificationManager = NotificationManagerCompat.from(this)
            // id 用于以后更新或者删除通知
            mNotificationManager.notify(notificationId, nb.build())

            notificationId++
        })


        btnAction.setOnClickListener {
            // 创建普通的通知
            // 在Android O以后，通知必须设置 channelId
            val nb: NotificationCompat.Builder = NotificationCompat.Builder(this).apply{
                // 设置小图标
                setSmallIcon(R.drawable.ic_fg)
                // 设置标题
                setContentTitle("Custom Notification - id = $notificationId")
                // 设置内容
                setContentText("This is just a demo for the custom notification!")
                // 设置优先级
                // 优先级决定了Android 7.1及更低版本的通知
                priority = NotificationCompat.PRIORITY_HIGH
                // 设置频道id
                setChannelId(channelId)

                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                // 参数1:上下文Context
                // 参数2: 发送者私有请求码
                // 参数3: intent对象
                // 参数4: 必须为FLAG_ONE_SHOT,FLAG_NO_CREATE,FLAG_CANCEL_CURRENT,FLAG_UPDATE_CURRENT中的一个i哦
                val pendingIntent = PendingIntent.getActivity(this@MainActivity, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT)

                // 在用户点击时，是否自动消失
                // 如果通过setContentIntent方法设置PendingIntent,当通知在取消时广播PendingIntent
                // 如果未通过setContentIntent设在PendingIntent，此设置将无效
                // 这里官方文档有误
                setAutoCancel(true)
                // 添加动作按钮
                addAction(R.drawable.ic_fg, "NoNoNo", pendingIntent)
            }

            // 显示通知
            val mNotificationManager = NotificationManagerCompat.from(this)
            // id 用于以后更新或者删除通知
            mNotificationManager.notify(notificationId, nb.build())

            notificationId++
        }

        btnReply.setOnClickListener({
            startActivity<ReplyNotificationActivity>()
        })

        btnProgress.setOnClickListener{
            startActivity<ProgressNotificationActivity>()
        }
    }

}
