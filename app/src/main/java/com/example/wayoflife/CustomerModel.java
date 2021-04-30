package com.example.wayoflife;

public class CustomerModel {

    //Variabili
    private int id;
    private String nome;
    private String data;
    private String durata;
    private String tipologia;
    private int calorie;
    private float chilometri;
    private int n_flessioni;
    private int state;

    /** Costruttore completo */
    public CustomerModel(String nome, String data, String durata, String tipologia, int calorie, float chilometri, int n_flessioni, int state) {
        this.id = -1;
        this.nome = nome;
        this.data = data;
        this.durata = durata;
        this.tipologia = tipologia;
        this.calorie = calorie;
        this.chilometri = chilometri;
        this.n_flessioni = n_flessioni;
        this.state = state;
    }

    /** Costruttore per attività extra */
    public CustomerModel(String nome, String data, String durata, String tipologia, int calorie, int state) {
        this.id = -1;
        this.nome = nome;
        this.data = data;
        this.durata = durata;
        this.tipologia = tipologia;
        this.calorie = calorie;
        this.chilometri = -1;
        this.n_flessioni = -1;
        this.state = state;
    }

    /** Costruttore per Freestyle o Pushup */
    public CustomerModel(String nome, String data, String durata, String tipologia, int calorie, int n_flessioni, int state) {
        this.id = -1;
        this.nome = nome;
        this.data = data;
        this.durata = durata;
        this.tipologia = tipologia;
        this.calorie = calorie;
        this.chilometri = -1;
        this.n_flessioni = n_flessioni;
        this.state = state;
    }

    /** Costruttore per Corsa, Ciclismo, Camminata */
    public CustomerModel(String nome, String data, String durata, String tipologia, int calorie, float chilometri, int state) {
        this.id = -1;
        this.nome = nome;
        this.data = data;
        this.durata = durata;
        this.tipologia = tipologia;
        this.calorie = calorie;
        this.chilometri = chilometri;
        this.n_flessioni = -1;
        this.state = state;
    }

    public CustomerModel() {
    }

    // ToString
    @Override
    public String toString() {
        return "CustomerModel{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", data='" + data + '\'' +
                ", durata='" + durata + '\'' +
                ", tipologia='" + tipologia + '\'' +
                ", calorie=" + calorie +
                ", chilometrri=" + chilometri +
                ", n_flessioni=" + n_flessioni +
                ", state=" + state +
                '}';
    }

    // Get e Set
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDurata() {
        return durata;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public float getChilometri() {
        return chilometri;
    }

    public void setChilometri(float chilometri) {
        this.chilometri = chilometri;
    }

    public int getN_flessioni() {
        return n_flessioni;
    }

    public void setN_flessioni(int n_flessioni) {
        this.n_flessioni = n_flessioni;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}