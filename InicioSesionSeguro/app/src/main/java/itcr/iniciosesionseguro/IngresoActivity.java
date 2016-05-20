package itcr.iniciosesionseguro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import itcr.iniciosesionseguro.R;

public class IngresoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso);
        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre");
        String correo = intent.getStringExtra("correo");
        TextView nombreTV = (TextView)
                findViewById(R.id.textViewNombre);
        TextView correoTV = (TextView)
                findViewById(R.id.textViewCorreo);
        nombreTV.setText(nombre);
        correoTV.setText(correo);
    }

}