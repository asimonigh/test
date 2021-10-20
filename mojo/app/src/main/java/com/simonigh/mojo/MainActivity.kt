package com.simonigh.mojo

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.simonigh.mojo.data.Member
import com.simonigh.mojo.databinding.*
import com.simonigh.mojo.databinding.ActivityMainBinding.inflate
import com.simonigh.mojo.presentation.MemberListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import timber.log.Timber.Forest

class MainActivity : AppCompatActivity() {
    
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    
    private val viewModel by viewModel<MemberListViewModel>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initUI()
        observeVM()
        
      
    }
    
    private fun observeVM() {
        viewModel.state.observe(this) { list ->
            addMembers(list)
            Timber.d("${list.toString()}")
        }
    }
    
    private fun addMembers(list: List<Member>) {
        binding.activityMembersListLayout.removeAllViews()
        list.forEach { member ->
            val itemBinding = ItemMemberBinding.inflate(layoutInflater)
            itemBinding.itemMemberName.text = member.name
            itemBinding.itemMemberPosition.text = member.position
            member.platform?.let {
                itemBinding.itemMemberPlatform.visibility = View.VISIBLE
                itemBinding.itemMemberPlatform.text = member.platform
            }
            member.picUrl?.let { url ->
                Glide.with(this)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemBinding.itemMemberPicture)
            }
            binding.activityMembersListLayout.addView(itemBinding.root)
        }
        
    }
    
    private fun initUI() {
        setSupportActionBar(binding.toolbar)
    
        binding.fab.setOnClickListener { view ->
            showAddMemberDialog()
            
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
    
    private fun showAddMemberDialog() {
        val builder = AlertDialog.Builder(this)
        
        val dialogBinding = DialogAddMemberBinding.inflate(layoutInflater)
    
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogBinding.root)
            // Add action buttons
            .setPositiveButton("Add",
                DialogInterface.OnClickListener { dialog, id ->
                    val name = dialogBinding.inputName.text.toString()
                    val position = dialogBinding.inputPosition.text.toString()
                    var platform : String? = dialogBinding.inputPlatform.text.toString()
                    if(!name.isNullOrBlank() && !position.isNullOrEmpty()){
                        if(platform.isNullOrEmpty()) {
                            platform = null
                        }
                        viewModel.addMember(name, position, platform)
                        dialog.cancel()
                    }
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })
        builder.create().show()
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}