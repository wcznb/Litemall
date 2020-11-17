package cn.edu.xmu.favorite.mapper;

import cn.edu.xmu.favorite.model.po.FavouriteGoodsPo;
import cn.edu.xmu.favorite.model.po.FavouriteGoodsPoExample;
import java.util.List;

public interface FavouriteGoodsPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table other_favourite_goods
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table other_favourite_goods
     *
     * @mbg.generated
     */
    int insert(FavouriteGoodsPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table other_favourite_goods
     *
     * @mbg.generated
     */
    int insertSelective(FavouriteGoodsPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table other_favourite_goods
     *
     * @mbg.generated
     */
    List<FavouriteGoodsPo> selectByExample(FavouriteGoodsPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table other_favourite_goods
     *
     * @mbg.generated
     */
    FavouriteGoodsPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table other_favourite_goods
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(FavouriteGoodsPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table other_favourite_goods
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(FavouriteGoodsPo record);
}