package com.example.foodino

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ProgressRequestBody
import com.example.foodino.databinding.ActivityAdminProductManagementBinding


import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AdminProductManagement : AppCompatActivity() {
    private lateinit var binding: ActivityAdminProductManagementBinding
    private var imageUri: Uri? = null
    private var imageFile: File? = null
    private lateinit var spinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAdminProductManagementBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {
            // دسترسی به Spinner
            spinner = findViewById(R.id.group_item)

            // ساخت Adapter از آرایه group_items
            val adapter = ArrayAdapter.createFromResource(
                this@AdminProductManagement,
                R.array.group_items,   // اینجا باید آرایه باشه نه آیدی ویو
                android.R.layout.simple_spinner_item
            )

            // تعیین استایل لیست بازشو
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // اتصال Adapter به Spinner
            spinner.adapter = adapter

            // گرفتن انتخاب کاربر
            spinner.setOnItemSelectedListener(object :
                android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedGroup = parent.getItemAtPosition(position).toString()
                    // اینجا می‌تونی انتخاب رو ذخیره کنی یا استفاده کنی
                }

                override fun onNothingSelected(parent: android.widget.AdapterView<*>) {
                    // وقتی چیزی انتخاب نشه
                }
            })
        }
        setupListeners()


    }


    private fun setupListeners() = binding.apply {
        btnDisplay.setOnClickListener {
            val p = Intent(this@AdminProductManagement, ShopScreen::class.java)
            startActivity(p)
        }

        btnEdit.setOnClickListener {
            val code = txtCode.text.toString().trim()
            val name = txtName.text.toString().trim()
            //val desc = txtDiscribe.text.toString().trim()
            val price = txtPrice.text.toString().trim()

            if (code.isEmpty() || name.isEmpty() || price.isEmpty()) {
                showToast("لطفاً تمام فیلدها را پر کنید")
                return@setOnClickListener
            }

            updateProduct(code, name, price, imageFile)
        }

        img.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, 100)
        }

        btnDelet.setOnClickListener {
            val code = txtCode.text.toString().trim()

            if (code.isEmpty()) {
                showToast("لطفاً کد محصول را وارد کنید")
                return@setOnClickListener
            }

            showDeletingProgressAndDelete(code)
        }

        btnInsert.setOnClickListener {
            val code = txtCode.text.toString().trim()
            val name = txtName.text.toString().trim()
            //val desc = txtDiscribe.text.toString().trim()
            val price = txtPrice.text.toString().trim()

            if (code.isEmpty() || name.isEmpty()  || price.isEmpty()) {
                showToast("لطفاً تمام فیلدها را پر کنید")
                return@setOnClickListener
            }

            insertProduct(code, name,  price, imageFile)
        }

        logoutButton.setOnClickListener {
            SharedPrefManager(this@AdminProductManagement).logout()
            startActivity(Intent(this@AdminProductManagement, LoginScreen::class.java))
            finish()
        }

    }


    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            Log.d("Image URI", "Selected Image URI: $imageUri")
            imageUri?.let { uri ->
                val originalFile = FileUtil.uriToFile(this, uri)
                imageFile = ImageCompressor.compressImage(originalFile.toString())
                binding.img.setImageURI(uri) // تصویر فشرده‌شده همون ظاهر رو داره
            }
        }
    }


    private fun insertProduct(
        id: String,
        name: String,
        price: String,
        image: File?
    ) {
        // تبدیل رشته‌ها به RequestBody برای ارسال به صورت فرم
        val idPart = id.toRequestBody("text/plain".toMediaTypeOrNull())
        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        //val descPart = desc.toRequestBody("text/plain".toMediaTypeOrNull())
        val pricePart = price.toRequestBody("text/plain".toMediaTypeOrNull())

        var imagePart: MultipartBody.Part? = null

        // اگر فایل عکس موجود باشد و قابل خواندن باشد
        if (image != null && image.exists()) {
            // استفاده از کلاس ProgressRequestBody برای رصد کردن پیشرفت آپلود عکس
            val requestBody = ProgressRequestBody(
                image,
                "image/*", // نوع فایل (می‌تواند image/jpeg یا غیره باشد)
                object : ProgressRequestBody.UploadCallbacks {
                    override fun onProgressUpdate(percentage: Int) {
                        // در هنگام پیشرفت آپلود، مقدار ProgressBar را به‌روزرسانی می‌کنیم
                        runOnUiThread {
                            binding.progressBar.progress = percentage
                        }
                    }

                    override fun onError() {
                        // در صورت بروز خطا در آپلود عکس
                        runOnUiThread {
                            showToast("خطا در آپلود عکس")
                        }
                    }

                    override fun onFinish() {
                        // وقتی آپلود کامل شد
                        runOnUiThread {
                            showToast("آپلود عکس کامل شد")
                        }
                    }
                })

            // ایجاد قسمت فایل برای ارسال در فرم (کلید "image" باید با سرور هماهنگ باشد)
            imagePart = MultipartBody.Part.createFormData("image", image.name, requestBody)
        }

        // فراخوانی API برای درج محصول همراه با اطلاعات و فایل تصویر
        RetrofitClient.instance.insertProduct(idPart, namePart, pricePart, imagePart)
            .enqueue(object : Callback<ServerResponse> {

                // پاسخ موفق از سرور
                override fun onResponse(
                    call: Call<ServerResponse>,
                    response: Response<ServerResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val res = response.body()
                        if (res?.success == true) {
                            showToast("موفق: ${res.message}") // پیام موفقیت
                        } else {
                            showToast("خطا: ${res?.message}") // پیام خطا از سرور
                        }
                    } else {
                        // اگر سرور پاسخ نامعتبر بدهد
                        showToast("خطا در پاسخ سرور: ${response.errorBody()?.string()}")
                    }
                }

                // خطا در ارتباط با سرور
                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    showToast("ارتباط با سرور برقرار نشد: ${t.localizedMessage}")
                }
            })
    }


    private fun updateProduct(
        code: String,
        name: String,
        price: String,
        image: File?
    ) {
        val codePart = code.toRequestBody("text/plain".toMediaTypeOrNull())
        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
//        val descPart = desc.toRequestBody("text/plain".toMediaTypeOrNull())
        val pricePart = price.toRequestBody("text/plain".toMediaTypeOrNull())

        var imagePart: MultipartBody.Part? = null

        if (image != null && image.exists()) {
            val requestBody = ProgressRequestBody(
                image,
                "image/*",
                object : ProgressRequestBody.UploadCallbacks {
                    override fun onProgressUpdate(percentage: Int) {
                        runOnUiThread {
                            binding.progressBar.progress = percentage
                        }
                    }

                    override fun onError() {
                        runOnUiThread {
                            showToast("خطا در آپلود عکس")
                        }
                    }

                    override fun onFinish() {
                        runOnUiThread {
                            showToast("آپلود عکس کامل شد")
                        }
                    }
                })
            imagePart = MultipartBody.Part.createFormData("image", image.name, requestBody)
        }

        RetrofitClient.instance.updateProduct(codePart, namePart, pricePart, imagePart)
            .enqueue(object : Callback<ServerResponse> {
                override fun onResponse(
                    call: Call<ServerResponse>,
                    response: Response<ServerResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val res = response.body()
                        if (res?.success == true) {
                            showToast("موفق: ${res.message}")
                        } else {
                            showToast("خطا: ${res?.message}")
                        }
                    } else {
                        showToast("خطا در پاسخ سرور: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    showToast("ارتباط با سرور برقرار نشد: ${t.localizedMessage}")
                }
            })
    }

    // متد برای حذف یک محصول بر اساس کد محصول از سرور
    private fun deleteProduct(code: String, callback: DeleteCallback) {
        // فراخوانی متد deleteProduct از RetrofitClient و قرار دادن آن در صف اجرا
        RetrofitClient.instance.deleteProduct(code).enqueue(object : Callback<ServerResponse> {

            // وقتی پاسخ موفقیت‌آمیز از سرور دریافت می‌شود
            override fun onResponse(
                call: Call<ServerResponse>,
                response: Response<ServerResponse>
            ) {
                if (response.isSuccessful) { // اگر وضعیت پاسخ 200 یا موفق بود
                    val serverResponse = response.body()
                    if (serverResponse != null) {
                        if (serverResponse.success) {
                            // اگر عملیات موفق بود، پیام موفقیت به callback فرستاده می‌شود
                            callback.onSuccess(serverResponse.message)
                        } else {
                            // اگر عملیات ناموفق بود، پیام خطا به callback فرستاده می‌شود
                            callback.onFailure(serverResponse.message)
                        }
                    } else {
                        // اگر پاسخ سرور null باشد
                        callback.onFailure("پاسخ خالی از سرور دریافت شد!")
                    }
                } else {
                    // اگر پاسخ ناموفق باشد (مثلاً خطای 500 یا 404)
                    callback.onFailure("خطای سرور: ${response.errorBody()?.string()}")
                }
            }

            // اگر ارتباط با سرور برقرار نشود یا خطای شبکه رخ دهد
            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                callback.onFailure("خطای شبکه: ${t.localizedMessage}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showDeletingProgressAndDelete(code: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("در حال حذف محصول...")
        progressDialog.setCancelable(true)
        progressDialog.show()

        deleteProduct(code, object : DeleteCallback {
            override fun onSuccess(message: String) {
                progressDialog.dismiss()
                Toast.makeText(this@AdminProductManagement, message, Toast.LENGTH_SHORT).show()
                // loadProducts() مثلا برای رفرش
            }

            override fun onFailure(message: String) {
                progressDialog.dismiss()
                Toast.makeText(this@AdminProductManagement, message, Toast.LENGTH_SHORT).show()
            }
        })
    }}