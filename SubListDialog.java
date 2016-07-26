package com.one.daybook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by one on 4/19/2016.
 */
public class SubListDialog extends DialogFragment {

    ListView lv;
    int position1;
    Image image1;
    ArrayList<String> mylist;
    String s1;
    ArrayAdapter aa;
    MyAdapter m;
    String path;
    Dialog d=null;
    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mylist.size();
        }

        @Override
        public Object getItem(int i) {
            return mylist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v=getActivity().getLayoutInflater().inflate(R.layout.notes_row,null);
                TextView tv= (TextView) v.findViewById(R.id.text1);
            String sname=mylist.get(i);
            tv.setText(sname);


            return v;
        }
    }

    public void pdfsave(String fldrname){

        //Bitmap bitmap = drawView.getDrawingCache();
        File file=new File(path +"/LearningUmbrella" +"/"+fldrname);
        File list[] = file.listFiles();

        //String page1=fldrname+ UUID.randomUUID().toString() +".pdf";
         String page1=fldrname+".pdf";
        try {
             image1=Image.getInstance(list[0].toString());
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document document = new Document(image1);
    String path1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "PDFFiles";
    File pdffol = new File(path1);
    if (!pdffol.exists()) {
        pdffol.mkdir();
    }
    File pdffile = new File(path1 + "/" + page1);

    try {


        PdfWriter.getInstance(document, new FileOutputStream(pdffile));
        //float documentWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
        // float documentHeight = document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin();


        document.open();

        for (int i = 0; i < list.length; i++) {


            float documentWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
            float documentHeight = document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin();
            Image image = Image.getInstance(list[i].toString());
            float imgw = image.getWidth();
            float imagh = image.getHeight();
            document.setPageSize(image);
            image.scaleToFit(documentWidth, documentHeight);
           // image.scaleToFit(imgw, imagh);
            //image.setAbsolutePosition(0, 0);
            document.setPageSize(image);

            // Toast.makeText(this,notes_list.length+"",Toast.LENGTH_LONG).show();
            document.add(image);
            document.newPage();

            if (i == list.length - 1) {
                document.close();
                return;
            }
        }


    } catch (DocumentException e) {
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

    }
    public void deleteFolder(String fldrname){

        AlertDialog.Builder ab1= new AlertDialog.Builder(getActivity());
        ab1.setTitle("Folder Deletion");
        ab1.setMessage("Do you Want to delete the folder" + " " + fldrname +"?");
        ab1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String dname=mylist.get(position1);

                File dfile = new File(path + "/LearningUmbrella"+ "/"+mylist.get(position1));
                mylist.remove(position1);
                m.notifyDataSetChanged();

                if ( dfile.isDirectory() )
                {
                    String[] items = dfile.list();
                    for ( int j = 0 ; j < items.length ; j ++ )
                    {
                        File child =    new File( dfile , items[j] );

                        child.delete();
                    }
                    dfile.delete();
                }
            }
        });
        ab1.setNegativeButton("No", null);
        ab1.create();
        ab1.show();

    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        if(v.getId()==R.id.listView){
        MenuInflater inflater=getActivity().getMenuInflater();
        inflater.inflate(R.menu.notes_pop_up_menu, menu);}
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) menuInfo;
     s1=mylist.get(info.position);
        position1=info.position;
        //Toast.makeText(getActivity(),s1+position1,Toast.LENGTH_LONG).show();
        for(int i=0;i<menu.size();i++){


            menu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.delete:
                            //Toast.makeText(getActivity(),menuInfo.toString(),Toast.LENGTH_LONG).show();
                            deleteFolder(s1);
                            break;
                        case R.id.pdf:
                           // menu.getItem(i);
                          pdfsave(s1);
                            Toast.makeText(getActivity(),"converted to pdf", Toast.LENGTH_LONG).show();
                             break;

                    }
                    return false;
                }
            });
        }

    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Dialog d=null;
        View v=getActivity().getLayoutInflater().inflate(R.layout.notes_sublistd,null);
        lv= (ListView) v.findViewById(R.id.listView);

       //mylist=new LinkedList<>();
       mylist=new ArrayList<String>();
        AlertDialog.Builder ab= new AlertDialog.Builder(getActivity());

        //String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";


         path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File( path + "/LearningUmbrella" ) ;
        File list[] = file.listFiles();

        for( int i=0; i<list.length; i++)
        {
            mylist.add(list[i].getName());


        }

        m = new MyAdapter();
        lv.setAdapter(m);
        registerForContextMenu(lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String name = mylist.get(i);
                //SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy" + " " + "HH:mm:ss");
                //String date = s.format(new Date());

                //Toast.makeText(getActivity(), name + " " + date, Toast.LENGTH_LONG).show();
                ((notes_MainActivity) getActivity()).saveFolder(name);
                d.dismiss();


            }
        });





       /* lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

               position1=i;
                AlertDialog.Builder ab1= new AlertDialog.Builder(getActivity());
                ab1.setTitle("Folder Deletion");
                ab1.setMessage("Do you Want to delete the folder" + " " + mylist.get(i) +"?");
                ab1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String dname=mylist.get(position1);

                        File dfile = new File(path + "/LearningUmbrella"+ "/"+mylist.get(position1));
                        mylist.remove(position1);
                        m.notifyDataSetChanged();

                        if ( dfile.isDirectory() )
                        {
                            String [] items = dfile.notes_list();
                            for ( int j = 0 ; j < items.length ; j ++ )
                            {
                                File child =    new File( dfile , items[j] );

                                child.delete();
                            }
                            dfile.delete();
                        }
                    }
                });
                ab1.setNegativeButton("No", null);
                ab1.create();
                ab1.show();

               // Toast.makeText(getActivity(),"hi",Toast.LENGTH_LONG).show();

                return true;
            }
        });*/


        //ab.setPositiveButton("Back", null);
        //ab.setTitle("Subject List");
        ab.setView(v);
        d=ab.create();

        return d;
        //return super.onCreateDialog(savedInstanceState);
    }




}
