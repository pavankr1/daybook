package com.one.daybook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by one on 4/19/2016.
 */
public class AddNoteDialog  extends android.app.DialogFragment{
    EditText et1;
    String foldername;
    FolderListDB fdb;
    String fname ="Daybook";

    ArrayList<String> flist;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = null;
        View v=getActivity().getLayoutInflater().inflate(R.layout.notes_addnotedialog,null);
        et1= (EditText) v.findViewById(R.id.editText);

        fdb=new FolderListDB(getActivity());
        fdb.openDB();
        AlertDialog.Builder ab=new AlertDialog.Builder(getActivity());
        ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                foldername = et1.getText().toString();
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fname;
                String path2 = path + "/" + foldername;



                File f1 = new File(path);
                File f2 = new File(path2);
                if (!f1.exists()) {
                    f1.mkdir();
                    if (!f2.exists()) {

                        //File f2 = new File(path2);
                        f2.mkdir();
                        //fdb.insertIntoDB(foldername);
                    }

                }
                if (f1.exists()) {

                    if (!f2.exists()) {

                        //File f2 = new File(path2);
                        f2.mkdir();
                        //fdb.insertIntoDB(foldername);
                    }

                }
                Toast.makeText(getActivity(), "notebook has been created" + f2.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        ab.setView(v);
        d=ab.create();

        return d;

        //return super.onCreateDialog(savedInstanceState);
    }
}
