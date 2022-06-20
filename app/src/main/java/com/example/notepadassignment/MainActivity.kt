package com.example.notepadassignment

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.core.view.setMargins
import androidx.core.view.updateMargins
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notepadassignment.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sp: SharedPreferences
    private lateinit var notes: List<NoteModel>
    private var isLongPressed = false
    private var isOneColumn = false
    private var pressed = 0
    private val gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.fab.imageTintList = null
        binding.side.clipToOutline = true
        binding.viewType.clipToOutline = true
        binding.profile.clipToOutline = true

        sp = application.getSharedPreferences("com.example.notepadassignment", MODE_PRIVATE)
        getNotes()
        setupRv()
        binding.fab.setOnClickListener {
            startActivity(Intent(this, ViewNoteActivity::class.java))
        }
        binding.side.setOnClickListener {
            binding.drawer.openDrawer(GravityCompat.START)
        }
        binding.viewType.setOnClickListener {
            isOneColumn = !isOneColumn
            if(isOneColumn) {
                binding.viewType.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_grid_view))
                binding.rv.layoutManager = LinearLayoutManager(this)
            }else {
                binding.viewType.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_single_column))
                val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                binding.rv.layoutManager = layoutManager
            }
        }
    }

    override fun onDestroy() {
        unselectAll()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isLongPressed) {
            menuInflater.inflate(R.menu.long_press_menu, menu)
            binding.llToolbar.visibility = View.GONE
            binding.toolbar.background = resources.getDrawable(R.color.light_gray, applicationContext.theme)
            val layoutParams = binding.toolbar.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.setMargins(0)
            binding.toolbar.layoutParams = layoutParams
        }else {
            binding.toolbar.background = resources.getDrawable(R.drawable.search_bar, applicationContext.theme)
            binding.llToolbar.visibility = View.VISIBLE
            val layoutParams = binding.toolbar.layoutParams as ConstraintLayout.LayoutParams
            val additionalMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt()
            layoutParams.setMargins(additionalMargin,0,additionalMargin,0)
            binding.toolbar.layoutParams = layoutParams
            binding.search.apply {
                this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        Toast.makeText(applicationContext, query, Toast.LENGTH_SHORT).show()
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
            }
        }

        renderMenuContent()
        renderTitle()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                notes = notes.map {
                    it.selected = false
                    it
                }
                resetActionBar()
            }
            R.id.delete -> {
                removeNotes()
                resetActionBar()
            }
            else -> {
            }
        }
        updateNotes()
        return super.onOptionsItemSelected(item)
    }

    override fun onSearchRequested(): Boolean {
        val appData = Bundle().apply {
            putBoolean("TEST", true)
        }
        startSearch(null, false, appData, false)
        return true
    }

    fun resetActionBar() {
        isLongPressed = false
        invalidateOptionsMenu()
        pressed = 0
    }

    fun renderMenuContent() {
        if (isLongPressed) {
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        } else {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        }
    }

    fun renderTitle() {
        if (isLongPressed) {
            supportActionBar!!.title = "$pressed selected"
        } else {
            supportActionBar!!.title = ""
        }
    }

    fun getNotes() {
        val json = sp.getString("NOTES", "[]") ?: "[]"
        Log.i("TEST", json)
        notes = gson.fromJson(json, object : TypeToken<List<NoteModel>>() {}.type)
    }

    fun removeNotes() {
        notes = notes.filter { !it.selected }
        val json = gson.toJson(notes)
        val editor = sp.edit()
        editor.putString("NOTES", json)
        editor.commit()
    }

    fun unselectAll() {
        val json = gson.toJson(notes.map {
            it.selected = false
            it
        })
        val editor = sp.edit()
        editor.putString("NOTES", json)
        editor.commit()
    }

    fun updateNotes() {
        (binding.rv.adapter as NotesAdapter).updateList(notes)
    }

    fun setupRv() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        binding.rv.layoutManager = layoutManager
        binding.rv.addItemDecoration(ItemOffsetDecoration(10))
        binding.rv.adapter = NotesAdapter(notes, { item, notifyChange ->
            if (isLongPressed) {
                item.selected = !item.selected
                pressed = notes.filter { it.selected }.size
                renderTitle()
                notifyChange()
            } else {
                val intent = Intent(this, ViewNoteActivity::class.java)
                intent.putExtra("NOTE_ID", item.id)
                startActivity(intent)
            }
        }) {
            isLongPressed = true
            invalidateOptionsMenu()
        }
    }

    fun removeData() {
        val editor = sp.edit()
        editor.remove("NOTES")
        editor.commit()
    }
}

class ItemOffsetDecoration(private val mItemOffset: Int) : ItemDecoration() {
    constructor(context: Context, @DimenRes itemOffsetId: Int) : this(
        context.resources.getDimensionPixelSize(itemOffsetId)
    )

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset)
    }
}