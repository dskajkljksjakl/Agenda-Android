package br.com.adsnoobs.myapplication;

import android.app.Service;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText editCodigo, editNome, editTelefone, editEmail;

    Button btnLimpar, btnSalvar, btnExcluir;
    ListView ListViewClientes;

    BancoDados db = new BancoDados(this);

    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;

    InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editCodigo = (EditText) findViewById(R.id.editCodigo);
        editNome = (EditText) findViewById(R.id.editNome);
        editTelefone = (EditText) findViewById(R.id.editTelefone);
        editEmail = (EditText) findViewById(R.id.editEmail);

        btnLimpar = (Button) findViewById(R.id.btnLimpar);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnExcluir = (Button) findViewById(R.id.btnExcluir);

        imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);

        ListViewClientes = (ListView) findViewById(R.id.ListViewClientes);
          listarClientes();

          btnLimpar.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  limpaCampos();
              }
          });

          ListViewClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                  String conteudo = (String) ListViewClientes.getItemAtPosition(position);

                  // Toast.makeText(MainActivity.this,"Select:" + conteudo, Toast.LENGTH_LONG).show();
                  String codigo = conteudo.substring(0, conteudo.indexOf("-"));
                  Cliente cliente = db.selecionarCliente(Integer.parseInt(codigo));

                  editCodigo.setText(String.valueOf( cliente.getCodigo()));
                  editNome.setText(cliente.getNome());
                  editTelefone.setText(cliente.getTelefone());
                  editEmail.setText(cliente.getEmail());
              }
          });

          btnSalvar.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  String codigo = editCodigo.getText().toString();
                  String nome = editNome.getText().toString();
                  String telefone = editTelefone.getText().toString();
                  String email = editEmail.getText().toString();
                  if(nome.isEmpty()) {
                      editNome.setError("Este campo é obrigatório");

                  } else if (codigo.isEmpty()){
                      db.addCliente(new Cliente(nome, telefone,email));
                      Toast.makeText(MainActivity.this,"Cliente adicionado com sucesso!", Toast.LENGTH_LONG).show();


                      limpaCampos();
                      listarClientes();
                      escondeTeclado();
                      } else {
                      db.atualizaCliente(new Cliente(Integer.parseInt(codigo), nome, telefone,email));
                      Toast.makeText(MainActivity.this,"Cliente atualizado com sucesso!", Toast.LENGTH_LONG).show();


                      limpaCampos();
                      listarClientes();
                      escondeTeclado(); 
                  }
                  }

          });

          btnExcluir.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  String codigo = editCodigo.getText().toString();

                  if (codigo.isEmpty()) {
                      Toast.makeText(MainActivity.this, "Nenhum cliente está selecionado!", Toast.LENGTH_LONG).show();
                  } else {
                       Cliente cliente = new Cliente();
                       cliente.setCodigo(Integer.parseInt(codigo));

                      db.apagarCliente(cliente);
                      Toast.makeText(MainActivity.this,"Cliente excluido com sucesso!", Toast.LENGTH_LONG).show();


                      limpaCampos();
                      listarClientes();
                  }
              }
          });
        // db.addCliente(new Cliente("joao", "98136624", "joaoalvaro1994@gmail.com"));

        // Cliente cliente = new Cliente();
        // cliente.setCodigo(3);

       //db.apagarCliente(cliente);

        // Toast.makeText(MainActivity.this,"Apagado com sucesso!", Toast.LENGTH_LONG).show();
//select ok
        //Cliente cliente = db.selecionarCliente(4);

        // Log.d("Cliente Seleciondo","Codigo:" + cliente.getCodigo()+ "Nome:" + cliente.getNome()+ "Telefone: " +cliente.getTelefone() + "Email: " + cliente.getEmail());
//update ok
/*
        Cliente cliente = new Cliente();
        cliente.setCodigo(4);
        cliente.setNome("Carlos Pereira da Silva");
        cliente.setTelefone("33333333");
        cliente.setEmail("carlos@pereira.com");

        db.atualizaCliente(cliente);

        Toast.makeText(MainActivity.this, "Atualizado com sucesso!", Toast.LENGTH_LONG).show();
        */
    }
void escondeTeclado(){

        imm.hideSoftInputFromWindow(editNome.getWindowToken(),0);
}
    void limpaCampos(){
        editCodigo.setText("");
        editNome.setText("");
        editTelefone.setText("");
        editEmail.setText("");

        editNome.requestFocus();
    }

    public  void listarClientes(){

        List<Cliente> clientes = db.listatodosClientes();

        arrayList = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
        ListViewClientes.setAdapter(adapter);

        for(Cliente c : clientes ) {
           // Log.d("Lista","\nID:" + c.getCodigo() + "Nome:" + c.getNome());
            arrayList.add(c.getCodigo() + "-" + c.getNome());
            adapter.notifyDataSetChanged();

        }
    }
}


