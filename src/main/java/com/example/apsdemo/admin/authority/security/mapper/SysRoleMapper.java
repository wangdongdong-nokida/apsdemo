package com.example.apsdemo.admin.authority.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.apsdemo.admin.authority.security.pojo.SysRole;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleMapper  extends BaseMapper<SysRole> {

    /**
     * 根据userId查询角色数据
     * @param userId 用户id
     * @return
     */
    @Select("SELECT\n" +
            "\tr.role_id,\n" +
            "\tr.role_name,\n" +
            "\tr.role_sign,\n" +
            "\tr.remark,\n" +
            "\tr.user_id_create,\n" +
            "\tr.create_date,\n" +
            "\tr.update_date \n" +
            "FROM\n" +
            "\tsys_role r\n" +
            "\tJOIN sys_user_role ur ON r.role_id = ur.role_id \n" +
            "\tAND ur.user_id = #{userId}")
    List<SysRole> findByUserId(String userId);

    @Select("SELECT * FROM sys_role WHERE role_sign IN (${roleSigns})")
    List<SysRole> findByRoleSign(String roleSigns);


}
