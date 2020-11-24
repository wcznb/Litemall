package cn.edu.xmu.share.mapper;

import cn.edu.xmu.share.model.po.BeSharePo;
import cn.edu.xmu.share.model.po.BeSharePoExample;
import java.util.List;

public interface BeSharePoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table be_share
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table be_share
     *
     * @mbg.generated
     */
    int insert(BeSharePo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table be_share
     *
     * @mbg.generated
     */
    int insertSelective(BeSharePo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table be_share
     *
     * @mbg.generated
     */
    List<BeSharePo> selectByExample(BeSharePoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table be_share
     *
     * @mbg.generated
     */
    BeSharePo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table be_share
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(BeSharePo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table be_share
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(BeSharePo record);
}