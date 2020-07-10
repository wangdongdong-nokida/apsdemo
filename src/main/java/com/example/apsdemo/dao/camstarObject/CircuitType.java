package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.camstarData.CircuitTypeData;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Table(name = "L_DLLX", catalog = "")
@Entity
public class CircuitType extends CircuitTypeData {

}
