package com.back.banka.Model;
import com.back.banka.Enums.Rol;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Entity
    @Table(name = "users")
    @Getter
    @Setter
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false)
        private int age;

        @Column(nullable = false, unique = true)
        private String email;

        @Column(nullable = false)
        private String password;

        @Column(nullable = false)
        private String country;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Rol role;

        @Column(nullable = false)
        private boolean status;

        @Column(nullable = false)
        private String DNI;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
        private List<Tokens> tokens;

        @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
        private List<AccountBank> accountUser;

         @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
         private List<Notifications> notifications;

         private LocalDate dateBirthDay;


    public String getEmail(){
            return email;
        }

        public String getDNI() {
            return DNI;
        }

        public void setDNI(String DNI) {
            this.DNI = DNI;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public Rol getRole() {
            return role;
        }

        public void setRole(Rol role) {
            this.role = role;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }

