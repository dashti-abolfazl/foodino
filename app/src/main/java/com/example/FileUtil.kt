import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

object FileUtil {

    // تابعی که یک URI رو به یک فایل واقعی تبدیل می‌کنه و در حافظه موقت (cache) ذخیره می‌کنه
    fun uriToFile(context: Context, uri: Uri): File? {
        val contentResolver = context.contentResolver // برای دسترسی به فایل از طریق content provider
        // گرفتن نام فایل با استفاده از URI
        val fileName = getFileName(context, uri) ?: "temp_file_${UUID.randomUUID()}" // اگر نام فایل پیدا نشد، یک نام تصادفی براش می‌سازه
        val tempFile = File(context.cacheDir, fileName)       // ساختن فایل جدید در دایرکتوری cache اپلیکیشن

        try {
            // باز کردن input stream از URI و خواندن داده‌ها
            contentResolver.openInputStream(uri)?.use { inputStream ->
                // نوشتن داده‌ها در فایل موقت
                FileOutputStream(tempFile).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    // خواندن و نوشتن داده‌ها به صورت تکه‌ای تا پایان فایل
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }
            }
            return tempFile // اگر همه چیز خوب پیش بره، فایل نهایی رو برمی‌گردونه
        } catch (e: IOException) {
            e.printStackTrace() // اگر خطایی رخ بده، چاپش می‌کنه
        }

        return null  // در صورت بروز خطا، null برمی‌گردونه
    }
    // تابعی برای گرفتن نام واقعی فایل از URI
    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        // اگر URI از نوع content باشه، از contentResolver برای گرفتن نام استفاده می‌کنیم
        if (uri.scheme == "content") {
            val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    // گرفتن نام فایل از ستون DISPLAY_NAME
                    result = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        // اگر نام فایل از قسمت بالا بدست نیاد، از مسیر فایل در URI استفاده می‌کنیم
        if (result == null) {
            result = uri.path // گرفتن مسیر کامل فایل
            val cut = result?.lastIndexOf('/')   // پیدا کردن آخرین / برای جدا کردن نام فایل
            if (cut != null && cut != -1) {
                result = result?.substring(cut + 1) // جدا کردن فقط نام فایل
            }
        }
        return result // برگردوندن نام نهایی فایل
    }
}
