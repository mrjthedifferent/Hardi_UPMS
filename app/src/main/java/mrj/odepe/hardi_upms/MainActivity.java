package mrj.odepe.hardi_upms;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private EditText signinEmail, signinPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btn_login,button_login,btn_back2login,button_off;
    private Button button_webportal,button_webmail;

    RelativeLayout login_layout;
    Animation slide_down;
    Animation slide_up;

    TextView date_show;
    TextView developer;

    final String TAG = this.getClass().getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //Firebase auth intance
        auth = FirebaseAuth.getInstance();


        //auto login if already signed in

        if (auth.getCurrentUser() !=null) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }




        //layout
        setContentView(R.layout.activity_main);

        signinEmail = (EditText)findViewById(R.id.email);
        signinPassword = (EditText)findViewById(R.id.password);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        btn_login = (Button)findViewById(R.id.btn_login);
        button_login = (Button)findViewById(R.id.button_login);
        btn_back2login = (Button)findViewById(R.id.btn_back2login);
        button_off = (Button)findViewById(R.id.button_off);
        button_webportal = (Button)findViewById(R.id.button_webportal);
        button_webmail = (Button)findViewById(R.id.button_webmail);

        button_webportal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webportal = new Intent(MainActivity.this, webportalActivity.class);
                startActivity(webportal);
            }
        });

        button_webmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent button_webmail = new Intent(MainActivity.this, webmailActivity.class);
                startActivity(button_webmail);
            }
        });

        button_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder exitapp = new AlertDialog.Builder(MainActivity.this);
                exitapp.setMessage("আপনি কি অ্যাপটি বন্ধ করতে চান?")
                        .setCancelable(false)
                        .setPositiveButton("হ্যাঁ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("না", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = exitapp.create();
                alert.setTitle("ভেবে সিদ্ধান্ত নিন...");
                alert.show();
            }
        });

        btn_back2login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_layout.setVisibility(View.GONE);
                slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_up);
                login_layout.startAnimation(slide_up);
            }
        });


        login_layout = (RelativeLayout)findViewById(R.id.login_layout);
        login_layout.setVisibility(View.GONE);


        //date show

        date_show = (TextView)findViewById(R.id.date);

        Calendar c = Calendar.getInstance();

        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String date = day+"-"+month+"-"+year;

        date_show.setText(date);


        //Scrolling developer info
        developer = (TextView)findViewById(R.id.developer);
        developer.setSelected(true);
        developer.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        developer.setSingleLine(true);


        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //setting OnClick Listener

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_down);
                login_layout.startAnimation(slide_down);
                login_layout.setVisibility(View.VISIBLE);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signinEmail.getText().toString();
                final String password = signinPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "দয়া করে ইমেইল ঠিকানা লিখুন..!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "দয়া করে পাসওয়ার্ড লিখুন..!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        Toast.makeText(MainActivity.this, getString(R.string.minimum_password), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(MainActivity.this, webappActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    //app exit coding start
    boolean twice;
    @Override
    public void onBackPressed() {

            Log.d(TAG, "click");

            if(twice == true){
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                System.exit(0);
            }

            twice = true;
            Log.d(TAG, "twice: " + twice);

            // super.onBackPressed();
            Toast.makeText(MainActivity.this, "অ্যাপ বন্ধ করতে দয়া করে দুই বার চাপুন...",Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    twice = false;
                    Log.d(TAG, "twice: " + twice);
                }
            },3000);
    }
}
