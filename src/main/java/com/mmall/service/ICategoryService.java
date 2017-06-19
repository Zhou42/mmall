package com.mmall.service;

import com.mmall.common.ServerResponse;

/**
 * Created by yangzhou on 2017-06-18.
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName );
}
