package com.example.lunara310522;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private Button btnTeste;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img =  (ImageView) findViewById(R.id.imgR);
        btnTeste = (Button) findViewById(R.id.button);
        btnTeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // podemos colocar a nossa logica aqui, como por exemplo o acesso a rede;
                        // para consultar a http, usamos  a classe URL
                        try {
                            URL githubEndpoint = new URL("https://dog.cep/api/breeds/image/random");
                            //"https://api.github.com/");
                            // Para abrir a conexão
                            HttpsURLConnection conexao = (HttpsURLConnection) githubEndpoint.openConnection();

                            // Muitas apis restful pede quem quer a conexao
                            // Cabeçalho de http: usuario, tamanho, tipo de dados e se precisa fazer o contato. Ajustando o cabeçalho do http para que o endpoint
                            // aceite a solicitacao
                            conexao.setRequestProperty("User-Agent", "minha-rest-v0.1");
                            conexao.setRequestProperty("Accept", "application/vnd.github.v3+json");
                            conexao.setRequestProperty("Contact-Me", "cc21106@g.unicamp.br");

                            // Método getResponse: conecta e espera resposta
                            if (conexao.getResponseCode() == 200) {
                                // já obtenho os dados da resposta http
                                InputStream corpo = conexao.getInputStream();
                                InputStreamReader corpoReader = new InputStreamReader(corpo, "UTF-8");
                                // aqui desmontamos o json e podemos usá-lo
                                JsonReader jsonReader = new JsonReader(corpoReader);
                                // aqui vou extrair as chaves do json recebido...

                                jsonReader.beginObject(); // inicia o processamento do json
                                while (jsonReader.hasNext()) { // faz um loop por todas as chaves
                                    String key = jsonReader.nextName();

                                    if (key.equals("message")) {
                                        String local = jsonReader.nextString();
                                        try {
                                            InputStream in = new java.net.URL(local).openStream();
                                            Bitmap fig = BitmapFactory.decodeStream(in);
                                            img.setImageBitmap(fig);
                                        } catch (Exception e) { // fim do try da linha 72
                                            Log.e("Get image", e.getMessage());
                                        } // fim do catch da linha 76
                                        //if (key.equals("organization_url")) {
                                        //String value = jsonReader.nextString();
                                        //Log.e("CHAVE", String.format("\n chave %s valor %s \n", key, value));
                                    } else { //fim do if da linha 70
                                        jsonReader.skipValue();// pula para a próxima chave
                                    } // fim do else da linha 82

                                    jsonReader.close();
                                    conexao.disconnect();
                                } //fim do while da linha 67
// fim do if da linha 58 na linha 90
                            } else {
                                Log.e("RESPONSE", "ERRO");
                                // colocar uma label para indicar a falha
                            }
// fim do try da linha 44 na linha 95
                        } catch (Exception e) {
                            Log.e("URL", e.getMessage());
                        } // fim do catch da linha 95

                    }// fim do run da linha 41


                }); // fim do AsyncTask da linha 39
            } // fim do onClick da linha 38
        }); // fim do onClickListener da linha 36

    } // fim do onCreate da linha 31
} // fim fo AppCompactActivity da linha 25