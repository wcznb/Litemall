package cn.edu.xmu.cart.mapper;

import cn.edu.xmu.cart.model.po.ShoppingCartPo;
import cn.edu.xmu.cart.model.po.ShoppingCartPoExample;
import java.util.List;

public interface ShoppingCartPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shopping_cart
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shopping_cart
     *
     * @mbg.generated
     */
    int insert(ShoppingCartPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shopping_cart
     *
     * @mbg.generated
     */
    int insertSelective(ShoppingCartPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shopping_cart
     *
     * @mbg.generated
     */
    List<ShoppingCartPo> selectByExample(ShoppingCartPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shopping_cart
     *
     * @mbg.generated
     */
    ShoppingCartPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shopping_cart
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ShoppingCartPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shopping_cart
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ShoppingCartPo record);
}