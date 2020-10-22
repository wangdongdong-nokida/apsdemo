package com.example.apsdemo.utils;

import com.example.apsdemo.domain.RequestPage;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Tools {
    public static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy/MM/dd");

    public static Pageable getDefaultPageable(RequestPage requestPage) {
        if (requestPage.getCurrent() <= 0 || requestPage.getPageSize() <= 0) {
            return null;
        }
        return PageRequest.of(requestPage.getCurrent() > 0 ? requestPage.getCurrent() - 1 : 0, requestPage.getPageSize() == 0 ? 1 : requestPage.getPageSize(), Sort.by(Sort.Direction.ASC, "ID"));
    }


    public static Specification getSpecification(Map<String, Object> map) {
        return new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new LinkedList<>();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    predicateList.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }


    public static Specification getSpecificationByParams(Map<String, Object> map) {
        return new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new LinkedList<>();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (entry.getValue() == null) {
                        continue;
                    }
                    Path path = root;
                    if (entry.getKey().contains("-")) {
                        String[] level = entry.getKey().split("-");
                        for (String key : level) {
                            try {
                                String replaceKey = key.replace("!", "");
                                replaceKey = replaceKey.replace("*", "");
                                replaceKey = replaceKey.replace("^", "");
                                replaceKey = replaceKey.replace("<>", "");
                                Path inside = path.get(replaceKey);
                                path = inside;
                            } catch (Exception e) {

                            }
                        }
                    } else {
                        try {
                            String replaceKey = entry.getKey().replace("!", "");
                            replaceKey = replaceKey.replace("*", "");
                            replaceKey = replaceKey.replace("^", "");
                            replaceKey = replaceKey.replace("<>", "");
                            Path inside = path.get(replaceKey);
                            path = inside;
                        } catch (Exception e) {

                        }
                    }
                    if (!(path instanceof Root)) {
                        if (entry.getKey().contains("!")) {
                            if (entry.getKey().contains("!*")) {
                                predicateList.add(criteriaBuilder.isNotEmpty(path));
                            } else if (entry.getKey().contains("!^")) {
                                predicateList.add(criteriaBuilder.isNotNull(path));
                            } else if (entry.getKey().contains("!<>")) {
                                predicateList.add(criteriaBuilder.notLike(path, "%" + (String) entry.getValue() + "%"));
                            } else {
                                predicateList.add(criteriaBuilder.notEqual(path, entry.getValue()));
                            }
                        } else if (entry.getKey().contains("*")) {
                            predicateList.add(criteriaBuilder.isEmpty(path));
                        } else if (entry.getKey().contains("^")) {
                            predicateList.add(criteriaBuilder.isNull(path));
                        } else if (entry.getKey().contains("<>")) {
                            predicateList.add(criteriaBuilder.like(path, "%" +(String) entry.getValue()+"%"));
                        } else {
                            if (entry.getValue() != null && !"".equals(entry.getValue())) {
                                if (entry.getValue() instanceof String) {
                                    predicateList.add(criteriaBuilder.like(path, "%" + entry.getValue() + "%"));
                                } else {
                                    predicateList.add(criteriaBuilder.equal(path, entry.getValue()));
                                }
                            }
                        }
                    }
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }


    public static Pageable getPageableByPagination(int current, int pageSize, String orderBy) {
        if (current <= 0 || pageSize <= 0) {
            return null;
        }
        return PageRequest.of(current - 1, pageSize, Sort.by(Sort.Direction.ASC, orderBy == null ? "ID" : orderBy));
    }


    public static Result getResult(@RequestBody Map<String, Object> requestPage, BaseService service) {
        int current = requestPage.get("current") != null ? Integer.valueOf(requestPage.get("current").toString()) : 0;
        int pageSize = requestPage.get("pageSize") != null ? Integer.valueOf(requestPage.get("pageSize").toString()) : 0;
        String orderBy = requestPage.get("orderBy") != null ? requestPage.get("orderBy").toString() : null;
        Result result = new Result(current, pageSize);
        Map<String, Object> params = requestPage.get("params") != null ? (Map<String, Object>) requestPage.get("params") : new HashMap<>();
        Specification specification = Tools.getSpecificationByParams(params);
        Pageable pageable = Tools.getPageableByPagination(current, pageSize, orderBy);

        if (pageable == null) {
            List list = service.findAll(specification);
            result.setData(list);
            result.setTotal(list.size());
        } else {
            Page page = service.findAll(specification, pageable);
            if (page.hasContent()) {
                result.setData(page.getContent());
                result.setTotal(page.getTotalElements());
            }
        }
        return result;
    }


    public static boolean checkIsEmpty(String object) {
        return object == null || "".equals(object);
    }

}
