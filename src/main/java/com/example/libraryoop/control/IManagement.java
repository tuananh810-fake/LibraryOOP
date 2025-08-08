package com.example.libraryoop.control;

import java.util.Map;

public interface IManagement<T> {
    void add(T t); // tính năng thêm đối tượng
    void update(String id, Map<String, Object> fieldsToUpdate) throws Exception; // tính năng cập nhật đối tượng ,
                                                                                //  map cho phép cập nhật nhiều trường cùng lúc
    void delete(String id);   // tính năng xóa đối tượng
    int findIndexById(String id);  // tìm kiếm vị trí của đối tượng theo id
}