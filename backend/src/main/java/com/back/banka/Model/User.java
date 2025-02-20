package com.back.banka.Model;

import com.back.banka.Enums.Rol;
import jakarta.persistence.*;
import lombok.*;


    @Entity
    @Table(name = "usuarios")
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
        private String nombre;

        @Column(nullable = false)
        private int edad;

        @Column(nullable = false, unique = true)
        private String email;

        @Column(nullable = false)
        private String password;

        @Column(nullable = false)
        private String pais;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Rol rol;

        @Column(nullable = false)
        private boolean status;

        @Column(nullable = false)
        private String DNI;

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
    }

