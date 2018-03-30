package cn.damonto.ssmTest.service;

import cn.damonto.ssmTest.dao.FunctionDao;
import cn.damonto.ssmTest.entity.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FunctionService {

    @Autowired
    private FunctionDao functionDao;

    public void addFunction(Function function){
        functionDao.save(function);
    }

    public void updateUrl(Long id,String url){
        functionDao.updateUrl(id, url);
    }

    public List<Function> getFunctions(int page, int size, Long parentId){
        return functionDao.findByPage(page, size, parentId);
    }

    public void deleteFunctionById(Long id){
        functionDao.deleteById(id);
    }

    public List<Function> getAllFunctions(){
        return functionDao.findAll();
    }
}
