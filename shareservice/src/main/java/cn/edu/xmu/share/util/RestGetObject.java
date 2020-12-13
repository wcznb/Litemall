package cn.edu.xmu.share.util;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class RestGetObject<T> {
    private RestTemplate restTemplate = new RestTemplate();

    public ReturnObject getfromurl(String url){
        return restTemplate.getForObject(url, ReturnObject.class);
    }

    public ReturnObject getfromurl(String url, Map<String, String> params){
        return restTemplate.getForObject(url, ReturnObject.class, params);
    }

    //无参数api,获取单个对象，访问api的Controller返回方法为：Common.decorateReturnObject（）
    public T getObject(String url, Class<T> t){
        ObjectMapper mapper = new ObjectMapper();
        ReturnObject ret = getfromurl(url);
        if(ret.getCode()== ResponseCode.OK){
            return mapper.convertValue(ret.getData(), t);
        }
        return null;
    }

    //带参数访问api，获取单个对象， 访问api的Controller返回方法为：Common.decorateReturnObject（）
    public T getObject(String url, Map<String, String> params, Class<T> t){
        ObjectMapper mapper = new ObjectMapper();
        ReturnObject ret = getfromurl(url, params);
        if(ret.getCode()== ResponseCode.OK){
            return mapper.convertValue(ret.getData(), t);
        }
        return null;
    }


    //带参数api， 获取对象列表,访问api的Controller返回方法为：Common.getListRetObject()
    public List<T> getObjectList(String url, Map<String, String> params, Class<T> t){
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, t);
        ReturnObject ret = getfromurl(url, params);
        List<T> lst=null;
        if(ret.getCode()== ResponseCode.OK){
            lst = (List<T>)mapper.convertValue(ret.getData(),javaType);
        }
        return lst;
    }

    //无参数api， 获取对象列表, 访问api的Controller返回方法为：Common.getListRetObject()
    public List<T> getObjectList(String url, Class<T> t){
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, t);
        ReturnObject ret = getfromurl(url);
        List<T> lst=null;
        if(ret.getCode()== ResponseCode.OK){
            lst = (List<T>)mapper.convertValue(ret.getData(),javaType);
        }
        return lst;
    }



    //无参数api， 获取对象分页查询列表, 访问api的Controller返回方法为：Common.getListRetObject()
    public List<T> getObjectPage(String url, Class<T> t){
        ObjectMapper mapper = new ObjectMapper();
        ReturnObject ret = getfromurl(url);
        if(ret.getCode()== ResponseCode.OK){
            GetPage page = mapper.convertValue(ret.getData(), GetPage.class);
            List<T> lst = new ArrayList<>();
            int size = page.getList().size();
            for(int i=0;i<size;i++){
                T z = mapper.convertValue(page.getList().get(i), t);
                lst.add(z);
            }
            return lst;
        }
        return null;
    }

    //无参数api， 获取对象分页查询列表, 访问api的Controller返回方法为：Common.getListRetObject()
    public List<T> getObjectPage(String url, Map<String, String> params, Class<T> t){
        ObjectMapper mapper = new ObjectMapper();
        ReturnObject ret = getfromurl(url, params);
        if(ret.getCode()== ResponseCode.OK){
            GetPage page = mapper.convertValue(ret.getData(), GetPage.class);
            List<T> lst = new ArrayList<>();
            int size = page.getList().size();
            for(int i=0;i<size;i++){
                T z = mapper.convertValue(page.getList().get(i), t);
                lst.add(z);
            }
            return lst;
        }
        return null;
    }

}

@Data
class GetPage{
    Integer total;
    Integer pages;
    Integer pageSize;
    Integer page;
    List list;
    public GetPage(){}
}
