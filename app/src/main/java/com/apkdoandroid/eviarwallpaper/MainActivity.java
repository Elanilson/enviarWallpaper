package com.apkdoandroid.eviarwallpaper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private EditText  editcategoria ,editcategoriaWallpaper;
    private Button btCategoria,btn_imagem;
    private static  final int VERSAO_CODE =100;
    private static  final int VERSAO_CODE2 =200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editcategoria = findViewById(R.id.editCategoria);
        editcategoriaWallpaper = findViewById(R.id.editCategoriaWallpaper);
        btCategoria = findViewById(R.id.btImagemCategoria);
        btn_imagem = findViewById(R.id.btImagemWalpaper);

        btCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editcategoria.getText().toString().equals(" ")){
                    salvarCategoria();

                }
            }
        });


        btn_imagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editcategoriaWallpaper.getText().toString().equals(" ")){
                    salvarWalpaper();

                }
            }
        });
    }

    private void salvarWalpaper() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,VERSAO_CODE2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null){
            Uri uri =data.getData();

            try{

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,70,stream);

                byte[] byteImagem = stream.toByteArray();

                if(requestCode == VERSAO_CODE){
                    ParseFile file = new ParseFile("imagem_categoria.png",byteImagem);
                    ParseObject object = new ParseObject("Categorias");
                    object.put("tipo","categorias");
                    object.put("categoria",editcategoria.getText().toString()); // aqui é o que o usuario digitou
                    object.put("img_categoria",file);

                    // salvando os dados
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e != null){
                                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(MainActivity.this, "Categoria salva com sucesso", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

                if(requestCode == VERSAO_CODE2){
                    ParseFile file = new ParseFile("imagem_walpaper.png",byteImagem);
                    ParseObject object = new ParseObject("Wallpapers");
                    object.put("tipo","wallpaper");
                    object.put("categoria",editcategoriaWallpaper.getText().toString()); // aqui é o que o usuario digitou
                    object.put("img_wallpaper",file);

                    // salvando os dados
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e != null){
                                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(MainActivity.this, "Wallpaper salva com sucesso", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }catch (FileNotFoundException e){
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void salvarCategoria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,VERSAO_CODE);
    }
}