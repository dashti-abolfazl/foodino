package com.example

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.*
import java.io.File
import java.io.IOException

// کلاس سفارشی برای ارسال فایل همراه با گزارش پیشرفت
class ProgressRequestBody(
    private val file: File,                 // فایلی که باید آپلود شود
    private val contentType: String,        // نوع محتوای فایل (مثلاً image/jpeg)
    private val callback: UploadCallbacks   // اینترفیس برای گزارش پیشرفت یا خطا
) : RequestBody() {

    // اینترفیس برای اطلاع‌رسانی وضعیت آپلود
    interface UploadCallbacks {
        fun onProgressUpdate(percentage: Int) // وقتی درصد آپلود تغییر می‌کند
        fun onError()                         // وقتی آپلود با خطا مواجه می‌شود
        fun onFinish()                        // وقتی آپلود به پایان می‌رسد
    }

    // متد مشخص‌کننده نوع محتوای فایل (MimeType)
    override fun contentType(): MediaType? {
        return contentType.toMediaTypeOrNull()
    }

    // متد مشخص‌کننده طول کلی فایل (بر حسب بایت)
    override fun contentLength(): Long {
        return file.length()
    }

    // متد اصلی که هنگام ارسال فایل فراخوانی می‌شود
    override fun writeTo(sink: BufferedSink) {
        val length = file.length()                    // اندازه کل فایل
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)   // بافر موقتی برای خواندن فایل تکه‌تکه
        val input = file.inputStream()                // باز کردن فایل برای خواندن
        var uploaded: Long = 0                        // تعداد بایت‌هایی که تاکنون ارسال شده‌اند

        try {
            var read: Int
            // تا زمانی که فایل به‌طور کامل خوانده نشده
            while (input.read(buffer).also { read = it } != -1) {
                // محاسبه درصد پیشرفت
                callback.onProgressUpdate((100 * uploaded / length).toInt())

                uploaded += read                          // افزایش مقدار ارسال‌شده
                sink.write(buffer, 0, read)               // نوشتن داده‌ها در خروجی برای ارسال
            }
        } catch (e: IOException) {
            callback.onError()                            // در صورت بروز خطا، اطلاع دادن به کاربر
        } finally {
            input.close()                                 // بستن فایل
        }

        callback.onFinish()                               // پایان موفقیت‌آمیز آپلود
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048      // اندازه هر تکه‌ای که از فایل می‌خوانیم
    }
}
