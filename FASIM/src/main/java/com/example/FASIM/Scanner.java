package com.example.FASIM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Scanner extends AppCompatActivity {

    CodeScanner codeScanner;
    CodeScannerView scannView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title b
        setContentView(R.layout.activity_scanner);

        scannView=findViewById(R.id.scanner_view);
        codeScanner=new CodeScanner(this,scannView);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(),result.getText(),Toast.LENGTH_LONG).show();

                            MediaType MEDIA_TYPE = MediaType.parse("application/json");
                            String url = "https://fasim.herokuapp.com/set-user";
                            OkHttpClient client = new OkHttpClient();
                            JSONObject postdata = new JSONObject();
                            try {
                                postdata.put("token", result.getText());
                                postdata.put("user", MainActivity.username);
                                postdata.put("email", MainActivity.emailid);
                            } catch(JSONException e){
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
                            Request request = new Request.Builder()
                                    .url(url)
                                    .post(body)
                                    .header("Accept", "application/json")
                                    .header("Content-Type", "application/json")
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    String mMessage = e.getMessage().toString();
                                    Log.w("failure Response", mMessage);
                                    //call.cancel();
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String mMessage = response.body().string();
                                    Log.d("Response", mMessage);
                                }
                            });


                        }

                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }
}