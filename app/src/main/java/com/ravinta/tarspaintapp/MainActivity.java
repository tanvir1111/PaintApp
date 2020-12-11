package com.ravinta.tarspaintapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.UUID;

import static com.ravinta.tarspaintapp.WhiteBoard.LARGE_BRUSH;
import static com.ravinta.tarspaintapp.WhiteBoard.MEDIUM_BRUSH;
import static com.ravinta.tarspaintapp.WhiteBoard.SMALL_BRUSH;

public class MainActivity extends AppCompatActivity {

    WhiteBoard whiteBoard;
    View currentBrushColor;
    ImageView currentBrushSizeSelected,eraser;
    Boolean eraseMode=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        currentBrushColor=findViewById(R.id.view);
        currentBrushColor.setBackground(ContextCompat.getDrawable(this,R.drawable.color_circle_selected));
        currentBrushSizeSelected=findViewById(R.id.smallBrush);
        whiteBoard=findViewById(R.id.paintView);
        eraser=findViewById(R.id.eraser);


   }

    public void setBrushColor(View view){
        if(eraseMode){
            Toast.makeText(this, "Select a brush first! Currently eraser is activated!", Toast.LENGTH_SHORT).show();
        }
        else {
            if (view != currentBrushColor) {
                currentBrushColor.setBackground(ContextCompat.getDrawable(this, R.drawable.color_circle));
                view.setBackground(ContextCompat.getDrawable(this, R.drawable.color_circle_selected));
                currentBrushColor = view;
                whiteBoard.setBrushColor(view.getTag().toString());
            }
        }

    }


    public void setBrushSize(View view) {
        if(currentBrushSizeSelected==view){}
        else {
            view.setBackground(ContextCompat.getDrawable(this,R.drawable.rectangle_border));
            currentBrushSizeSelected.setBackground(null);
            currentBrushSizeSelected=findViewById(view.getId());
            int id = view.getId();
            if (id == R.id.eraser) {
                eraseMode=true;
                whiteBoard.setErase(eraseMode);
                currentBrushColor.setBackground(ContextCompat.getDrawable(this,R.drawable.color_circle));
            } else {
                eraseMode=false;
                currentBrushColor.setBackground(ContextCompat.getDrawable(this,R.drawable.color_circle_selected));
                whiteBoard.setErase(false);
                eraser.setBackground(null);
                if (id == R.id.largeBrush) {
                    whiteBoard.setBrushSize(LARGE_BRUSH);
                } else if (id == R.id.mediumBrush) {
                    whiteBoard.setBrushSize(MEDIUM_BRUSH);
                } else if (id == R.id.smallBrush) {
                    whiteBoard.setBrushSize(SMALL_BRUSH);

                }
            }
        }
    }

    public void reset(View view) {
        whiteBoard.reset();
    }

    public void saveToGallery(View view) {
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Save drawing");
        saveDialog.setMessage("Save drawing to device Gallery?");
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                whiteBoard.setDrawingCacheEnabled(true);
                String imgSaved = MediaStore.Images.Media.insertImage(
                        getContentResolver(), whiteBoard.getDrawingCache(),
                        UUID.randomUUID().toString()+".png", "drawing");
                if(imgSaved!=null){
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                    savedToast.show();
                }
                else{
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
                whiteBoard.destroyDrawingCache();

            }
        });
        saveDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        saveDialog.show();

    }



}