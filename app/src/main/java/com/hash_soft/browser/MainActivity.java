package com.hash_soft.browser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends Activity {
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final WebView web = (WebView) findViewById(R.id.web_hash);
        final ImageView webpage_go = (ImageView) findViewById(R.id.go);
        final EditText webaddress_input = (EditText) findViewById(R.id.juso);
        final Button address_remove = (Button) findViewById(R.id.edittext_remove);
        final ImageView menu = (ImageView) findViewById(R.id.menu);
        final ProgressBar browsing_progress = (ProgressBar) findViewById(R.id.browsing_progress);
        final Button tab_button_caller = (Button)findViewById(R.id.tab_popup_caller);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOptionsMenu();
            }
        });

        tab_button_caller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent TabView = new Intent(MainActivity.this, TabViewActivity.class);
                startActivity(TabView);
            }
        });

        //기본 홈페이지 불러오기
        SharedPreferences DefHmPgSharedPref = getSharedPreferences("default_homepage_set", 0);
        String homepage_load = DefHmPgSharedPref.getString("default_homepage_set", "");
        Toast.makeText(getApplicationContext(), homepage_load, Toast.LENGTH_LONG).show();
//        if (homepage_load == "") {
//            web.loadUrl("http://www.google.com");
//        } else if (homepage_load.length() >= 7) {
//            if (homepage_load.substring(0, 7).equals("http://") || homepage_load.substring(0, 8).equals("https://") || homepage_load.substring(0, 6).equals("ftp://")) {
//                web.loadUrl(homepage_load);
//            } else {
//                homepage_load = "http://" + homepage_load;
//                web.loadUrl(homepage_load);
//            }
//        } else {
//            homepage_load = "http://" + homepage_load;
//            web.loadUrl(homepage_load);
//        }

        web.loadUrl("http://sunrin.wiki/index.php/%EC%98%A4%EC%A4%80%EC%84%9D");
        // 맨처음 로딩...표시
