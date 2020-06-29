package org.wazir.build.elemenophee;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;
import org.wazir.build.elemenophee.ModelObj.SubscribedTOmodel;
import org.wazir.build.elemenophee.ModelObj.SubscribersModel;
import org.wazir.build.elemenophee.ModelObj.TeacherObj;


public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    CardView basic_pay,standard_pay,plus_pay;
    String tID;
    String basicAmount = "49";
    String standardAmount = "149";
    String plusAmount = "249";
    TeacherObj obj;

    CollectionReference TeacherRef = FirebaseFirestore.getInstance().collection("TEACHERS");
    CollectionReference StudentRef = FirebaseFirestore.getInstance().collection("STUDENTS");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        basic_pay = findViewById(R.id.basic_pay);
        standard_pay = findViewById(R.id.standard_pay);
        plus_pay = findViewById(R.id.plus_pay);

        tID = getIntent().getStringExtra("TEACHER_ID");
        TeacherRef
                .document(tID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                obj = documentSnapshot.toObject(TeacherObj.class);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PaymentActivity.this,  e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        basic_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment(basicAmount);
            }
        });
        standard_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment(standardAmount);
            }
        });
        plus_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment(plusAmount);
            }
        });
    }


    public void startPayment(String payment) {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", obj.getName());
            options.put("description", "App Payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            preFill.put("contact", tID);
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment successfully done! " +s, Toast.LENGTH_SHORT).show();

        TeacherRef.document(tID)
                .collection("SUBSCRIBERS")
                .document(user.getPhoneNumber())//TODO:place Student ID here
                .set(new SubscribersModel(user.getPhoneNumber()))//TODO:add Student data acoordingly
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Subscription",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        StudentRef.document(user.getPhoneNumber())
                .collection("SUBSCRIBED_TO")
                .document(tID)//TODO:place Teacher Id here
                .set(new SubscribedTOmodel(tID))//TODO: add Teacher data Accordingly
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }

    }
}