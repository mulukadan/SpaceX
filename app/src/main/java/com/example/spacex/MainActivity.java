package com.example.spacex;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spacex.ADAPTERS.LauchAdapter;
import com.example.spacex.API.Client;
import com.example.spacex.API.Endpoints;
import com.example.spacex.API.Service;
import com.example.spacex.MODELS.Launch;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;

import static java.time.temporal.ChronoUnit.DAYS;

public class MainActivity extends AppCompatActivity {
    private ImageView filter, closeFilterdialog;
    private TextView companyInfoTV;
    private RecyclerView launchesRV;
    private SweetAlertDialog pDialog;
    private Button applyBtn;
    private EditText filterYrs;
    private RadioButton radio1, radio2, radio11, radio22, radioFilter;
    private RadioGroup radio_group;

    private LauchAdapter launchAdapter;

    private List<Launch> Alllaunches = new ArrayList<>();
    private List<Launch> displayingLaunches = new ArrayList<>();

    private Boolean hasMoreLaunches = true;
    private final int sizeLimit = 10;
    private int currentOffset = 0;
    private Dialog filterDialog;
    private LinearLayout filterLayout, sortLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        filter = findViewById(R.id.filter);

        companyInfoTV = findViewById(R.id.companyInfoTV);
        launchesRV = findViewById(R.id.launchesRV);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);

        filterDialog = new Dialog(this);
        filterDialog.setCanceledOnTouchOutside(false);
        filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        filterDialog.setContentView(R.layout.filter_dialog);

        closeFilterdialog = filterDialog.findViewById(R.id.closeFilterdialog);
        filterYrs = filterDialog.findViewById(R.id.filterYrs);
        applyBtn = filterDialog.findViewById(R.id.applyBtn);
        sortLayout = filterDialog.findViewById(R.id.sortLayout);
        filterLayout = filterDialog.findViewById(R.id.filterLayout);
        radio_group = filterDialog.findViewById(R.id.radio_group);
        sortLayout.setVisibility(View.GONE);
        radio1 = filterDialog.findViewById(R.id.radio1);
        radio11 = filterDialog.findViewById(R.id.radio11);
        radioFilter = filterDialog.findViewById(R.id.radioFilter);
        radio22 = filterDialog.findViewById(R.id.radio22);
        radio2 = filterDialog.findViewById(R.id.radio2);

        closeFilterdialog.setOnClickListener(v -> {
            displayingLaunches.clear();
            displayingLaunches.addAll(Alllaunches);
            launchAdapter.notifyDataSetChanged();
            filterYrs.setText("");
            filterDialog.dismiss();
        });
        filter.setOnClickListener(v -> {
            filterDialog.show();
        });

        radio_group.setOnCheckedChangeListener((group, checkedId) -> {
            if (radioFilter.isChecked()) {
                filterLayout.setVisibility(View.VISIBLE);
                sortLayout.setVisibility(View.GONE);
            } else {
                filterLayout.setVisibility(View.GONE);
                sortLayout.setVisibility(View.VISIBLE);
            }
        });

        applyBtn.setOnClickListener(v -> {
            if (radioFilter.isChecked()) {
                //Filtering
                String filterYear = filterYrs.getText().toString().trim();
                int launchStatus = 1;
                if (radio1.isChecked()) {
                    launchStatus = 1;
                } else if (radio2.isChecked()) {
                    launchStatus = 2;
                } else {
                    launchStatus = 3;
                }

                filter(filterYear, launchStatus);
            } else {
                //Sort
                displayingLaunches.clear();
                displayingLaunches.addAll(Alllaunches);
                if (radio11.isChecked()) {
                    //Sortby Year
                    displayingLaunches.sort(Comparator.comparing(Launch::getLaunch_year));
                } else {
                    displayingLaunches.sort(Comparator.comparing(Launch::getSuccessful));
                }

                if (radio22.isChecked()) {
                    Collections.reverse(displayingLaunches);
                }
                launchAdapter.notifyDataSetChanged();
                filterDialog.dismiss();
            }


        });

        launchesRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        launchesRV.smoothScrollToPosition(0);

        launchAdapter = new LauchAdapter(getApplicationContext(), displayingLaunches);
        launchesRV.setAdapter(launchAdapter);
        launchAdapter.setOnItemClick(clickedlaunch -> showOpenOptionsDialog(clickedlaunch));

        loadCompanyInfo();
    }

    private void showOpenOptionsDialog(Launch clickedlaunch) {
        Dialog openOptionsDialog;
        ImageView icon, closeDialog;
        Button articleBtn, wikipediaBtn, youtubeBtn;
        TextView title;

        openOptionsDialog = new Dialog(this);
        openOptionsDialog.setCanceledOnTouchOutside(false);
        openOptionsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        openOptionsDialog.setContentView(R.layout.open_options_dialog);

        icon = openOptionsDialog.findViewById(R.id.icon);
        closeDialog = openOptionsDialog.findViewById(R.id.closeDialog);
        articleBtn = openOptionsDialog.findViewById(R.id.articleBtn);
        wikipediaBtn = openOptionsDialog.findViewById(R.id.wikipediaBtn);
        youtubeBtn = openOptionsDialog.findViewById(R.id.youtubeBtn);
        title = openOptionsDialog.findViewById(R.id.title);

        closeDialog.setOnClickListener(v -> {
            openOptionsDialog.dismiss();
        });
        title.setText(clickedlaunch.getMission());
        Picasso.get().load(clickedlaunch.getImgUrl()).fit().centerCrop()
                .placeholder(R.drawable.image_search_24)
                .error(R.drawable.red_button_background)
                .into(icon);

        articleBtn.setOnClickListener(v1 -> {
            OpenLink(clickedlaunch.getArticle_link());
        });
        wikipediaBtn.setOnClickListener(v1 -> {
            OpenLink(clickedlaunch.getWikipedia());
        });
        youtubeBtn.setOnClickListener(v1 -> {
            OpenLink(clickedlaunch.getVideo_link());
        });
        openOptionsDialog.show();
    }

    public void OpenLink(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void loadCompanyInfo() {
        pDialog.show();
        try {
            Service apiService = Client.getInfo(Endpoints.COMPANY_INFO).create(Service.class);
            Call<String> call = apiService.getRequestResponse();
            call.enqueue(new retrofit2.Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> Response) {
                    if (Response.isSuccessful()) {//

                        try {
                            JSONObject jsonObject = new JSONObject(Response.body().toString());
                            String companyNAme = jsonObject.get("name").toString();
                            String founder = jsonObject.get("founder").toString();
                            String founded = jsonObject.get("founded").toString();
                            String employees = jsonObject.get("employees").toString();
                            String launch_sites = jsonObject.get("launch_sites").toString();
                            String valuation = jsonObject.get("valuation").toString();
                            String info = companyNAme + " was founded by " + founder + " in " + founded + ". It has now " + employees + " employees, " +
                                    launch_sites + " launch sites, and is valued at USD " + valuation;
                            companyInfoTV.setText(info);

                            getLaunches(sizeLimit, currentOffset);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                        Toast.makeText(getApplicationContext(), "Error Fecthing Data!\nPlease try Again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error Fecthing Data!\n Check your  Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getLaunches(int limit, int offset) {
        try {
            pDialog.setTitleText("Loading Launches ...");
            Service apiService = Client.getInfo(Endpoints.LAUNCH_INFO + "?limit=" + limit + "&offset=" + offset).create(Service.class);
            Call<String> call = apiService.getRequestResponse();
            call.enqueue(new retrofit2.Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> Response) {
                    if (Response.isSuccessful()) {//
                        JsonParser parser = new JsonParser();
                        JsonArray jsonArray = parser.parse(String.valueOf(Response.body())).getAsJsonArray();
                        int arraySize = jsonArray.size();
                        if (arraySize == sizeLimit) {
                            hasMoreLaunches = true;
                        } else {
                            hasMoreLaunches = false;
                        }
                        for (int i = 0; i < arraySize; i++) {
                            try {
                                JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                                String mission, launch_date_local, date, time, daysBetween, rocketName, rocketType, imgUrl, launch_year, article_link, wikipedia, video_link;
                                Boolean launch_success;
                                mission = jsonObject.get("mission_name").getAsString();
                                launch_year = jsonObject.get("launch_year").getAsString();
                                launch_success = jsonObject.get("launch_success").getAsBoolean();
                                launch_date_local = jsonObject.get("launch_date_local").getAsString();
                                date = dateFormat(launch_date_local, "date");
                                time = dateFormat(launch_date_local, "time");
                                daysBetween = dateFormat(launch_date_local, "days");
                                rocketName = jsonObject.get("rocket").getAsJsonObject().get("rocket_name").getAsString();
                                rocketType = jsonObject.get("rocket").getAsJsonObject().get("rocket_type").getAsString();
                                imgUrl = jsonObject.get("links").getAsJsonObject().get("mission_patch").getAsString();
                                article_link = jsonObject.get("links").getAsJsonObject().get("article_link").getAsString();
                                wikipedia = jsonObject.get("links").getAsJsonObject().get("wikipedia").getAsString();
                                video_link = jsonObject.get("links").getAsJsonObject().get("video_link").getAsString();
                                Launch launch = new Launch(mission, date, time, daysBetween, rocketName, rocketType, imgUrl, launch_year, article_link, wikipedia, video_link, launch_success);
                                Alllaunches.add(launch);
                            } catch (Exception e) {

                            }
                        }
                        displayingLaunches.clear();
                        displayingLaunches.addAll(Alllaunches);
                        launchAdapter.notifyDataSetChanged();
                        pDialog.dismiss();

                        if (hasMoreLaunches) {
                            currentOffset = currentOffset + sizeLimit;
                            getLaunches(sizeLimit, currentOffset);
                        }


                    } else {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error Fecthing Data!\nPlease try Again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error Fecthing Data!\n Check your  Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public String dateFormat(String myDateObj, String getWhat) {
        ZonedDateTime result = ZonedDateTime.parse(myDateObj, DateTimeFormatter.ISO_DATE_TIME);

        LocalDate localDate = result.toLocalDate();
        LocalTime localTime = result.toLocalTime();

        //Date difference
        LocalDate today = LocalDate.now();
        long daysBetween = DAYS.between(today, localDate);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");

        if (getWhat.equals("date")) {
            return formatter.format(localDate);
        } else if (getWhat.equals("date")) {
            return localTime.toString();
        } else {
            return String.valueOf(daysBetween);
        }
    }

    public void filter(String year, int statusOption) {
        pDialog.show();
        filterDialog.dismiss();
        displayingLaunches.clear();
        for (Launch launch : Alllaunches) {
            if (year.length() == 0) {
                if (statusOption == 1) {
                    displayingLaunches.addAll(Alllaunches);
                } else {
                    addLauncesToFilteredListBasedOnStatus(launch, statusOption);
                }
            } else if (year.length() == 4) {
                if (launch.getDate().contains(year)) {
                    addLauncesToFilteredListBasedOnStatus(launch, statusOption);
                }
            }

        }
        launchAdapter.notifyDataSetChanged();
        pDialog.dismiss();
    }

    public void addLauncesToFilteredListBasedOnStatus(Launch launch, int statusOption) {
        switch (statusOption) {
            case 1:
                displayingLaunches.add(launch);
                break;
            case 2:
                if (launch.getSuccessful()) {
                    displayingLaunches.add(launch);
                }
                break;
            case 3:
                if (!launch.getSuccessful()) {
                    displayingLaunches.add(launch);
                }
                break;
        }
    }
}