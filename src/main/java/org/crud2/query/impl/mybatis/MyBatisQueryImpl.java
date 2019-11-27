package org.crud2.query.impl.mybatis;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.crud2.db.QueryDAO;
import org.crud2.jdbc.ColumnKeyNameResolver;
import org.crud2.jdbc.DataTable;
import org.crud2.jdbc.OrignalColumnKeyNameResolver;
import org.crud2.query.QueryParameter;
import org.crud2.query.condition.OperatorFactory;
import org.crud2.query.impl.AbstractQueryImpl;
import org.crud2.query.result.PagerResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class MyBatisQueryImpl extends AbstractQueryImpl {

    private QueryParameter parameter;

    @Autowired
    private QueryDAO queryDao;

    @Override
    public List queryList() {
        return null;
    }

    @Override
    public DataTable queryDataTable() {
        return null;
    }

    @Override
    public List<Map<String, Object>> queryListMap() {
        return null;
    }

    @Override
    public <T> T get() {
        return null;
    }

    @Override
    public Map<String, Object> getMap() {
        return null;
    }

    @Override
    public <T> PagerResult<T> queryPager() {
        return null;
    }

    @Override
    public PagerResult<List<Map<String, Object>>> queryListMapPager() {
        PagerResult<List<Map<String, Object>>> result = innerQueryMapPager();
        List<Map<String, Object>> data = result.getData();
        if (data.size() > 0L) {
            /*
             * set result map key name by MapResultKeyNameResolver
             * OrignalMapResultKeyNameResolver means not change
             */
            {
                if (columnKeyNameResolver instanceof OrignalColumnKeyNameResolver) {
                    result.setData(data);
                } else {
                    Map<String, String> nameMap = getKeyNameMap(data);
                    List<Map<String, Object>> tempData = new ArrayList<>();
                    data.forEach(m -> {
                        Map<String, Object> rm = new HashMap<>();
                        m.forEach((k, v) -> {
                            rm.put(nameMap.get(k), m.get(k));
                        });
                        tempData.add(rm);
                    });
                    result.setData(tempData);
                }
            }
        }
        return result;
    }

    @Override
    public PagerResult<DataTable> queryDataTablePager() {
        PagerResult<DataTable> result = new PagerResult<DataTable>();
        DataTable data = new DataTable();
        PagerResult<List<Map<String, Object>>> mapPagerResult = innerQueryMapPager();
        {
            result.setPageNum(mapPagerResult.getPageNum());
            result.setPages(mapPagerResult.getPages());
            result.setPageSize(mapPagerResult.getPageSize());
            result.setTotal(mapPagerResult.getTotal());
        }
        //TODO:BUG FIX
        /*
        List<Map<String, Object>> listMapData = mapPagerResult.getData();
        if (listMapData.size() > 0L) {
            if (mapResultKeyNameResolver instanceof OrignalColumnKeyNameResolver) {
                data.parse(listMapData);
            } else {
                Map<String, String> nameMap = getKeyNameMap(listMapData);
                data.parse(nameMap, listMapData);
            }
        }
        */
        return result;
    }


    private PagerResult<List<Map<String, Object>>> innerQueryMapPager() {
        switch (parameter.getPageType()) {
            case QueryParameter.PAGE_TYPE_SIZE_INDEX:
                PageHelper.startPage(parameter.getPageIndex(), parameter.getPageSize());
                break;
            case QueryParameter.PAGE_TYPE_OFFSET_LIMIT:
                PageHelper.offsetPage(parameter.getOffset(), parameter.getLimit());
                break;
        }
        List<Map<String, Object>> data = queryDao.query(parameter);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(data);
        PagerResult<List<Map<String, Object>>> result = new PagerResult<>();
        //resolve result pageinfo
        {
            result.setTotal(pageInfo.getTotal());
            result.setPages(pageInfo.getPages());
            result.setPageSize(pageInfo.getPageSize());
            result.setPageNum(pageInfo.getPageNum());
        }
        result.setData(data);
        return result;
    }

    public Map<String, String> getKeyNameMap(List<Map<String, Object>> data) {
        Map<String, Object> item0 = data.get(0);
        Map<String, String> nameMap = new HashMap<>();
        item0.keySet().forEach(k -> {
            nameMap.put(k, columnKeyNameResolver.resolve(k));
        });
        return nameMap;
    }
}
