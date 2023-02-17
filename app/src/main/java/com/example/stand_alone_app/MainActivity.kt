package com.example.stand_alone_app

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    // parameters to store string
    private var mFirstName: String? = null
    private var mMiddleName: String? = null
    private var mLastName: String? = null
    private var mThumbnail: Bitmap? = null
    private var mFilepathString: String? = null

    //store ui elements
    private var mEtFirstName: EditText? = null
    private var mEtMiddleName: EditText? = null
    private var mEtLastName: EditText? = null
    private var mSubmitButton: Button? = null
    private var mSnapPicButton: Button? = null
    private var mThumbnailImgView: ImageView? = null
    private var mIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSubmitButton = findViewById(R.id.bttn_submit)
        mSnapPicButton = findViewById(R.id.bttn_pic)
        mThumbnailImgView = findViewById(R.id.profile_pic)

        mSubmitButton!!.setOnClickListener(this)
        mSnapPicButton!!.setOnClickListener(this)
        mIntent = Intent(this, MainActivity::class.java)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.bttn_submit->{

                mEtFirstName = findViewById(R.id.et_fn)
                mEtMiddleName = findViewById(R.id.et_mn)
                mEtLastName = findViewById(R.id.et_ln)

                mFirstName = mEtFirstName!!.text.toString()
                mMiddleName = mEtMiddleName!!.text.toString()
                mLastName = mEtLastName!!.text.toString()

                if(mFirstName.isNullOrBlank()){
                    Toast.makeText(this@MainActivity, "First Name required!", Toast.LENGTH_SHORT).show()
                }

                if(mLastName.isNullOrBlank()){
                    Toast.makeText(this@MainActivity, "Last Name required!", Toast.LENGTH_SHORT).show()
                }

                val msgIntent = Intent(this, homepage::class.java)
                msgIntent.putExtra("FIRST_NAME", mFirstName)
                msgIntent.putExtra("MIDDLE_NAME", mMiddleName)
                msgIntent.putExtra("LAST_NAME", mLastName)
                if(!mFirstName.isNullOrBlank() && !mLastName.isNullOrBlank() && mThumbnail != null)
                    this.startActivity(msgIntent)
            }
            R.id.bttn_pic->{
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try{
                    cameraActivity.launch(cameraIntent)
                }
                catch(ex:ActivityNotFoundException){

                }

            }
        }
    }

    private val cameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        if(result.resultCode == RESULT_OK){
            val extras = result.data!!.extras
            mThumbnail = extras!!["data"] as Bitmap?
            mThumbnailImgView!!.setImageBitmap(mThumbnail)

            if(isExteralStorageWritable){
                mFilepathString = saveImage(mThumbnail)
                mIntent!!.putExtra("PROFILE_PIC", mFilepathString)

            }
//            if(Build.VERSION.SDK_INT >= 33) {
//                val thumbnailImage = result.data!!.getParcelableExtra("data", Bitmap::class.java)
//                mThumbnail!!.setImageBitmap(thumbnailImage)
//            }
//            else{
//                val thumbnailImage = result.data!!.getParcelableExtra<Bitmap>("data")
//                mThumbnail!!.setImageBitmap(thumbnailImage)
//            }
        }
    }

    private fun saveImage(finalBitmap: Bitmap?) : String {
        val root = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val myDir = File("$root/saved_images")
        myDir.mkdirs()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss")
        val fname = "Thumbnail_$timeStamp.jpg"
        val file = File(myDir, fname)
        if(file.exists()) file.delete()
        try{
            val out = FileOutputStream(file)
            finalBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            Toast.makeText(this, "file saved", Toast.LENGTH_SHORT).show()
        }
        catch(e: Exception){
            e.printStackTrace()
        }
        return file.absolutePath
    }

    private val isExteralStorageWritable: Boolean
    get(){
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("PROFILE_PIC_PATH", mFilepathString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        mFilepathString = savedInstanceState.getString("PROFILE_PIC_PATH")
        val imgBitmapDecoded = BitmapFactory.decodeFile(mFilepathString)
        mThumbnailImgView!!.setImageBitmap(imgBitmapDecoded)

    }
}


