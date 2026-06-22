import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener

import com.bumptech.glide.request.target.Target
import com.example.foodino.Product
import com.example.foodino.R
import java.text.DecimalFormat

// آداپتر برای نمایش لیستی از محصولات در RecyclerView
class ProductAdapter(private val productList:List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // ViewHolder کلاس داخلی که اجزای XML مربوط به هر آیتم را نگه می‌دارد
    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.textViewName)           // متن نام محصول
        val price: TextView = itemView.findViewById(R.id.textViewPrice)         // متن قیمت محصول
        val image: ImageView = itemView.findViewById(R.id.imageViewProduct)     // تصویر محصول
    }

    // متد برای ساخت ViewHolder جدید با استفاده از layout آیتم محصول
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        // تبدیل فایل XML به یک View واقعی
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ghaleb_product, parent, false)
        return ProductViewHolder(view) // برمی‌گردونه ViewHolder جدیدی که شامل View مربوط به یک محصوله
    }

    // تعداد آیتم‌ها در لیست
    override fun getItemCount(): Int =
        productList.size // به RecyclerView میگه چند آیتم باید نشون بده

    // متد اتصال داده‌ها به ViewHolderها
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position] // گرفتن شیء محصول در موقعیت فعلی لیست

        // مقداردهی به فیلدهای متنی با داده‌های محصول
        holder.name.text = product.name


        // فرمت‌کردن عدد قیمت با جداکننده سه‌تایی (مثلاً: 25,000)
        holder.price.text = "قیمت: ${product.price} تومان"

        // گرفتن آدرس تصویر
        val imageUrl = product.imageUrl
        Log.d("IMAGE_URL", "Loading image from: $imageUrl") // نمایش URL در Logcat برای بررسی

        // استفاده از کتابخانه Glide برای بارگذاری تصویر
        Glide.with(holder.itemView.context)
            .load(imageUrl) // بارگذاری URL تصویر
            .listener(object : RequestListener<Drawable> {

                // در صورتی که بارگذاری تصویر با خطا مواجه بشه
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("GLIDE_ERROR", "تصویر بارگذاری نشد: ${e?.localizedMessage}", e)
                    return false // false یعنی Glide خودش خطای تصویری رو هندل کنه
                }

                // وقتی تصویر با موفقیت بارگذاری بشه
                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("GLIDE_SUCCESS", "تصویر با موفقیت بارگذاری شد")
                    return false // false یعنی Glide تصویر رو خودش نمایش بده
                }
            })
            .placeholder(R.drawable.ic_launcher_background) // تصویر پیش‌فرض هنگام بارگذاری
            .error(R.drawable.ic_launcher_foreground)       // تصویر جایگزین در صورت خطا
            .into(holder.image)                              // جایگذاری تصویر داخل ImageView
    }

}

//getItemCount: تعداد کل آیتم‌های لیست رو به RecyclerView اعلام می‌کنه.
//onBindViewHolder:: این متد برای هر آیتم فراخوانی می‌شه تا داده‌ها رو توی View مربوطه بنویسه (متن، تصویر، قیمت و ...).
//کتابخانه Glide:

//برای بارگذاری تصاویر از اینترنت به‌صورت بهینه استفاده می‌شه. قابلیت‌هایی مثل:
//    مدیریت خطا ، کش کردن تصویر،
//    listener برای مدیریت وضعیت بارگذاری