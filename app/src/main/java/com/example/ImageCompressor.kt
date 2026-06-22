import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

object ImageCompressor {

    // تابعی برای فشرده‌سازی تصویر با ورودی مسیر فایل و تنظیمات دلخواه
    fun compressImage(
        filePath: String,        // مسیر فایل اصلی
        maxWidth: Int = 800,     // عرض نهایی (پیش‌فرض 800)
        maxHeight: Int = 800,    // ارتفاع نهایی (پیش‌فرض 800)
        quality: Int = 80        // کیفیت فشرده‌سازی (0 تا 100)
    ): File {

        // خواندن تصویر اصلی از روی دیسک
        val originalBitmap = BitmapFactory.decodeFile(filePath)

        // تغییر اندازه (اسکیل) تصویر به اندازه‌های دلخواه
        val scaledBitmap = Bitmap.createScaledBitmap(
            originalBitmap,
            maxWidth,
            maxHeight,
            true // فیلتر کردن تصویر برای حفظ کیفیت
        )

        // ساخت یک فایل موقت برای ذخیره تصویر فشرده‌شده
        val compressedFile = File.createTempFile("compressed_", ".jpg")

        // ایجاد خروجی برای نوشتن در فایل
        val outputStream = FileOutputStream(compressedFile)

        // فشرده‌سازی تصویر و نوشتن آن در فایل
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

        // پاک‌سازی بافر خروجی و بستن فایل
        outputStream.flush()
        outputStream.close()

        // برگرداندن فایل نهایی فشرده‌شده
        return compressedFile
    }
}

