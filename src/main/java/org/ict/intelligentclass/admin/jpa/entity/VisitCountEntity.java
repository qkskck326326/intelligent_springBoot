package org.ict.intelligentclass.admin.jpa.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_visitcounts")
public class VisitCountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "visit_date")
    private LocalDate date;

    @Column(name = "visit_count")
    private Long count;

    @Column(name = "user_email")
    private String userEmail;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
