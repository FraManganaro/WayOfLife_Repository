package com.example.wayoflife.workouts.util;

public class WorkoutModel {

    //Variabili
    private int id;
    private String nome;
    private String data;
    private int n_squat;
    private String durata;
    private String tipologia;
    private int calorie;
    private float chilometri;
    private int n_flessioni;
    private int state;
    private int like;

    /** Costruttore completo (senza ID) */
    public WorkoutModel(String nome, String data, int n_squat, String durata, float chilometri, String tipologia, int calorie, int n_flessioni, int state, int like) {
        this.nome = nome;
        this.data = data;
        this.n_squat = n_squat;
        this.durata = durata;
        this.chilometri = chilometri;
        this.tipologia = tipologia;
        this.calorie = calorie;
        this.n_flessioni = n_flessioni;
        this.state = state;
        this.like = like;
    }

    /** Costruttore per attività extra */
    public WorkoutModel(String nome, String data, String durata, String tipologia, int calorie, int state, int like) {
        this.nome = nome;
        this.data = data;
        this.n_squat = -1;
        this.durata = durata;
        this.tipologia = tipologia;
        this.calorie = calorie;
        this.chilometri = -1;
        this.n_flessioni = -1;
        this.state = state;
        this.like = like;
    }

    /** Costruttore per Freestyle, Pushup e Squat */
    public WorkoutModel(String nome, String data, int n_squat, String durata, String tipologia, int calorie, int n_flessioni, int state, int like) {
        this.nome = nome;
        this.data = data;
        this.n_squat = n_squat;
        this.durata = durata;
        this.tipologia = tipologia;
        this.calorie = calorie;
        this.chilometri = -1;
        this.n_flessioni = n_flessioni;
        this.state = state;
        this.like = like;
    }

    /** Costruttore per Corsa, Ciclismo, Camminata */
    public WorkoutModel(String nome, String data, String durata, float chilometri, String tipologia, int calorie, int state, int like) {
        this.nome = nome;
        this.data = data;
        this.n_squat = -1;
        this.durata = durata;
        this.chilometri = chilometri;
        this.tipologia = tipologia;
        this.calorie = calorie;
        this.n_flessioni = -1;
        this.state = state;
        this.like = like;
    }

    /** Costruttore completo (con ID) */
    public WorkoutModel(int id, String nome, String data, int n_squat, String durata, float chilometri, String tipologia, int calorie, int n_flessioni, int state, int like) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.n_squat = n_squat;
        this.durata = durata;
        this.chilometri = chilometri;
        this.tipologia = tipologia;
        this.calorie = calorie;
        this.n_flessioni = n_flessioni;
        this.state = state;
        this.like = like;
    }

    // ToString
    @Override
    public String toString() {
        return "Nome: " + nome + '\n' +
                "Data: " + data + '\n' +
                "Tipologia: " + tipologia + '\n' +
                "Durata: " + durata + '\n' +
                "Calorie: " + calorie + " kcal" + '\n' +
                "Flessioni: " + n_flessioni + '\n' +
                "Squat: " + n_squat + '\n' +
                "Chilometri: " + chilometri + '\n' +
                "Preferito: " + like + '\n' +
                "Id: " + id + '\n' +
                "Valutazione: " + state + "\n\n";
    }

    // Get e Set
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

    public int getN_squat() {
        return n_squat;
    }

    public void setN_squat(int n_squat) {
        this.n_squat = n_squat;
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

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
