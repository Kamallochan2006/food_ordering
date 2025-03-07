package com.example.foodordering

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodordering.adapter.PlaceYourOrderAdapter
import com.example.foodordering.models.RestaurentModel

class PlaceYourOrderActivity : AppCompatActivity() {

    var inputName: TextView = findViewById(R.id.inputName)
    var inputAddress: TextView = findViewById(R.id.inputAddress)
    var inputCity: TextView = findViewById(R.id.inputCity)
    var inputZip: TextView = findViewById(R.id.inputZip)
    var inputCardNumber: TextView = findViewById(R.id.inputCardNumber)
    var inputCardExpiry: TextView = findViewById(R.id.inputCardExpiry)
    var inputCardPin: TextView = findViewById(R.id.inputCardPin)

    var placeYourOrderAdapter: PlaceYourOrderAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_your_order)

        val restaurantModel: RestaurentModel? = intent.getParcelableExtra("RestaurantModel")
        val actionbar: ActionBar? = supportActionBar
        actionbar?.setTitle(restaurantModel?.name)
        actionbar?.setSubtitle(restaurantModel?.address)
        actionbar?.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.buttonPlaceYourOrder).setOnClickListener {
            onPlaceOrderButtonCLick(restaurantModel)
        }


        initRecyclerView(restaurantModel)
        calculateTotalAmount(restaurantModel)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initRecyclerView(restaurantModel: RestaurentModel?) {
        findViewById<RecyclerView>(R.id.cartItemsRecyclerView).layoutManager = LinearLayoutManager(this)
        placeYourOrderAdapter = PlaceYourOrderAdapter(restaurantModel?.menus)
        findViewById<RecyclerView>(R.id.cartItemsRecyclerView).adapter =placeYourOrderAdapter
    }

    private fun calculateTotalAmount(restaurantModel: RestaurentModel?) {
        var subTotalAmount = 0f
        for(menu in restaurantModel?.menus!!) {
            subTotalAmount += menu?.price!!  * menu.totalInCart

        }
        findViewById<TextView>(R.id.tvSubtotalAmount).text = "Rs."+ String.format("%.2f", subTotalAmount)
        findViewById<TextView>(R.id.tvDeliveryChargeAmount).text = "Rs."+String.format("%.2f", restaurantModel.delivery_charge?.toFloat())
        subTotalAmount += restaurantModel?.delivery_charge?.toFloat()!!
        findViewById<TextView>(R.id.tvTotalAmount).text = "Rs."+ String.format("%.2f", subTotalAmount)
    }

    private fun onPlaceOrderButtonCLick(restaurantModel: RestaurentModel?) {
        if(TextUtils.isEmpty(inputName.text.toString())) {
            inputName.error =  "Enter your name"
            return
        } else if(TextUtils.isEmpty(inputAddress.text.toString())) {
            inputAddress.error =  "Enter your address"
            return
        } else if(TextUtils.isEmpty(inputCity.text.toString())) {
            inputCity.error =  "Enter your City Name"
            return
        } else if(TextUtils.isEmpty(inputZip.text.toString())) {
            inputZip.error =  "Enter your Zip code"
            return
        } else if( TextUtils.isEmpty(inputCardNumber.text.toString())) {
            inputCardNumber.error =  "Enter your credit card number"
            return
        } else if( TextUtils.isEmpty(inputCardExpiry.text.toString())) {
            inputCardExpiry.error =  "Enter your credit card expiry"
            return
        } else if( TextUtils.isEmpty(inputCardPin.text.toString())) {
            inputCardPin.error =  "Enter your credit card pin/cvv"
            return
        }
        val intent = Intent(this@PlaceYourOrderActivity, SuccessOrderActivity::class.java)
        intent.putExtra("RestaurantModel", restaurantModel)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
        finish()
    }

}