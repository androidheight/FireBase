package com.androdheight.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androdheight.firebase.adapter.PlayerRecyclerAdapter;
import com.androdheight.firebase.model.PlayerModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements  PlayerRecyclerAdapter.OnListItemClickListener{

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<PlayerModel> playerlist;
    private EditText etplayername,etplayermatch,etplayerinning,etplayerrun,
            etplayerwicket,playerBestRun,etplayerbestbowling,etplayerAvg;
    private Button btnSubmit;

    DatabaseReference databasePlayers;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databasePlayers = FirebaseDatabase.getInstance().getReference("players");


        initilize();

        playerlist = new ArrayList<PlayerModel>();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFieldsValue();
            }
        });

        // specify an adapter (see also next example)

    }



    private  void initilize(){
        etplayername = (EditText)findViewById(R.id.etplayername);
        etplayermatch = (EditText)findViewById(R.id.etplayermatch);
        etplayerinning = (EditText)findViewById(R.id.etplayerinning);
        etplayerrun = (EditText)findViewById(R.id.etplayerrun);
        etplayerwicket = (EditText)findViewById(R.id.etplayerwicket);
        playerBestRun = (EditText)findViewById(R.id.etplayerBestRun);
        etplayerbestbowling = (EditText)findViewById(R.id.etplayerbestbowling);
        etplayerAvg = (EditText)findViewById(R.id.etplayerAvg);
        btnSubmit =(Button)findViewById(R.id.btnSubmit);




        recyclerView = (RecyclerView)findViewById(R.id.rvPlayerlist);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//////////////////retrieve the value//////////////


        databasePlayers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                playerlist.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {


                    if(postSnapshot != null) {
                        String parentid = postSnapshot.getKey();

                        PlayerModel model = postSnapshot.getValue(PlayerModel.class);
                        /////parse the single parameters//////////////
                        int p_id = model.getP_id();
                        String image = model.getPlayerImage();
                        String name = model.getPlayer_name();
                        String match = model.getPlayer_match();
                        String inning = model.getPlayer_inning();
                        String run = model.getPlayer_run();
                        String wicket = model.getPlayer_wicket();
                        String bestRun = model.getPlayer_bestRun();
                        String bestBowling = model.getPlayer_bestBowling();
                        String avg = model.getAverage();
                        model.setParentid(parentid);


                        playerlist.add(model);

                        adapter = new PlayerRecyclerAdapter(MainActivity.this,playerlist);
                        recyclerView.setAdapter(adapter);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }


        });

    }


    private void getFieldsValue(){
        int p_id = playerlist.size();
        String name = etplayername.getText().toString();
        String match = etplayermatch.getText().toString();
        String inning = etplayerinning.getText().toString();
        String run = etplayerrun.getText().toString();
        String wicket = etplayerwicket.getText().toString();
        String bestRun = playerBestRun.getText().toString();
        String bestBowling = etplayerbestbowling.getText().toString();
        String avg = etplayerAvg.getText().toString();

//////////insert the value in firebase database////////////////////
         id =databasePlayers.push().getKey();


        PlayerModel model = new PlayerModel();
        model.setPlayerImage("");
        model.setPlayer_name(name);
        model.setPlayer_match(match);
        model.setPlayer_inning(inning);
        model.setPlayer_run(run);
        model.setPlayer_wicket(wicket);
        model.setPlayer_bestRun(bestRun);
        model.setPlayer_bestBowling(bestBowling);
        model.setAverage(avg);
        model.setParentid(id);



        databasePlayers.child(id).setValue(model);
        Toast.makeText(this,"Playes added",Toast.LENGTH_LONG).show();
        ////retrieve data from firebase database//////////////
        //Value event listener for realtime data update





        /*adapter = new PlayerRecyclerAdapter(this,playerlist);
        recyclerView.setAdapter(adapter);*/


    }


    @Override
    public void onItemClickL(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("player",playerlist.get(position-1));
        Intent i= new Intent(MainActivity.this,UpdatePlayerActivity.class);
        i.putExtras(bundle);

        startActivity(i);

    }
}
