package com.boostcamp.travery.save

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.boostcamp.travery.Constants
import com.boostcamp.travery.R
import com.boostcamp.travery.base.BaseActivity
import com.boostcamp.travery.databinding.ActivitySaveUserActionBinding
import com.boostcamp.travery.utils.toast
import com.esafirm.imagepicker.features.ImagePicker
import com.tedpark.tedpermission.rx2.TedRx2Permission
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_save_user_action.*

class UserActionSaveActivity : BaseActivity<ActivitySaveUserActionBinding>(), UserActionSaveViewModel.Contract {
    lateinit var viewModel: UserActionSaveViewModel
    private val compositeDisposable = CompositeDisposable()

    override val layoutResourceId: Int
        get() = R.layout.activity_save_user_action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewDataBinding.root)
        setSupportActionBar(toolbar as Toolbar)
        title = ""

//        viewModel = UserActionSaveViewModel(application)
        viewModel = ViewModelProviders.of(this).get(UserActionSaveViewModel::class.java)
        viewDataBinding.viewmodel = viewModel

        requestPermission()

        viewModel.setContract(this)
    }

    private fun requestPermission() {
        TedRx2Permission.with(this)
                .setRationaleTitle(getString(R.string.permission_title))
                .setRationaleMessage(getString(R.string.permission_message_select_image))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .request()
                .subscribe({ tedPermissionResult ->
                    if (!tedPermissionResult.isGranted) {
                        getString(R.string.permission_denied) + tedPermissionResult.deniedPermissions.toString().toast(this)
                    }
                }, { }).also { compositeDisposable.add(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_course_add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.menu_course_save -> {
            with(intent) {
                viewModel.saveUserAction(
                        getDoubleExtra(Constants.EXTRA_LATITUDE, 0.0),
                        getDoubleExtra(Constants.EXTRA_LONGITUDE, 0.0),
                        getLongExtra(Constants.EXTRA_COURSE_CODE, 0)
                )
            }
            finish()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun saveSelectedImage() {
        ImagePicker.create(this)
                .imageDirectory(getString(R.string.app_name))
                .single()
                .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            ImagePicker.getImages(data).forEach {
                rv_save_useraction_image_list.scrollToPosition(viewModel.imageList.size)
                viewModel.imageList.add(viewModel.imageList.size - 1, UserActionImage(it.path))
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}