//        webaddress_input.setText("Loading....");

        // 기본 클라리언트 설정
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);

        // 텍스트 변경사항 감지
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 텍스트의 길이가 변경되었을 경우 이벤트
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 텍스트 변경되면 이벤트
                if (webaddress_input.length() != 0) {
                    address_remove.setVisibility(View.VISIBLE);
                } else address_remove.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        //
        address_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webaddress_input.setText(null);
            }
        });
        webaddress_input.addTextChangedListener(textWatcher);

        webaddress_input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    SharedPreferences input = getSharedPreferences("input", 0);
                    SharedPreferences.Editor editor = input.edit();
                    editor.putString("input", webaddress_input.getText().toString());
                    editor.commit();
                    String url;
                    url = webaddress_input.getText().toString();
                    if (url.length() >= 8) {
                        if (url.substring(0, 8).equals("https://") || url.substring(0, 7).equals("http://") || url.substring(0, 6).equals("ftp://"))
                            web.loadUrl(url);
                        else {
                            url = "http://" + url;
                            web.loadUrl(url);
                        }
                        System.out.println(01);
                    } else if (url.length() >= 7) {
                        if (url.substring(0, 7).equals("http://") || url.substring(0, 6).equals("ftp://"))
                            web.loadUrl(url);
                        else {
                            url = "http://" + url;
                            web.loadUrl(url);
                        }
                        System.out.println(02);
                    } else if (url.length() >= 6) {
                        if (url.substring(0, 6).equals("ftp://")) web.loadUrl(url);
                        else {
                            url = "http://" + url;
                            web.loadUrl(url);
                        }
                        System.out.println(03);
                    } else {
                        System.out.println(04);
                        String error_address = input.getString("input", "");
                        SharedPreferences SearchEngine_sPref = getSharedPreferences("selectSearchEngine", 0);
                        int SearchEngine = SearchEngine_sPref.getInt("selectSearchEngine", 0);
                        switch (SearchEngine) {
                            case 0: {
                                String error_input_address = "https://www.google.com/search?q=" + error_address;
                                web.loadUrl(error_input_address);
                                break;
                            }
                            case 1: {
                                String error_input_address = "https://www.google.jp/search?q=" + error_address;
                                web.loadUrl(error_input_address);
                                break;
                            }
                            case 2: {
                                String error_input_address = "http://search.naver.com/search.naver?query=" + error_address;
                                web.loadUrl(error_input_address);
                                break;
                            }
                            case 3: {
                                String error_input_address = "http://search.daum.net/" + error_address;
                                web.loadUrl(error_input_address);
                                break;
                            }
                        }
                    }
                }
                return false;
            }
        });


        webpage_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("버튼 클릭");
                SharedPreferences input = getSharedPreferences("input", 0);
                SharedPreferences.Editor editor = input.edit();
                editor.putString("input", webaddress_input.getText().toString());
                editor.commit();
                String url;
                url = webaddress_input.getText().toString();
                if (url.length() == 0) {
                    Toast.makeText(getApplicationContext(), "d", Toast.LENGTH_SHORT).show();
                } else if (url.length() >= 8) {
                    if (url.substring(0, 8).equals("https://") || url.substring(0, 7).equals("http://") || url.substring(0, 6).equals("ftp://"))
                        web.loadUrl(url);
                    else {
                        url = "http://" + url;
                        web.loadUrl(url);
                    }
                    System.out.println(01);
                } else if (url.length() >= 7) {
                    if (url.substring(0, 7).equals("http://") || url.substring(0, 6).equals("ftp://"))
                        web.loadUrl(url);
                    else {
                        url = "http://" + url;
                        web.loadUrl(url);
                    }
                    System.out.println(02);
                } else if (url.length() >= 6) {
                    if (url.substring(0, 6).equals("ftp://")) web.loadUrl(url);
                    else {
                        url = "http://" + url;
                        web.loadUrl(url);
                    }
                    System.out.println(03);
                } else {
                    System.out.println(04);
                    String error_address = input.getString("input", "");
                    SharedPreferences SearchEngine_sPref = getSharedPreferences("selectSearchEngine", 0);
                    int SearchEngine = SearchEngine_sPref.getInt("selectSearchEngine", 0);
                    switch (SearchEngine) {
                        case 0: {
                            String error_input_address = "https://www.google.com/search?q=" + error_address;
                            web.loadUrl(error_input_address);
                            break;
                        }
                        case 1: {
                            String error_input_address = "https://www.google.jp/search?q=" + error_address;
                            web.loadUrl(error_input_address);
                            break;
                        }
                        case 2: {
                            String error_input_address = "http://search.naver.com/search.naver?query=" + error_address;
                            web.loadUrl(error_input_address);
                            break;
                        }
                        case 3: {
                            String error_input_address = "http://search.daum.net/" + error_address;
                            web.loadUrl(error_input_address);
                            break;
                        }
                    }
                }
            }

        });

        web.setWebChromeClient(new HashWebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return true;
            }

            public void onProgressChanged(WebView view, int newProgress) {
                browsing_progress.setProgress(newProgress);
                if (newProgress == 100) {

                    String string = web.getUrl().toString();
                    webaddress_input.setText(string);
                    browsing_progress.setVisibility(View.GONE);
                }
            }


        });

        web.setWebViewClient(new HashWebViewClient() {
            public void onPageStarted(WebView web, String url, Bitmap favicon) {
                browsing_progress.setVisibility(View.VISIBLE);
                super.onPageStarted(web, url, favicon);
            }

            public void onPageFinished(WebView view, String url) {

            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                switch (errorCode) {
                    case ERROR_CONNECT:
                }
                web.stopLoading();
                SharedPreferences input = getSharedPreferences("input", 0);
                String error_address = input.getString("input", "");
                boolean error_address_contain = error_address.contains("http://");
                if (error_address_contain != true) {
                    SharedPreferences SearchEngine_sPref = getSharedPreferences("selectSearchEngine", 0);
                    int SearchEngine = SearchEngine_sPref.getInt("selectSearchEngine", 0);
                    switch (SearchEngine) {
                        case 0: {
                            String error_input_address = "https://www.google.com/search?q=" + error_address;
                            web.loadUrl(error_input_address);
                            break;
                        }
                        case 1: {
                            String error_input_address = "https://www.google.jp/search?q=" + error_address;
                            web.loadUrl(error_input_address);
                            break;
                        }
                        case 2: {
                            String error_input_address = "http://search.naver.com/search.naver?query=" + error_address;
                            web.loadUrl(error_input_address);
                            break;
                        }
                        case 3: {
                            String error_input_address = "http://search.daum.net/" + error_address;
                            web.loadUrl(error_input_address);
                            break;
                        }
                    }
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }
            }
        });

    }


    private class HashWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private class HashWebChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            // Should implement this function.
            final String myOrigin = origin;
            final GeolocationPermissions.Callback myCallback = callback;
            AlertDialog.Builder locationService = new AlertDialog.Builder(MainActivity.this);
            final SharedPreferences onLocationService = getSharedPreferences("onLocationService", 0);
            int i = onLocationService.getInt("onLocationService", 0);
            if (i == 0) {
                locationService.setTitle("위치 정보");
                locationService.setMessage("웹 사이트에서 위치 정보에 액세스하려 합니다. 허용하시겠습니까?");
                locationService.setPositiveButton("허용", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myCallback.invoke(myOrigin, true, false);
                        SharedPreferences.Editor onLocationServiceEditor = onLocationService.edit();
                        onLocationServiceEditor.putInt("onLocationService", 1);
                        onLocationServiceEditor.commit();
                    }
                });

                locationService.setNegativeButton("거부", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myCallback.invoke(myOrigin, false, false);
                    }
                });
                AlertDialog alert = locationService.create();
                alert.show();
            } else {
                myCallback.invoke(myOrigin, true, false);
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (mBackPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            android.os.Process.killProcess(android.os.Process.myPid());
            return;
        } else
            Toast.makeText(getApplicationContext(), "다시 한번 누르면 종료됩니다", Toast.LENGTH_SHORT).show();
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView web = (WebView) findViewById(R.id.web_hash);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (event.isLongPress()) {
                super.onBackPressed();
            }
            if (web.canGoBack()) {
                web.goBack();
                return true;
            } else onBackPressed();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings_intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settings_intent);
            return true;
        } else if (id == R.id.action_history) {
            Intent history_intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(history_intent);
        } else Toast.makeText(getApplicationContext(), "준비중입니다", Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}