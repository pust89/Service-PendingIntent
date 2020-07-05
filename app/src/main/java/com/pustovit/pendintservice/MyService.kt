package com.pustovit.pendintservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class MyService : Service() {

    private val serviceJob: Job
    private val serviceCoroutineScope: CoroutineScope

    init {
        serviceJob = Job()
        serviceCoroutineScope = CoroutineScope(Dispatchers.Main + serviceJob)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand: called")
        readFlags(flags)
        serviceCoroutineScope.launch {
            if (intent != null) {
                val time: Int = intent.getIntExtra(PARAM_TIME, 1)
                val task: Int = intent.getIntExtra(PARAM_TASK, 0)

                serviceDoSomething(startId, time, task)

            }
        }

        return START_NOT_STICKY;
    }

    private fun readFlags(flags: Int) {
        if (flags and START_FLAG_REDELIVERY == START_FLAG_REDELIVERY)
            Log.d(TAG, "START_FLAG_REDELIVERY")
        if (flags and START_FLAG_RETRY == START_FLAG_RETRY)
            Log.d(TAG, "START_FLAG_RETRY")
    }

    private suspend fun serviceDoSomething(startId: Int, time: Int, task: Int) {
        withContext(Dispatchers.IO) {
            Log.i(TAG, "Work start: startId = $startId, task = $task, time = $time")
            try {
                val intent: Intent = Intent(BROADCAST_ACTION)

                // сообщаем о старте задачи
                intent.putExtra(PARAM_TASK, task);
                intent.putExtra(PARAM_STATUS, STATUS_START);
                sendBroadcast(intent);


                TimeUnit.SECONDS.sleep(time.toLong())


                // сообщаем об окончании задачи
                intent.putExtra(PARAM_STATUS, STATUS_FINISH)
                intent.putExtra(PARAM_RESULT, time * 100)
                sendBroadcast(intent)

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            val isStopped = stopSelfResult(startId)
            Log.i(TAG, "Work ends: startId= $startId , stopSelfResult($startId) = $isStopped");
        }
    }

    override fun onCreate() {
        Log.i(TAG, "onCreate: called")
        super.onCreate()
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy: called")
        super.onDestroy()
        serviceJob.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}