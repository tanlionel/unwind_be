package com.capstone.unwind.entity;

import com.capstone.unwind.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static com.capstone.unwind.enums.UserRole.TIMESHARECOMPANYSTAFF;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "timeshare_company_staff")
public class TimeshareCompanyStaff implements UserDetails {
    @Id
    @Column(name = "timeshare_company_staff_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_name", length = 45)
    private String userName;

    @Column(name = "password", length = 200)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeshare_company_id")
    private TimeshareCompany timeshareCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resort_id")
    private Resort resort;

    @Column(name = "is_active")
    private Boolean isActive;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("TIMESHARECOMPANYSTAFF"));
    }

    @Override
    public String getUsername() {
        return this.userName;
    }
}