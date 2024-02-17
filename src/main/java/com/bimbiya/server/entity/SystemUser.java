package com.bimbiya.server.entity;

import com.bimbiya.server.util.enums.District;
import com.bimbiya.server.util.enums.Status;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Data
@Entity
@Table(name = "system_user")
public class SystemUser extends CommonEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "USERNAME", nullable = false, unique = true, length = 64)
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ROLE", nullable = false)
    private UserRole userRole;

    @Column(name = "FULL_NAME", nullable = false, length = 128)
    private String fullName;

    @Column(name = "NIC", nullable = false)
    private String nic;

    @Column(name = "EMAIL", nullable = false, length = 128)
    private String email;

    @Column(name = "MOBILE_NO", nullable = false, length = 16)
    private String mobileNo;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_OF_BIRTH", nullable = false)
    private Date dateOfBirth;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "CITY", length = 128)
    private String city;

    @Column(name = "STATUS", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "DISTRICT", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private District district;

    @Column(name = "PASSWORD_STATUS", length = 8)
    @Enumerated(EnumType.STRING)
    private Status pwStatus;

    @Column(name = "ATTEMPT", nullable = false)
    private int attempt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_LOGGED_DATE", length = 19)
    private Date lastLoggedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PASSWORD_EXPIRE_DATE", length = 19)
    private Date passwordExpireDate;

    @Column(name = "OTP")
    private Integer otp;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("llllllllllllllllll");
        return Collections.singleton(new SimpleGrantedAuthority(userRole.getAuthority()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
