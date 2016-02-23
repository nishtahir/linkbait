package com.nishtahir.islackbot

/**
 * Created by nish on 2/23/16.
 */
class TacoRequest {

    TacoRequest(){
        isValid = true
        timer = new Timer()
        timer.runAfter(60000){
            isValid = false
        }
    }

    String timestamp

    String user

    int upvotes

    int downvotes

    boolean isValid

    Timer timer
}
