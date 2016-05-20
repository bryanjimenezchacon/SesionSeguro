package itcr.iniciosesionseguro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import itcr.iniciosesionseguro.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class RegistroActivity extends AppCompatActivity {

    public DBHandler db = new DBHandler(this);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Button registrar = (Button)
                findViewById(R.id.botonRegistrarNuevoUsuario);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nombre = (EditText)
                        findViewById(R.id.campo_registro_nombre);
                EditText email = (EditText)
                        findViewById(R.id.campo_registro_eMail);
                EditText pass = (EditText)
                        findViewById(R.id.campo_registro_pwd);
                EditText passval = (EditText)
                        findViewById(R.id.campo_verificacion_pwd);
                String contraseña = pass.getText().toString();
                String validarContraseña =
                        passval.getText().toString();
                if (contraseña.equals(validarContraseña)) {
                    String MD5Pass = md5(contraseña);
                    db.addUsuario(new Usuario(1,nombre.getText().toString(),
                            email.getText().toString(), MD5Pass));
                    Toast.makeText(RegistroActivity.this,"Usuario registrado con exito",Toast.LENGTH_LONG);
                            RegistroActivity.this.finish();
                }else{
                    Toast.makeText(RegistroActivity.this,"Las contraseñas no coinciden",Toast.LENGTH_LONG);
                }
            }
        });
    }
    public static String md5(String string) {

        try {
            MessageDigest digest =
                    java.security.MessageDigest.getInstance("MD5");
            digest.update(string.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF &
                        messageDigest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}