package com.shuishou.sysmgr.beans;

import com.google.gson.annotations.SerializedName;
import com.shuishou.sysmgr.ConstantValue;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public class Dish implements Serializable{
	@SerializedName(value = "id", alternate={"objectid"})
    private int id;

    private String chineseName;

    private String englishName;

    private int sequence;

    private Category2 category2;

    private double price;

    private String pictureName;

    private boolean isNew = false;

    private boolean isSpecial = false;

    private boolean isSoldOut;

    private int hotLevel;

    private String abbreviation;

    private int chooseMode = ConstantValue.DISH_CHOOSEMODE_DEFAULT;
	
	private DishChoosePopinfo choosePopInfo;
	
	private List<DishChooseSubitem> chooseSubItems;
	
	private int subitemAmount = 0;
	
	private boolean autoMergeWhileChoose = true;

	public boolean isAutoMergeWhileChoose() {
		return autoMergeWhileChoose;
	}

	public void setAutoMergeWhileChoose(boolean autoMergeWhileChoose) {
		this.autoMergeWhileChoose = autoMergeWhileChoose;
	}
	
	public Dish(){

    }

	
	public int getSubitemAmount() {
		return subitemAmount;
	}

	public void setSubitemAmount(int subitemAmount) {
		this.subitemAmount = subitemAmount;
	}
	
    
//    public Dish(int id, String chineseName, String englishName, int sequence, double price, String pictureName, Category2 category2){
//        this.id = id;
//        this.chineseName = chineseName;
//        this.englishName = englishName;
//        this.sequence = sequence;
//        this.price = price;
//        this.pictureName = pictureName;
//        this.category2 = category2;
//    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public int getHotLevel() {
        return hotLevel;
    }

    public void setHotLevel(int hotLevel) {
        this.hotLevel = hotLevel;
    }

    public boolean isSoldOut() {
        return isSoldOut;
    }

    public void setSoldOut(boolean soldOut) {
        isSoldOut = soldOut;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean isSpecial) {
        this.isSpecial = isSpecial;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Category2 getCategory2() {
        return category2;
    }

    public void setCategory2(Category2 category2) {
        this.category2 = category2;
    }

    
    public int getChooseMode() {
		return chooseMode;
	}

	public void setChooseMode(int chooseMode) {
		this.chooseMode = chooseMode;
	}

	public DishChoosePopinfo getChoosePopInfo() {
		return choosePopInfo;
	}

	public void setChoosePopInfo(DishChoosePopinfo choosePopInfo) {
		this.choosePopInfo = choosePopInfo;
	}

	public List<DishChooseSubitem> getChooseSubItems() {
		return chooseSubItems;
	}

	public void setChooseSubItems(List<DishChooseSubitem> chooseSubItems) {
		this.chooseSubItems = chooseSubItems;
	}

	@Override
    public String toString() {
        return "Dish [chineseName=" + chineseName + ", englishName=" + englishName + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Dish other = (Dish) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
