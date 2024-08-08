package com.vs.springdemo1.pojo;

import lombok.*;


/*@Getter
@Setter
@ToString
@EqualsAndHashCode*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer uid;
    private String username;
    private String password;
    private Address address;

/*    public User() {
        uid = 00000;
        username = "void name";
        password = "no pwd";
    }*/

/*    public User(String username, String password, Integer uid, Address addr) {
        this.username = username;
        this.password = password;
        this.uid = uid;
        this.address = addr;
    }

    public Integer getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Address getAddress() {
        return address;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", address=" + address +
                '}';
    }*/
}

class Address {
    private String address;

    public Address(String addr) {
        this.address = addr;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Address{" +
                "address='" + address + '\'' +
                '}';
    }
}
