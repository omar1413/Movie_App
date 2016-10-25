package com.omar.themovieapp;


interface Communicator {
    public void response(MyMovie movie);

    void sendOnfirst(MyMovie movie);

    void hideFragment();

    void showFragment();

    void setTitleForActivity(int status);
}
