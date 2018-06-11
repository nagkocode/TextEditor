
// This is sample app to save and open file.

package com.example.snippet.texteditor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {

    final int READ_REQUEST_CODE  = 1;
    final int WRITE_REQUEST_CODE = 2;

    final String WINDOW_TITLE = "key name";

    // In layout,
    // set background to transparent to remove line at bottom
    // set gravity to top to start cursor from top
    private EditText editText;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // reference control
        editText = (EditText) findViewById(R.id.editText);

        // retain window title on orientation changes
        // window title is saved on onSaveInstanceState function
        if(savedInstanceState != null) {
            str = savedInstanceState.getString(WINDOW_TITLE);
            setTitle(str);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(WINDOW_TITLE, str);
    }

    // create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // handle menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.menu_open:   onMenuOpen();   break;
            case R.id.menu_saveas: onMenuSaveAs(); break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    // handle the result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle read resquest code
        if(requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK){

            Uri uri = null;
            if (data != null) {

                uri = data.getData();
                str = uri.toString();
                setTitle(str);
                openFile(uri);
            }
        }

        // handle write resquest code
        if(requestCode == WRITE_REQUEST_CODE && resultCode == RESULT_OK){

            Uri uri = null;
            if (data != null) {

                uri = data.getData();
                str = uri.toString();
                setTitle(str);
                saveFile(uri);
            }
        }
    }

    // open file menu handler function
    private void onMenuOpen(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/plain");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    // save as file menu handler function
    private void onMenuSaveAs(){
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/plain");

        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    // open file for reading
    private void openFile(Uri uri){
        ParcelFileDescriptor pfd;
        FileInputStream fis;
        byte[] b;
        int n;
        String str;

        try{
            pfd = getContentResolver().openFileDescriptor(uri, "r");
            fis = new FileInputStream(pfd.getFileDescriptor());
            n = fis.available();
            b = new byte[n];
            fis.read(b);
            str = new String(b);
            fis.close();
            pfd.close();

            editText.setText(str);

        } catch (FileNotFoundException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    // open file for writing
    private void saveFile(Uri uri){
        ParcelFileDescriptor pfd;
        FileOutputStream fos;
        String str;

        try{

            str = editText.getText().toString();

            pfd = getContentResolver().openFileDescriptor(uri, "w");
            fos = new FileOutputStream(pfd.getFileDescriptor());
            fos.write(str.getBytes());
            fos.close();
            pfd.close();

            Toast.makeText(this, "Na save na.", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
