package cn.njust.label.main.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * <p>
 *
 * </p>
 *
 * @author lian
 * @since 2022-02-17
 */
//民航航迹存储表由mysql存储改为mongodb存储
//@TableName("shiptrack") //必须写
@Document(collection = "shiptrack")
public class Shiptrack{

    @Id
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    //@TableId(value = "Fragment_ID", type = IdType.AUTO)
    @Field("Fragment_ID")
    private Integer fragmentId;

    //@TableField("Ship_ID")
    @Field("Ship_ID")
    private String shipId;

    //@TableField("Fragment_num")
    @Field("Fragment_num")
    private Integer fragmentNum;

    //@TableField("Now_fragment")
    @Field("Now_fragment")
    private Integer nowFragment;

    //@TableField("FM_ID")
    @Field("FM_ID")
    private Float fmId;

    //@TableField("Data_packet")
    @Field("Data_packet")
    private String dataPacket;

    //@TableField("checkData")
    @Field("checkData")
    private String checkData;


    public Integer getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(Integer fragmentId) {
        this.fragmentId = fragmentId;
    }

    public String getShipId() {
        return shipId;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    public Integer getFragmentNum() {
        return fragmentNum;
    }

    public void setFragmentNum(Integer fragmentNum) {
        this.fragmentNum = fragmentNum;
    }

    public Integer getNowFragment() {
        return nowFragment;
    }

    public void setNowFragment(Integer nowFragment) {
        this.nowFragment = nowFragment;
    }

    public Float getFmId() {
        return fmId;
    }

    public void setFmId(Float fmId) {
        this.fmId = fmId;
    }

    public String getDataPacket() {
        return dataPacket;
    }

    public void setDataPacket(String dataPacket) {
        this.dataPacket = dataPacket;
    }

    public String getCheckData() {
        return checkData;
    }

    public void setCheckData(String checkData) {
        this.checkData = checkData;
    }

    @Override
    public String toString() {
        return "Shiptrack{" +
                "fragmentId=" + fragmentId +
                ", shipId=" + shipId +
                ", fragmentNum=" + fragmentNum +
                ", nowFragment=" + nowFragment +
                ", fmId=" + fmId +
                ", dataPacket=" + dataPacket +
                ", checkData=" + checkData +
                "}";
    }
}
