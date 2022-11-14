package com.evermine.industryapp;

public class User {

    private String name;
    private String password;

    public User(){

    }
    public User(String name,String password){
        this.name=name;
        this.password=password;
    }
    public void setNom(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }
    public void setContra(String contra){
        this.name=contra;
    }
    public String getPassword(){
        return this.password;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Nom: "+name+"\nContrasenya"+this.password;
    }
}
