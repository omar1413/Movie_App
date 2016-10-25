package com.omar.themovieapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener {

    GridView moviesGrid;
    List<MyMovie> movies;

    FetchMovie fetchMovie;
    Communicator communicator;

    public static final String MY_PREFS_NAME = "prefs";
    public static final int MODE_PRIVATE = 0;
    public static final String SORT_KEY = "sort_key";

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator = (Communicator) getActivity();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(!Utils.hasActiveInternetConnection(getContext())){
            Utils.showToast(getContext(), "No Internet Connection");
            return false;
        }
        MyApp.moviePosStatus = 0;

        if (item.getItemId() == R.id.action_refresh) {

            editor.putInt(SORT_KEY, MyApp.POPULAR_STATUS).commit();

            getMovies(MyApp.POPULAR_STATUS);
            beginWithTheFirstFilm(MyApp.moviePosStatus);
        } else if (item.getItemId() == R.id.top_rated) {
            editor.putInt(SORT_KEY, MyApp.TOP_RATED_STATUS).commit();

            getMovies(MyApp.TOP_RATED_STATUS);
            beginWithTheFirstFilm(MyApp.moviePosStatus);
        } else if (item.getItemId() == R.id.favourite) {
            editor.putInt(SORT_KEY, MyApp.FAVOURITE_STATUS).commit();

            getMovies(MyApp.FAVOURITE_STATUS);
        }

        return true;
    }

    public void getFavourites() {
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        movies = db.getmovie();
        MyAdapter adapter = new MyAdapter(getActivity(), movies);
        moviesGrid.setAdapter(adapter);
        MainActivity mainActivity = (MainActivity) communicator;
        MovieDetailsFragment movieDetailsFragment = (MovieDetailsFragment) mainActivity.manager.findFragmentById(R.id.detailsFragment);
        if (movieDetailsFragment != null && movieDetailsFragment.isVisible()) {
            if (movies != null && movies.size() > 0) {
                beginWithTheFirstFilm(MyApp.moviePosStatus);
            } else {
                communicator.hideFragment();
            }
        }
    }


    private void getMovies(int type) {

        communicator.setTitleForActivity(type);
        String sort_type = MyApp.POPULAR_SORT_TYPE;
        MyApp.status = type;
        if(type == MyApp.TOP_RATED_STATUS) {
            sort_type = MyApp.TOP_RATED_SORT_TYPE;
        }
        else if (type == MyApp.FAVOURITE_STATUS){
            getFavourites();
            return;
        }
        fetchMovie = new FetchMovie(getActivity(), movies);
        movies = null;
        try {
            movies = fetchMovie.execute(sort_type).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        MyAdapter adapter = new MyAdapter(getActivity(), movies);
        moviesGrid.setAdapter(adapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);

        moviesGrid = (GridView) root.findViewById(R.id.grid);
        moviesGrid.setOnItemClickListener(this);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MyApp.status = prefs.getInt(SORT_KEY,MyApp.status);
        communicator.setTitleForActivity(MyApp.status);
        if (Utils.hasActiveInternetConnection(getContext())) {
            getMovies(MyApp.status);
        } else {
            Utils.showToast(getContext(), "No Internet Connection");
            communicator.hideFragment();
        }

    }



    @Override
    public void onStart() {
        super.onStart();
        if (Utils.hasActiveInternetConnection(getContext())) {
            beginWithTheFirstFilm(MyApp.moviePosStatus);
        } else {
            Utils.showToast(getContext(), "No Internet Connection");
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyApp.moviePosStatus = position;
        if(!Utils.hasActiveInternetConnection(getContext())){
            Utils.showToast(getContext(), "No Internet Connection");
            return;
        }
        FetchMovie fetchMovie = new FetchMovie(getActivity(), movies);
        try {
            movies = fetchMovie.execute(movies.get(position).id + "", position + "").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        communicator.response(movies.get(position));
    }

    public void beginWithTheFirstFilm(int pos) {
        communicator.showFragment();
        if(movies != null && movies.size() > pos ) {
            FetchMovie fetchMovie = new FetchMovie(getActivity(), movies);

            try {
                movies = fetchMovie.execute(movies.get(pos).id + "", pos + "").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            communicator.sendOnfirst(movies.get(pos));
        }
    }
}


