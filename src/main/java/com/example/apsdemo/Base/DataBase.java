package com.example.apsdemo.Base;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
public class DataBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long ID;

    protected String teamName;
}
