package data.models;

import java.sql.Timestamp;

public class User {
    private int id;
    private String email, password, username, firstname, lastname;
    private Timestamp last_login, date_joined;
    private boolean superuser, staff, active;
 
    public User(int id, String email, String password, Timestamp last_login, boolean superuser, String username, String firstname,
    		String lastname, boolean staff, boolean active, Timestamp date_joined) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.last_login = last_login;
        this.superuser = superuser;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.staff = staff;
        this.active = active;
        this.date_joined = date_joined;
    }
    
    public String getFirstname(){
    	return firstname;
    }
    public void setFirstname(String firstname){
    	this.firstname = firstname;
    	
    }
    public String getLastname(){
    	return lastname;
    }
    public void setLastname(String lastname){
    	this.lastname = lastname;
    	
    }
    
    
    
    public String getUsername(){
    	return username;
    }
    
    public void setUsername(String username){
    	this.username = username;
    	
    }
    
    
    public boolean getSuperuser() {
        return superuser;
    }
    
    public void setSuperuser(boolean superuser){
    	this.superuser = superuser;
    	
    }
    
    public boolean getStaff() {
        return staff;
    }
    
    public void setStaff(boolean staff){
    	this.staff = staff;
    	
    }
    
    public boolean getActive() {
        return active;
    }
    
    public void setActive(boolean active){
    	this.active = active;
    	
    }
    
    public Timestamp getDate_joined(){
    	return date_joined;
    	
    }
    public void setDate_joined(Timestamp date_joined){
    	this.date_joined = date_joined;
    	
    }
    
    
    public Timestamp getLast_login(){
    	return last_login;
    	
    }
    public void setLast_login(Timestamp last_login){
    	this.last_login = last_login;
    	
    }
    

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
