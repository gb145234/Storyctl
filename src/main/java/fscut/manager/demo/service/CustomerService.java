package fscut.manager.demo.service;

import fscut.manager.demo.dto.CustomerDTO;
import fscut.manager.demo.entity.Customer;
import fscut.manager.demo.entity.CustomerRole;
import fscut.manager.demo.entity.UPK.CustomerRoleUPK;
import fscut.manager.demo.exception.CustomerAlreadyExitsException;
import fscut.manager.demo.exception.CustomerNotExitsException;
import fscut.manager.demo.vo.CustomerAuthVO;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    /**
     * 添加用户到某个产品
     * @param customerAuthVO 用户视图
     * @return 用户角色
     */
    CustomerRole addToProduct(CustomerAuthVO customerAuthVO);

    /**
     * 根据产品id得到用户列表
     * @param productId 产品id
     * @return 用户列表
     */
    List<Customer> getCustomerListByProductId(Integer productId);

    /**
     * 根据用户id和产品id将用户从某个产品删除
     * @param customerId 用户id
     * @param productId 产品id
     * @return 删除条数
     */
    Integer deleteFromProduct(Integer customerId, Integer productId);

    /**
     * 删除用户
     * @param username 用户名
     * @return 删除条数
     * @throws CustomerNotExitsException 用户不存在异常
     */
    Integer deleteCustomer(String username) throws CustomerNotExitsException;

    /**
     * 根据用户id得到用户名
     * @param userId 用户Id
     * @return 用户名
     */
    String getUsernameById(Integer userId);

    /**
     * 根据用户id得到真实姓名
     * @param userId 用户id
     * @return 真实姓名
     */
    String getRealnameById(Integer userId);

    /**
     * 根据用户名得到用户id
     * @param username 用户名
     * @return 用户id
     */
    Integer getIdByUsername(String username);

    /**
     * 得到用户列表
     * @return 用户列表
     */
    List<Customer> getCustomers();

    /**
     * 根据用户id得到角色名
     * @param userId 用户id
     * @return 角色名
     */
    String getRoleCodeByUserId(Integer userId);

    /**
     * 添加用户
     * @param customerDTO 用户
     * @return 新用户
     * @throws CustomerAlreadyExitsException 用户已经存在异常
     */
    Optional<Customer> createCustomer(CustomerDTO customerDTO) throws CustomerAlreadyExitsException;

    /**
     * 修改密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param username 用户名
     * @return 更新结果
     */
    Integer updateCustomerPassword(String oldPassword, String newPassword, String username);

    /**
     * 修改昵称
     * @param newName 新昵称
     * @param username 用户名
     * @return 修改条数
     */
    Integer updateCustomerRealName(String newName, String username);

    /**
     * 得到管理员列表
     * @return 管理员列表
     */
    List<String> getAdmins();

    /**
     * 更新用户角色
     * @param customerAuthVO 用户视图
     * @return 更新条数
     */
    Integer updateCustomerRole(CustomerAuthVO customerAuthVO);

    /**
     * 根据用户id和产品id得到用户列表
     * @param customerAuthVO 用户视图
     * @return 用户角色主键
     */
    CustomerRoleUPK getCustomerRoleByCustomerIdAndProductId(CustomerAuthVO customerAuthVO);

    /**
     * 根据角色id得到角色名
     * @param userId 角色id
     * @return 角色名
     */
    String getRoleName(Integer userId);
}
