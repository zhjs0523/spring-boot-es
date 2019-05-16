package com.zhjs.elasicsearch.demo;

import com.zhjs.elasicsearch.utils.BeanCopyUtils;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by zhjs on 2019/5/8.
 */
@RestController
@RequestMapping("/es")
public class ESController {

    @Autowired
    private TransportClient client;

    private String INDEX = "person";

    private String TYPE = "student";


    @RequestMapping("/add")
    public String add(@RequestBody Student student, @RequestHeader String id) {
        IndexResponse response = null;
        try {
            response = client.prepareIndex(INDEX,TYPE,id)
                    .setSource(BeanCopyUtils.objectToMap(student))
                    .get();
            if (response.status() != RestStatus.CREATED && response.status() != RestStatus.OK) {
                return "fail";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }


    @RequestMapping("/query/byId")
    public Student queryById(@RequestParam("id") String id){
        GetResponse  response = null;
        try{
            response = client.prepareGet(INDEX,TYPE,id)
                    .get();
        }catch(Exception e){
            e.printStackTrace();
        }
        return (Student) BeanCopyUtils.mapToObj(response.getSource(),Student.class);

    }


    /**
     * @param student
     * @param id
     * @return
     */
    @RequestMapping("/update")
    public String update(@RequestBody Student student, @RequestHeader String id){
        UpdateResponse response = client.prepareUpdate(INDEX,TYPE,id)
                .setDoc(BeanCopyUtils.objectToMap(student))
                .get();
        if(response.status()  == RestStatus.OK){
            return "success";
        }
        return "fail";
    }


    @RequestMapping("/del")
    public String del(@RequestParam String id){
        DeleteResponse response = client.prepareDelete(INDEX,TYPE,id)
                .get();
        if(response.status()  == RestStatus.OK){
            return "success";
        }
        return "fail";
    }

    @RequestMapping("/query/byCond")
    public List<Student> queryByCond(@RequestParam("name") String name,@RequestParam("hobbies") String hobbies){
        String[] hobbiesArr = hobbies.split(",");
        List<String> hobbiesList = Stream.of(hobbiesArr).collect(Collectors.toList());
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery("name",name));
        boolQuery.must(QueryBuilders.matchQuery("hobbies",hobbiesList));
        SearchResponse response = client.prepareSearch(INDEX).setTypes(TYPE)
                .setQuery(boolQuery)
//                .addSort(new ScoreSortBuilder())
                .addSort("age", SortOrder.DESC)
//                .setFrom(1)
//                .setSize(5)
                .get();
        List<Student> resultList = new ArrayList<>();
        SearchHits searchHits = response.getHits();
        for (SearchHit searchHit : searchHits) {
            resultList.add((Student) BeanCopyUtils.mapToObj(searchHit.getSourceAsMap(),Student.class));
        }
        return resultList;
    }

}
