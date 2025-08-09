package com.example.libraryoop.model;

public class Staff {
    private String idStaff; // Mã nhân viên
    private String nameStaff; // Tên nhân viên
    private String emailStaff;      // Email nhân viên
    private String numberPhoneStaff;  // Số điện thoại nhân viên
    private String addressStaff;   // Địa chỉ nhân viên
    private String role;   // Vai trò của nhân viên (ví dụ: quản lý, thủ thư, v.v.)
    private String username;   // Tên đăng nhập của nhân viên
    private String password;   // Mật khẩu của nhân viên

    /**
     * Default constructor.
     */
    public Staff() {
    }

    /**
     * Constructor with parameters.
     */
    public Staff(String nameStaff, String emailStaff, String numberPhoneStaff, String addressStaff, String username, String password, String role) {


        this.nameStaff = nameStaff;
        this.emailStaff = emailStaff;
        this.numberPhoneStaff = numberPhoneStaff;
        this.addressStaff = addressStaff;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    /**
     * Constructor with all parameters.
     */
    public Staff(String idStaff, String nameStaff, String emailStaff,
                 String numberPhoneStaff, String addressStaff, String role, String username, String password) {
        this.idStaff = idStaff;
        this.nameStaff = nameStaff;
        this.emailStaff = emailStaff;
        this.numberPhoneStaff = numberPhoneStaff;
        this.addressStaff = addressStaff;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    /**
     * Getter and Setter
     */
    public String getIdStaff() {
        return idStaff;
    }

    public void setIdStaff(String idStaff) {
        this.idStaff = idStaff;
    }

    public String getNameStaff() {
        return nameStaff;
    }

    public void setNameStaff(String nameStaff) {
        this.nameStaff = nameStaff;
    }

    public String getEmailStaff() {
        return emailStaff;
    }

    public void setEmailStaff(String emailStaff) {
        this.emailStaff = emailStaff;
    }

    public String getNumberPhoneStaff() {
        return numberPhoneStaff;
    }

    public void setNumberPhoneStaff(String numberPhoneStaff) {
        this.numberPhoneStaff = numberPhoneStaff;
    }

    public String getAddressStaff() {
        return addressStaff;
    }

    public void setAddressStaff(String addressStaff) {
        this.addressStaff = addressStaff;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassWord() {
        return password;
    }

    public void setPassWord(String password) {
        this.password = password;
    }
}
