package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public class EmployeeData {

    @Column(name = "DOCMANAGERPASSWORD")
    private String docmanagerpassword;
    @Column(name = "DOCMANAGERUSER")
    private String docmanageruser;
    @Column(name = "DOMAINNAME")
    private String domainname;
    @Id
    @Column(name = "EMPLOYEEID")
    private String ID;
    @Column(name = "EMPLOYEENAME")
    private String name;
    @Column(name = "FULLNAME")
    private String fullname;
    @Column(name = "USERCOMMENT")
    private String usercomment;

    @Transient
    @Column(name = "EMPLOYEE_TEAMID")
    private String employeeTeamid;
}
