package org.check1.entities.sqldatabaseentities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name="SmsRequestData")
public class SmsRequestData{

    public SmsRequestData(String phoneNumber, String message, String status, Long failureCode, String failureComment) {
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.status = status;
        this.failureCode = failureCode;
        this.failureComment = failureComment;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @Id
    @Column(name="id")
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name="phoneNumber",nullable = false,length = 16)
    private String phoneNumber;

    @Getter
    @Setter
    @Column(name = "message")
    private String message;

    @Getter
    @Setter
    @Column(name = "status", nullable = false)
    private String status;

    @Getter
    @Setter
    @JoinColumn(name = "failureCode")
    private Long failureCode;

    @Getter
    @Setter
    @JoinColumn(name = "failureComment")
    private String failureComment;

    @Getter
    @Column(name="createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Getter
    @Setter
    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();


    @PreUpdate
    private void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
