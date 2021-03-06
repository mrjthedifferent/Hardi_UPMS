package mrj.odepe.hardi_upms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class webmailActivity extends AppCompatActivity {

    WebView webmail;
    ProgressDialog progressDialog;


    Button button;
    Button button2;
    Button button3;

    final Activity activity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webmail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        if(isNetworkAvailable()) {
            setContentView(R.layout.activity_webportal);
            webmail = (WebView) findViewById(R.id.webview_webportal);
            WebSettings webSettings2 = webmail.getSettings();
            webSettings2.setJavaScriptEnabled(true);
            webmail.loadUrl("http://hardiup.com/webmail");
            webmail.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    if (progressDialog == null) {
                        progressDialog = new ProgressDialog(activity);
                        progressDialog.setMessage("অপেক্ষা করুন");
                        progressDialog.show();

                        // Hide the webview while loading
                        webmail.setEnabled(false);
                    }
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                        webmail.setEnabled(true);
                    }
                }

                public void onReceivedError(WebView view, int errorCod, String description, String failingUrl) {
                    setContentView(R.layout.activity_error);

                    button = (Button)findViewById(R.id.button);
                    button2 = (Button)findViewById(R.id.button2);
                    button3 = (Button)findViewById(R.id.button3);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent goback = new Intent(webmailActivity.this, MainActivity.class);
                            startActivity(goback);
                        }
                    });

                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent tryagain = new Intent(webmailActivity.this, MainActivity.class);
                            startActivity(tryagain);
                        }
                    });

                    button3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent settings = new Intent(android.provider.Settings.ACTION_SETTINGS);
                            settings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(settings);
                        }
                    });
                    Toast.makeText(webmailActivity.this, "আপনার ইন্টারনেট কানেকশনে হয়তো কোন সমস্যা আছে অথবা" + description, Toast.LENGTH_LONG).show();
                }
            });


        }
        if (!isNetworkAvailable()) {
            setContentView(R.layout.activity_error);

            button = (Button)findViewById(R.id.button);
            button2 = (Button)findViewById(R.id.button2);
            button3 = (Button)findViewById(R.id.button3);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goback = new Intent(webmailActivity.this, MainActivity.class);
                    startActivity(goback);
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent tryagain = new Intent(webmailActivity.this, webportalActivity.class);
                    startActivity(tryagain);
                }
            });

            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent settings = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    settings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(settings);
                }
            });

            Toast.makeText(webmailActivity.this, "আপনি ইন্টারনেট কানেক্ট না করলে এই পৃষ্ঠা দেখতে পারবেন না", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        Intent gotomain = new Intent(webmailActivity.this, MainActivity.class);
        startActivity(gotomain);
        return;
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo=connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo !=null;
    }
}
