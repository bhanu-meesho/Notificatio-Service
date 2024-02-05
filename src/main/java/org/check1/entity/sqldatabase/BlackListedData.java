package org.check1.entities.sqldatabaseentities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BlackListedData")
public class BlackListedData {
    @Id
    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;
}
