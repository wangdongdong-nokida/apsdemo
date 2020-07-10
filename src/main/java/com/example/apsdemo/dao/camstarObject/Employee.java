package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.camstarData.EmployeeData;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Table(name = "EMPLOYEE",  catalog = "")
@Entity
public class Employee extends EmployeeData {

}
