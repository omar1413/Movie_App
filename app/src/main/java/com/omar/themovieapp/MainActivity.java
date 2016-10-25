package com.omar.themovieapp;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements Communicator{


    FragmentManager manager;
    MovieDetailsFragment movieDetailsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getSupportFragmentManager();


    }



    @Override
    public void response(MyMovie movie) {
         movieDetailsFragment= (MovieDetailsFragment) manager.findFragmentById(R.id.detailsFragment);
        if(movieDetailsFragment != null && movieDetailsFragment.isVisible()){
            if(!Utils.hasActiveInternetConnection(this)){
                Utils.showToast(this, "No Internet Connection");
                manager.beginTransaction().hide(movieDetailsFragment).commit();
                return;
            }
            movieDetailsFragment.change(movie);
        }
        else{
            Intent intent = new Intent(this, DetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("movie", movie);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void sendOnfirst(MyMovie movie) {
        movieDetailsFragment= (MovieDetailsFragment) manager.findFragmentById(R.id.detailsFragment);
        if(movieDetailsFragment != null ){
            movieDetailsFragment.change(movie);
        }
    }

    @Override
    public void hideFragment() {
        movieDetailsFragment= (MovieDetailsFragment) manager.findFragmentById(R.id.detailsFragment);
        if(movieDetailsFragment != null ){
            FragmentTransaction transaction= manager.beginTransaction();
            transaction.hide(movieDetailsFragment);
            transaction.commit();
        }
    }

    @Override
    public void showFragment() {
        movieDetailsFragment= (MovieDetailsFragment) manager.findFragmentById(R.id.detailsFragment);
        if(movieDetailsFragment != null ){
            FragmentTransaction transaction= manager.beginTransaction();
            transaction.show(movieDetailsFragment);
            transaction.commit();
        }
    }

    @Override
    public void setTitleForActivity(int status) {
        if(getSupportActionBar() == null){
            return;
        }
        if(status == MyApp.POPULAR_STATUS){
            getSupportActionBar().setTitle("Popular Movies");
        }
        else if(status == MyApp.TOP_RATED_STATUS){
            getSupportActionBar().setTitle("TopRated Movies");
        }
        else {

            getSupportActionBar().setTitle("My Favourite Movies");
        }
    }
}
