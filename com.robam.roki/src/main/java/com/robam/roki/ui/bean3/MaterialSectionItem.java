package com.robam.roki.ui.bean3;

import com.chad.library.adapter.base.entity.JSectionEntity;
import com.robam.common.pojos.Material;

/**
 * @author r210190
 * des 食材数量数据
 */
public class MaterialSectionItem extends JSectionEntity {
    public boolean isHeader;
    public Material material;
    public String  title;

    public MaterialSectionItem(boolean isHeader, String title) {
        this.isHeader = isHeader;
        this.title = title;
    }

    public MaterialSectionItem(boolean isHeader, Material material) {
        this.isHeader = isHeader;
        this.material = material;
    }

    @Override
    public boolean isHeader() {
        return isHeader;
    }
}
