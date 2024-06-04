package com.carin.activities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.carin.R
import com.carin.di.RepositoryModule
import com.carin.fragments.MainRoutesListFragment
import com.carin.viewmodels.RoutesViewModel
import com.carin.viewmodels.RoutesViewModelFactory
import com.carin.viewmodels.events.RoutesListEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RoutesListActivity : AppCompatActivity() {
    private lateinit var viewModel: RoutesViewModel
    private var searchJob : Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routes_list)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.container, MainRoutesListFragment())
                .commitNow()
        }

        val iconImageView: ImageView = findViewById(R.id.iconImageView)
        iconImageView.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val searchEditText: EditText = findViewById(R.id.searchEditText)

        val routeRepository = RepositoryModule.provideRouteRepository(this)
        val factory = RoutesViewModelFactory(routeRepository)
        viewModel = ViewModelProvider(this, factory)[RoutesViewModel::class.java]

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            // Close the keyboard when the search button is clicked
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(searchEditText.text.toString())
                searchEditText.clearFocus()
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
                true
            } else {
                false
            }
        }

        // Add a text watcher to handle text changes
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(500)
                    s?.let {
                        performSearch(it.toString())
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Menu Buttons
        val buttonRoute: ImageView = findViewById(R.id.buttonRoute)
        buttonRoute.setOnClickListener {
            val intent = Intent(this, RoutesListActivity::class.java)
            startActivity(intent)
            finish()
        }

        val buttonVehicle: ImageView = findViewById(R.id.buttonVehicle)
        buttonVehicle.setOnClickListener {
            val intent = Intent(this, VehicleActivity::class.java)
            startActivity(intent)
        }

        val buttonHome: ImageView = findViewById(R.id.buttonHome)
        buttonHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val buttonPerson: ImageView = findViewById(R.id.buttonPerson)
        buttonPerson.setOnClickListener {
            val intent = Intent(this, InfoUserActivity::class.java)
            startActivity(intent)
        }

        val buttonMore = findViewById<ImageButton>(R.id.buttonMore)
        val layoutNewAppointment = findViewById<RelativeLayout>(R.id.layoutNewAppointment)
        val layoutAddRoute = findViewById<RelativeLayout>(R.id.layoutAddRoute)
        val layoutAddVehicle = findViewById<RelativeLayout>(R.id.layoutAddVehicle)
        val layoutAddUser = findViewById<RelativeLayout>(R.id.layoutAddUser)

        buttonMore.setOnClickListener {
            val rotateAnimator = ObjectAnimator.ofFloat(buttonMore, "rotation", 0f, 45f)
                .apply {
                    duration = 500
                    interpolator = AccelerateDecelerateInterpolator()
                }

            val animatorSet = AnimatorSet()
            animatorSet.play(rotateAnimator)
            animatorSet.start()

            layoutNewAppointment.visibility = View.VISIBLE
            layoutAddRoute.visibility = View.VISIBLE
            layoutAddVehicle.visibility = View.VISIBLE
            layoutAddUser.visibility = View.VISIBLE
        }

        layoutAddUser.setOnClickListener {
            val intent = Intent(this, NewUserActivity::class.java)
            startActivity(intent)
        }
        layoutAddVehicle.setOnClickListener {
            val intent = Intent(this, NewVehicleActivity::class.java)
            startActivity(intent)
        }
        layoutAddRoute.setOnClickListener {
            val intent = Intent(this, NewRouteActivity::class.java)
            startActivity(intent)
        }
        layoutNewAppointment.setOnClickListener {
            val intent = Intent(this, NewSchedulingActivity::class.java)
            startActivity(intent)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun performSearch(query: String) {
        viewModel.onEvent(RoutesListEvent.UpdateSearch(query))
    }
}
