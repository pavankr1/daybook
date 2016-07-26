package com.one.daybook;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.set.drawing.adapter.GridViewImageAdapter;
import com.set.drawing.helper.AppConstant;
import com.set.drawing.helper.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by one on 3/14/2016.
 */
public class GridViewActivity extends Activity {


    private Utils utils;
    private ArrayList<String> imagePaths = new ArrayList<String>();
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private int columnWidth;
    String path;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_activity_grid_view);
        Intent i =getIntent();
        Bundle b=i.getExtras();
         path=b.getString("path");
         name=b.getString("name");
        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#634646")));
        }
        gridView = (GridView) findViewById(R.id.grid_view);

        utils = new Utils(this);

        // Initilizing Grid View
        InitilizeGridLayout();

        // loading all image paths from SD card
        imagePaths = utils.getFilePaths();



        // Gridview adapter
        adapter = new GridViewImageAdapter(GridViewActivity.this, imagePaths,
                columnWidth);

        // setting grid view adapter
        gridView.setAdapter((ListAdapter) adapter);

        //gridView.setLongClickable(true);

       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Intent intent = new Intent(GridViewActivity.this, FullScreenViewActivity.class);
               intent.putExtra("position", i);
               startActivity(intent);
               //Toast.makeText(GridViewActivity.this,"short", Toast.LENGTH_LONG).show();
           }
       });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int pos1=i;

                AlertDialog.Builder ab= new AlertDialog.Builder(GridViewActivity.this);
                ab.setTitle("Remove Page?");
                ab.setMessage("This page will be removed.This action cannot be undone" );
                ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String img = imagePaths.get(pos1);
                        imagePaths.remove(pos1);

                        adapter.notifyDataSetChanged();

                        File sname = new File(path);
                        if (sname.isDirectory()) {
                            String[] items = sname.list();
                            for (int j = 0; j < items.length; j++) {

                                File child = new File(sname, items[j]);
                                if (img.contains(items[j])) {

                                    child.delete();
                                    Toast.makeText(GridViewActivity.this, img, Toast.LENGTH_LONG).show();
                                }

                            }
                        }

                    }
                });
                ab.setNegativeButton("CANCEL", null);
                ab.create();
                ab.show();



                return true;
            }
        });
    }

    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConstant.GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

        gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }



}
