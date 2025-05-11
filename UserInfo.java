/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbserver;

/**
 *
 * @author aisxi
 */
public class UserInfo {
    private String fullname;
    private int phone;
    private String email;
    private String address;
    private String username;
    private String password;
    private boolean isAdmin;

    public UserInfo(String fullname, int phone, String email, String address, String username, String password, boolean isAdmin) {
        this.fullname = fullname;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public UserInfo() {
    }

    public String getFullname() {
        return fullname;
    }

    public int getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "UserInfo{" + "fullname=" + fullname + ", phone=" + phone + ", email=" + email + ", address=" + address + ", username=" + username + ", password=" + password + ", isAdmin=" + isAdmin + '}';
    }
    
    
}
